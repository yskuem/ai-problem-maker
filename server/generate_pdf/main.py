# app.py
# pip install fastapi "uvicorn[standard]" xhtml2pdf pydantic
# ./fonts/NotoSansJP-Regular.ttf を置く（推奨）。任意で NotoSansJP-Bold.ttf も。

from fastapi import FastAPI, HTTPException
from fastapi.responses import StreamingResponse
from pydantic import BaseModel
from typing import List, Optional
from io import BytesIO
from datetime import datetime
from xhtml2pdf import pisa
import html as html_escape
import re
from pathlib import Path
import os
from urllib.parse import quote
import logging

# ReportLab: 日本語フォント登録
from reportlab.pdfbase import pdfmetrics
from reportlab.pdfbase.ttfonts import TTFont

# ---- logging ----
logging.basicConfig(
    level=os.environ.get("QUIZ_PDF_LOGLEVEL", "INFO"),
    format="%(asctime)s [%(levelname)s] %(name)s - %(message)s",
)
logger = logging.getLogger("quiz-pdf")

app = FastAPI(title="Quiz PDF Generator (xhtml2pdf + JP font)")

# ---------- Pydantic Models ----------
class Question(BaseModel):
    question: str
    choices: List[str]
    answer: str           # "A"/"B"/"C"/"D" or "1..n" or choice text
    explanation: str

class InputModel(BaseModel):
    title: str
    questions: List[Question]

# ---------- Helpers ----------
ALPH = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"

def sanitize_text(s: str) -> str:
    esc = html_escape.escape(s, quote=True)
    esc = esc.replace("\r\n", "\n").replace("\r", "\n").replace("\n", "<br>")
    return esc

def choice_letter(i: int) -> str:
    return ALPH[i] if i < len(ALPH) else f"({i+1})"

def resolve_answer_letter(q: Question) -> str:
    raw = (q.answer or "").strip()
    if re.fullmatch(r"[A-Za-z]", raw):
        return raw.upper()
    if re.fullmatch(r"\d+", raw):
        idx = int(raw) - 1
        if 0 <= idx < len(q.choices):
            return choice_letter(idx)
    for idx, c in enumerate(q.choices):
        if raw == c or raw.strip().replace(" ", "") == c.strip().replace(" ", ""):
            return choice_letter(idx)
    return "A"

def filename_from_title(title: str) -> str:
    base = re.sub(r"[^\w\-ぁ-んァ-ン一-龥]", "_", title)
    base = re.sub(r"_+", "_", base).strip("_")
    return f"{base or 'quiz'}.pdf"

def ascii_filename_from_title(title: str) -> str:
    base = re.sub(r"[^A-Za-z0-9_.-]+", "_", title)
    base = re.sub(r"_+", "_", base).strip("_")
    if not base:
        base = "quiz"
    if not base.lower().endswith(".pdf"):
        base = f"{base}.pdf"
    return base

def _first_existing(paths: List[Path]) -> Optional[Path]:
    for p in paths:
        if p and p.exists():
            return p
    return None

def register_jp_font() -> dict:
    """フォント登録とログ出力。戻り値: dict(info)"""
    font_dir = Path(os.getenv("QUIZ_PDF_FONT_DIR", "./fonts"))
    regular = _first_existing([
        Path(os.getenv("QUIZ_PDF_FONT_REGULAR", "")),
        font_dir / "NotoSansJP-Regular.ttf",
        font_dir / "IPAexGothic.ttf",
        font_dir / "MPLUS1-Regular.ttf",
    ])
    bold = _first_existing([
        Path(os.getenv("QUIZ_PDF_FONT_BOLD", "")),
        font_dir / "NotoSansJP-Bold.ttf",
        font_dir / "IPAexGothicBold.ttf",
        font_dir / "MPLUS1-Bold.ttf",
    ])

    info = {
        "regular_path": str(regular) if regular else None,
        "bold_path": str(bold) if bold else None,
        "css_family": "Helvetica",           # CSSで使う先頭名
        "css_decl": "Helvetica, sans-serif", # ログ表示用
        "registered": [],
        "used_as_default": None,
    }

    if not regular:
        logger.warning("JP-FONT regular not found. Fallback to Helvetica. Place ./fonts/NotoSansJP-Regular.ttf")
        return info

    try:
        pdfmetrics.registerFont(TTFont("QuizJP-Regular", str(regular)))
        info["registered"].append("QuizJP-Regular")
        logger.info(f"JP-FONT regular={regular}")

        if bold:
            pdfmetrics.registerFont(TTFont("QuizJP-Bold", str(bold)))
            info["registered"].append("QuizJP-Bold")
            logger.info(f"JP-FONT bold={bold}")
            pdfmetrics.registerFontFamily(
                "QuizJP",
                normal="QuizJP-Regular",
                bold="QuizJP-Bold",
                italic="QuizJP-Regular",
                boldItalic="QuizJP-Bold",
            )
        else:
            pdfmetrics.registerFontFamily(
                "QuizJP",
                normal="QuizJP-Regular",
                bold="QuizJP-Regular",
                italic="QuizJP-Regular",
                boldItalic="QuizJP-Regular",
            )

        # xhtml2pdf の既定フォントを差し替え（重要）
        pisa.DEFAULT_FONT = "QuizJP-Regular"
        info["used_as_default"] = "QuizJP-Regular"
        logger.info("xhtml2pdf DEFAULT_FONT=QuizJP-Regular")

        # CSS で先頭に明示
        info["css_family"] = "QuizJP-Regular"
        info["css_decl"] = "QuizJP-Regular, QuizJP, sans-serif"

    except Exception as e:
        logger.exception(f"JP-FONT registration failed: {e}")

    # 登録済みフォント一覧を確認出力
    try:
        names = sorted(pdfmetrics.getRegisteredFontNames())
        logger.info(f"PDFMETRICS registered fonts count={len(names)}")
        # 長い場合は先頭のみ
        logger.info(f"PDFMETRICS sample={names[:20]}")
    except Exception:
        pass

    return info

