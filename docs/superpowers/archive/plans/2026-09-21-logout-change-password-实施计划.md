# 实施计划：登出 + 改密 API

> 状态：已交付

## Tasks
- T1 TokenDenylist + JwtService(jti/tv)
- T2 DemoUser.tokenVersion + updatePassword/resetCredentials
- T3 AuthService logout/changePassword + Controller
- T4 CorsConfigurationSource
- T5 集成测试 + README/需求
- T6 feature-eng 收口与优化点回写

## 顺序决策（跨仓摩擦）
**API 契约先于 FE Spec 定稿**（本仓先写 §2 契约，web 消费契约节引用）。
