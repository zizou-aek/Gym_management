import com.gymmanager.dto.CourseDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gymmanager.dto.RoomDto;
import com.gymmanager.dto.ReservationDto;
import com.gymmanager.dto.UserDto;
import com.gymmanager.model.Reservation;
import com.gymmanager.repository.ReservationRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class ReservationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ReservationRepository reservationRepository;

    private HashMap<String, Long> createdResources = new HashMap<>();

    private ResultActions testRegisterUser(UserDto userDto) throws Exception {
        ResultActions result = mockMvc.perform(post("/api/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userDto)));
        return result;
    }

    private ResultActions testCreateRoom(RoomDto roomDto) throws Exception {
        ResultActions result = mockMvc.perform(post("/api/rooms")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(roomDto)));
        return result;
    }

    private ResultActions testCreateCourse(CourseDto courseDto) throws Exception {
        ResultActions result = mockMvc.perform(post("/api/courses")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(courseDto)));
        return result;
    }

    private ResultActions testCreateReservation(ReservationDto reservationDto) throws Exception {
        ResultActions result = mockMvc.perform(post("/api/reservations")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(reservationDto)));
        return result;
    }

    
    @Test
    public void testCreateReservation() throws Exception {

        UserDto userDto = new UserDto();
        userDto.setUsername("testuser");
        userDto.setPassword("testpass");
        userDto.setEmail("test@example.com");
        ResultActions userResult = testRegisterUser(userDto);
        String userContent = userResult.andReturn().getResponse().getContentAsString();
        UserDto createdUser = objectMapper.readValue(userContent, UserDto.class);
        createdResources.put("user", createdUser.getId());

        RoomDto roomDto = new RoomDto();
        roomDto.setName("Test Room");
        roomDto.setCapacity(20);
        ResultActions roomResult = testCreateRoom(roomDto);
        String roomContent = roomResult.andReturn().getResponse().getContentAsString();
        RoomDto createdRoom = objectMapper.readValue(roomContent, RoomDto.class);
        createdResources.put("room", createdRoom.getId());

        CourseDto courseDto = new CourseDto();
        courseDto.setName("Test Course");
        courseDto.setRoomId(createdRoom.getId());
        ResultActions courseResult = testCreateCourse(courseDto);
        String courseContent = courseResult.andReturn().getResponse().getContentAsString();
        CourseDto createdCourse = objectMapper.readValue(courseContent, CourseDto.class);
        createdResources.put("course", createdCourse.getId());

        ReservationDto reservationDto = new ReservationDto();
        reservationDto.setUserId(createdUser.getId());
        reservationDto.setCourseId(createdCourse.getId());
        reservationDto.setReservationTime(LocalDateTime.now().plusDays(1));
        reservationDto.setStatus("Confirmed");

        ResultActions result = testCreateReservation(reservationDto);

        result.andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }
    @Test
    public void testGetAllReservations() throws Exception {
        ResultActions result = mockMvc.perform(get("/api/reservations").contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }
    @Test
    public void testGetReservationById() throws Exception {
        UserDto userDto = new UserDto();
        userDto.setUsername("testuser2");
        userDto.setPassword("testpass");
        userDto.setEmail("test2@example.com");
        ResultActions userResult = testRegisterUser(userDto);
        String userContent = userResult.andReturn().getResponse().getContentAsString();
        UserDto createdUser = objectMapper.readValue(userContent, UserDto.class);

        RoomDto roomDto = new RoomDto();
        roomDto.setName("Test Room 2");
        roomDto.setCapacity(20);
        ResultActions roomResult = testCreateRoom(roomDto);
        String roomContent = roomResult.andReturn().getResponse().getContentAsString();
        RoomDto createdRoom = objectMapper.readValue(roomContent, RoomDto.class);

        CourseDto courseDto = new CourseDto();
        courseDto.setName("Test Course 2");
        courseDto.setRoomId(createdRoom.getId());
        ResultActions courseResult = testCreateCourse(courseDto);
        String courseContent = courseResult.andReturn().getResponse().getContentAsString();
        CourseDto createdCourse = objectMapper.readValue(courseContent, CourseDto.class);

        ReservationDto reservationDto = new ReservationDto();
        reservationDto.setUserId(createdUser.getId());
        reservationDto.setCourseId(createdCourse.getId());
        reservationDto.setReservationTime(LocalDateTime.now().plusDays(1));
        reservationDto.setStatus("Confirmed");
        ResultActions reservationResult = testCreateReservation(reservationDto);
        String reservationContent = reservationResult.andReturn().getResponse().getContentAsString();
        ReservationDto createdReservation = objectMapper.readValue(reservationContent, ReservationDto.class);

        ResultActions result = mockMvc.perform(get("/api/reservations/" + createdReservation.getId())
                .contentType(MediaType.APPLICATION_JSON));

        result.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }
    @Test
    public void testUpdateReservation() throws Exception {
        ReservationDto reservationDto = new ReservationDto();
        reservationDto.setUserId(createdResources.get("user"));
        reservationDto.setCourseId(createdResources.get("course"));
        reservationDto.setReservationTime(LocalDateTime.now().plusDays(2));
        reservationDto.setStatus("Updated");

        ResultActions result = mockMvc.perform(put("/api/reservations/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(reservationDto)));

        result.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }
    @Test
    public void testDeleteReservation() throws Exception {
        ResultActions result = mockMvc.perform(delete("/api/reservations/1"));

        result.andExpect(status().isNoContent());
    }
}