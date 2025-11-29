from fastapi import FastAPI, HTTPException
from fastapi.responses import Response
from pydantic import BaseModel
from typing import List
import base64
from datetime import datetime
import io
from reportlab.lib import colors
from reportlab.lib.pagesizes import A4
from reportlab.lib.styles import getSampleStyleSheet, ParagraphStyle
from reportlab.lib.units import mm
from reportlab.platypus import SimpleDocTemplate, Table, TableStyle, Paragraph, Spacer, PageBreak, KeepTogether
from reportlab.platypus.flowables import Flowable
from reportlab.pdfbase import pdfmetrics
from reportlab.pdfbase.cidfonts import UnicodeCIDFont
from reportlab.lib.enums import TA_LEFT, TA_CENTER
from reportlab.graphics.shapes import Drawing, Rect, String
from reportlab.graphics import renderPDF

app = FastAPI()

# 日本語フォントの登録
pdfmetrics.registerFont(UnicodeCIDFont('HeiseiMin-W3'))
pdfmetrics.registerFont(UnicodeCIDFont('HeiseiKakuGo-W5'))

class Question(BaseModel):
    question: str
    choices: List[str]
    answer: str           # "A"/"B"/"C"/"D" or "1..n" or choice text
    explanation: str

class InputModel(BaseModel):
    title: str
    questions: List[Question]
    color_mode: bool = True  # True: カラー, False: 白黒

class ColorScheme:
    """カラースキームを管理するクラス"""
    def __init__(self, color_mode: bool):
        self.color_mode = color_mode
        
        if color_mode:
            # カラーモード
            self.primary = colors.HexColor('#667eea')
            self.secondary = colors.HexColor('#56ab2f')
            self.text_primary = colors.HexColor('#667eea')
            self.text_secondary = colors.HexColor('#56ab2f')
            self.text_explanation = colors.HexColor('#5a6c7d')
        else:
            # 白黒モード
            self.primary = colors.black
            self.secondary = colors.black
            self.text_primary = colors.black
            self.text_secondary = colors.black
            self.text_explanation = colors.HexColor('#333333')
        
        # 共通色
        self.white = colors.white
        self.black = colors.black
        self.border = colors.HexColor('#cccccc')
        self.bg_light = colors.HexColor('#f5f5f5')

def create_styles(color_scheme: ColorScheme):
    """カスタムスタイルを作成"""
    styles = getSampleStyleSheet()
    
    # タイトルスタイル
    styles.add(ParagraphStyle(
        name='CustomTitle',
        parent=styles['Heading1'],
        fontName='HeiseiKakuGo-W5',
        fontSize=18,
        textColor=color_scheme.text_primary,
        alignment=TA_CENTER,
        spaceAfter=12
    ))
    
    # 問題番号スタイル
    styles.add(ParagraphStyle(
        name='QuestionNumber',
        fontName='HeiseiKakuGo-W5',
        fontSize=12,
        textColor=color_scheme.text_primary,
        leftIndent=0,
        spaceAfter=6
    ))
    
    # 問題文スタイル
    styles.add(ParagraphStyle(
        name='QuestionText',
        fontName='HeiseiMin-W3',
        fontSize=11,
        textColor=colors.black,
        leftIndent=0,
        spaceAfter=8,
        leading=16
    ))
    
    # 選択肢スタイル
    styles.add(ParagraphStyle(
        name='Choice',
        fontName='HeiseiMin-W3',
        fontSize=10,
        textColor=colors.black,
        leftIndent=20,
        spaceAfter=4,
        leading=14
    ))
    
    # 正解スタイル
    styles.add(ParagraphStyle(
        name='CorrectAnswer',
        fontName='HeiseiKakuGo-W5',
        fontSize=11,
        textColor=color_scheme.text_secondary,
        leftIndent=0,
        spaceAfter=6
    ))
    
    # 解説スタイル
    styles.add(ParagraphStyle(
        name='Explanation',
        fontName='HeiseiMin-W3',
        fontSize=10,
        textColor=color_scheme.text_explanation,
        leftIndent=0,
        spaceAfter=8,
        leading=15
    ))
    
    return styles

