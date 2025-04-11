package com.gymmanager.service;

import com.gymmanager.dto.RoomDto;
import com.gymmanager.model.Room;
import com.gymmanager.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RoomService {

    @Autowired
    private RoomRepository roomRepository;
    
    public RoomDto createRoom(RoomDto roomDto) {
        Room room = new Room();
        room.setName(roomDto.getName());
        room.setCapacity(roomDto.getCapacity());
        room.setDescription(roomDto.getDescription());
        Room savedRoom = roomRepository.save(room);
        return convertToDto(savedRoom);
    }

    public List<RoomDto> getAllRooms() {
        return roomRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public RoomDto getRoomById(Long id) {
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Room not found with id: " + id));
        return convertToDto(room);
    }

    public RoomDto updateRoom(Long id, RoomDto roomDto) {
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Room not found with id: " + id));
        room.setName(roomDto.getName());
        room.setCapacity(roomDto.getCapacity());
        room.setDescription(roomDto.getDescription());
        Room updatedRoom = roomRepository.save(room);
        return convertToDto(updatedRoom);
    }

    public void deleteRoom(Long id) {
        roomRepository.deleteById(id);
    }
    private RoomDto convertToDto(Room room) {
        RoomDto roomDto = new RoomDto();
        roomDto.setName(room.getName());
        roomDto.setCapacity(room.getCapacity());
        roomDto.setDescription(room.getDescription());
        return roomDto;
    }
}