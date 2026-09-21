package com.example.accountlogin.service;

import com.example.accountlogin.dto.LoginRequest;
import com.example.accountlogin.dto.LoginResponse;
import com.example.accountlogin.dto.MeResponse;
import com.example.accountlogin.security.JwtService;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class AuthService {

    private final DemoUserStore userStore;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(DemoUserStore userStore, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.userStore = userStore;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public LoginResponse login(LoginRequest request) {
        DemoUser user = userStore.findByUsername(request.username())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "用户名或密码错误"));

        if (!passwordEncoder.matches(request.password(), user.passwordHash())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "用户名或密码错误");
        }

        String token = jwtService.generateToken(user.username());
        return LoginResponse.bearer(token, jwtService.getExpirationMs());
    }

    public MeResponse me(String username) {
        DemoUser user = userStore.findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "用户不存在"));
        return new MeResponse(user.username(), user.displayName());
    }
}
