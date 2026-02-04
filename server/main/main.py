from fastapi import FastAPI, UploadFile, File, HTTPException
from fastapi.responses import JSONResponse
from google import genai
from google.genai import types
from pydantic import BaseModel
import io
import os
import uuid
import uvicorn
from typing import Any, Callable, Dict, Tuple, Type

app = FastAPI()

# Gemini クライアントの初期化
client = genai.Client(api_key=os.getenv("GEMINI_API_KEY"))

#
# Pydanticモデル定義
#

class ImageQuestion(BaseModel):
    question: str
    choices: list[str]
    answer: str
    explanation: str

class QuizOutput(BaseModel):
    title: str
    questions: list[ImageQuestion]

class NoteOutput(BaseModel):
    title: str
    html: str

#
# 共通処理関数
#

async def handle_generation(
    file: UploadFile,
    prompt: str,
    response_schema: Type[BaseModel],
    id_key: str,
) -> Dict[str, Any]:
    """
    1. 画像ファイルを読み込み
    2. Geminiにアップロード
    3. 指定プロンプトでモデル呼び出し
    4. Pydanticモデルにパース
    5. UUIDを付与して dict を返却
    """
    # 1. ファイル読み込み
    file_bytes = await file.read()
    file_obj = io.BytesIO(file_bytes)
    file_obj.name = file.filename
    file_obj.seek(0)

    # 2. ファイルアップロード
    upload_config = types.UploadFileConfig(
        mime_type=file.content_type,
        display_name=file.filename,
    )
    uploaded = client.files.upload(file=file_obj, config=upload_config)

    # 3. モデル呼び出し
    generate_config = types.GenerateContentConfig(
        response_mime_type="application/json",
        response_schema=response_schema,
    )
    contents = [uploaded, "\n\n" + prompt]
    result = client.models.generate_content(
        model="gemini-flash-lite-latest",
        contents=contents,
        config=generate_config,
    )

    # 4. パース済みオブジェクト取得
    parsed = result.parsed
    if parsed is None:
        raise HTTPException(status_code=500, detail="モデルから有効なレスポンスが返されませんでした。")

    # 5. UUID付与
    obj = parsed.dict()
    obj[id_key] = str(uuid.uuid4())
    return obj

#
# エンドポイント定義
#

@app.post("/generate_quizzes")
async def generate_quizzes(file: UploadFile = File(...)):
    prompt = (
        "画像からクイズを生成してください。"
        "クイズ全体のタイトルと、複数の4択問題を含むJSON形式で出力してください。"
    )
    # handle_generation は一問分ではなく QuizOutput 全体を返すので、
    # 質問ごとの UUID 付与処理はここで行います。
    quiz_obj = await handle_generation(file, prompt, QuizOutput, id_key="group_id")
    # group_id を生成値から取り出し、questions に個別IDを振り分け
    group_id = quiz_obj.pop("group_id")
    enriched_questions = []
    for q in quiz_obj["questions"]:
        q["id"] = str(uuid.uuid4())
        q["group_id"] = group_id
        q["title"] = quiz_obj["title"]
        enriched_questions.append(q)
    return JSONResponse(content=enriched_questions)

@app.post("/generate_note")
async def generate_note(file: UploadFile = File(...)):
    prompt = (
        "画像の資料内容をhtmlで分かりやすくまとめてください。"
        "サイズはスマホで閲覧することを想定してください。"
        "美しくカラフルなレイアウトで視覚的にわかりやすいデザインにしてください。"
        "内容の漏れはないようにしてください。"
    )
    note_obj = await handle_generation(file, prompt, NoteOutput, id_key="id")
    # id は handle_generation で付与済み、title/html は note_obj に含まれる
    return JSONResponse(content=note_obj)

if __name__ == "__main__":
    uvicorn.run(app, host="0.0.0.0", port=8000)
