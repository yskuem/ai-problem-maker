from fastapi import FastAPI, UploadFile, File, HTTPException
from fastapi.responses import JSONResponse
from google import genai
from google.genai import types
import io
import os
import uvicorn

app = FastAPI()
client = genai.Client(
    api_key=os.getenv("GEMINI_API_KEY"),
)

@app.post("/analyze_image")
async def analyze_image(file: UploadFile = File(...)):
    try:
        # ファイル読み込み
        file_bytes = await file.read()
        file_obj = io.BytesIO(file_bytes)
        file_obj.name = file.filename
        # ストリーム位置を先頭に戻す
        file_obj.seek(0)

        # アップロード設定として MIME タイプ／表示名を渡す
        upload_config = types.UploadFileConfig(
            mime_type=file.content_type,
            display_name=file.filename,
        )

        # ファイルをアップロード（config 引数を追加）
        uploaded = client.files.upload(
            file=file_obj,
            config=upload_config,
        )

        generate_content_config = types.GenerateContentConfig(
            response_mime_type="text/plain",
            tools = tools
        )

        # モデル呼び出し
        result = client.models.generate_content(
            model="gemini-2.5-flash-preview-04-17",
            contents=[
                uploaded,
                "\n\n",
                "この画像から複数の問題を作成してください。"
            ],
            config=generate_content_config,
        )

        return result.function_calls

    except Exception as e:
        raise HTTPException(status_code=500, detail=str(e))

if __name__ == "__main__":
    uvicorn.run(app, host="0.0.0.0", port=8000)




tools = [
        types.Tool(
            function_declarations=[
                types.FunctionDeclaration(
                    name="create_english_questions",
                    description="画像の資料から複数の4択問題を作成する。",
                    parameters=genai.types.Schema(
                        type = genai.types.Type.OBJECT,
                        required = ["questions"],
                        properties = {
                            "questions": genai.types.Schema(
                                type = genai.types.Type.ARRAY,
                                description = "四択問題のリスト。問題を作成できるだけ多く作成する。",
                                items = genai.types.Schema(
                                    type = genai.types.Type.OBJECT,
                                    required = ["question", "choices", "answer", "explanation", "category"],
                                    properties = {
                                        "question": genai.types.Schema(
                                            type = genai.types.Type.STRING,
                                            description = "問題文",
                                        ),
                                        "choices": genai.types.Schema(
                                            type = genai.types.Type.ARRAY,
                                            description = "4つの選択肢",
                                            items = genai.types.Schema(
                                                type = genai.types.Type.STRING,
                                            ),
                                        ),
                                        "answer": genai.types.Schema(
                                            type = genai.types.Type.STRING,
                                            description = "問題の正解",
                                        ),
                                        "explanation": genai.types.Schema(
                                            type = genai.types.Type.STRING,
                                            description = "解説",
                                        ),
                                        "category": genai.types.Schema(
                                            type = genai.types.Type.STRING,
                                            description = "問題のカテゴリ",
                                        ),
                                    },
                                ),
                            ),
                        },
                    ),
                ),
            ])
    ]
