package com.gymmanager.repository;

import com.gymmanager.model.Membership;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MembershipRepository extends JpaRepository<Membership, Long> {

    Membership findByUserId(Long userId);
}