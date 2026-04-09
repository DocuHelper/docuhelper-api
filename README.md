# DocuHelper API

DocuHelper의 메인 백엔드 API 서버입니다. Kotlin과 Spring Boot 기반의 멀티모듈 프로젝트로, GraphQL을 통해 문서 관리, 채팅, 파일 업로드 등의 기능을 제공합니다.

---

## 목차

- [프로젝트 개요](#프로젝트-개요)
- [기술 스택](#기술-스택)
- [프로젝트 구조](#프로젝트-구조)
- [사전 요구사항](#사전-요구사항)
- [로컬 개발 환경 설정](#로컬-개발-환경-설정)
- [빌드 및 실행](#빌드-및-실행)
- [주요 API 엔드포인트](#주요-api-엔드포인트)
- [환경 변수](#환경-변수)
- [관련 서비스](#관련-서비스)

---

## 프로젝트 개요

DocuHelper API는 문서 기반 AI 지식관리 플랫폼의 핵심 백엔드 서버입니다.  
주요 기능은 다음과 같습니다.

- 문서 업로드 및 파싱 요청 처리
- 문서 벡터 임베딩 기반 의미 검색 (Elasticsearch + AI)
- GraphQL 기반 클라이언트 인터페이스 제공
- Kafka 이벤트 기반 마이크로서비스 연동 (파서, 임베딩 서비스)
- JWT 기반 사용자 인증 및 토큰 관리
- Server-Sent Events (SSE) 방식의 실시간 채팅 응답 스트리밍

---

## 기술 스택

| 분류 | 기술 |
|------|------|
| 언어 | Kotlin 1.9.25 |
| 프레임워크 | Spring Boot 3.4.4 |
| API 방식 | GraphQL (graphql-kotlin-spring-server 8.3.0) |
| 반응형 | Spring WebFlux, Coroutines, Reactor |
| 데이터베이스 | PostgreSQL (R2DBC 비동기) |
| 검색 엔진 | Elasticsearch |
| 캐시/메시징 | Redis, Apache Kafka |
| AI 연동 | OpenAI API, Ollama (임베딩) |
| 인증 | JWT (jjwt 0.12.6) |
| 빌드 도구 | Gradle (Kotlin DSL) |
| 배포 | Docker, Kubernetes |

---

## 프로젝트 구조

```
docuhelper-api/
├── api-module/
│   ├── app/           # 메인 애플리케이션 (Spring Boot 실행 진입점)
│   ├── gateway/       # GraphQL 스키마 및 리졸버 (Query / Mutation / Subscription)
│   ├── event/         # Kafka 이벤트 발행 및 수신
│   ├── user-notice/   # 사용자 알림 처리
│   └── user-client/   # 외부 사용자 서비스 클라이언트
├── common-module/
│   ├── core/          # 도메인 모델, UseCase, OutPort 인터페이스
│   ├── ai/            # AI 임베딩 클라이언트 (OpenAI / Ollama)
│   ├── docuhelper-file/ # 파일 서비스 연동
│   └── redis/         # Redis 설정 및 유틸리티
├── build.gradle.kts   # 루트 Gradle 빌드 설정
├── settings.gradle.kts
├── docker-compose.yml
└── docuhelper-api.yaml  # Kubernetes 배포 스펙
```

---

## 사전 요구사항

| 항목 | 버전 |
|------|------|
| JDK | 17 이상 |
| Gradle | Wrapper 사용 (`./gradlew`) |
| Docker | 최신 버전 (인프라 구동 시) |
| PostgreSQL | 13 이상 |
| Redis | 6 이상 |
| Kafka | 2.x / 3.x |
| Elasticsearch | 8.x |

---

## 로컬 개발 환경 설정

### 1. 레포지토리 클론

```bash
git clone https://github.com/DocuHelper/docuhelper-api.git
cd docuhelper-api
```

### 2. 인프라 서비스 실행 (Docker Compose)

```bash
docker-compose up -d
```

> PostgreSQL, Redis, Kafka, Elasticsearch 등 외부 서비스를 별도로 구동해야 합니다.  
> 팀 내부의 개발 인프라 주소를 사용하거나, 로컬 Docker 환경을 직접 설정하세요.

### 3. 환경 변수 설정

`api-module/app/src/main/resources/application-default.yml`을 참고하여  
로컬 환경에 맞게 접속 정보를 수정하거나, 환경 변수를 설정합니다.

```bash
export OPENAI_API_KEY=your_openai_api_key
```

---

## 빌드 및 실행

### 빌드

```bash
# 전체 빌드 (테스트 포함)
./gradlew build

# 테스트 제외 빌드
./gradlew build -x test
```

### 로컬 실행

```bash
./gradlew :api-module:app:bootRun
```

서버는 기본적으로 **`http://localhost:8080`** 에서 실행됩니다.

### Docker 빌드 및 실행

```bash
# Docker 이미지 빌드
docker build -t docuhelper-api .

# 컨테이너 실행
docker-compose up
```

---

## 주요 API 엔드포인트

이 서비스는 **GraphQL** 방식으로 API를 제공합니다.

| 엔드포인트 | 설명 |
|-----------|------|
| `POST /graphql` | GraphQL 쿼리 및 뮤테이션 |
| `GET /graphql` | GraphQL Subscription (WebSocket / SSE) |
| `GET /graphiql` | GraphiQL 브라우저 기반 IDE (개발 환경) |

### 주요 GraphQL 도메인

#### 문서 (Document)
- `Query.documents` — 문서 목록 조회
- `Mutation.createDocument` — 문서 생성 (파싱 파이프라인 트리거)
- `Mutation.deleteDocument` — 문서 삭제

#### 파일 (File)
- `Mutation.createUploadUrl` — 파일 업로드 Presigned URL 발급

#### 채팅 (Chat)
- `Query.chats` — 채팅 목록 조회
- `Mutation.sendChat` — AI 채팅 전송
- `Query.answerRefs` — 답변 참조 문서 청크 조회

#### 인증 (Auth)
- `Query.auth` — 현재 사용자 인증 정보 조회

---

## 환경 변수

| 변수명 | 설명 | 예시 |
|--------|------|------|
| `OPENAI_API_KEY` | OpenAI API 키 | `sk-...` |
| `SPRING_R2DBC_URL` | PostgreSQL R2DBC 접속 URL | `r2dbc:pool:postgres://localhost:5432/docuhelper` |
| `SPRING_R2DBC_USERNAME` | DB 사용자명 | `postgres` |
| `SPRING_R2DBC_PASSWORD` | DB 비밀번호 | `password` |
| `SPRING_SECURITY_JWT_SECRET` | JWT 서명 시크릿 키 | (256-bit hex string) |
| `SPRING_KAFKA_BOOTSTRAP_SERVERS` | Kafka 브로커 주소 | `localhost:9092` |
| `SPRING_DATA_REDIS_HOST` | Redis 호스트 | `localhost` |
| `SPRING_DATA_REDIS_PORT` | Redis 포트 | `6379` |
| `SPRING_DATA_REDIS_PASSWORD` | Redis 비밀번호 | `passwd` |
| `ELASTICSEARCH_HOST` | Elasticsearch 호스트 | `localhost` |
| `ELASTICSEARCH_PORT` | Elasticsearch 포트 | `9200` |
| `DOCUHELPER_FILE_ENDPOINT` | 파일 서비스 URL | `http://localhost:8082` |

> `.env` 파일 또는 Kubernetes ConfigMap으로 주입합니다. 시크릿 값을 소스코드에 직접 커밋하지 마세요.

---

## 관련 서비스

DocuHelper는 마이크로서비스 아키텍처로 구성되어 있습니다.

| 서비스 | 레포지토리 | 설명 |
|--------|-----------|------|
| **docuhelper-api** | [현재 레포] | 메인 API 게이트웨이 (GraphQL) |
| **docuhelper-auth** | [DocuHelper/docuhelper-auth](https://github.com/DocuHelper/docuhelper-auth) | 사용자 인증 서비스 |
| **docuhelper-file** | [DocuHelper/docuhelper-file](https://github.com/DocuHelper/docuhelper-file) | 파일 저장 및 관리 서비스 |
| **docuhelper-document-parser** | [DocuHelper/docuhelper-document-parser](https://github.com/DocuHelper/docuhelper-document-parser) | 문서 파싱 서비스 |
| **docuhelper-embed** | [DocuHelper/docuhelper-embed](https://github.com/DocuHelper/docuhelper-embed) | 벡터 임베딩 서비스 |
| **docuhelper-nextjs** | [DocuHelper/docuhelper-nextjs](https://github.com/DocuHelper/docuhelper-nextjs) | 웹 프론트엔드 (Next.js) |
| **docuhelper-flutter** | [DocuHelper/docuhelper-flutter](https://github.com/DocuHelper/docuhelper-flutter) | 모바일 앱 (Flutter) |
| **docuhelper-n8n** | [DocuHelper/docuhelper-n8n](https://github.com/DocuHelper/docuhelper-n8n) | 워크플로우 자동화 (n8n) |
