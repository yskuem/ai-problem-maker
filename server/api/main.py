from fastapi import FastAPI, UploadFile, File, HTTPException
from fastapi.responses import JSONResponse
from google import genai
from google.genai import types
from pydantic import BaseModel
import io
import os
import uvicorn
import uuid
from typing import Optional

app = FastAPI()
client = genai.Client(
    api_key=os.getenv("GEMINI_API_KEY"),
)

# Pydanticモデル: 画像から生成される4択問題
class ImageQuestion(BaseModel):
    title: Optional[str]
    question: str
    options: list[str]
    answer: str

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

        # モデル呼び出し: Structured Output (JSON)
        generate_config = types.GenerateContentConfig(
            response_mime_type="application/json",
            response_schema=list[ImageQuestion]
        )
        result = client.models.generate_content(
            model="gemini-2.5-flash-preview-04-17",
            contents=[uploaded, "\n\n画像から4択問題を複数生成してください。"],
            config=generate_config,
        )

        # パース済みオブジェクトを取得
        raw_questions = result.parsed or []

        # 質問群IDを生成
        group_uuid = str(uuid.uuid4())
        enriched = []
        for q in raw_questions:
            question_id = str(uuid.uuid4())
            # dict化してID, group_idを注入
            q_dict = q.dict()
            q_dict.update({
                "id": question_id,
                "group_id": group_uuid
            })
            enriched.append(q_dict)

        return JSONResponse(content=enriched)

    except Exception as e:
        raise HTTPException(status_code=500, detail=str(e))

if __name__ == "__main__":
    uvicorn.run(app, host="0.0.0.0", port=8000)
