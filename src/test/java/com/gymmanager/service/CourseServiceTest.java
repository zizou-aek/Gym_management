package com.gymmanager.service;

import com.gymmanager.dto.CourseDto;
import com.gymmanager.model.Course;
import com.gymmanager.dto.CourseDto;
import com.gymmanager.model.Room;
import com.gymmanager.repository.CourseRepository;
import com.gymmanager.repository.RoomRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CourseServiceTest {

    @InjectMocks
    private CourseService courseService;

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private RoomRepository roomRepository;

    
    @Test
    public void testCreateCourse() {
        CourseDto courseDto = new CourseDto();
        courseDto.setName("Yoga");
        courseDto.setDescription("Relaxing yoga session");
        courseDto.setSchedule("Monday 7 PM");
        courseDto.setInstructor("John Doe");
        courseDto.setMaxParticipants(15);
        courseDto.setRoomId(1L);

        Room room = new Room();
        room.setId(1L);
        room.setName("Studio 1");
        room.setCapacity(20);

        Course course = new Course();
        course.setId(1L);
        course.setName(courseDto.getName());
        course.setDescription(courseDto.getDescription());
        course.setSchedule(courseDto.getSchedule());
        course.setInstructor(courseDto.getInstructor());
        course.setMaxParticipants(courseDto.getMaxParticipants());
        course.setRoom(room);

        when(roomRepository.findById(1L)).thenReturn(Optional.of(room));
        when(courseRepository.save(any(Course.class))).thenReturn(course);

        CourseDto createdCourse = courseService.createCourse(courseDto);

        assertEquals(courseDto.getName(), createdCourse.getName());
        assertEquals(courseDto.getDescription(), createdCourse.getDescription());
        assertEquals(courseDto.getSchedule(), createdCourse.getSchedule());
        assertEquals(courseDto.getInstructor(), createdCourse.getInstructor());
        assertEquals(courseDto.getMaxParticipants(), createdCourse.getMaxParticipants());
        assertEquals(1L, createdCourse.getRoomId());
    }

    @Test
    public void testGetAllCourses() {
        Room room = new Room();
        room.setId(1L);
        room.setName("Studio 1");
        room.setCapacity(20);

        Course course1 = new Course();
        course1.setId(1L);
        course1.setName("Yoga");
        course1.setDescription("Relaxing yoga session");
        course1.setSchedule("Monday 7 PM");
        course1.setInstructor("John Doe");
        course1.setMaxParticipants(15);
        course1.setRoom(room);

        Course course2 = new Course();
        course2.setId(2L);
        course2.setName("Pilates");
        course2.setDescription("Core strengthening class");
        course2.setSchedule("Tuesday 6 PM");
        course2.setInstructor("Jane Smith");
        course2.setMaxParticipants(10);
        course2.setRoom(room);

        List<Course> courses = Arrays.asList(course1, course2);

        when(courseRepository.findAll()).thenReturn(courses);

        List<CourseDto> courseDtos = courseService.getAllCourses();

        assertEquals(2, courseDtos.size());
        assertEquals("Yoga", courseDtos.get(0).getName());
        assertEquals("Pilates", courseDtos.get(1).getName());
    }

    @Test
    public void testGetCourseById() {
        Room room = new Room();
        room.setId(1L);
        room.setName("Studio 1");
        room.setCapacity(20);

        Course course = new Course();
        course.setId(1L);
        course.setName("Yoga");
        course.setDescription("Relaxing yoga session");
        course.setSchedule("Monday 7 PM");
        course.setInstructor("John Doe");
        course.setMaxParticipants(15);
        course.setRoom(room);

        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));

        CourseDto courseDto = courseService.getCourseById(1L);

        assertEquals("Yoga", courseDto.getName());
        assertEquals("Relaxing yoga session", courseDto.getDescription());
    }

    @Test
    public void testUpdateCourse() {
        CourseDto courseDto = new CourseDto();
        courseDto.setName("Updated Yoga");
        courseDto.setDescription("Updated relaxing yoga session");
        courseDto.setSchedule("Wednesday 8 PM");
        courseDto.setInstructor("John Smith");
        courseDto.setMaxParticipants(12);
        courseDto.setRoomId(2L);

        Room room = new Room();
        room.setId(1L);
        room.setName("Studio 1");
        room.setCapacity(20);

        Room updatedRoom = new Room();
        updatedRoom.setId(2L);
        updatedRoom.setName("Studio 2");
        updatedRoom.setCapacity(15);

        Course course = new Course();
        course.setId(1L);
        course.setName("Yoga");
        course.setDescription("Relaxing yoga session");
        course.setSchedule("Monday 7 PM");
        course.setInstructor("John Doe");
        course.setMaxParticipants(15);
        course.setRoom(room);

        Course updatedCourse = new Course();
        updatedCourse.setId(1L);
        updatedCourse.setName(courseDto.getName());
        updatedCourse.setDescription(courseDto.getDescription());
        updatedCourse.setSchedule(courseDto.getSchedule());
        updatedCourse.setInstructor(courseDto.getInstructor());
        updatedCourse.setMaxParticipants(courseDto.getMaxParticipants());
        updatedCourse.setRoom(updatedRoom);

        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        when(roomRepository.findById(2L)).thenReturn(Optional.of(updatedRoom));
        when(courseRepository.save(any(Course.class))).thenReturn(updatedCourse);

        CourseDto resultDto = courseService.updateCourse(1L, courseDto);

        assertEquals(courseDto.getName(), resultDto.getName());
        assertEquals(courseDto.getDescription(), resultDto.getDescription());
        assertEquals(courseDto.getSchedule(), resultDto.getSchedule());
        assertEquals(courseDto.getInstructor(), resultDto.getInstructor());
        assertEquals(courseDto.getMaxParticipants(), resultDto.getMaxParticipants());
        assertEquals(2L, resultDto.getRoomId());
    }

    @Test
    public void testDeleteCourse() {
        doNothing().when(courseRepository).deleteById(1L);
        courseService.deleteCourse(1L);
        verify(courseRepository, times(1)).deleteById(1L);
    }
}