# 📝 BE-memo-app

Spring Boot와 Kotlin으로 개발된 메모 애플리케이션의 백엔드 서비스입니다.  
Redis, MySQL, JWT 기반 인증 및 API Rate Limiting을 적용하여 보안과 성능을 강화하였습니다.

---

## 🌟 주요 기능

✅ **메모 CRUD**: 메모 생성, 수정, 삭제, 조회 기능 제공  
✅ **주제별 메모 관리**: 주제별로 메모를 분류하고 관리  
✅ **JWT 기반 인증**: JWT 토큰을 활용한 사용자 인증 및 보안 강화  
✅ **API Rate Limiting**: `bucket4j`를 활용한 API 요청 제한 기능  
✅ **Redis 캐싱**: Redis를 활용한 데이터 캐싱 및 성능 최적화  
✅ **Spring Security 적용**: 보안 강화를 위한 인증 및 인가 시스템  

---

## 👨‍💻 개발자 소개

<div align="center">
  <img src="https://avatars.githubusercontent.com/u/67574367?s=150&v=4" alt="조승빈" width="150">
  <br>
  <strong>조승빈</strong>
  <br>
  Backend 개발
  <br>
  🔗 <a href="https://github.com/vkflco08">GitHub 프로필</a>
</div>

---

## 🚀 프로젝트 개요
- **프레임워크**: Spring Boot 3.3.3  
- **언어**: Kotlin 1.9.25  
- **DB**: MySQL  
- **캐시**: Redis  
- **배포 방식**: 개인 리눅스 서버에서 Jenkins를 활용하여 Nginx 컨테이너를 배포  
- **보안**: Spring Security + JWT
- **프론트엔드**: React (JSX)  
  - **프론트엔드 GitHub**: [FE-memo-app](https://github.com/vkflco08/FE-memo-app.git)

---

## 📌 사용된 기술 및 라이브러리

### 📦 주요 라이브러리
| 라이브러리 | 버전 | 설명 |
|------------|------|------|
| `spring-boot-starter-web` | 최신 | 웹 애플리케이션 개발 |
| `spring-boot-starter-data-jpa` | 최신 | JPA 사용 |
| `spring-boot-starter-security` | 최신 | Spring Security 적용 |
| `spring-boot-starter-validation` | 최신 | 데이터 검증 |
| `mysql-connector-j` | 최신 | MySQL 연결 |
| `jackson-module-kotlin` | 최신 | Kotlin JSON 직렬화 지원 |
| `lombok` | 최신 | 코드 간소화 |

### 🔒 보안 및 인증
| 라이브러리 | 버전 | 설명 |
|------------|------|------|
| `io.jsonwebtoken:jjwt-api` | 0.11.5 | JWT 인증 |
| `io.jsonwebtoken:jjwt-impl` | 0.11.5 | JWT 구현체 |
| `io.jsonwebtoken:jjwt-jackson` | 0.11.5 | Jackson JSON 지원 |

### ⚡ API Rate Limiting
| 라이브러리 | 버전 | 설명 |
|------------|------|------|
| `bucket4j-core` | 8.0.1 | API 요청 속도 제한 |

### 🗂 데이터 캐싱
| 라이브러리 | 버전 | 설명 |
|------------|------|------|
| `spring-boot-starter-data-redis` | 최신 | Redis 캐싱 |

### 🧪 테스트 및 개발 도구
| 라이브러리 | 버전 | 설명 |
|------------|------|------|
| `spring-boot-starter-test` | 최신 | Spring Boot 테스트 지원 |
| `junit-jupiter` | 5.10.0 | JUnit 5 테스트 프레임워크 |
| `spring-security-test` | 최신 | Spring Security 테스트 지원 |

---

## 🚀 배포 방식
- **Docker Hub 푸시**: GitHub에서 코드를 빌드하여 Docker Hub에 이미지를 푸시하면, Docker Hub의 Webhook이 작동합니다.
- **Jenkins CD 실행**: 개인 리눅스 서버의 Jenkins가 Docker Hub Webhook을 통해 CD 파이프라인을 실행합니다.
- **Spring Boot 컨테이너 배포**: Jenkins 스크립트에서 최신 이미지를 pull 받아 Redis와 함께 Spring Boot 컨테이너를 실행합니다.
