import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gymmanager.dto.MembershipDto;
import com.gymmanager.dto.UserDto;
import com.gymmanager.model.Membership;
import com.gymmanager.repository.MembershipRepository;
import com.gymmanager.repository.UserRepository;
import com.gymmanager.service.MembershipService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class MembershipControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MembershipService membershipService;

    @Autowired
    private MembershipRepository membershipRepository;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;
    private String registerUser(UserDto userDto) throws Exception {
        MvcResult result = mockMvc.perform(post("/api/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isCreated())
                .andReturn();
        return result.getResponse().getContentAsString();
    }

    private String createMembership(MembershipDto membershipDto) throws Exception {
        MvcResult result = mockMvc.perform(post("/api/memberships")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(membershipDto)))
                .andExpect(status().isCreated())
                .andReturn();
        return result.getResponse().getContentAsString();
    }
    @Test
    public void testCreateMembership() throws Exception {
        UserDto userDto = new UserDto();
        userDto.setUsername("testuser");
        userDto.setPassword("password");
        userDto.setEmail("test@example.com");

        String userJson = registerUser(userDto);
        UserDto createdUser = objectMapper.readValue(userJson, UserDto.class);

        MembershipDto membershipDto = new MembershipDto();
        membershipDto.setUserId(createdUser.getId());
        membershipDto.setType("Premium");
        membershipDto.setStartDate(LocalDate.now());
        membershipDto.setEndDate(LocalDate.now().plusMonths(12));
        membershipDto.setPaymentStatus("Paid");

        MvcResult result = mockMvc.perform(post("/api/memberships")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(membershipDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.type").value("Premium"))
                .andReturn();

        String content = result.getResponse().getContentAsString();
        MembershipDto responseDto = objectMapper.readValue(content, MembershipDto.class);
        assertEquals(membershipDto.getType(), responseDto.getType());
        assertEquals(membershipDto.getPaymentStatus(), responseDto.getPaymentStatus());
        assertEquals(membershipDto.getStartDate(), responseDto.getStartDate());
        assertEquals(membershipDto.getEndDate(), responseDto.getEndDate());
        assertEquals(membershipDto.getUserId(),responseDto.getUserId());
    }

    @Test
    public void testGetAllMemberships() throws Exception {
         mockMvc.perform(get("/api/memberships"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    public void testGetMembershipById() throws Exception {
        UserDto userDto = new UserDto();
        userDto.setUsername("testuser");
        userDto.setPassword("password");
        userDto.setEmail("test@example.com");

        String userJson = registerUser(userDto);
        UserDto createdUser = objectMapper.readValue(userJson, UserDto.class);

        MembershipDto membershipDto = new MembershipDto();
        membershipDto.setUserId(createdUser.getId());
        membershipDto.setType("Premium");
        membershipDto.setStartDate(LocalDate.now());
        membershipDto.setEndDate(LocalDate.now().plusMonths(12));
        membershipDto.setPaymentStatus("Paid");

        String membershipJson = createMembership(membershipDto);
        MembershipDto createdMembership = objectMapper.readValue(membershipJson, MembershipDto.class);

        mockMvc.perform(get("/api/memberships/" + createdMembership.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(createdMembership.getId()));
    }

    @Test
    public void testUpdateMembership() throws Exception {
        UserDto userDto = new UserDto();
        userDto.setUsername("testuser");
        userDto.setPassword("password");
        userDto.setEmail("test@example.com");

        String userJson = registerUser(userDto);
        UserDto createdUser = objectMapper.readValue(userJson, UserDto.class);


        MembershipDto membershipDto = new MembershipDto();
        membershipDto.setUserId(createdUser.getId());
        membershipDto.setType("Premium");
        membershipDto.setStartDate(LocalDate.now());
        membershipDto.setEndDate(LocalDate.now().plusMonths(12));
        membershipDto.setPaymentStatus("Paid");

        String membershipJson = createMembership(membershipDto);
        MembershipDto createdMembership = objectMapper.readValue(membershipJson, MembershipDto.class);


        MembershipDto updatedMembershipDto = new MembershipDto();
        updatedMembershipDto.setUserId(createdUser.getId());
        membershipDto.setType("Gold");
        membershipDto.setStartDate(LocalDate.now());
        membershipDto.setEndDate(LocalDate.now().plusMonths(6));
        membershipDto.setPaymentStatus("Paid");

        mockMvc.perform(put("/api/memberships/"+createdMembership.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(membershipDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.type").value("Gold"));
    }

    @Test
    public void testDeleteMembership() throws Exception {
        UserDto userDto = new UserDto();
        userDto.setUsername("testuser");
        userDto.setPassword("password");
        userDto.setEmail("test@example.com");

        String userJson = registerUser(userDto);
        UserDto createdUser = objectMapper.readValue(userJson, UserDto.class);

        MembershipDto membershipDto = new MembershipDto();
        membershipDto.setUserId(createdUser.getId());
        membershipDto.setType("Premium");
        membershipDto.setStartDate(LocalDate.now());
        membershipDto.setEndDate(LocalDate.now().plusMonths(12));
        membershipDto.setPaymentStatus("Paid");

        String membershipJson = createMembership(membershipDto);
        MembershipDto createdMembership = objectMapper.readValue(membershipJson, MembershipDto.class);        mockMvc.perform(delete("/api/memberships/"+createdMembership.getId()))
                .andExpect(status().isNoContent());
    }
}