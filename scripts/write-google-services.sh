#!/usr/bin/env bash
set -euo pipefail

if [[ -z "${DEV_GOOGLE_SERVICES_JSON_BASE64:-}" ]]; then
  echo "DEV_GOOGLE_SERVICES_JSON_BASE64 is not set. Skipping google-services.json creation." >&2
  exit 0
fi

mkdir -p composeApp/src/dev

python <<'PY'
import base64
import os
from pathlib import Path

output_path = Path("composeApp/src/dev/google-services.json")
output_path.write_bytes(base64.b64decode(os.environ["DEV_GOOGLE_SERVICES_JSON_BASE64"]))
PY
