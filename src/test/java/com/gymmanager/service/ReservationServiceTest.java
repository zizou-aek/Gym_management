package com.gymmanager.service;

import com.gymmanager.dto.CourseDto;
import com.gymmanager.dto.ReservationDto;
import com.gymmanager.model.Course;
import com.gymmanager.model.Reservation;
import com.gymmanager.model.User;
import com.gymmanager.repository.CourseRepository;
import com.gymmanager.repository.ReservationRepository;
import com.gymmanager.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ReservationServiceTest {

    @InjectMocks
    private ReservationService reservationService;

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CourseRepository courseRepository;

    @Test
    public void testCreateReservation() {
        // Given
        ReservationDto reservationDto = new ReservationDto();
        reservationDto.setUserId(1L);
        reservationDto.setCourseId(1L);
        reservationDto.setReservationTime(LocalDateTime.now());
        reservationDto.setStatus("pending");

        User user = new User();user.setId(1L);
        user.setUsername("testuser");
        user.setPassword("testpassword");

        Course course = new Course();course.setId(1L);
        course.setName("Test Course");

        Reservation reservation = new Reservation();
        reservation.setId(1L);
        reservation.setUser(user);
        reservation.setCourse(course);
        reservation.setReservationTime(reservationDto.getReservationTime());
        reservation.setReservationTime(reservationDto.getReservationTime());
        reservation.setStatus(reservationDto.getStatus());

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        when(reservationRepository.save(any(Reservation.class))).thenReturn(reservation);

        // When
        ReservationDto createdReservation = reservationService.createReservation(reservationDto);

        // Then
        assertEquals(reservation.getId(), createdReservation.getId());
        assertEquals(reservation.getUser().getId(), createdReservation.getUserId() );
        assertEquals(reservation.getCourse().getId(), createdReservation.getCourseId());
        assertEquals(reservation.getReservationTime(), createdReservation.getReservationTime());
        assertEquals(reservation.getStatus(), createdReservation.getStatus());
    }

    @Test
    public void testGetAllReservations() {
        // Given
        User user = new User();user.setId(1L);
        user.setUsername("testuser");
        user.setPassword("testpassword");

        Course course = new Course();course.setId(1L);
        course.setName("Test Course");

        Reservation reservation = new Reservation();reservation.setId(1L);
        reservation.setUser(user);
        reservation.setCourse(course);
        reservation.setReservationTime(LocalDateTime.now());
        reservation.setStatus("pending");

        List<Reservation> reservations = Collections.singletonList(reservation);
        
        when(reservationRepository.findAll()).thenReturn(reservations);

        when(reservationRepository.findAll()).thenReturn(reservations);

        // When
        List<ReservationDto> allReservations = reservationService.getAllReservations();

        // Then
        assertEquals(1, allReservations.size());
        assertEquals(reservation.getId(), allReservations.get(0).getId());
    }

    @Test
    public void testGetReservationById() {
        // Given
        User user = new User();user.setId(1L);
        user.setUsername("testuser");
        user.setPassword("testpassword");

        Course course = new Course();course.setId(1L);
        course.setName("Test Course");

        Reservation reservation = new Reservation();reservation.setId(1L);
        reservation.setReservationTime(LocalDateTime.now());
        reservation.setUser(user);
        reservation.setStatus("approved");

        when(reservationRepository.findById(1L)).thenReturn(Optional.of(reservation));

        // When
        ReservationDto foundReservation = reservationService.getReservationById(1L);

        // Then
        assertEquals(reservation.getId(), foundReservation.getId());
        assertEquals(reservation.getReservationTime(), foundReservation.getReservationTime());
        assertEquals(reservation.getStatus(), foundReservation.getStatus());
    }

    @Test
    public void testUpdateReservation() {
        // Given
        ReservationDto reservationDto = new ReservationDto();
        reservationDto.setUserId(2L);
        reservationDto.setCourseId(2L);
        reservationDto.setReservationTime(LocalDateTime.now().plusDays(1));
        reservationDto.setStatus("approved");


        User initialUser = new User();initialUser.setId(1L);
        initialUser.setUsername("testuser");
        initialUser.setPassword("testpassword");

        Course initialCourse = new Course();initialCourse.setId(1L);
        initialCourse.setName("Test Course");

        Reservation existingReservation = new Reservation();existingReservation.setId(1L);
        existingReservation.setUser(initialUser);
        existingReservation.setCourse(initialCourse);
        existingReservation.setReservationTime(LocalDateTime.now());
        existingReservation.setStatus("pending");

        User updatedUser = new User();updatedUser.setId(2L);
        updatedUser.setUsername("updateduser");
        updatedUser.setPassword("updatedpassword");

        Course updatedCourse = new Course();updatedCourse.setId(2L);
        updatedCourse.setName("Updated Test Course");
        Reservation updatedReservation = new Reservation();updatedReservation.setId(1L);
        updatedReservation.setId(1L);
        updatedReservation.setUser(user);
        updatedReservation.setCourse(course);
        updatedReservation.setReservationTime(reservationDto.getReservationTime());
        updatedReservation.setStatus(reservationDto.getStatus());

        when(reservationRepository.findById(1L)).thenReturn(Optional.of(existingReservation));
        when(userRepository.findById(2L)).thenReturn(Optional.of(updatedUser));
        when(courseRepository.findById(2L)).thenReturn(Optional.of(updatedCourse));
        when(reservationRepository.save(any(Reservation.class))).thenReturn(updatedReservation);

        // When
        ReservationDto result = reservationService.updateReservation(1L, reservationDto);

        // Then
        assertEquals(updatedReservation.getId(), result.getId());
        assertEquals(updatedReservation.getReservationTime(), result.getReservationTime());
        assertEquals(updatedReservation.getStatus(), result.getStatus());
    }

    @Test
    public void testDeleteReservation() {
        // Given
        Long reservationId = 1L;

        // When
        reservationService.deleteReservation(reservationId);

        // Then
        verify(reservationRepository, times(1)).deleteById(reservationId);
    }
    
}