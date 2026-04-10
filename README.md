# DocuHelper API

> DocuHelper 마이크로서비스 플랫폼의 **메인 API 게이트웨이** 서비스입니다.

[![Kotlin](https://img.shields.io/badge/Kotlin-1.9.25-7F52FF?logo=kotlin)](https://kotlinlang.org/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4.4-6DB33F?logo=springboot)](https://spring.io/projects/spring-boot)
[![Java](https://img.shields.io/badge/JDK-17-ED8B00?logo=openjdk)](https://openjdk.org/)
[![GraphQL](https://img.shields.io/badge/GraphQL-Kotlin-E10098?logo=graphql)](https://expediagroup.github.io/graphql-kotlin/)

---

## 📋 목차

- [프로젝트 소개](#-프로젝트-소개)
- [기술 스택](#-기술-스택)
- [아키텍처 개요](#-아키텍처-개요)
- [모듈 구조](#-모듈-구조)
- [시작하기](#-시작하기)
- [환경 변수 설정](#-환경-변수-설정)
- [API 문서](#-api-문서)
- [관련 서비스](#-관련-서비스)

---

## 🚀 프로젝트 소개

`docuhelper-api`는 DocuHelper 플랫폼의 중심 API 게이트웨이로, 클라이언트(웹/모바일)의 모든 요청을 처리합니다.  
**GraphQL** 기반의 단일 엔드포인트를 통해 문서 관리, 사용자 인증, AI 기반 채팅, 파일 처리 등의 기능을 제공합니다.

### 주요 기능

- 📄 **문서 관리** — 문서 업로드, 파싱 요청 및 상태 관리
- 💬 **AI 채팅** — 문서 기반 질의응답 (RAG)
- 🔐 **인증/인가** — JWT 기반 사용자 인증
- 📁 **파일 처리** — 파일 업로드 및 관리
- 🔔 **사용자 알림** — 실시간 이벤트 구독 (GraphQL Subscription)
- ⚡ **이벤트 처리** — Kafka 기반 비동기 이벤트 아키텍처

---

## 🛠 기술 스택

| 분류 | 기술 |
|------|------|
| **언어** | Kotlin 1.9.25 |
| **프레임워크** | Spring Boot 3.4.4 |
| **API** | GraphQL Kotlin 8.3.0 (Spring Server) |
| **데이터베이스** | PostgreSQL (R2DBC 리액티브) |
| **캐시** | Redis |
| **메시지 브로커** | Apache Kafka |
| **검색** | Elasticsearch |
| **AI** | Spring AI 1.0.0-M7 (OpenAI, Ollama) |
| **인증** | JWT (JJWT 0.12.6) |
| **빌드 도구** | Gradle (Kotlin DSL) |
| **컨테이너** | Docker, Kubernetes |
| **CI/CD** | GitHub Actions |

---

## 🏗 아키텍처 개요

DocuHelper는 마이크로서비스 아키텍처로 구성되어 있습니다.  
`docuhelper-api`는 모든 클라이언트 요청의 진입점(API Gateway)으로 동작하며, 각 전문 서비스와 연동합니다.

```
┌─────────────────────────────────────────────────────────────┐
│                        Clients                              │
│              (docuhelper-nextjs / docuhelper-flutter)       │
└───────────────────────────┬─────────────────────────────────┘
                            │ GraphQL (:8080/graphql)
                            ▼
┌─────────────────────────────────────────────────────────────┐
│                    docuhelper-api                           │
│          (API Gateway / GraphQL Server)                     │
└──┬───────────────┬──────────────┬───────────────┬───────────┘
   │               │              │               │
   ▼               ▼              ▼               ▼
┌──────┐    ┌──────────┐   ┌──────────┐   ┌─────────────────┐
│ auth │    │   file   │   │ document │   │     embed       │
│      │    │          │   │ -parser  │   │                 │
└──────┘    └──────────┘   └──────────┘   └─────────────────┘
   │               │              │               │
   └───────────────┴──────────────┴───────────────┘
                            │
              ┌─────────────┼─────────────┐
              ▼             ▼             ▼
         PostgreSQL       Kafka       Elasticsearch
         (R2DBC)       (Events)       (검색)
```

---

## 📦 모듈 구조

```
docuhelper-api/
├── api-module/
│   ├── app/               # 메인 Spring Boot 애플리케이션 (진입점)
│   ├── gateway/           # GraphQL 스키마 및 Query/Mutation/Subscription 정의
│   ├── event/             # Kafka 이벤트 핸들러 (문서 파싱 이벤트 등)
│   ├── user-notice/       # 사용자 알림 서비스
│   └── user-client/       # 사용자 클라이언트 서비스
│
├── common-module/
│   ├── core/              # 핵심 도메인 로직 (엔티티, 레포지토리, 서비스)
│   ├── docuhelper-file/   # 파일 처리 공통 유틸리티
│   ├── redis/             # Redis 연동 공통 모듈
│   └── ai/                # AI 모델 연동 (OpenAI, Ollama)
│
├── docker-compose.yml     # 로컬 개발용 Docker Compose
├── docuhelper-api.yaml    # Kubernetes 배포 매니페스트 / OpenAPI 스펙
└── Dockerfile             # (api-module/Dockerfile)
```

### 모듈 설명

| 모듈 | 설명 |
|------|------|
| `api-module:app` | Spring Boot 애플리케이션 진입점, 보안 설정, CORS 설정 |
| `api-module:gateway` | GraphQL Query/Mutation/Subscription 리졸버 |
| `api-module:event` | Kafka Consumer/Producer, 비동기 이벤트 처리 |
| `api-module:user-notice` | 사용자 알림 관리 |
| `api-module:user-client` | 사용자 정보 클라이언트 |
| `common-module:core` | 문서, 채팅 등 핵심 비즈니스 로직 |
| `common-module:docuhelper-file` | 파일 서비스 연동 공통 모듈 |
| `common-module:redis` | Redis 캐시 공통 설정 |
| `common-module:ai` | OpenAI / Ollama AI 모델 공통 설정 |

---

## ⚙️ 시작하기

### 사전 요구사항

- **JDK 17** 이상 ([Eclipse Temurin](https://adoptium.net/) 권장)
- **Docker** & **Docker Compose**
- **Gradle** (프로젝트 내 Gradle Wrapper 사용 가능)

### 저장소 클론

```bash
git clone https://github.com/DocuHelper/docuhelper-api.git
cd docuhelper-api
```

### 로컬 환경 설정

1. 환경 변수 파일을 생성합니다.

```bash
cp .env.example .env
# .env 파일을 열어 필요한 값을 채워넣으세요
```

2. (선택) 로컬 인프라를 Docker Compose로 실행합니다.

```bash
docker-compose up -d
```

### 빌드

```bash
./gradlew build
```

테스트를 제외한 빌드:

```bash
./gradlew build -x test
```

### 실행

**Gradle로 직접 실행:**

```bash
./gradlew :api-module:app:bootRun
```

**Docker Compose로 실행:**

```bash
docker-compose up --build
```

서버가 시작되면 아래 주소에서 접근할 수 있습니다.

| 서비스 | URL |
|--------|-----|
| GraphQL 엔드포인트 | `http://localhost:8080/graphql` |
| GraphQL Subscription | `ws://localhost:8080/subscriptions` |
| GraphQL Playground | `http://localhost:8080/graphiql` |

---

## 🔧 환경 변수 설정

프로덕션 환경(`application-prod.yml`)에서 사용하는 환경 변수 목록입니다.

### 데이터베이스

| 변수명 | 설명 | 예시 |
|--------|------|------|
| `DB_HOST` | PostgreSQL 호스트 | `localhost` |
| `DB_PORT` | PostgreSQL 포트 | `5432` |
| `DB_USER` | 데이터베이스 사용자 | `postgres` |
| `DB_PASSWD` | 데이터베이스 비밀번호 | `password` |

### 인증 (JWT)

| 변수명 | 설명 | 예시 |
|--------|------|------|
| `JWT_SECRET` | JWT 서명 시크릿 키 | `your-secret-key` |

### 메시지 브로커 (Kafka)

| 변수명 | 설명 | 예시 |
|--------|------|------|
| `KAFKA_HOST` | Kafka 브로커 호스트 | `localhost` |
| `KAFKA_PORT` | Kafka 브로커 포트 | `9092` |

### 캐시 (Redis)

| 변수명 | 설명 | 예시 |
|--------|------|------|
| `REDIS_HOST` | Redis 호스트 | `localhost` |
| `REDIS_PORT` | Redis 포트 | `6379` |
| `REDIS_PASSWORD` | Redis 비밀번호 | `password` |

### AI 서비스

| 변수명 | 설명 | 기본값 |
|--------|------|--------|
| `OLLAMA_ENDPOINT` | Ollama API 엔드포인트 | `http://localhost:11434` |
| `OLLAMA_EMBEDDING_MODEL` | Ollama 임베딩 모델 | `bge-m3` |
| `OPENAI_API_KEY` | OpenAI API 키 | — |

### 검색 (Elasticsearch)

| 변수명 | 설명 | 기본값 |
|--------|------|--------|
| `ELASTICSEARCH_HOST` | Elasticsearch 호스트 | `localhost` |
| `ELASTICSEARCH_PORT` | Elasticsearch 포트 | `9200` |
| `ELASTICSEARCH_API_KEY` | Elasticsearch 인증 키 | — |

### 외부 서비스

| 변수명 | 설명 | 예시 |
|--------|------|------|
| `DOCUHELPER_FILE_ENDPOINT` | 파일 서비스 엔드포인트 | `http://docuhelper-file:8081` |

---

## 📖 API 문서

`docuhelper-api`는 **GraphQL** 단일 엔드포인트를 사용합니다.

### GraphQL 엔드포인트

| 방식 | URL | 설명 |
|------|-----|------|
| Query / Mutation | `POST /graphql` | 데이터 조회 및 변경 |
| Subscription | `WS /subscriptions` | 실시간 이벤트 구독 |

### 주요 GraphQL 오퍼레이션

| 분류 | 오퍼레이션 | 설명 |
|------|-----------|------|
| **Auth** | `AuthGqlQuery` | 인증 상태 조회 |
| **Document** | `DocumentGqlQuery` / `DocumentGqlMutation` | 문서 조회 및 생성/수정 |
| **Chat** | `ChatGqlQuery` / `ChatGqlMutation` | AI 채팅 메시지 |
| **File** | `FileGqlMutation` | 파일 업로드 |
| **Answer** | `AnswerRefGqlQuery` | 답변 참조 정보 조회 |
| **Subscription** | `SimpleSubscription` | 실시간 알림 구독 |

### Kubernetes / OpenAPI 스펙

레포지토리 루트의 `docuhelper-api.yaml` 파일에서 Kubernetes 배포 매니페스트와 API 스펙을 확인할 수 있습니다.

---

## 🔗 관련 서비스

DocuHelper 마이크로서비스 생태계의 구성 서비스입니다.

| 서비스 | 레포지토리 | 설명 |
|--------|-----------|------|
| **docuhelper-api** | 현재 레포 | 메인 API 게이트웨이 (GraphQL) |
| **docuhelper-auth** | `DocuHelper/docuhelper-auth` | 인증 서비스 |
| **docuhelper-file** | `DocuHelper/docuhelper-file` | 파일 저장/관리 서비스 |
| **docuhelper-document-parser** | `DocuHelper/docuhelper-document-parser` | 문서 파싱 서비스 |
| **docuhelper-embed** | `DocuHelper/docuhelper-embed` | 텍스트 임베딩 서비스 |
| **docuhelper-nextjs** | `DocuHelper/docuhelper-nextjs` | 웹 프론트엔드 (Next.js) |
| **docuhelper-flutter** | `DocuHelper/docuhelper-flutter` | 모바일 앱 (Flutter) |
| **docuhelper-n8n** | `DocuHelper/docuhelper-n8n` | 워크플로우 자동화 (n8n) |

---

## 🚢 배포

### Docker 이미지 빌드

```bash
docker build -f api-module/Dockerfile -t docuhelper-api:latest .
```

### Kubernetes 배포

```bash
kubectl apply -f docuhelper-api.yaml
```

**배포 정보:**
- **레플리카:** 3개
- **컨테이너 포트:** 8080
- **서비스 타입:** NodePort (외부 포트: 30001)
- **이미지 레지스트리:** `docker.bmserver.org/docuhelper-api`

### CI/CD

`main` 브랜치에 push 시 GitHub Actions 워크플로우(`.github/workflows/kubernetes-deploy.yml`)가 자동으로 실행됩니다.

1. Docker 이미지 빌드 및 레지스트리 push
2. Kubernetes 매니페스트 생성
3. 클러스터 자동 배포

---

## 📝 라이선스

이 프로젝트는 내부 프로젝트입니다.
