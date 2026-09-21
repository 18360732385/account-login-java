# Spec：账号登录 API

> 状态：已交付  
> 路径：F Full · slug：`2026-09-21-account-login`  
> 需求来源：`docs/requirements/账号登录.md`

## 1. 术语

| 术语 | 含义 |
|---|---|
| accessToken | JWT 访问令牌，放在 `Authorization: Bearer` |
| 演示用户 | 进程内预置账号（demo / admin） |
| 受保护资源 | 需有效 JWT 的接口，本轮仅 `/api/me` |

## 2. 范围

### 做

- 登录签发 JWT；校验密码（BCrypt）
- `/api/me` 返回当前用户基本信息
- Spring Security 无状态过滤器链
- 集成测试覆盖成功/失败/鉴权

### 不做

- 注册、刷新、登出、持久化、UI、OAuth

## 3. API 契约

### POST /api/auth/login

请求：

```json
{ "username": "string", "password": "string" }
```

响应 200：

```json
{ "accessToken": "string", "tokenType": "Bearer", "expiresInMs": 3600000 }
```

响应 401：`{ "code": "...", "message": "用户名或密码错误" }`  
响应 400：缺字段校验失败

### GET /api/me

请求头：`Authorization: Bearer <accessToken>`  
响应 200：`{ "username": "string", "displayName": "string" }`  
响应 401：未认证 / Token 无效

## 4. 安全

- CSRF 关闭（纯 API + Bearer）
- SessionCreationPolicy.STATELESS
- JWT secret 可配置（`app.jwt.secret`）
- 登录失败不区分用户是否存在

## 5. 验收

见需求文档 §6；测试设计见 runs 内 `测试用例.md`。
