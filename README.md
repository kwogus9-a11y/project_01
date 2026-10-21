# project_01

React 프론트엔드와 Spring Boot 백엔드로 구성된 할 일(Todo) 관리 웹 애플리케이션입니다.

## 기술 스택

| 구분 | 기술 |
|---|---|
| Frontend | React 19, TypeScript, Vite 8, Vitest, Testing Library |
| Backend | Spring Boot 4.1, Java 21, Gradle, Spring Data JPA, Bean Validation |
| Database | H2 (인메모리, 개발용) |

## 프로젝트 구조

```
project_01/
├── frontend/                  React + TypeScript + Vite (포트 5173)
│   └── src/
│       ├── api/               백엔드 API 호출
│       ├── components/        UI 컴포넌트
│       ├── types/             API 타입 정의
│       └── App.tsx            메인 화면
└── backend/                   Spring Boot (포트 8080)
    └── src/main/java/com/example/backend/
        ├── controller/        REST API 엔드포인트
        ├── service/           비즈니스 로직
        ├── repository/        데이터 접근 (JPA)
        ├── domain/            JPA 엔티티
        ├── dto/               요청/응답 DTO
        └── exception/         예외 및 전역 예외 처리
```

## 사전 준비

- JDK 21
- Node.js 20 이상 (LTS 권장)

## 실행 방법

백엔드와 프론트엔드를 각각 다른 터미널에서 실행합니다.

```bash
# 백엔드 (http://localhost:8080)
cd backend
./gradlew bootRun

# 프론트엔드 (http://localhost:5173)
cd frontend
npm install
npm run dev
```

브라우저에서 http://localhost:5173 에 접속합니다.
프론트엔드의 `/api` 요청은 Vite 프록시를 통해 백엔드(8080)로 전달됩니다.

> Windows에서는 `./gradlew` 대신 `gradlew.bat`을 사용할 수 있습니다.

## 테스트

```bash
# 백엔드
cd backend
./gradlew test

# 프론트엔드
cd frontend
npm test
```

## API

모든 API는 `/api`로 시작합니다.

| Method | Path | 설명 |
|---|---|---|
| GET | `/api/todos` | 할 일 목록 조회 |
| GET | `/api/todos/{id}` | 할 일 단건 조회 |
| POST | `/api/todos` | 할 일 생성 |
| PUT | `/api/todos/{id}` | 할 일 수정 |
| DELETE | `/api/todos/{id}` | 할 일 삭제 |

요청 예시:

```json
// POST /api/todos
{ "title": "공부하기" }

// PUT /api/todos/1
{ "title": "공부하기", "completed": true }
```

오류 응답 형식:

```json
{ "status": 404, "message": "Todo not found: 1" }
```

## 개발 규칙

- API 경로는 `/api/**`로 시작합니다.
- 백엔드는 controller / service / repository / dto 계층 구조를 따릅니다.
- 엔티티를 API 응답으로 직접 반환하지 않고 DTO를 사용합니다.
- 새 기능에는 테스트를 작성합니다.

## 참고

- H2 콘솔: 백엔드 실행 중 http://localhost:8080/h2-console
  (JDBC URL: `jdbc:h2:mem:backend`, 사용자: `sa`, 비밀번호 없음)
- H2는 인메모리 DB이므로 백엔드를 재시작하면 데이터가 초기화됩니다.
