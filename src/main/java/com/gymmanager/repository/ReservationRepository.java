package com.gymmanager.repository;

import com.gymmanager.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    List<Reservation> findByUserId(Long userId);

    List<Reservation> findByCourseId(Long courseId);

    List<Reservation> findByReservationTimeBetween(LocalDateTime startTime, LocalDateTime endTime);
}