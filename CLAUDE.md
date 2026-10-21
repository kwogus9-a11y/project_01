# 프로젝트 개요
- frontend: React + TypeScript + Vite (포트 5173)
- backend: Spring Boot, Java 21, Gradle (포트 8080)

# 명령어
- 프론트 실행: cd frontend && npm run dev
- 백엔드 실행: cd backend && ./gradlew bootRun
- 백엔드 테스트: cd backend && ./gradlew test

# 규칙
- API 경로는 /api/** 로 시작
- 백엔드는 controller / service / repository / dto 계층 구조
- 엔티티를 API 응답으로 직접 반환하지 말고 DTO 사용
- 새 기능에는 테스트 작성