# ScholarshipHub Backend

Spring Boot 後端 API 服務

## 技術堆疊

| 技術 | 版本 |
|------|------|
| Java | 17 |
| Spring Boot | 3.2.0 |
| Spring Security | 6.x |
| Spring Data JPA | 3.x |
| MariaDB | 10.4+ |
| JWT (jjwt) | 0.12.3 |

## 專案結構

```
backend/src/main/java/com/scholarship/
├── ScholarshipApplication.java    # 主程式
├── config/
│   └── SecurityConfig.java        # Spring Security 配置
├── controller/
│   └── AuthController.java        # 認證 API
├── dto/
│   ├── LoginRequest.java
│   ├── RegisterRequest.java
│   └── AuthResponse.java
├── entity/
│   ├── User.java
│   ├── Student.java
│   ├── Advisor.java
│   ├── Admin.java
│   └── Reviewer.java
├── repository/
│   ├── UserRepository.java
│   ├── StudentRepository.java
│   └── AdvisorRepository.java
├── security/
│   ├── JwtUtil.java
│   └── JwtAuthenticationFilter.java
└── service/
    └── AuthService.java
```

## 啟動方式

```bash
# 1. 確保 MariaDB 已啟動並建立 scholarshipsystem 資料庫
# 2. 修改 application.yml 中的資料庫密碼
# 3. 執行
cd backend
mvn spring-boot:run
```

## API 端點

### 認證 API

| Method | Endpoint | 描述 |
|--------|----------|------|
| POST | `/api/auth/login` | 使用者登入 |
| POST | `/api/auth/register` | 使用者註冊 |
| GET | `/api/auth/health` | 健康檢查 |

### 登入請求範例

```json
{
  "account": "student001",
  "password": "123456"
}
```

### 註冊請求範例 (學生)

```json
{
  "account": "student001",
  "password": "123456",
  "name": "王小明",
  "email": "student@example.com",
  "role": "STUDENT",
  "department": "資訊工程學系",
  "grade": "3"
}
```
