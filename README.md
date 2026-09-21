# account-login-java

Spring Boot 3 + Java 17 账号登录 API 演示：用户名密码换 JWT，再用 JWT 访问受保护接口。

## 要求

- JDK 17+（本机验证使用 OpenJDK 21，`pom` 目标仍为 17）
- Maven 3.9+

## 快速开始

```bash
# 运行测试
mvn -q test

# 启动
mvn spring-boot:run
```

默认端口：`8080`

## 演示账号

| 用户名 | 密码 |
|---|---|
| `demo` | `demo123` |
| `admin` | `admin123` |

## curl 示例

### 登录（成功）

```bash
curl -s -X POST http://localhost:8080/api/auth/login \
  -H 'Content-Type: application/json' \
  -d '{"username":"demo","password":"demo123"}'
```

响应示例：

```json
{
  "accessToken": "<jwt>",
  "tokenType": "Bearer",
  "expiresInMs": 3600000
}
```

### 登录（失败）

```bash
curl -s -o /dev/null -w '%{http_code}\n' -X POST http://localhost:8080/api/auth/login \
  -H 'Content-Type: application/json' \
  -d '{"username":"demo","password":"wrong"}'
# → 401
```

### 当前用户（需 Token）

```bash
TOKEN=$(curl -s -X POST http://localhost:8080/api/auth/login \
  -H 'Content-Type: application/json' \
  -d '{"username":"demo","password":"demo123"}' | jq -r .accessToken)

curl -s http://localhost:8080/api/me \
  -H "Authorization: Bearer $TOKEN"
```

无 Token：

```bash
curl -s -o /dev/null -w '%{http_code}\n' http://localhost:8080/api/me
# → 401
```

## 文档

- 产品需求：`docs/requirements/账号登录.md`
- feature-eng 过程态：`docs/runs/`（收口后见 `archive/`）
- Spec / Plan：`docs/superpowers/`

## License

MIT
