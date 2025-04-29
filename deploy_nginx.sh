#!/bin/bash
cd ~/timebank

# 1) Blue 컨테이너가 없으면 먼저 생성
if ! docker ps --filter name=gateway-blue --filter status=running | grep -q gateway-blue; then
  docker-compose up -d gateway-blue
fi

# 2) Nginx 실행 (이미 올라갔으면 재시작)
docker-compose up -d nginx

echo "Nginx is now proxying to gateway-blue"
