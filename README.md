# docuhelper-api

DocuHelper 플랫폼의 메인 백엔드 API 서버입니다.  
GraphQL 기반의 리액티브 API를 제공하며, 문서 관리, AI 채팅, 파일 업로드, 사용자 인증 등의 핵심 기능을 담당합니다.

---

## 목차

- [아키텍처 개요](#아키텍처-개요)
- [기술 스택](#기술-스택)
- [모듈 구조](#모듈-구조)
- [주요 기능 및 GraphQL API](#주요-기능-및-graphql-api)
- [환경 변수](#환경-변수)
- [로컬 실행 방법](#로컬-실행-방법)
- [Docker 실행](#docker-실행)
- [CI/CD](#cicd)

---

## 아키텍처 개요

```
┌─────────────────────────────────────────────────────┐
│                  docuhelper-api                     │
│                                                     │
│  [GraphQL Gateway] ──▶ [Core Domain / UseCases]    │
│         │                        │                  │
│         │               [App / Adapters]            │
│         │                        │                  │
│    JWT Auth             ┌────────┴────────┐         │
│    WebFlux Filter       │                 │         │
│                      PostgreSQL      Elasticsearch  │
│                      (R2DBC)         (Vector Search)│
│                                                     │
│  [Event Module] ◀──▶ Kafka ◀──▶ [User Notice]      │
│  [User Client]  ◀──▶ Redis                         │
│  [AI Module]    ◀──▶ OpenAI / Ollama               │
│  [File Module]  ◀──▶ docuhelper-file               │
└─────────────────────────────────────────────────────┘
```

- **GraphQL (graphql-kotlin)** 기반의 Query / Mutation / Subscription API
- **Spring WebFlux** 기반 논블로킹 리액티브 처리
- **Kafka** 를 통한 마이크로서비스 간 이벤트 기반 통신
- **Hexagonal Architecture** 적용 (core 도메인 독립, 어댑터 분리)

---

## 기술 스택

| 분류 | 기술 |
|------|------|
| Language | Kotlin 1.9 |
| Framework | Spring Boot 3.4, Spring WebFlux |
| API | GraphQL (graphql-kotlin) |
| Database | PostgreSQL (R2DBC, 비동기) |
| Search | Elasticsearch (벡터 유사도 검색) |
| Cache / Session | Redis |
| Message Queue | Apache Kafka |
| AI | OpenAI GPT-4.1 (채팅), OpenAI / Ollama (임베딩) |
| Build | Gradle (Kotlin DSL), Multi-Module |
| Container | Docker |
| Deploy | Kubernetes |
| Java | JDK 17 |

---

## 모듈 구조

```
docuhelper-api/
├── common-module/
│   ├── core/             # 도메인 모델, UseCase, OutPort 인터페이스 (순수 도메인)
│   ├── ai/               # OpenAI / Ollama 임베딩 & 채팅 어댑터
│   ├── redis/            # Redis 설정 및 유틸
│   └── docuhelper-file/  # docuhelper-file 서비스 HTTP 클라이언트
│
└── api-module/
    ├── app/              # Spring Boot 애플리케이션 진입점, DB/ES 어댑터, 설정
    ├── gateway/          # GraphQL Query / Mutation / Subscription 엔드포인트
    ├── event/            # Kafka 이벤트 발행 / 수신
    ├── user-notice/      # Kafka 기반 사용자 실시간 알림 (GraphQL Subscription)
    └── user-client/      # Redis 기반 접속 사용자 클라이언트 관리
```

### 핵심 모듈 설명

| 모듈 | 역할 |
|------|------|
| `common-module:core` | 도메인 모델(Document, Chat, User 등), 포트 인터페이스, 이벤트 정의 |
| `common-module:ai` | OpenAI/Ollama 임베딩 클라이언트, GPT 채팅 스트리밍 어댑터 |
| `api-module:gateway` | GraphQL 스키마, JWT 인증 필터, 요청/응답 매핑 |
| `api-module:app` | 실행 가능한 Spring Boot 앱, PostgreSQL(R2DBC) + ES 어댑터 구현체 |
| `api-module:event` | Kafka Producer/Consumer, 도메인 이벤트 브리지 |
| `api-module:user-notice` | Kafka 메시지 → GraphQL Subscription 스트림 변환 |

---

## 주요 기능 및 GraphQL API

### 인증 (Auth)
| 타입 | 필드 | 설명 |
|------|------|------|
| Query | `loginState` | 현재 JWT 토큰의 로그인 상태 및 사용자 정보 반환 |

### 문서 (Document)
| 타입 | 필드 | 설명 |
|------|------|------|
| Query | `findDocument` | 문서 목록 조회 (필터링, 페이지네이션 지원) |
| Mutation | `createDocument` | 새 문서 등록 |
| Mutation | `deleteDocument` | 문서 삭제 (UUID) |

### 채팅 / AI 질의응답 (Chat)
| 타입 | 필드 | 설명 |
|------|------|------|
| Query | `findChat` | 채팅 이력 조회 |
| Mutation | `send` | 문서 기반 AI 질문 전송 (벡터 검색 + GPT 답변) |
| Subscription | `chatAnswer` | AI 답변 스트리밍 수신 |

### 파일 (File)
| 타입 | 필드 | 설명 |
|------|------|------|
| Mutation | `uploadFileUrl` | S3 Pre-signed URL 발급 (직접 업로드용) |

### 청크 (Chunk)
| 타입 | 필드 | 설명 |
|------|------|------|
| Query | `findChunk` | 문서 청크 및 유사도 검색 결과 조회 |

---

## 환경 변수

운영 환경(`prod` 프로파일)에서 아래 환경 변수가 필요합니다.

| 환경 변수 | 설명 |
|-----------|------|
| `DB_HOST` | PostgreSQL 호스트 |
| `DB_PORT` | PostgreSQL 포트 |
| `DB_USER` | PostgreSQL 사용자 |
| `DB_PASSWD` | PostgreSQL 비밀번호 |
| `JWT_SECRET` | JWT 서명 시크릿 키 |
| `KAFKA_HOST` | Kafka 브로커 호스트 |
| `KAFKA_PORT` | Kafka 브로커 포트 |
| `REDIS_HOST` | Redis 호스트 |
| `REDIS_PORT` | Redis 포트 |
| `REDIS_PASSWORD` | Redis 비밀번호 |
| `OPENAI_API_KEY` | OpenAI API 키 |
| `OLLAMA_ENDPOINT` | Ollama 서버 엔드포인트 |
| `OLLAMA_EMBEDDING_MODEL` | Ollama 임베딩 모델명 (예: `bge-m3`) |
| `ELASTICSEARCH_HOST` | Elasticsearch 호스트 |
| `ELASTICSEARCH_PORT` | Elasticsearch 포트 |
| `ELASTICSEARCH_API_KEY` | Elasticsearch API 키 |
| `DOCUHELPER_FILE_ENDPOINT` | docuhelper-file 서비스 URL |

---

## 로컬 실행 방법

### 사전 요구사항

- JDK 17+
- PostgreSQL, Redis, Kafka, Elasticsearch 실행 중
- (선택) Ollama 서버 또는 OpenAI API 키

### 설정

`application-default.yml` 에 로컬 환경 설정이 미리 구성되어 있습니다.  
필요에 따라 DB/Kafka/Redis 등의 호스트 정보를 수정하세요.

```yaml
# api-module/app/src/main/resources/application-default.yml
spring:
  r2dbc:
    url: r2dbc:pool:postgres://<DB_HOST>:5432/dev-docuhelper
    username: postgres
    password: password
  kafka:
    bootstrap-servers: <KAFKA_HOST>:9092
  data:
    redis:
      host: <REDIS_HOST>
      port: 6379
```

### 빌드 및 실행

```bash
# 빌드
./gradlew :api-module:app:build

# 실행 (default 프로파일)
./gradlew :api-module:app:bootRun --args='--spring.profiles.active=default'

# 또는 JAR 직접 실행
java -jar api-module/app/build/libs/app-*.jar --spring.profiles.active=default
```

서버가 기동되면 다음 주소에서 GraphQL Playground에 접근할 수 있습니다.

```
http://localhost:8080/graphql
```

---

## Docker 실행

```bash
# 이미지 빌드
docker build \
  --build-arg DB_HOST=<host> \
  --build-arg DB_PORT=5432 \
  --build-arg DB_USER=postgres \
  --build-arg DB_PASSWD=<password> \
  --build-arg JWT_SECRET=<secret> \
  --build-arg KAFKA_HOST=<host> \
  --build-arg KAFKA_PORT=9092 \
  --build-arg REDIS_HOST=<host> \
  --build-arg REDIS_PORT=6379 \
  --build-arg REDIS_PASSWORD=<password> \
  --build-arg OPENAI_API_KEY=<key> \
  --build-arg ELASTICSEARCH_HOST=<host> \
  --build-arg ELASTICSEARCH_PORT=9200 \
  --build-arg ELASTICSEARCH_API_KEY=<key> \
  --build-arg DOCUHELPER_FILE_ENDPOINT=http://<host>:8082 \
  --build-arg OLLAMA_ENDPOINT=http://<host>:11434 \
  --build-arg OLLAMA_EMBEDDING_MODEL=bge-m3 \
  -f api-module/Dockerfile \
  -t docuhelper-api .

# 컨테이너 실행
docker run -p 8080:8080 docuhelper-api
```

또는 `docker-compose` 사용:

```bash
docker-compose up
```

---

## CI/CD

GitHub Actions + Kubernetes로 자동 배포됩니다.

- **트리거**: `main` 브랜치 push 또는 PR
- **빌드**: Gradle로 JAR 빌드 후 Docker 이미지 생성
- **레지스트리**: `docker.bmserver.org/docuhelper-api:<version>`
- **버전**: `api-module/app/build.gradle.kts`의 `version` 필드 자동 추출
- **배포**: `docuhelper-api.yaml` 템플릿 기반 Kubernetes manifest 생성 후 `kubectl apply`

워크플로우 파일: [`.github/workflows/kubernetes-deploy.yml`](.github/workflows/kubernetes-deploy.yml)

---

## 관련 서비스

| 서비스 | 설명 |
|--------|------|
| [docuhelper-auth](https://github.com/DocuHelper/docuhelper-auth) | 사용자 인증/인가 서비스 |
| [docuhelper-file](https://github.com/DocuHelper/docuhelper-file) | 파일 저장 및 S3 Pre-signed URL 관리 |
| [docuhelper-document-parser](https://github.com/DocuHelper/docuhelper-document-parser) | 문서 파싱 및 청크 분할 서비스 |
| [docuhelper-embed](https://github.com/DocuHelper/docuhelper-embed) | 텍스트 임베딩 생성 서비스 |
| [docuhelper-nextjs](https://github.com/DocuHelper/docuhelper-nextjs) | 웹 프론트엔드 (Next.js) |
| [docuhelper-flutter](https://github.com/DocuHelper/docuhelper-flutter) | 모바일 앱 (Flutter) |
