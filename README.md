# docuhelper-api

DocuHelper 플랫폼의 메인 API 서버입니다. GraphQL 기반의 Reactive API를 제공하며, 문서 관리·채팅·임베딩·파일 처리 등의 핵심 비즈니스 로직을 담당합니다.

## 기술 스택

| 분류 | 기술 |
|------|------|
| Language | Kotlin 1.9.25 |
| Framework | Spring Boot 3.4.4 (WebFlux) |
| Java | JDK 17 |
| Build | Gradle (Kotlin DSL) |
| API | GraphQL (graphql-kotlin-spring-server 8.3.0) |
| DB | PostgreSQL (R2DBC) |
| Search | Elasticsearch |
| Message | Apache Kafka |
| Cache | Redis |
| AI | Spring AI — OpenAI / Ollama |
| Auth | JWT (jjwt 0.12.6) |
| Deploy | Docker, Kubernetes |
| CI/CD | GitHub Actions (self-hosted runner) |

---

## 모듈 구조

```
docuhelper-api/
├── common-module/
│   ├── core/            # 핵심 도메인 모델, 포트(OutPort), 이벤트, 유스케이스 인터페이스
│   ├── ai/              # AI 어댑터 (OpenAI · Ollama 임베딩 & 채팅)
│   ├── redis/           # Redis 설정 및 관리 유틸
│   └── docuhelper-file/ # docuhelper-file 서비스 클라이언트
└── api-module/
    ├── app/             # Spring Boot 실행 엔트리포인트, DB/Elasticsearch 어댑터
    ├── gateway/         # GraphQL 스키마 정의 (Query · Mutation · Subscription)
    ├── event/           # Kafka 이벤트 발행 / 구독
    ├── user-notice/     # Kafka 기반 사용자 실시간 알림
    └── user-client/     # Redis 기반 접속 사용자 세션 관리
```

### 주요 도메인

| 도메인 | 설명 |
|--------|------|
| **Document** | 문서 생성·삭제·상태 관리, 파싱 이벤트 처리 |
| **Chunk** | 문서 청크 저장 (PostgreSQL + Elasticsearch), 벡터 유사도 검색 |
| **Chat** | 채팅 세션, AI 답변 생성 및 스트리밍 |
| **File** | 파일 업로드 Presigned URL 발급 |
| **User / Token** | JWT 인증 토큰 발급·검증·히스토리 관리 |

---

## 환경 변수

로컬 실행 시 `application-default.yml`이 활성화됩니다. 프로덕션 빌드는 아래 환경변수를 Docker build-arg 또는 Kubernetes ConfigMap으로 주입합니다.

| 변수명 | 설명 |
|--------|------|
| `DB_HOST` / `DB_PORT` | PostgreSQL 호스트 및 포트 |
| `DB_USER` / `DB_PASSWD` | PostgreSQL 인증 정보 |
| `JWT_SECRET` | JWT 서명 시크릿 키 |
| `KAFKA_HOST` / `KAFKA_PORT` | Kafka 브로커 주소 |
| `REDIS_HOST` / `REDIS_PORT` / `REDIS_PASSWORD` | Redis 연결 정보 |
| `OPENAI_API_KEY` | OpenAI API 키 |
| `ELASTICSEARCH_HOST` / `ELASTICSEARCH_PORT` / `ELASTICSEARCH_API_KEY` | Elasticsearch 연결 정보 |
| `OLLAMA_ENDPOINT` / `OLLAMA_EMBEDDING_MODEL` | Ollama 서버 주소 및 임베딩 모델명 |
| `DOCUHELPER_FILE_ENDPOINT` | docuhelper-file 서비스 URL |

> **로컬 개발** 시 `api-module/app/src/main/resources/application-default.yml` 파일을 복사하여 개인 환경에 맞게 수정하세요.

---

## 실행 방법

### 로컬 실행

```bash
# 전체 빌드
./gradlew :api-module:app:build

# 애플리케이션 실행 (default 프로파일)
./gradlew :api-module:app:bootRun --args='--spring.profiles.active=default'
```

서버가 기동되면 `http://localhost:8080`에서 GraphQL Playground에 접근할 수 있습니다.

### Docker 실행

```bash
docker-compose up --build
```

> 기본 포트: **8080**

---

## GraphQL API

GraphQL 엔드포인트: `POST /graphql`  
GraphQL Playground (개발용): `GET /graphql`

| 타입 | 주요 오퍼레이션 |
|------|----------------|
| Query | 문서 목록/상세, 청크 검색, 채팅 내역, 답변 참조 조회 |
| Mutation | 문서 생성, 파일 업로드 URL 발급, 채팅 메시지 전송 |
| Subscription | 사용자 실시간 알림 (WebSocket) |

---

## CI/CD & 배포

`main` 브랜치에 push되면 GitHub Actions 워크플로우(`.github/workflows/kubernetes-deploy.yml`)가 자동 실행됩니다.

1. `api-module/app/build.gradle.kts`의 `version`을 이미지 태그로 사용
2. Docker 이미지 빌드 → 프라이빗 레지스트리(`docker.bmserver.org`) 푸시
3. `docuhelper-api.yaml` 템플릿으로 Kubernetes 매니페스트 생성
4. `kubectl apply`로 클러스터 배포 (Replicas: 3, NodePort: 30001)

---

## 관련 서비스

| 서비스 | 레포지토리 | 설명 |
|--------|-----------|------|
| 인증 | docuhelper-auth | JWT 발급 및 사용자 인증 |
| 파일 | docuhelper-file | 파일 업로드/다운로드 |
| 문서 파서 | docuhelper-document-parser | PDF 등 문서 파싱 |
| 임베딩 | docuhelper-embed | 벡터 임베딩 생성 |
| 웹 프론트엔드 | docuhelper-nextjs | Next.js 웹 클라이언트 |
| 모바일 | docuhelper-flutter | Flutter 모바일 앱 |
