package com.example.event.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * リクエストごとにJWTを検証し、
 * 認証情報をSecurityContextにセットするフィルター
 */
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil; // JWTの生成・検証ユーティリティ
    private final UserDetailsService userDetailsService; // ユーザー情報取得サービス

    public JwtAuthenticationFilter(JwtUtil jwtUtil, UserDetailsService userDetailsService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        try {
            // Authorizationヘッダーからトークンを取得
            String token = resolveToken(request);

            // トークンが存在し、有効で、まだ認証されていない場合
            if (StringUtils.hasText(token)
                    && jwtUtil.validateToken(token)
                    && SecurityContextHolder.getContext().getAuthentication() == null) {

                // トークンからユーザー名を抽出
                String username = jwtUtil.extractUsername(token);

                // ユーザー名からユーザー情報を取得（DBなどから）
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                // 認証済みトークンを作成（パスワードは不要なのでnull）
                UsernamePasswordAuthenticationToken auth =
                        new UsernamePasswordAuthenticationToken(
                                userDetails, null, userDetails.getAuthorities());

                // リクエスト情報を付加（IPアドレスなど）
                auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // SecurityContextに認証情報をセット
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        } catch (Exception e) {
            // 例外があれば認証情報をクリア
            SecurityContextHolder.clearContext();
        }

        // 次のフィルターへ処理を渡す
        filterChain.doFilter(request, response);
    }

    /**
     * Authorizationヘッダーから「Bearer 」付きのトークンを取り出す
     */
    private String resolveToken(HttpServletRequest request) {
        String bearer = request.getHeader("Authorization");
        if (StringUtils.hasText(bearer) && bearer.startsWith("Bearer ")) {
            return bearer.substring(7); // "Bearer " を除去
        }
        return null;
    }

    /**
     * このフィルターをスキップする条件
     * 認証不要なURLやOPTIONSメソッドの場合は実行しない
     */
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getServletPath();
        String method = request.getMethod();
        return "OPTIONS".equalsIgnoreCase(method)     // CORSプリフライトリクエスト
                || path.startsWith("/api/auth/")      // 認証API
                || path.startsWith("/public/")        // 公開API
                || path.startsWith("/actuator/health");// ヘルスチェック
    }
}
