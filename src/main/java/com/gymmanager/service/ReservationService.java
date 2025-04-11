package com.gymmanager.service;

import com.gymmanager.dto.ReservationDto;
import com.gymmanager.model.Course;
import com.gymmanager.model.Reservation;
import com.gymmanager.model.User;
import com.gymmanager.repository.CourseRepository;
import com.gymmanager.repository.ReservationRepository;
import com.gymmanager.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ReservationService {

    @Autowired
    private ReservationRepository reservationRepository;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CourseRepository courseRepository;

    public ReservationDto createReservation(ReservationDto reservationDto) {
        Reservation reservation = new Reservation();
        reservation.setReservationTime(reservationDto.getReservationTime());
        reservation.setStatus(reservationDto.getStatus());
        User user = userRepository.findById(reservationDto.getUserId()).orElseThrow(() -> new RuntimeException("User not found"));
        reservation.setUser(user);
        Course course = courseRepository.findById(reservationDto.getCourseId()).orElseThrow(() -> new RuntimeException("Course not found"));
        reservation.setCourse(course);
        Reservation savedReservation = reservationRepository.save(reservation);
        return convertToDto(savedReservation);
    }

    public List<ReservationDto> getAllReservations() {
        List<Reservation> reservations = reservationRepository.findAll();
        List<ReservationDto> reservationDtos = new ArrayList<>();
        for (Reservation reservation : reservations) {
            reservationDtos.add(convertToDto(reservation));
        }
        return reservationDtos;
    }

    public ReservationDto getReservationById(Long id) {
        Reservation reservation = reservationRepository.findById(id).orElseThrow(() -> new RuntimeException("Reservation not found"));
        return convertToDto(reservation);
    }

    public List<Reservation> getReservationsByUserId(Long userId) {
        return reservationRepository.findByUserId(userId);
    }

    public ReservationDto updateReservation(Long id, ReservationDto reservationDto) {
        Reservation reservation = reservationRepository.findById(id).orElseThrow(() -> new RuntimeException("Reservation not found"));
        reservation.setReservationTime(reservationDto.getReservationTime());
        reservation.setStatus(reservationDto.getStatus());
        User user = userRepository.findById(reservationDto.getUserId()).orElseThrow(() -> new RuntimeException("User not found"));
        reservation.setUser(user);
        Course course = courseRepository.findById(reservationDto.getCourseId()).orElseThrow(() -> new RuntimeException("Course not found"));
        reservation.setCourse(course);
        Reservation updatedReservation = reservationRepository.save(reservation);
        return convertToDto(updatedReservation);
    }

    public void deleteReservation(Long id) {
        reservationRepository.deleteById(id);
    }

    private ReservationDto convertToDto(Reservation reservation) {
        ReservationDto dto = new ReservationDto();
        dto.setId(reservation.getId());
        dto.setUserId(reservation.getUser().getId());
        dto.setCourseId(reservation.getCourse().getId());
        dto.setReservationTime(reservation.getReservationTime());
        dto.setStatus(reservation.getStatus());
        return dto;
    }
}