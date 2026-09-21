# account-login-java

Spring Boot 3 + Java 17 账号登录 API 演示：用户名密码换 JWT，再用 JWT 访问受保护接口；支持 **登出（jti denylist）** 与 **改密（需旧密码，tokenVersion 失效）**。

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

配对前端：[account-login-web](https://github.com/18360732385/account-login-web)（Vite proxy 或直连；本仓已配最小 CORS）。

## 演示账号

| 用户名 | 密码 |
|---|---|
| `demo` | `demo123` |
| `admin` | `admin123` |

> 改密会改写内存哈希。集成测试会在 `@AfterEach` 恢复演示密码；手工改密后重启进程即可恢复预置账号。

## API

| 方法 | 路径 | 鉴权 | 说明 |
|---|---|---|---|
| POST | `/api/auth/login` | 无 | `{username,password}` → JWT |
| GET | `/api/me` | Bearer | 当前用户资料 |
| POST | `/api/auth/logout` | Bearer | 吊销当前 token（jti denylist） |
| POST | `/api/auth/change-password` | Bearer | `{oldPassword,newPassword}`；成功后须重登 |

## curl 示例

### 登录

```bash
curl -s -X POST http://localhost:8080/api/auth/login \
  -H 'Content-Type: application/json' \
  -d '{"username":"demo","password":"demo123"}'
```

### 当前用户

```bash
TOKEN=$(curl -s -X POST http://localhost:8080/api/auth/login \
  -H 'Content-Type: application/json' \
  -d '{"username":"demo","password":"demo123"}' | jq -r .accessToken)

curl -s http://localhost:8080/api/me -H "Authorization: Bearer $TOKEN"
```

### 登出

```bash
curl -s -X POST http://localhost:8080/api/auth/logout \
  -H "Authorization: Bearer $TOKEN"
# 之后同一 TOKEN 访问 /api/me → 401
```

### 改密

```bash
curl -s -X POST http://localhost:8080/api/auth/change-password \
  -H "Authorization: Bearer $TOKEN" \
  -H 'Content-Type: application/json' \
  -d '{"oldPassword":"demo123","newPassword":"demo456"}'
```

## CORS

`SecurityConfig` 提供 `CorsConfigurationSource`，允许 `http://localhost:5173` / `127.0.0.1:5173`。前端亦可继续用 Vite `/api` 代理。

## 文档

- 需求：`docs/requirements/`
- feature-eng：`docs/runs/`
- Spec / Plan：`docs/superpowers/`
- 全栈优化点：见 web 仓 `docs/feature-eng-优化点-fullstack.md`（本仓镜像链接）

## License

MIT
