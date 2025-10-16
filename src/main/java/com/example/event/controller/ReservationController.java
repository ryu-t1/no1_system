package com.example.event.controller;

import com.example.event.model.Reservation;
import com.example.event.service.ReservationService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/reservations")
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    /**
     * 認証済ユーザーによる予約登録
     * フロント側は fetch で JSON を期待しているので、ResponseEntity<Map> で返す
     */
    @PostMapping
    public ResponseEntity<Map<String, String>> createReservation(
            @RequestBody Reservation reservation,
            @AuthenticationPrincipal UserDetails userDetails) { //UserDetailsは今ログインしているユーザの情報。spring sequrityによって情報が変数に入る
                                                                
        String email = userDetails.getUsername();
        String message = reservationService.createReservation(reservation, email);

        Map<String, String> response = new HashMap<>();
        response.put("message", message);
        return ResponseEntity.ok(response);
    }

    /**
     * 認証済ユーザーによる予約削除
     * 自分の予約のみ削除できる
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteReservation(
            @PathVariable int id,
            @AuthenticationPrincipal UserDetails userDetails) {

        String email = userDetails.getUsername();
        String message = reservationService.deleteReservation(id, email);

        Map<String, String> response = new HashMap<>();
        response.put("message", message);
        return ResponseEntity.ok(response);
    }

    /**
     * 全予約一覧取得（管理者用・開発用）
     */
    @GetMapping
    public List<Reservation> getAllReservations() {
        return reservationService.getAllReservations();
    }
}

//認証はsequrityのFilterで終わっているから、コントローラ層とサービス層にその記述はない。
//認可が必要な場合はサービス層で書く。　ログインしているユーザ情報の取得は可能。