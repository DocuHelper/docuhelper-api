# DocuHelper API

문서 관리 및 AI 기반 Q&A 플랫폼의 백엔드 API 서버입니다.  
업로드된 문서를 파싱하고, 벡터 임베딩을 생성하여 사용자의 질문에 대해 문서 기반 답변을 제공합니다.

## 기술 스택

| 분류 | 기술 |
|------|------|
| Language | Kotlin 1.9.25 |
| Framework | Spring Boot 3.4.4 (WebFlux) |
| API | GraphQL ([graphql-kotlin](https://github.com/ExpediaGroup/graphql-kotlin) 8.3.0) |
| Database | PostgreSQL (R2DBC) + pgvector |
| Search | Elasticsearch |
| Cache | Redis (Reactive) |
| Messaging | Apache Kafka (Reactor Kafka) |
| AI | Spring AI 1.0.0-M7 (OpenAI, Ollama) |
| Auth | JWT (jjwt 0.12.6) |
| Java | 17+ |

## 프로젝트 구조

```
docuhelper-api/
├── api-module/
│   ├── app/            # Spring Boot 메인 애플리케이션
│   ├── gateway/        # GraphQL API 게이트웨이 (Query, Mutation, Subscription)
│   ├── event/          # Kafka 이벤트 발행/수신
│   ├── user-notice/    # 사용자 알림 (Kafka Consumer)
│   └── user-client/    # Redis 기반 사용자 클라이언트 관리
│
├── common-module/
│   ├── core/           # 도메인 모델, 비즈니스 로직, Port & Adapter
│   ├── docuhelper-file/ # 파일 처리 유틸리티
│   ├── redis/          # Redis 통합 레이어
│   └── ai/             # AI/ML 통합 (OpenAI, Ollama)
│
├── docker-compose.yml
├── docuhelper-api.yaml  # Kubernetes 배포 매니페스트
└── build.gradle.kts
```

## 아키텍처

### Hexagonal Architecture (Ports & Adapters)

```
GraphQL Gateway  →  OutPort (Interface)  →  Adapter (Service)  →  Database / External API
```

- **Gateway**: GraphQL 요청을 도메인 계층으로 전달
- **OutPort**: `DocumentOutPort`, `ChatOutPort`, `ChunkOutPort`, `AiOutPort` 등 인터페이스 정의
- **Adapter**: OutPort 구현체로 실제 데이터 접근 처리

### Event-Driven Architecture

Kafka를 통한 비동기 이벤트 처리:
- `DocumentCreate`, `DocumentDelete` — 문서 생명주기
- `ChatSend`, `ChatAnswer` — 채팅 Q&A 처리
- `DocumentParse*` — 문서 파싱 파이프라인

### Reactive Streams

전체 스택이 비동기 논블로킹으로 구성되어 있습니다:
- `Mono<T>` / `Flux<T>` 기반 반환
- R2DBC를 통한 비동기 DB 접근
- Reactor Kafka를 통한 비동기 메시징

## 주요 기능

### GraphQL API

| 분류 | 엔드포인트 | 설명 |
|------|-----------|------|
| Document | `findDocument` | 문서 검색 |
| Document | `createDocument` | 문서 생성/업로드 |
| Document | `deleteDocument` | 문서 삭제 |
| Chat | `findChat` | 채팅 이력 조회 |
| Chat | `send` | 문서 기반 AI Q&A |
| File | `uploadFileUrl` | 파일 업로드 Pre-signed URL 생성 |
| Auth | `loginState` | 인증 상태 확인 |
| Notice | `subNotice` | 실시간 알림 (Subscription) |

### 도메인 모델

- **Document**: 문서 메타데이터 및 상태 관리 (`READING` → `COMPLETED` / `FAILED`)
- **Chunk**: 문서 분할 단위, 벡터 임베딩 포함
- **Chat**: 사용자 질문-답변 세션
- **ChatAnswerRef**: 답변 근거가 된 Chunk 참조 및 유사도 점수
- **UserToken**: 사용자별 API 사용량 관리

## 시작하기

### 사전 요구사항

- JDK 17+
- PostgreSQL (pgvector 확장)
- Elasticsearch
- Redis
- Apache Kafka
- OpenAI API Key (또는 Ollama)

### 환경 설정

`api-module/app/src/main/resources/application-default.yml`에서 개발 환경을 설정합니다:

```yaml
spring:
  r2dbc:
    url: r2dbc:postgresql://<host>:5432/<database>
    username: <username>
    password: <password>
  data:
    redis:
      host: <redis-host>
      port: 6379
  elasticsearch:
    uris: http://<elasticsearch-host>:9200
  kafka:
    bootstrap-servers: <kafka-host>:9092
  ai:
    openai:
      api-key: ${OPENAI_API_KEY}
```

### 빌드 및 실행

```bash
# 빌드
./gradlew build

# 실행
./gradlew :api-module:app:bootRun
```

서버는 기본적으로 `8080` 포트에서 시작됩니다.

## 배포

### Docker

```bash
docker compose up -d
```

### Kubernetes

```bash
kubectl apply -f docuhelper-api.yaml
```

- 3 replicas 구성
- NodePort 서비스 (외부 포트: 30001)
- ConfigMap(`docuhelper-configmap-prod`)을 통한 환경 변수 관리

### CI/CD

GitHub Actions를 통한 자동 배포:
- `main` 브랜치 push 시 Docker 이미지 빌드 및 Kubernetes 배포
- 버전은 `build.gradle.kts`에서 자동 추출

## 관련 서비스

| 서비스 | 설명 |
|--------|------|
| [docuhelper-auth](https://github.com/DocuHelper/docuhelper-auth) | 인증 서비스 |
| [docuhelper-file](https://github.com/DocuHelper/docuhelper-file) | 파일 관리 서비스 |
| [docuhelper-document-parser](https://github.com/DocuHelper/docuhelper-document-parser) | 문서 파싱 서비스 |
| [docuhelper-embed](https://github.com/DocuHelper/docuhelper-embed) | 임베딩 서비스 |
| [docuhelper-nextjs](https://github.com/DocuHelper/docuhelper-nextjs) | 웹 프론트엔드 |
| [docuhelper-flutter](https://github.com/DocuHelper/docuhelper-flutter) | 모바일 앱 |
