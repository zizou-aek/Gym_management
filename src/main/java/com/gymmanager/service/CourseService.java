package com.gymmanager.service;

import com.gymmanager.dto.CourseDto;
import com.gymmanager.model.Room;
import com.gymmanager.model.Course;
import com.gymmanager.repository.RoomRepository;
import com.gymmanager.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CourseService {

    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private RoomRepository roomRepository;

    public CourseDto createCourse(CourseDto courseDto) {
        Course course = new Course();
        course.setName(courseDto.getName());
        course.setDescription(courseDto.getDescription());
        course.setSchedule(courseDto.getSchedule());
        course.setInstructor(courseDto.getInstructor());
        course.setMaxParticipants(courseDto.getMaxParticipants());
        Room room = roomRepository.findById(courseDto.getRoomId()).orElseThrow(()-> new RuntimeException("Room not found"));
        course.setRoom(room);
        Course savedCourse = courseRepository.save(course);
        return convertToDto(savedCourse);
    }

    public List<CourseDto> getAllCourses() {
        List<Course> courses = courseRepository.findAll();
        List<CourseDto> courseDtos = new ArrayList<>();
        for (Course course : courses) {
            courseDtos.add(convertToDto(course));
        }
        return courseDtos;
    }

    public CourseDto getCourseById(Long id) {
        Course course = courseRepository.findById(id).orElseThrow(() -> new RuntimeException("Course not found"));
        return convertToDto(course);
    }

    public CourseDto updateCourse(Long id, CourseDto courseDto) {
        Course course = courseRepository.findById(id).orElseThrow(() -> new RuntimeException("Course not found"));
        course.setName(courseDto.getName());
        course.setDescription(courseDto.getDescription());
        course.setSchedule(courseDto.getSchedule());
        course.setInstructor(courseDto.getInstructor());
        course.setMaxParticipants(courseDto.getMaxParticipants());
        Room room = roomRepository.findById(courseDto.getRoomId()).orElseThrow(()-> new RuntimeException("Room not found"));
        course.setRoom(room);
        Course updatedCourse = courseRepository.save(course);
        return convertToDto(updatedCourse);
    }

    public void deleteCourse(Long id) {
        courseRepository.deleteById(id);
    }
    private CourseDto convertToDto(Course course) {
        CourseDto courseDto = new CourseDto();
        courseDto.setId(course.getId());
        courseDto.setName(course.getName());
        courseDto.setDescription(course.getDescription());
        courseDto.setSchedule(course.getSchedule());
        courseDto.setInstructor(course.getInstructor());
        courseDto.setMaxParticipants(course.getMaxParticipants());
        if (course.getRoom() != null){
            courseDto.setRoomId(course.getRoom().getId());
        }

        return courseDto;
    }
}