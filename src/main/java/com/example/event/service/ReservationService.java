package com.example.event.service;

import com.example.event.model.Reservation;
import com.example.event.model.User;
import com.example.event.repository.ReservationRepository;
import com.example.event.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final UserRepository userRepository;

    public ReservationService(ReservationRepository reservationRepository, UserRepository userRepository) {
        this.reservationRepository = reservationRepository;
        this.userRepository = userRepository;
    }

    /**
     * 予約を作成する処理
     */
    public String createReservation(Reservation reservation, String email) {
        // ユーザーをメールアドレスから取得
        User user = userRepository.findByEmail(email);
        if (user == null) return "ユーザーが見つかりません";

        // ユーザーIDを予約情報に紐づけて保存
        reservation.setUserId(user.getId());
        reservationRepository.save(reservation);
        return "予約が完了しました";
    }

    /**
     * 自分の予約だけ削除できる処理
     */
    public String deleteReservation(int reservationId, String email) {
        // ユーザー情報を取得
        User user = userRepository.findByEmail(email);
        if (user == null) return "ユーザーが見つかりません";

        // 予約情報を取得
        Reservation reservation = reservationRepository.findById(reservationId);
        if (reservation == null) return "予約が見つかりません";

        // ログインユーザーの予約か確認
        if (reservation.getUserId() != user.getId()) {
            return "自分の予約のみ削除できます";
        }

        // 予約削除
        reservationRepository.deleteById(reservationId);
        return "予約を削除しました";
    }

    /**
     * 全予約一覧を取得（管理者用やデバッグ用）
     */
    public List<Reservation> getAllReservations() {
        return reservationRepository.findAll();
    }
}
