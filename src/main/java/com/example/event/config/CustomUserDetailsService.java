package com.example.event.config;

import com.example.event.model.User;
import com.example.event.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

@Service  //UserDetailsServiceの役割 = DBなどから取得したユーザー情報を、Spring Security共通のUserDetails型に変換して返すだけ
public class CustomUserDetailsService implements UserDetailsService { //userDetails型のオブジェクトから、ログインしているユーザーの
                                                                    //情報を取得できる

    private final UserRepository userRepository;

    @Autowired
    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email);

        if (user == null) {
            throw new UsernameNotFoundException("ユーザーが見つかりません: " + email);
        }

        return org.springframework.security.core.userdetails.User
                .withUsername(user.getEmail())
                .password(user.getPassword()) // BCryptでハッシュ化済み
                .authorities("USER")
                .accountExpired(false)
                .accountLocked(false)
                .credentialsExpired(false)
                .disabled(false)
                .build();
    }
}
