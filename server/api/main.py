from fastapi import FastAPI, UploadFile, File, HTTPException
from fastapi.responses import JSONResponse
from google import genai
from google.genai import types
from pydantic import BaseModel
import io
import os
import uvicorn
import uuid
from typing import Optional, List

app = FastAPI()
client = genai.Client(
    api_key=os.getenv("GEMINI_API_KEY"),
)

# Pydanticモデル: 単一の4択問題
class ImageQuestion(BaseModel):
    question: str
    options: List[str]
    answer: str

# Pydanticモデル: クイズ全体の出力
class QuizOutput(BaseModel):
    title: str
    questions: List[ImageQuestion]

@app.post("/analyze_image")
async def analyze_image(file: UploadFile = File(...)):
    try:
        # ファイル読み込み
        file_bytes = await file.read()
        file_obj = io.BytesIO(file_bytes)
        file_obj.name = file.filename
        file_obj.seek(0)

        # ファイルアップロード
        upload_config = types.UploadFileConfig(
            mime_type=file.content_type,
            display_name=file.filename,
        )
        uploaded = client.files.upload(
            file=file_obj,
            config=upload_config,
        )

        # モデル呼び出し: Structured Output (JSON) with overall quiz title
        generate_config = types.GenerateContentConfig(
            response_mime_type="application/json",
            response_schema=QuizOutput
        )
        prompt = (
            "画像からクイズを生成してください。"
            "クイズ全体のタイトルと、複数の4択問題を含むJSON形式で出力してください。"
        )
        result = client.models.generate_content(
            model="gemini-2.5-flash-preview-04-17",
            contents=[uploaded, "\n\n" + prompt],
            config=generate_config,
        )

        # パース済みオブジェクトを取得
        quiz: QuizOutput = result.parsed
        if not quiz or not quiz.questions:
            raise HTTPException(status_code=500, detail="モデルから有効なクイズデータが返されませんでした。")

        # 質問群IDを生成
        group_uuid = str(uuid.uuid4())
        enriched = []
        for img_q in quiz.questions:
            question_id = str(uuid.uuid4())
            q_dict = img_q.dict()
            # 全体タイトルと group_id, 個別ID を注入
            q_dict.update({
                "id": question_id,
                "group_id": group_uuid,
                "title": quiz.title
            })
            enriched.append(q_dict)

        return JSONResponse(content=enriched)

    except Exception as e:
        raise HTTPException(status_code=500, detail=str(e))

if __name__ == "__main__":
    uvicorn.run(app, host="0.0.0.0", port=8000)
