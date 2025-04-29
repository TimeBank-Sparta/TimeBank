# gateway-blue 만 배포
# !/usr/bin/env bash
set -e

# ① TAG 자동 생성
export TAG=$(date +'%Y%m%d%H%M%S')

echo "→ Deploying with TAG=${TAG}"

# ② (필요하다면) .env 파일로도 기록
echo "TAG=${TAG}" > .env

# ③ Compose 실제 실행
docker-compose up -d gateway-blue