def create_question_elements(questions: List[Question], styles, color_scheme: ColorScheme):
    """問題ページの要素を作成（各問題をKeepTogetherでグループ化）"""
    elements = []
    
    for idx, q in enumerate(questions, 1):
        question_elements = []
        
        # 問題番号（枠線+文字色）
        question_num_data = [[f'問題 {idx}']]
        question_num_table = Table(question_num_data, colWidths=[70*mm])
        question_num_table.setStyle(TableStyle([
            ('BACKGROUND', (0, 0), (-1, -1), color_scheme.white),
            ('TEXTCOLOR', (0, 0), (-1, -1), color_scheme.text_primary),
            ('ALIGN', (0, 0), (-1, -1), 'CENTER'),
            ('VALIGN', (0, 0), (-1, -1), 'MIDDLE'),
            ('FONTNAME', (0, 0), (-1, -1), 'HeiseiKakuGo-W5'),
            ('FONTSIZE', (0, 0), (-1, -1), 12),
            ('LEFTPADDING', (0, 0), (-1, -1), 12),
            ('RIGHTPADDING', (0, 0), (-1, -1), 12),
            ('TOPPADDING', (0, 0), (-1, -1), 6),
            ('BOTTOMPADDING', (0, 0), (-1, -1), 6),
            ('BOX', (0, 0), (-1, -1), 2, color_scheme.primary),
            ('ROUNDEDCORNERS', [5]),
        ]))
        question_elements.append(question_num_table)
        question_elements.append(Spacer(1, 8*mm))
        
        # 問題文
        question_para = Paragraph(q.question, styles['QuestionText'])
        question_data = [[question_para]]
        question_table = Table(question_data, colWidths=[170*mm])
        question_table.setStyle(TableStyle([
            ('BACKGROUND', (0, 0), (-1, -1), color_scheme.white),
            ('BOX', (0, 0), (-1, -1), 1, color_scheme.border),
            ('LEFTPADDING', (0, 0), (-1, -1), 10),
            ('RIGHTPADDING', (0, 0), (-1, -1), 10),
            ('TOPPADDING', (0, 0), (-1, -1), 8),
            ('BOTTOMPADDING', (0, 0), (-1, -1), 8),
            ('LINEBELOW', (0, 0), (0, 0), 3, color_scheme.primary),
        ]))
        question_elements.append(question_table)
        question_elements.append(Spacer(1, 6*mm))
        
        # 選択肢
        for choice_idx, choice in enumerate(q.choices):
            choice_label = chr(65 + choice_idx)
            choice_data = [[f'{choice_label}', choice]]
            choice_table = Table(choice_data, colWidths=[15*mm, 155*mm])
            choice_table.setStyle(TableStyle([
                ('BACKGROUND', (0, 0), (-1, -1), color_scheme.white),
                ('TEXTCOLOR', (0, 0), (0, 0), color_scheme.text_primary),
                ('TEXTCOLOR', (1, 0), (1, 0), color_scheme.black),
                ('ALIGN', (0, 0), (0, 0), 'CENTER'),
                ('VALIGN', (0, 0), (-1, -1), 'MIDDLE'),
                ('FONTNAME', (0, 0), (0, 0), 'HeiseiKakuGo-W5'),
                ('FONTNAME', (1, 0), (1, 0), 'HeiseiMin-W3'),
                ('FONTSIZE', (0, 0), (-1, -1), 10),
                ('BOX', (0, 0), (-1, -1), 1, color_scheme.border),
                ('LINEAFTER', (0, 0), (0, 0), 1, color_scheme.primary),
                ('LEFTPADDING', (0, 0), (0, 0), 8),
                ('LEFTPADDING', (1, 0), (1, 0), 12),
                ('TOPPADDING', (0, 0), (-1, -1), 6),
                ('BOTTOMPADDING', (0, 0), (-1, -1), 6),
            ]))
            question_elements.append(choice_table)
            question_elements.append(Spacer(1, 3*mm))
        
        # 各問題をKeepTogetherでグループ化
        elements.append(KeepTogether(question_elements))
        elements.append(Spacer(1, 10*mm))
    
    return elements

