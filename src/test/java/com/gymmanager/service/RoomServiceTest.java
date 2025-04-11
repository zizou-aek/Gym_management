package com.gymmanager.service;

import com.gymmanager.dto.RoomDto;
import com.gymmanager.model.Room;
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
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RoomServiceTest {

    @InjectMocks
    private RoomService roomService;

    @Mock
    private RoomRepository roomRepository;

    @Test
    public void testCreateRoom() {
        RoomDto roomDto = new RoomDto();
        roomDto.setId(1L);
        roomDto.setName("Test Room");
        roomDto.setCapacity(20);
        roomDto.setDescription("A room for testing");

        Room room = new Room();
        room.setId(1L);
        room.setName(roomDto.getName());
        room.setCapacity(roomDto.getCapacity());
        room.setDescription(roomDto.getDescription());

        when(roomRepository.save(any(Room.class))).thenReturn(room);

        RoomDto createdRoom = roomService.createRoom(roomDto);

        assertNotNull(createdRoom);
        assertEquals(room.getId(), createdRoom.getId());
        assertEquals(roomDto.getName(), createdRoom.getName());
        assertEquals(roomDto.getCapacity(), createdRoom.getCapacity());
        assertEquals(roomDto.getDescription(), createdRoom.getDescription());
        verify(roomRepository, times(1)).save(any(Room.class));
    }

    @Test
    public void testGetAllRooms() {
        Room room1 = new Room();
        room1.setId(1L);
        room1.setName("Room 1");
        room1.setCapacity(10);
        room1.setDescription("Description 1");

        Room room2 = new Room();
        room2.setId(2L);
        room2.setName("Room 2");
        room2.setCapacity(15);
        room2.setDescription("Description 2");

        List<Room> rooms = Arrays.asList(room1, room2);

        when(roomRepository.findAll()).thenReturn(rooms);

        List<RoomDto> roomDtos = roomService.getAllRooms();

        assertEquals(2, roomDtos.size());
        assertNotNull(roomDtos.get(0));
        assertEquals(room1.getName(), roomDtos.get(0).getName());
        assertNotNull(roomDtos.get(1));
        assertEquals(room2.getName(), roomDtos.get(1).getName());

    }

    @Test
    public void testGetRoomById() {
        Room room = new Room();
        room.setId(1L);
        room.setName("Test Room");
        room.setCapacity(20);
        room.setDescription("A room for testing");

        when(roomRepository.findById(1L)).thenReturn(Optional.of(room));

        RoomDto roomDto = roomService.getRoomById(1L);

        assertNotNull(roomDto);
        assertEquals(room.getId(), roomDto.getId());
        assertEquals(room.getName(), roomDto.getName());
        assertEquals(room.getCapacity(), roomDto.getCapacity());
        assertEquals(room.getDescription(), roomDto.getDescription());
    }

    @Test
    public void testUpdateRoom() {
        Long roomId = 1L;

        RoomDto roomDto = new RoomDto();
        roomDto.setName("Updated Room");
        roomDto.setCapacity(25);
        roomDto.setDescription("An updated room");

        Room existingRoom = new Room();
        existingRoom.setId(1L);
        existingRoom.setName("Original Room");
        existingRoom.setCapacity(20);
        existingRoom.setDescription("Original description");

        Room updatedRoom = new Room();
        updatedRoom.setId(1L);
        updatedRoom.setName(roomDto.getName());
        updatedRoom.setCapacity(roomDto.getCapacity());
        updatedRoom.setDescription(roomDto.getDescription());

        when(roomRepository.findById(roomId)).thenReturn(Optional.of(existingRoom));
        when(roomRepository.save(any(Room.class))).thenReturn(updatedRoom);

        RoomDto resultDto = roomService.updateRoom(roomId, roomDto);

        assertNotNull(resultDto);
        assertEquals(roomId, resultDto.getId());
        assertEquals(updatedRoom.getName(), resultDto.getName());
        assertEquals(updatedRoom.getCapacity(), resultDto.getCapacity());
        assertEquals(updatedRoom.getDescription(), resultDto.getDescription());
        verify(roomRepository, times(1)).save(any(Room.class));
    }

    @Test
    public void testDeleteRoom() {
        doNothing().when(roomRepository).deleteById(1L);

        roomService.deleteRoom(1L);

        verify(roomRepository, times(1)).deleteById(1L);
    }
}