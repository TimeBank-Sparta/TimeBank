FROM gradle:7.6-jdk17 as builder

# 작업 디렉토리 설정
WORKDIR /app

WORKDIR /app
COPY config /app/config/
WORKDIR /app/config
RUN gradle clean bootJar -x test

EXPOSE 8888

# 최종 실행 이미지 설정
FROM eclipse-temurin:17-jre
COPY --from=builder /app/config/build/libs/*.jar /app/app.jar
ENTRYPOINT ["java", "-jar", "/app/app.jar"]