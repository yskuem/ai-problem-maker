import sys
import types
import io
import json
import os
import asyncio
import pytest
from fastapi import UploadFile

# Ensure the server package is importable
sys.path.append(os.path.abspath(os.path.join(os.path.dirname(__file__), "..")))

# Stub modules required during import
multipart = types.ModuleType('multipart')
multipart.__version__ = '0.0'
sub = types.ModuleType('multipart.multipart')
sub.parse_options_header = lambda v: ('', {})
multipart.multipart = sub
sys.modules.setdefault('multipart', multipart)
sys.modules.setdefault('multipart.multipart', sub)

fake_google = types.ModuleType('google')
fake_genai = types.ModuleType('google.genai')
fake_genai.Client = lambda *a, **k: None
fake_genai.types = types.SimpleNamespace(
    UploadFileConfig=lambda *a, **k: None,
    GenerateContentConfig=lambda *a, **k: None,
)
fake_google.genai = fake_genai
sys.modules.setdefault('google', fake_google)
sys.modules.setdefault('google.genai', fake_genai)

import server.api.main as main

class DummyResult:
    def __init__(self, parsed):
        self.parsed = parsed

class DummyClient:
    def __init__(self, parsed):
        self.files = types.SimpleNamespace(upload=lambda file, config: 'uploaded')
        self.models = types.SimpleNamespace(
            generate_content=lambda **kwargs: DummyResult(parsed)
        )


def _upload_file() -> UploadFile:
    return UploadFile(filename="image.png", file=io.BytesIO(b"data"))


def test_handle_generation(monkeypatch):
    parsed = main.NoteOutput(title="t", html="h")
    monkeypatch.setattr(main, "client", DummyClient(parsed))
    result = asyncio.run(
        main.handle_generation(_upload_file(), "prompt", main.NoteOutput, id_key="id")
    )
    assert result["title"] == "t"
    assert "id" in result


def test_generate_note(monkeypatch):
    async def fake_handle(file, prompt, schema, id_key):
        return {"title": "t", "html": "h", "id": "123"}

    monkeypatch.setattr(main, "handle_generation", fake_handle)
    response = asyncio.run(main.generate_note(_upload_file()))
    body = json.loads(response.body.decode())
    assert body == {"title": "t", "html": "h", "id": "123"}


def test_generate_quizzes(monkeypatch):
    async def fake_handle(file, prompt, schema, id_key):
        return {
            "title": "quiz",
            "group_id": "gid",
            "questions": [
                {
                    "question": "Q1",
                    "choices": ["A"],
                    "answer": "A",
                    "explanation": "E",
                }
            ],
        }

    monkeypatch.setattr(main, "handle_generation", fake_handle)
    response = asyncio.run(main.generate_quizzes(_upload_file()))
    body = json.loads(response.body.decode())
    assert body[0]["group_id"] == "gid"
    assert body[0]["title"] == "quiz"
    assert "id" in body[0]
