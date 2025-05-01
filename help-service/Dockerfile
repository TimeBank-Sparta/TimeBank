# ----- Builder Stage -----
FROM gradle:7.6-jdk17 as builder

# 작업 디렉토리 설정
WORKDIR /app

# common 모듈과 wait-for-it.sh 스크립트를 복사
COPY common /app/common/
COPY wait-for-it.sh /wait-for-it.sh
RUN chmod +x /wait-for-it.sh

# common 모듈 빌드 및 로컬 Maven 저장소에 발행
WORKDIR /app/common
RUN gradle clean build publishToMavenLocal -x test

# help-service 모듈 복사 및 빌드
WORKDIR /app
COPY help-service /app/help-service/
WORKDIR /app/help-service
RUN gradle clean bootJar -x test

# ----- Final Runtime Image -----
FROM eclipse-temurin:17-jre

# Builder 스테이지에서 wait-for-it.sh를 복사 (최종 이미지에 포함)
COPY --from=builder /wait-for-it.sh /wait-for-it.sh

# 빌드한 help 애플리케이션 jar 복사
COPY --from=builder /app/help-service/build/libs/*.jar /app/app.jar

# ENTRYPOINT: Config Server와 Eureka가 모두 준비될 때까지 대기한 후 애플리케이션 실행
ENTRYPOINT [
  "/wait-for-it.sh", "config-server:8888", "--timeout=15", "--strict", "--",
  "/wait-for-it.sh", "eureka:8761",         "--timeout=15", "--strict", "--",
  "/wait-for-it.sh", "gateway-service:8080", "--timeout=15", "--strict", "--",
  "java", "-jar", "/app/app.jar"
]
