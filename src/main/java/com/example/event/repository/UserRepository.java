package com.example.event.repository;

import com.example.event.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class UserRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // ユーザー登録
    public void save(User user) {
        String sql = "INSERT INTO users (name, email, password) VALUES (?, ?, ?)";
        jdbcTemplate.update(sql, user.getName(), user.getEmail(), user.getPassword());
    }

   // メールアドレスでユーザーを検索（登録時の重複確認用）
public User findByEmail(String email) {
    String sql = "SELECT * FROM users WHERE email = ?";
    List<User> users = jdbcTemplate.query(sql, new UserRowMapper(), email);
    return users.isEmpty() ? null : users.get(0);
}

// メールアドレスとパスワードでログイン
public User findByEmailAndPassword(String email, String password) {
    String sql = "SELECT * FROM users WHERE email = ? AND password = ?";
    List<User> users = jdbcTemplate.query(sql, new UserRowMapper(), email, password);
    return users.isEmpty() ? null : users.get(0);
}

// IDでユーザーを取得（セッションからの復元用）
public User findById(int id) {
    String sql = "SELECT * FROM users WHERE id = ?";
    List<User> users = jdbcTemplate.query(sql, new UserRowMapper(), id);
    return users.isEmpty() ? null : users.get(0);
}


    // 全ユーザー取得（オプション）
    public List<User> findAll() {
        String sql = "SELECT * FROM users";
        return jdbcTemplate.query(sql, new UserRowMapper());
    }

    // RowMapper（内部クラス）
    private static class UserRowMapper implements RowMapper<User> {
        @Override
        public User mapRow(ResultSet rs, int rowNum) throws SQLException {
            User user = new User();
            user.setId(rs.getInt("id"));
            user.setName(rs.getString("name"));
            user.setEmail(rs.getString("email"));
            user.setPassword(rs.getString("password"));
            return user;
        }
    }
}