def create_answer_elements(questions: List[Question], styles, color_scheme: ColorScheme):
    """解答・解説ページの要素を作成（各解答をKeepTogetherでグループ化）"""
    elements = []
    
    for idx, q in enumerate(questions, 1):
        answer_elements = []
        
        # 回答番号（枠線+文字色）
        answer_num_data = [[f'回答 {idx}']]
        answer_num_table = Table(answer_num_data, colWidths=[70*mm])
        answer_num_table.setStyle(TableStyle([
            ('BACKGROUND', (0, 0), (-1, -1), color_scheme.white),
            ('TEXTCOLOR', (0, 0), (-1, -1), color_scheme.text_secondary),
            ('ALIGN', (0, 0), (-1, -1), 'CENTER'),
            ('VALIGN', (0, 0), (-1, -1), 'MIDDLE'),
            ('FONTNAME', (0, 0), (-1, -1), 'HeiseiKakuGo-W5'),
            ('FONTSIZE', (0, 0), (-1, -1), 12),
            ('LEFTPADDING', (0, 0), (-1, -1), 12),
            ('RIGHTPADDING', (0, 0), (-1, -1), 12),
            ('TOPPADDING', (0, 0), (-1, -1), 6),
            ('BOTTOMPADDING', (0, 0), (-1, -1), 6),
            ('BOX', (0, 0), (-1, -1), 2, color_scheme.secondary),
            ('ROUNDEDCORNERS', [5]),
        ]))
        answer_elements.append(answer_num_table)
        answer_elements.append(Spacer(1, 6*mm))
        
        # 問題文（再掲）
        question_recap = Paragraph(q.question, styles['QuestionText'])
        recap_data = [[question_recap]]
        recap_table = Table(recap_data, colWidths=[170*mm])
        recap_table.setStyle(TableStyle([
            ('BACKGROUND', (0, 0), (-1, -1), color_scheme.bg_light),
            ('BOX', (0, 0), (-1, -1), 1, color_scheme.border),
            ('LEFTPADDING', (0, 0), (-1, -1), 10),
            ('RIGHTPADDING', (0, 0), (-1, -1), 10),
            ('TOPPADDING', (0, 0), (-1, -1), 8),
            ('BOTTOMPADDING', (0, 0), (-1, -1), 8),
        ]))
        answer_elements.append(recap_table)
        answer_elements.append(Spacer(1, 6*mm))
        
        # 正解
        correct_choice_text = ""
        if q.answer.upper() in ['A', 'B', 'C', 'D', 'E', 'F', 'G', 'H']:
            choice_idx = ord(q.answer.upper()) - 65
            if choice_idx < len(q.choices):
                correct_choice_text = q.choices[choice_idx]
            answer_display = f"{q.answer.upper()}. {correct_choice_text}"
        elif q.answer.isdigit():
            choice_idx = int(q.answer) - 1
            if 0 <= choice_idx < len(q.choices):
                correct_choice_text = q.choices[choice_idx]
                answer_display = f"{chr(65 + choice_idx)}. {correct_choice_text}"
        else:
            answer_display = q.answer
        
        answer_data = [['正解', answer_display]]
        answer_table = Table(answer_data, colWidths=[30*mm, 140*mm])
        answer_table.setStyle(TableStyle([
            ('BACKGROUND', (0, 0), (-1, -1), color_scheme.white),
            ('TEXTCOLOR', (0, 0), (0, 0), color_scheme.text_secondary),
            ('TEXTCOLOR', (1, 0), (1, 0), color_scheme.black),
            ('ALIGN', (0, 0), (0, 0), 'CENTER'),
            ('VALIGN', (0, 0), (-1, -1), 'MIDDLE'),
            ('FONTNAME', (0, 0), (0, 0), 'HeiseiKakuGo-W5'),
            ('FONTNAME', (1, 0), (1, 0), 'HeiseiKakuGo-W5'),
            ('FONTSIZE', (0, 0), (-1, -1), 11),
            ('BOX', (0, 0), (-1, -1), 2, color_scheme.secondary),
            ('LINEAFTER', (0, 0), (0, 0), 2, color_scheme.secondary),
            ('LEFTPADDING', (1, 0), (1, 0), 12),
            ('TOPPADDING', (0, 0), (-1, -1), 8),
            ('BOTTOMPADDING', (0, 0), (-1, -1), 8),
        ]))
        answer_elements.append(answer_table)
        answer_elements.append(Spacer(1, 6*mm))
        
        # 解説
        explanation_title = Paragraph('💡 解説', styles['QuestionNumber'])
        explanation_text = Paragraph(q.explanation, styles['Explanation'])
        explanation_data = [[explanation_title], [explanation_text]]
        explanation_table = Table(explanation_data, colWidths=[170*mm])
        explanation_table.setStyle(TableStyle([
            ('BACKGROUND', (0, 0), (-1, -1), color_scheme.white),
            ('BOX', (0, 0), (-1, -1), 1, color_scheme.border),
            ('LEFTPADDING', (0, 0), (-1, -1), 12),
            ('RIGHTPADDING', (0, 0), (-1, -1), 12),
            ('TOPPADDING', (0, 0), (-1, -1), 8),
            ('BOTTOMPADDING', (0, 0), (-1, -1), 8),
            ('TEXTCOLOR', (0, 0), (0, 0), color_scheme.text_primary),
            ('FONTNAME', (0, 0), (0, 0), 'HeiseiKakuGo-W5'),
        ]))
        answer_elements.append(explanation_table)
        
        # 各解答をKeepTogetherでグループ化
        elements.append(KeepTogether(answer_elements))
        elements.append(Spacer(1, 12*mm))
    
    return elements

