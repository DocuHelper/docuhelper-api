# DocuHelper API

DocuHelper 플랫폼의 메인 백엔드 API 서버입니다.  
**GraphQL** 기반의 리액티브 아키텍처로 구성된 **Kotlin / Spring Boot WebFlux** 애플리케이션입니다.

---

## 목차

- [프로젝트 개요](#프로젝트-개요)
- [기술 스택](#기술-스택)
- [프로젝트 구조](#프로젝트-구조)
- [모듈 설명](#모듈-설명)
- [환경 변수](#환경-변수)
- [로컬 실행](#로컬-실행)
- [Docker 실행](#docker-실행)
- [GraphQL API](#graphql-api)
- [CI/CD](#cicd)

---

## 프로젝트 개요

DocuHelper API는 마이크로서비스 기반 문서 관리 플랫폼의 메인 게이트웨이 서버로,  
다음과 같은 핵심 기능을 제공합니다.

- **문서 관리** — 문서 업로드, 조회, 파싱 파이프라인 연동
- **AI 채팅** — 문서 기반 RAG(Retrieval-Augmented Generation) 채팅
- **파일 처리** — Presigned URL 발급 및 파일 서비스 연동
- **유저 토큰 관리** — JWT 기반 인증 및 토큰 이력 관리
- **실시간 알림** — GraphQL Subscription + Kafka 기반 사용자 알림

---

## 기술 스택

| 분류 | 기술 |
|------|------|
| 언어 | Kotlin 1.9, Java 17 |
| 프레임워크 | Spring Boot 3.4.4, Spring WebFlux |
| API | GraphQL (graphql-kotlin-spring-server 8.3.0) |
| 데이터베이스 | PostgreSQL (R2DBC 리액티브 드라이버) |
| 검색 엔진 | Elasticsearch |
| 캐시 / 실시간 | Redis |
| 메시지 큐 | Apache Kafka |
| AI | OpenAI (`gpt-4.1-nano`, `text-embedding-3-small`), Ollama (`bge-m3`) |
| 인증 | JWT (jjwt 0.12.6) |
| 컨테이너 | Docker, Kubernetes |
| 빌드 도구 | Gradle (Kotlin DSL) |

---

## 프로젝트 구조

```
docuhelper-api/
├── api-module/
│   ├── app/            # Spring Boot 애플리케이션 진입점
│   ├── gateway/        # GraphQL 리졸버 (Query / Mutation / Subscription)
│   ├── event/          # Kafka 이벤트 발행 / 수신
│   ├── user-notice/    # 사용자 실시간 알림 (Kafka → GraphQL Subscription)
│   └── user-client/    # 접속 중인 유저 클라이언트 관리 (Redis)
├── common-module/
│   ├── core/           # 도메인 모델, 유스케이스, 포트 인터페이스
│   ├── ai/             # AI 어댑터 (OpenAI / Ollama 임베딩 & 채팅)
│   ├── redis/          # Redis 설정 및 유틸리티
│   └── docuhelper-file/# 파일 서비스 HTTP 클라이언트
├── docker-compose.yml
├── api-module/Dockerfile
└── docuhelper-api.yaml # Kubernetes 배포 매니페스트 템플릿
```

---

## 모듈 설명

### `common-module:core`
도메인 중심 설계의 핵심 모듈입니다.

- **도메인 모델**: `Document`, `Chat`, `Chunk`, `User`, `UserToken` 등
- **포트 인터페이스**: `DocumentOutPort`, `ChatOutPort`, `ChunkOutPort`, `FileOutPort`, `AiOutPort`
- **이벤트 시스템**: `AbstractEvent`, `EventHandler`, `EventPublisher` — 내부 도메인 이벤트 처리
- **유스케이스**: `CreateUploadUrlUseCase` 등

### `common-module:ai`
AI 임베딩 및 채팅 어댑터 모듈입니다.

- OpenAI 임베딩 (`text-embedding-3-small`)
- Ollama 임베딩 (`bge-m3`)

### `api-module:gateway`
GraphQL 엔드포인트 정의 모듈입니다.

| 타입 | 리졸버 | 설명 |
|------|--------|------|
| Query | `DocumentGqlQuery` | 문서 목록 조회 |
| Query | `ChatGqlQuery` | 채팅 이력 조회 |
| Query | `ChunkGqlQuery` | 청크 조회 |
| Query | `AnswerRefGqlQuery` | 답변 참조 청크 조회 |
| Query | `AuthGqlQuery` | 인증 정보 조회 |
| Mutation | `DocumentGqlMutation` | 문서 생성 / 삭제 |
| Mutation | `ChatGqlMutation` | 채팅 전송 |
| Mutation | `FileGqlMutation` | 파일 업로드 URL 발급 |
| Subscription | `UserNoticeGqlSubscription` | 사용자 실시간 알림 |

### `api-module:event`
Kafka 기반 이벤트 발행/수신 모듈입니다.  
`KafkaEventPublisher`, `KafkaEventListener`, `KafkaTopic` 정의를 포함합니다.

### `api-module:user-notice`
Kafka 메시지를 수신하여 GraphQL Subscription으로 전달하는 알림 모듈입니다.

### `api-module:user-client`
Redis를 이용해 현재 접속 중인 유저 WebSocket 클라이언트를 관리합니다.

### `api-module:app`
Spring Boot 애플리케이션 메인 모듈입니다.  
모든 하위 모듈을 조합하여 단일 실행 가능한 JAR를 생성합니다.

---

## 환경 변수

| 변수명 | 설명 |
|--------|------|
| `DB_HOST` | PostgreSQL 호스트 |
| `DB_PORT` | PostgreSQL 포트 |
| `DB_USER` | PostgreSQL 사용자 |
| `DB_PASSWD` | PostgreSQL 비밀번호 |
| `JWT_SECRET` | JWT 서명 시크릿 |
| `KAFKA_HOST` | Kafka 브로커 호스트 |
| `KAFKA_PORT` | Kafka 브로커 포트 |
| `DOCUHELPER_FILE_ENDPOINT` | 파일 서비스 엔드포인트 |
| `OPENAI_API_KEY` | OpenAI API 키 |
| `ELASTICSEARCH_HOST` | Elasticsearch 호스트 |
| `ELASTICSEARCH_PORT` | Elasticsearch 포트 |
| `ELASTICSEARCH_API_KEY` | Elasticsearch API 키 |
| `REDIS_HOST` | Redis 호스트 |
| `REDIS_PORT` | Redis 포트 |
| `REDIS_PASSWORD` | Redis 비밀번호 |
| `OLLAMA_ENDPOINT` | Ollama 서버 엔드포인트 |
| `OLLAMA_EMBEDDING_MODEL` | Ollama 임베딩 모델명 |

---

## 로컬 실행

### 사전 요구사항

- JDK 17 이상
- PostgreSQL, Kafka, Redis, Elasticsearch 인스턴스

### 실행 방법

```bash
# 1. 의존성 빌드
./gradlew :api-module:app:build

# 2. 애플리케이션 실행 (default 프로파일)
./gradlew :api-module:app:bootRun --args='--spring.profiles.active=default'
```

> 로컬 개발 시 `application-default.yml`이 활성화됩니다.  
> `dev.default.login.enable=true` 설정으로 인증 없이 개발 가능합니다.

기본 포트: **8080**

---

## Docker 실행

```bash
# 프로젝트 루트에서 실행
docker-compose up --build
```

또는 수동으로 이미지 빌드:

```bash
docker build \
  --build-arg DB_HOST=<값> \
  --build-arg DB_PORT=<값> \
  --build-arg DB_USER=<값> \
  --build-arg DB_PASSWD=<값> \
  --build-arg JWT_SECRET=<값> \
  --build-arg KAFKA_HOST=<값> \
  --build-arg KAFKA_PORT=<값> \
  --build-arg OPENAI_API_KEY=<값> \
  --build-arg ELASTICSEARCH_HOST=<값> \
  --build-arg ELASTICSEARCH_PORT=<값> \
  --build-arg ELASTICSEARCH_API_KEY=<값> \
  --build-arg REDIS_HOST=<값> \
  --build-arg REDIS_PORT=<값> \
  --build-arg REDIS_PASSWORD=<값> \
  --build-arg OLLAMA_ENDPOINT=<값> \
  --build-arg OLLAMA_EMBEDDING_MODEL=<값> \
  -f api-module/Dockerfile .
```

---

## GraphQL API

서버 실행 후 GraphQL Playground에 접속할 수 있습니다.

```
http://localhost:8080/graphql
```

WebSocket(Subscription) 엔드포인트:

```
ws://localhost:8080/subscriptions
```

### 예시 쿼리

```graphql
# 문서 목록 조회
query {
  findDocument(query: { userId: "..." }) {
    id
    title
    state
    createdAt
  }
}

# 채팅 전송
mutation {
  sendChat(request: { documentId: "...", message: "문서 내용을 요약해줘" }) {
    id
    state
  }
}

# 실시간 알림 구독
subscription {
  userNotice {
    type
    payload
  }
}
```

---

## CI/CD

`main` 브랜치에 push되거나 PR이 열릴 때 GitHub Actions 워크플로우가 자동으로 실행됩니다.

**워크플로우**: `.github/workflows/kubernetes-deploy.yml`

1. `./gradlew :api-module:app:properties`로 버전 추출
2. Docker 이미지 빌드 & 프라이빗 레지스트리(`docker.bmserver.org`)에 push
3. `docuhelper-api.yaml` 템플릿으로 Kubernetes 매니페스트 생성
4. `kubectl apply`로 클러스터 배포

> PR 이벤트에서는 빌드만 수행하고 push / 배포는 건너뜁니다.

---

## 관련 서비스

| 서비스 | 설명 |
|--------|------|
| [docuhelper-auth](https://github.com/DocuHelper/docuhelper-auth) | 인증 서비스 |
| [docuhelper-file](https://github.com/DocuHelper/docuhelper-file) | 파일 관리 서비스 |
| [docuhelper-document-parser](https://github.com/DocuHelper/docuhelper-document-parser) | 문서 파싱 서비스 |
| [docuhelper-embed](https://github.com/DocuHelper/docuhelper-embed) | 임베딩 서비스 |
| [docuhelper-nextjs](https://github.com/DocuHelper/docuhelper-nextjs) | 웹 프론트엔드 |
| [docuhelper-flutter](https://github.com/DocuHelper/docuhelper-flutter) | 모바일 앱 |
