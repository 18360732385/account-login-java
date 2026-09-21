package com.example.accountlogin.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 演示用户内存仓。预置：
 * <ul>
 *   <li>demo / demo123</li>
 *   <li>admin / admin123</li>
 * </ul>
 */
@Component
public class DemoUserStore {

    private final Map<String, DemoUser> users = new ConcurrentHashMap<>();

    public DemoUserStore(PasswordEncoder passwordEncoder) {
        users.put("demo", new DemoUser("demo", passwordEncoder.encode("demo123"), "演示用户"));
        users.put("admin", new DemoUser("admin", passwordEncoder.encode("admin123"), "管理员"));
    }

    public Optional<DemoUser> findByUsername(String username) {
        return Optional.ofNullable(users.get(username));
    }
}
