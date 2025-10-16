package com.example.event.controller;

import com.example.event.model.User;
import com.example.event.repository.UserRepository;
import com.example.event.config.JwtUtil;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder; // ← 追加

    public AuthController(
            AuthenticationManager authenticationManager,
            JwtUtil jwtUtil,
            UserRepository userRepository,
            PasswordEncoder passwordEncoder // ← 追加
    ) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder; // ← 追加
    }

    /**
     * ログイン（JWTトークン発行）
     */
    @PostMapping("/login")
    public String login(@RequestBody User user) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword())
            );
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            return jwtUtil.generateToken(userDetails.getUsername()); // JWTトークンを返す
        } catch (BadCredentialsException e) {
            return "認証失敗：メールアドレスまたはパスワードが違います";
        } catch (Exception e) {
            return "システムエラー：" + e.getMessage();
        }
    }

    /**
     * ユーザー登録
     */
    @PostMapping("/register")
    public String register(@RequestBody User user) {
        if (userRepository.findByEmail(user.getEmail()) != null) {
            return "すでに登録されています";
        }

        // パスワードをハッシュ化して保存
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        userRepository.save(user);
        return "登録完了";
    }
}