FONT_INFO = register_jp_font()
JP_FONT_CSS = FONT_INFO["css_decl"]

def build_html(data: InputModel) -> str:
    title = sanitize_text(data.title)
    today = datetime.now().strftime("%Y-%m-%d")

    logger.info(f"Using CSS font-family='{JP_FONT_CSS}'")

    q_blocks = []
    answers_cells = []
    explanations = []

    for idx, q in enumerate(data.questions, start=1):
        qtext = sanitize_text(q.question)
        choices_html = []
        for cidx, choice in enumerate(q.choices):
            label = choice_letter(cidx)
            ctext = sanitize_text(choice)
            choices_html.append(f"""
            <div class="choice">
                <span class="choice-label">{label}</span>
                <span class="choice-text">{ctext}</span>
            </div>
            """.strip())

        q_blocks.append(f"""
        <div class="question-block">
            <div class="question-header">
                <div class="question-number">Q{idx}.</div>
                <div class="question-text">{qtext}</div>
            </div>
            <div class="choices">
                {''.join(choices_html)}
            </div>
        </div>
        """.strip())

        letter = resolve_answer_letter(q)
        try:
            li = ALPH.index(letter)
            correct_text = q.choices[li] if li < len(q.choices) else ""
        except ValueError:
            correct_text = ""

        answers_cells.append(f"""
        <td class="answer-cell">
            <div class="answer-number">Q{idx}</div>
            <div class="correct-answer">{letter}</div>
        </td>
        """.strip())

        explanations.append(f"""
        <div class="explanation-block">
            <div class="explanation-header">Q{idx}. 正解: {letter}) {sanitize_text(correct_text)}</div>
            <div class="explanation-text">{sanitize_text(q.explanation)}</div>
        </div>
        """.strip())

    rows = []
    for i in range(0, len(answers_cells), 5):
        row_cells = answers_cells[i:i+5]
        if len(row_cells) < 5:
            row_cells += ['<td class="answer-cell"></td>'] * (5 - len(row_cells))
        rows.append(f"<tr>{''.join(row_cells)}</tr>")
    answers_table = f"""
    <table class="answer-grid" cellspacing="0" cellpadding="6" border="0">
        {''.join(rows)}
    </table>
    """

    css = f"""
    /* 決定したフォント名を先頭に置く（ReportLabに登録済みの実フォント名） */
    body {{ font-family: {JP_FONT_CSS}; }}

    .page-break {{ page-break-after: always; }}
    .answer-page {{ page-break-before: always; }}
    .container {{ width: 180mm; margin: 0 auto; padding: 12mm; background: #ffffff; }}
    .header {{ text-align: center; margin-bottom: 16px; border-bottom: 2px solid #333; padding-bottom: 8px; }}
    .h1-title {{ font-size: 20pt; font-weight: bold; margin: 0 0 6px 0; }}
    .header-info-table {{ width: 100%; font-size: 10pt; margin-top: 8px; }}
    .name-field {{ display: inline-block; border-bottom: 1px solid #333; min-width: 60mm; height: 12pt; vertical-align: bottom; }}

    .question-block {{ border: 1px solid #e0e0e0; background: #fafafa; padding: 8pt; margin: 0 0 14pt 0; }}
    .question-header {{ margin-bottom: 6pt; }}
    .question-number {{ display: inline-block; width: 24pt; font-weight: bold; color: #0066cc; font-size: 11pt; vertical-align: top; }}
    .question-text {{ display: inline-block; width: 140mm; font-size: 10.5pt; line-height: 1.6; vertical-align: top; font-weight: 500; }}
    .choices {{ margin-left: 24pt; margin-top: 6pt; }}
    .choice {{ border: 1px solid #ddd; background: #ffffff; margin: 0 0 6pt 0; padding: 6pt; font-size: 10pt; }}
    .choice-label {{ display: inline-block; width: 14pt; text-align: center; font-weight: bold; color: #ffffff; background: #0066cc; margin-right: 6pt; }}
    .choice-text {{ display: inline-block; width: 150mm; }}

    .footer {{ margin-top: 20pt; border-top: 1px solid #ccc; text-align: center; font-size: 8.5pt; color: #666; padding-top: 8pt; }}

    .answer-header {{ text-align: center; font-size: 14pt; font-weight: bold; margin-bottom: 10pt; border-bottom: 2px solid #333; padding-bottom: 6pt; }}
    .section-title {{ font-size: 12pt; margin: 10pt 0 6pt 0; padding: 4pt 6pt; background: #f0f0f0; border-left: 4pt solid #0066cc; }}

    .answer-grid {{ width: 100%; border-collapse: separate; border-spacing: 4pt; background: #fafafa; border: 1px solid #ddd; }}
    .answer-cell {{ text-align: center; font-size: 11pt; padding: 4pt; height: 28pt; }}
    .answer-number {{ font-weight: bold; color: #0066cc; margin-bottom: 3pt; }}
    .correct-answer {{ display: inline-block; min-width: 20pt; height: 16pt; line-height: 16pt; background: #0066cc; color: #ffffff; font-weight: bold; font-size: 10pt; }}

    .explanation-block {{ border-left: 3pt solid #0066cc; background: #f9f9f9; padding: 6pt; margin: 0 0 10pt 0; }}
    .explanation-header {{ font-weight: bold; color: #0066cc; font-size: 10.5pt; margin-bottom: 4pt; }}
    .explanation-text {{ font-size: 10pt; line-height: 1.6; }}
    """

    html = f"""<!DOCTYPE html>
<html lang="ja">
<head>
<meta charset="UTF-8">
<title>{title}</title>
<style>
{css}
</style>
</head>
<body>
<div class="container">
  <!-- 問題ページ -->
  <div class="question-page">
    <div class="header">
      <div class="h1-title">{title}</div>
      <table class="header-info-table">
        <tr>
          <td style="text-align:left;">氏名: <span class="name-field"></span></td>
          <td style="text-align:right;">実施日: {today}</td>
        </tr>
      </table>
    </div>
    <div id="questions-container">
      {''.join(q_blocks)}
    </div>
    <div class="footer">Generated by Quiz PDF Generator (xhtml2pdf)</div>
  </div>

  <!-- 解答・解説ページ（別紙） -->
  <div class="answer-page page-break">
    <div class="answer-header">解答・解説</div>
    <div class="section-title">正解一覧</div>
    {answers_table}
    <div class="section-title">詳細解説</div>
    {''.join(explanations)}
  </div>
</div>
</body>
</html>
"""
    return html

