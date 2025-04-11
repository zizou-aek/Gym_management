package com.gymmanager.controller;

import com.gymmanager.dto.MembershipDto;
import com.gymmanager.service.MembershipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/memberships")
public class MembershipController {

  @Autowired private MembershipService membershipService;

  @PostMapping("/")
  @PreAuthorize("isAuthenticated()")
  public ResponseEntity<MembershipDto> createMembership(
      @RequestBody MembershipDto membershipDto) {
    MembershipDto createdMembership = membershipService.createMembership(membershipDto);
    return new ResponseEntity<>(createdMembership, HttpStatus.CREATED);
  }

  @GetMapping("/")
  @PreAuthorize("isAuthenticated()")
  public ResponseEntity<List<MembershipDto>> getAllMemberships() {
    List<MembershipDto> memberships = membershipService.getAllMemberships();
    return new ResponseEntity<>(memberships, HttpStatus.OK);
  }

  @GetMapping("/{id}")
  @PreAuthorize("isAuthenticated()")
  public ResponseEntity<MembershipDto> getMembershipById(@PathVariable Long id) {
    try {
      MembershipDto membership = membershipService.getMembershipById(id);
      return new ResponseEntity<>(membership, HttpStatus.OK);
    } catch (RuntimeException e) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }

  @PutMapping("/{id}")
  @PreAuthorize("isAuthenticated()")
  public ResponseEntity<MembershipDto> updateMembership(
      @PathVariable Long id, @RequestBody MembershipDto membershipDto) {
    try {
      MembershipDto updatedMembership = membershipService.updateMembership(id, membershipDto);
      return new ResponseEntity<>(updatedMembership, HttpStatus.OK);
    } catch (RuntimeException e) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }

  @DeleteMapping("/{id}")
  @PreAuthorize("isAuthenticated()")
  public ResponseEntity<Void> deleteMembership(@PathVariable Long id) {
    membershipService.deleteMembership(id);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }
}