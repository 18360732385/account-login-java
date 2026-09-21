# 设计：登出 + 改密 API

> 状态：已交付 · Path F · slug `2026-09-21-logout-change-password`

## 1. 术语
- **jti**：JWT ID，登出 denylist 键
- **tokenVersion (tv)**：用户级版本，改密递增使旧 JWT 全部失效
- **denylist**：内存 `TokenDenylist`，按 jti→过期时间

## 2. 契约（本仓为 API SSOT）

### POST /api/auth/logout
- Auth: Bearer
- 200: `{ "message": "已登出" }`
- 无 Bearer: 401
- 副作用：当前 jti 进入 denylist；之后同 token → 401

### POST /api/auth/change-password
- Auth: Bearer
- Body: `{ "oldPassword": string, "newPassword": string }`（new ≥ 6）
- 200: `{ "message": "密码已修改，请重新登录" }`
- 旧密码错误: 400 `旧密码不正确`
- 副作用：更新哈希、`tokenVersion++`、吊销当前 jti

### 既有
- POST /api/auth/login、GET /api/me 不变

## 3. CORS
`CorsConfigurationSource` 允许 `localhost:5173` / `127.0.0.1:5173`。与 sibling web 的 Vite proxy **并存**（`integration_ready: cors|proxy`）。

## 4. 跨仓
- sibling: account-login-web（role=web）消费本契约
- 共享演示账号：改密污染内存口令 → 测试须 `@AfterEach` 恢复（见 O15）

## 5. 非目标
刷新令牌、持久化会话、多设备管理 UI