def generate_pdf_content(data: InputModel) -> bytes:
    """PDFコンテンツを生成"""
    buffer = io.BytesIO()
    doc = SimpleDocTemplate(
        buffer,
        pagesize=A4,
        rightMargin=20*mm,
        leftMargin=20*mm,
        topMargin=20*mm,
        bottomMargin=20*mm
    )
    
    color_scheme = ColorScheme(data.color_mode)
    styles = create_styles(color_scheme)
    elements = []
    
    # タイトル（問題ページ）
    title_data = [[data.title]]
    title_table = Table(title_data, colWidths=[170*mm])
    title_table.setStyle(TableStyle([
        ('BACKGROUND', (0, 0), (-1, -1), color_scheme.white),
        ('TEXTCOLOR', (0, 0), (-1, -1), color_scheme.text_primary),
        ('ALIGN', (0, 0), (-1, -1), 'CENTER'),
        ('VALIGN', (0, 0), (-1, -1), 'MIDDLE'),
        ('FONTNAME', (0, 0), (-1, -1), 'HeiseiKakuGo-W5'),
        ('FONTSIZE', (0, 0), (-1, -1), 18),
        ('TOPPADDING', (0, 0), (-1, -1), 15),
        ('BOTTOMPADDING', (0, 0), (-1, -1), 15),
        ('BOX', (0, 0), (-1, -1), 2, color_scheme.primary),
        ('ROUNDEDCORNERS', [8]),
    ]))
    elements.append(title_table)
    elements.append(Spacer(1, 15*mm))
    
    # 問題ページの内容
    elements.extend(create_question_elements(data.questions, styles, color_scheme))
    
    # ページ区切り
    elements.append(PageBreak())
    
    # タイトル（解答ページ）
    answer_title_data = [[data.title], ['回答・解説']]
    answer_title_table = Table(answer_title_data, colWidths=[170*mm])
    answer_title_table.setStyle(TableStyle([
        ('BACKGROUND', (0, 0), (-1, -1), color_scheme.white),
        ('TEXTCOLOR', (0, 0), (-1, -1), color_scheme.text_primary),
        ('ALIGN', (0, 0), (-1, -1), 'CENTER'),
        ('VALIGN', (0, 0), (-1, -1), 'MIDDLE'),
        ('FONTNAME', (0, 0), (-1, -1), 'HeiseiKakuGo-W5'),
        ('FONTSIZE', (0, 0), (0, 0), 16),
        ('FONTSIZE', (0, 1), (0, 1), 12),
        ('TOPPADDING', (0, 0), (-1, -1), 12),
        ('BOTTOMPADDING', (0, 0), (-1, -1), 12),
        ('BOX', (0, 0), (-1, -1), 2, color_scheme.primary),
        ('ROUNDEDCORNERS', [8]),
    ]))
    elements.append(answer_title_table)
    elements.append(Spacer(1, 15*mm))
    
    # 解答・解説ページの内容
    elements.extend(create_answer_elements(data.questions, styles, color_scheme))
    
    # フッター
    footer_text = f"© 2024 Study Quiz Generator - 生成日: {datetime.now().strftime('%Y年%m月%d日')}"
    footer_para = Paragraph(footer_text, styles['Explanation'])
    elements.append(Spacer(1, 10*mm))
    elements.append(footer_para)
    
    # PDF生成
    doc.build(elements)
    buffer.seek(0)
    return buffer.read()

@app.post("/generate-pdf")
async def generate_pdf(data: InputModel):
    """問題と解答・解説を含むPDFを生成するエンドポイント"""
    try:
        pdf_content = generate_pdf_content(data)
        
        mode_suffix = "color" if data.color_mode else "bw"
        return Response(
            content=pdf_content,
            media_type="application/pdf",
            headers={
                "Content-Disposition": f"attachment; filename=quiz_{mode_suffix}_{datetime.now().strftime('%Y%m%d_%H%M%S')}.pdf"
            }
        )
        
    except Exception as e:
        raise HTTPException(status_code=500, detail=str(e))

