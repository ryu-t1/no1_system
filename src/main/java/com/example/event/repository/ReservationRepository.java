// ReservationRepository.java（完全版 - userId 対応）
package com.example.event.repository;

import com.example.event.model.Reservation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public class ReservationRepository {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public ReservationRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Reservation> findAll() {
        String sql = "SELECT * FROM reservations";
        return jdbcTemplate.query(sql, new ReservationRowMapper());
    }

    public Reservation save(Reservation reservation) {
        String sql = "INSERT INTO reservations (event_id, name, email, reserved_at, user_id) VALUES (?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql,
                reservation.getEventId(),
                reservation.getName(),
                reservation.getEmail(),
                LocalDateTime.now().toString(),
                reservation.getUserId());

        return reservation; // ※id取得して返したい場合は KeyHolder 使用
    }

    public int deleteById(int id) {
        String sql = "DELETE FROM reservations WHERE id = ?";
        return jdbcTemplate.update(sql, id);
    }

    public Reservation findById(int id) {
        String sql = "SELECT * FROM reservations WHERE id = ?";
        List<Reservation> list = jdbcTemplate.query(sql, new ReservationRowMapper(), id);
        return list.isEmpty() ? null : list.get(0);
    }

    private static class ReservationRowMapper implements RowMapper<Reservation> {
        @Override
        public Reservation mapRow(ResultSet rs, int rowNum) throws SQLException {
            Reservation r = new Reservation();
            r.setId(rs.getInt("id"));
            r.setEventId(rs.getInt("event_id"));
            r.setName(rs.getString("name"));
            r.setEmail(rs.getString("email"));
            r.setReservedAt(rs.getString("reserved_at"));
            r.setUserId(rs.getInt("user_id")); // user_idをマッピング
            return r;
        }
    }
}
