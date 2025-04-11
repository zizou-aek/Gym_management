import com.fasterxml.jackson.databind.ObjectMapper;
import com.gymmanager.dto.CourseDto;
import com.gymmanager.dto.RoomDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class CourseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private Long roomId;
    private Long courseId;

    private ResultActions createRoom(RoomDto roomDto) throws Exception {
        return mockMvc.perform(post("/api/rooms")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(roomDto)));
    }

    @BeforeEach
    public void setup() throws Exception {
        RoomDto roomDto = new RoomDto();
        roomDto.setName("Test Room");
        roomDto.setCapacity(20);
        roomDto.setDescription("A test room");
        String roomResponse = createRoom(roomDto).andReturn().getResponse().getContentAsString();
        roomId = objectMapper.readTree(roomResponse).get("id").asLong();
    }

    @Test
    public void testCreateCourse() throws Exception {
        CourseDto courseDto = new CourseDto();
        courseDto.setName("Yoga");
        courseDto.setDescription("Relaxing yoga class");
        courseDto.setSchedule("Monday 6 PM");
        courseDto.setRoomId(roomId);

        ResultActions result = mockMvc.perform(post("/api/courses")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(courseDto)));

        result.andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is("Yoga")))
                 .andExpect(content().contentType(MediaType.APPLICATION_JSON))

                .andExpect(jsonPath("$.roomId", is(1)));
    }

    @Test
    public void testGetAllCourses() throws Exception {
        ResultActions result = mockMvc.perform(get("/api/courses")
                .contentType(MediaType.APPLICATION_JSON));

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0))); // Assuming initially no courses exist. Adjust if needed.
    }


    @Test
    public void testGetCourseById() throws Exception {
        CourseDto courseDto = new CourseDto();
        courseDto.setName("Yoga");
        courseDto.setDescription("Relaxing yoga class");
        courseDto.setSchedule("Monday 6 PM");
        courseDto.setRoomId(roomId);

        String courseResponse = mockMvc.perform(post("/api/courses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(courseDto)))
                .andReturn().getResponse().getContentAsString();
        courseId = objectMapper.readTree(courseResponse).get("id").asLong();


        ResultActions result = mockMvc.perform(get("/api/courses/"+courseId)
                .contentType(MediaType.APPLICATION_JSON));

        result.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name", is("Yoga")))
                .andExpect(jsonPath("$.description", is("Relaxing yoga class")))
                .andExpect(jsonPath("$.schedule", is("Monday 6 PM")))
                .andExpect(jsonPath("$.roomId", is(roomId.intValue())));

    }

    @Test
    public void testUpdateCourse() throws Exception {
        CourseDto initialCourseDto = new CourseDto();
        initialCourseDto.setName("Yoga");
        initialCourseDto.setDescription("Relaxing yoga class");
        initialCourseDto.setSchedule("Monday 6 PM");
        initialCourseDto.setRoomId(roomId);

        String initialCourseResponse = mockMvc.perform(post("/api/courses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(initialCourseDto)))
                .andReturn().getResponse().getContentAsString();
        courseId = objectMapper.readTree(initialCourseResponse).get("id").asLong();

        CourseDto courseDto = new CourseDto();
        courseDto.setName("Updated Yoga");
        courseDto.setDescription("More advanced yoga");
        courseDto.setSchedule("Tuesday 7 PM");
        courseDto.setRoomId(roomId);

        ResultActions result = mockMvc.perform(put("/api/courses/"+courseId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(courseDto)));

        result.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name", is("Updated Yoga")))
                .andExpect(jsonPath("$.description", is("More advanced yoga")))
                .andExpect(jsonPath("$.schedule", is("Tuesday 7 PM")))
                .andExpect(jsonPath("$.roomId", is(roomId.intValue())));


    }

    @Test
    public void testDeleteCourse() throws Exception {
        // Assuming a course with ID 1 exists.  You may need to create one in a setup method.
        ResultActions result = mockMvc.perform(delete("/api/courses/1")
                .contentType(MediaType.APPLICATION_JSON));
       CourseDto initialCourseDto = new CourseDto();
        initialCourseDto.setName("Yoga");
        initialCourseDto.setDescription("Relaxing yoga class");
        initialCourseDto.setSchedule("Monday 6 PM");
        initialCourseDto.setRoomId(roomId);

        String initialCourseResponse = mockMvc.perform(post("/api/courses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(initialCourseDto)))
                .andReturn().getResponse().getContentAsString();
        courseId = objectMapper.readTree(initialCourseResponse).get("id").asLong();
          result = mockMvc.perform(delete("/api/courses/"+courseId)
                .contentType(MediaType.APPLICATION_JSON));
       result.andExpect(status().isNoContent());
    }
}