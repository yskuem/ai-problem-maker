from fastapi import FastAPI, UploadFile, File, HTTPException
from fastapi.responses import JSONResponse
from google import genai
from google.genai import types
import io
import os
import uvicorn
import uuid

app = FastAPI()
client = genai.Client(
    api_key=os.getenv("GEMINI_API_KEY"),
)

tools = [
    types.Tool(
        function_declarations=[
            types.FunctionDeclaration(
                name="create_english_questions",
                description="画像の資料から複数の4択問題を作成する。",
                parameters=genai.types.Schema(
                    type=genai.types.Type.OBJECT,
                    required=["questions"],
                    properties={
                        "title": genai.types.Schema(
                            type=genai.types.Type.STRING,
                            description="クイズ全体の大まかなタイトル",
                        ),
                        "questions": genai.types.Schema(
                            type=genai.types.Type.ARRAY,
                            description="四択問題のリスト。問題を作成できるだけ多く作成する。",
                            items=genai.types.Schema(
                                type=genai.types.Type.OBJECT,
                                required=["question", "choices", "answer", "explanation", "category"],
                                properties={
                                    "question": genai.types.Schema(
                                        type=genai.types.Type.STRING,
                                        description="問題文",
                                    ),
                                    "choices": genai.types.Schema(
                                        type=genai.types.Type.ARRAY,
                                        description="4つの選択肢。選択肢にはanswerと文字列が完全一致した選択肢を必ず1つ含めること。",
                                        items=genai.types.Schema(
                                            type=genai.types.Type.STRING,
                                        ),
                                    ),
                                    "answer": genai.types.Schema(
                                        type=genai.types.Type.STRING,
                                        description="問題の正解",
                                    ),
                                    "explanation": genai.types.Schema(
                                        type=genai.types.Type.STRING,
                                        description="解説",
                                    ),
                                },
                            ),
                        ),
                    },
                ),
            ),
        ]
    )
]

@app.post("/analyze_image")
async def analyze_image(file: UploadFile = File(...)):
    try:
        # ファイル読み込み
        file_bytes = await file.read()
        file_obj = io.BytesIO(file_bytes)
        file_obj.name = file.filename
        file_obj.seek(0)

        # アップロード設定として MIME タイプ／表示名を渡す
        upload_config = types.UploadFileConfig(
            mime_type=file.content_type,
            display_name=file.filename,
        )

        # ファイルをアップロード
        uploaded = client.files.upload(
            file=file_obj,
            config=upload_config,
        )

        generate_content_config = types.GenerateContentConfig(
            response_mime_type="text/plain",
            tools=tools
        )

        # モデル呼び出し
        result = client.models.generate_content(
            model="gemini-2.5-flash-preview-04-17",
            contents=[uploaded, "\n\n", "この画像から複数の問題を作成してください。問題は画像の言語で出力してください。"],
            config=generate_content_config,
        )

        # function calling から返ってきたオブジェクトに group_id を付与
        raw_calls = result.function_calls or []
        enriched = []
        # 呼び出しごとに一意のグループIDを付与
        for fc in raw_calls:
            # 一意な UUID を生成して group_id として使用
            group_id = str(uuid.uuid4())
            # Pydantic FunctionCall オブジェクトを dict に変換
            try:
                fc_dict = fc.dict()
            except Exception:
                fc_dict = fc.to_dict()
            # group_id を追加
            fc_dict["group_id"] = group_id
            enriched.append(fc_dict)

        return enriched

    except Exception as e:
        raise HTTPException(status_code=500, detail=str(e))

if __name__ == "__main__":
    uvicorn.run(app, host="0.0.0.0", port=8000)

