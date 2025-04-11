package com.gymmanager.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gymmanager.dto.RoomDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class RoomControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private RoomDto createRoomForTest() throws Exception {
        RoomDto roomDto = new RoomDto();
        roomDto.setName("Test Room");
        roomDto.setCapacity(15);
        roomDto.setDescription("Test Room Description");

        String responseContent = mockMvc.perform(post("/api/rooms")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(roomDto)))
                .andReturn().getResponse().getContentAsString();
        return objectMapper.readValue(responseContent, RoomDto.class);

    }

    @Test
    public void testCreateRoom() throws Exception {
        RoomDto roomDto = new RoomDto();
        roomDto.setName("Room A");
        roomDto.setCapacity(20);
        roomDto.setDescription("Description for Room A");

        ResultActions result = mockMvc.perform(post("/api/rooms")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(roomDto)));

        result.andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name", is("Room A")))
                .andExpect(jsonPath("$.capacity", is(20)))
                .andExpect(jsonPath("$.description", is("Description for Room A")));
    }

    @Test
    public void testGetAllRooms() throws Exception {
        ResultActions result = mockMvc.perform(get("/api/rooms")
                .contentType(MediaType.APPLICATION_JSON));

        result.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
               
    }

    @Test
    public void testGetRoomById() throws Exception {
        RoomDto createdRoom = createRoomForTest();
        
        ResultActions result = mockMvc.perform(get("/api/rooms/" + createdRoom.getId())
                .contentType(MediaType.APPLICATION_JSON));

        result.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
                
    }

    @Test
    public void testUpdateRoom() throws Exception {
        RoomDto createdRoom = createRoomForTest();

        RoomDto roomDto = new RoomDto();
        roomDto.setName("Updated Room A");
        roomDto.setCapacity(25);
        roomDto.setDescription("Updated description");

        ResultActions result = mockMvc.perform(put("/api/rooms/" + createdRoom.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(roomDto)));

        result.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name", is("Updated Room A")));
                
    }

    @Test
    public void testDeleteRoom() throws Exception {
        RoomDto createdRoom = createRoomForTest();

        ResultActions result = mockMvc.perform(delete("/api/rooms/" + createdRoom.getId()));

        result.andExpect(status().isNoContent());
                .andExpect(jsonPath("$.capacity", is(25)))
                .andExpect(jsonPath("$.description", is("Updated description")));
    }

    @Test
    public void testDeleteRoom() throws Exception {
        // Assuming a room with ID 1 exists in the database
        RoomDto createdRoom = createRoomForTest();
        ResultActions result = mockMvc.perform(delete("/api/rooms/" + createdRoom.getId()));

        result.andExpect(status().isNoContent());
    }
}