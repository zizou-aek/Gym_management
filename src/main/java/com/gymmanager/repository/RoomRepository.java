package com.gymmanager.repository;

import com.gymmanager.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomRepository extends JpaRepository<Room, Long> {
    // You can add custom query methods here if needed
}