package com.example.event.repository;

import com.example.event.model.Event;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.beans.factory.annotation.Autowired;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class EventRepository {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public EventRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // イベント一覧を取得するメソッド
    public List<Event> findAll() {
        String sql = "SELECT id, title, description, event_date, capacity FROM events";
        return jdbcTemplate.query(sql, new EventRowMapper());
    }

    // ResultSet → Eventへの変換
    private static class EventRowMapper implements RowMapper<Event> { //Repositoryクラスはテーブルごとに作る。また、RowMapperクラスも同様に作る。    
        @Override
        public Event mapRow(ResultSet rs, int rowNum) throws SQLException {// Eventはテーブルに対応したフィールドを持つmodel層の自作クラス
            return new Event(                                               //DBから取得したデータをjava型に変換してそのフィールドに代入していくメソッド
                rs.getInt("id"),
                rs.getString("title"),
                rs.getString("description"),
                rs.getString("event_date"), // ← DBカラム名に合わせてください
                rs.getInt("capacity")
            );
        }
    }
}