@app.post("/generate-pdf-base64")
async def generate_pdf_base64(data: InputModel):
    """Base64エンコードされたPDFを返すエンドポイント"""
    try:
        pdf_content = generate_pdf_content(data)
        pdf_base64 = base64.b64encode(pdf_content).decode('utf-8')
        
        mode_suffix = "color" if data.color_mode else "bw"
        return {
            "status": "success",
            "pdf_base64": pdf_base64,
            "filename": f"quiz_{mode_suffix}_{datetime.now().strftime('%Y%m%d_%H%M%S')}.pdf"
        }
        
    except Exception as e:
        raise HTTPException(status_code=500, detail=str(e))

@app.get("/")
async def root():
    return {"message": "PDF Generator API is running (ReportLab version with color mode support)"}

@app.get("/test-pdf")
async def test_pdf():
    """サンプルデータでPDF生成をテスト（カラーモード）"""
    sample_data = InputModel(
        title="生物学 - 細胞の構造と機能",
        color_mode=True,
        questions=[
            Question(
                question="真核細胞において、タンパク質の合成が行われる細胞小器官はどれか？",
                choices=[
                    "ミトコンドリア",
                    "リボソーム",
                    "ゴルジ体",
                    "リソソーム"
                ],
                answer="B",
                explanation="リボソームは、mRNAの情報を読み取ってアミノ酸を結合させ、タンパク質を合成する細胞小器官です。真核細胞では、リボソームは細胞質（遊離リボソーム）と小胞体表面（付着リボソーム）に存在します。"
            ),
            Question(
                question="植物細胞に存在し、動物細胞には存在しない構造として正しいものはどれか？",
                choices=[
                    "細胞膜",
                    "細胞壁",
                    "核",
                    "細胞質"
                ],
                answer="B",
                explanation="細胞壁は植物細胞特有の構造で、主にセルロースから構成されています。細胞壁は細胞膜の外側に存在し、細胞の形を保ち、外部からの物理的ストレスから細胞を保護する役割があります。"
            ),
            Question(
                question="ATP（アデノシン三リン酸）が「細胞のエネルギー通貨」と呼ばれる理由として最も適切なものはどれか？",
                choices=[
                    "細胞内で最も多く存在する物質だから",
                    "高エネルギーリン酸結合を持ち、エネルギーの貯蔵と供給に適しているから",
                    "タンパク質の構成成分だから",
                    "細胞膜を通過しやすいから"
                ],
                answer="B",
                explanation="ATPは3つのリン酸基を持ち、末端の2つのリン酸結合は高エネルギーリン酸結合と呼ばれます。この結合が加水分解されると大量のエネルギーが放出され、細胞内の様々な反応に利用されます。"
            ),
            Question(
                question="細胞分裂の過程で、染色体が赤道面に並ぶ時期はどれか？",
                choices=[
                    "前期",
                    "中期",
                    "後期",
                    "終期"
                ],
                answer="B",
                explanation="細胞分裂の中期（metaphase）では、紡錘体が完全に形成され、全ての染色体が細胞の赤道面（中央部）に整列します。この配置により、姉妹染色分体が均等に分離される準備が整います。"
            )
        ]
    )
    
    return await generate_pdf(sample_data)

@app.get("/test-pdf-bw")
async def test_pdf_bw():
    """サンプルデータでPDF生成をテスト（白黒モード）"""
    sample_data = InputModel(
        title="生物学 - 細胞の構造と機能",
        color_mode=False,
        questions=[
            Question(
                question="真核細胞において、タンパク質の合成が行われる細胞小器官はどれか？",
                choices=[
                    "ミトコンドリア",
                    "リボソーム",
                    "ゴルジ体",
                    "リソソーム"
                ],
                answer="B",
                explanation="リボソームは、mRNAの情報を読み取ってアミノ酸を結合させ、タンパク質を合成する細胞小器官です。真核細胞では、リボソームは細胞質（遊離リボソーム）と小胞体表面（付着リボソーム）に存在します。"
            )
        ]
    )
    
    return await generate_pdf(sample_data)

if __name__ == "__main__":
    import uvicorn
    uvicorn.run(app, host="0.0.0.0", port=8000)