![Auto Assign](https://github.com/TimeBank-Sparta/demo-repository/actions/workflows/auto-assign.yml/badge.svg)

![Proof HTML](https://github.com/TimeBank-Sparta/demo-repository/actions/workflows/proof-html.yml/badge.svg)

# Time Bank (타임뱅크)

> **“시간을 화폐처럼 거래하는 커뮤니티 기반 상호 도움 플랫폼”**

---

## 🎯 프로젝트 목표

타임뱅크는 **시간(Time)** 을 거래 단위로 활용해, 서로 도움을 주고받는 신개념 플랫폼입니다.  
경제적·물리적 제약 없이 사용자 간 서비스 거래를 가능케 하고, **신뢰 구축**과 **데이터 무결성 관리**, **효율적인 매칭 시스템** 구현을 목표로 합니다.

---

## 🔍 주제 및 개요

### 🕑 주제
- 시간 기반 커뮤니티 서비스 거래 플랫폼

### 📋 개요
- 사용자들은 자신의 시간을 투자해 서비스를 제공하고, 제공한 시간만큼 타인의 서비스를 받을 수 있습니다.  
- 로컬 지역사회 결속력 강화, 사용자 간 신뢰 구축을 지향합니다.  
- **MSA**(Microservices Architecture) 적용으로 **확장성**, **유연성**, **유지 보수성**을 확보합니다.  
- AI 기술(Gemini API) 도입을 통해 콘텐츠 작성, 매칭 최적화 등 다양한 보조 기능을 제공합니다.

---

## 🚀 주요 기능 & 서비스 프로세스

1. **회원 가입 & 초기 포인트 지급**  
   - 가입 시 기본 시간 포인트 지급  
   - 프로필 설정 (제공 가능한 서비스, 필요한 서비스)

2. **서비스 요청 & 신청**  
   - 도움 요청글 작성 → 포인트 임시 차감  
   - 제공자 신청 → 선착순 또는 매칭 로직에 따라 선정

3. **서비스 거래 시작**  
   - 제공자 “서비스 시작” 클릭 → 요청자 승인  
   - 시간 자동 기록 및 진행 상태 업데이트

4. **거래 종료 & 포인트 정산**  
   - 제공자 “서비스 종료” 클릭 → 요청자 최종 승인  
   - 최종 승인 시 포인트 정산 및 사용자 잔여 시간 업데이트

5. **리뷰 & 신뢰도 관리**  
   - 거래 완료 후 상호 리뷰 및 평점 작성  
   - 평점 반영 → 사용자 신뢰도, 후속 거래 우선순위에 활용

6. **예외 상황 처리**  
   - 포인트 부족, 서비스 미이행, 거래 중단 등  
   - 자동 처리 로직 또는 관리자의 개입

---

## 🏗️ 아키텍처 & 기술 스택

- **MSA 구성**  
  - Config Server (Git-based)  
  - Eureka Server (서비스 레지스트리)  
  - API Gateway (Blue-Green 배포)  
  - 개별 마이크로서비스 (Help, Notification, Review, Point, User)  
  - 데이터베이스: MySQL (서비스별)  
  - 인프라: Redis, Kafka, Zookeeper  
  - 모니터링: Prometheus & Grafana

- **CI/CD 파이프라인**  
  - GitHub Actions → 변경된 모듈만 빌드/푸시  
  - Docker Hub → EC2 Blue-Green 배포  
  - Slack 알림 (성공/실패)

- **AI 통합**  
  - Gemini API 활용: 구인 공고, 콘텐츠 보조 작성 등

---

## 📈 서비스 흐름도

    1) 회원가입 → 프로필 설정  
      ↓  
    2) 시간 포인트 지급 → 포인트 관리  
      ↓  
    3) 도움 요청글 작성 (포인트 임시 차감)  
      ↓  
    4) 제공자 신청 → 요청자 승인  
      ↓  
    5) 서비스 진행 (시작/종료 → 승인)  
      ↓  
    6) 포인트 정산 → 잔여 포인트 업데이트  
      ↓  
    7) 거래 완료 후 리뷰 및 평점 작성  

---

## ⚙️ 설치 및 실행

    1. 레포지토리 클론  
       $ git clone https://github.com/your-org/timebank.git  
       $ cd timebank  

    2. 환경 변수 설정  
       $ cp .env-tag.example .env-tag  
       // .env-tag 에 TAG, DB 비밀번호 등 설정  

    3. Docker Compose 빌드 & 실행  
       $ docker-compose up -d --build  

    4. 접속 확인  
       - Eureka: http://localhost:8761  
       - Gateway 헬스체크: http://localhost/actuator/health  
       - 개별 서비스 헬스: http://localhost:8081/actuator/health 등  

---

## 📚 브랜치 전략 & 코드 컨벤션

- **브랜치 전략**  
  - `main`: 프로덕션  
  - `develop`: 통합 개발  
  - `feature/*`, `hotfix/*` 활용  

- **코딩 컨벤션**  
  - 메소드·변수명 통일 (예: `getOrders` vs `retrieveProducts` 중 하나)  
  - 공통 모듈 SemVer (MAJOR.MINOR.PATCH)  

---

## 🤝 기여 가이드

1. Fork & Clone  
2. `feature/xxx` 브랜치 생성  
3. Commit → PR 요청  
4. 리뷰 후 Merge  

---

## 📝 라이선스

MIT © Your Name  