def html_to_pdf_bytes_xhtml2pdf(html: str) -> bytes:
    out = BytesIO()
    result = pisa.CreatePDF(src=html, dest=out, encoding='utf-8')
    if result.err:
        raise RuntimeError("xhtml2pdf failed to generate PDF")
    out.seek(0)
    return out.read()

# ---------- Endpoint ----------
@app.post("/generate-quiz-pdf")
def generate_quiz_pdf(data: InputModel):
    if not data.questions:
        raise HTTPException(status_code=400, detail="questionsが空です")
    for i, q in enumerate(data.questions, start=1):
        if not q.choices or len(q.choices) < 2:
            raise HTTPException(status_code=400, detail=f"Q{i}: choicesは2つ以上必要です")

    logger.info(f"FONT_INFO={FONT_INFO}")

    html = build_html(data)
    try:
        pdf_bytes = html_to_pdf_bytes_xhtml2pdf(html)
    except Exception as e:
        logger.exception("PDF生成失敗")
        raise HTTPException(status_code=500, detail=f"PDF生成に失敗しました: {e}")

    display_name = filename_from_title(data.title)           # 日本語名
    ascii_name   = ascii_filename_from_title(data.title)     # ASCIIフォールバック
    cd_value = f"attachment; filename=\"{ascii_name}\"; filename*=UTF-8''{quote(display_name, safe='')}"

    return StreamingResponse(
        BytesIO(pdf_bytes),
        media_type="application/pdf",
        headers={"Content-Disposition": cd_value},
    )

@app.get("/healthz")
def healthz():
    # ここでもフォント状況を露出
    try:
        names = sorted(pdfmetrics.getRegisteredFontNames())
    except Exception:
        names = []
    return {
        "status": "ok",
        "font_css": JP_FONT_CSS,
        "default_font_for_xhtml2pdf": getattr(pisa, "DEFAULT_FONT", None),
        "registered_fonts_count": len(names),
        "registered_fonts_sample": names[:50],
        "regular_path": FONT_INFO["regular_path"],
        "bold_path": FONT_INFO["bold_path"],
    }
