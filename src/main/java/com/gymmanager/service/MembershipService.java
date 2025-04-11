package com.gymmanager.service;

import com.gymmanager.dto.MembershipDto;
import com.gymmanager.model.User;
import com.gymmanager.model.Membership;
import com.gymmanager.repository.UserRepository;
import com.gymmanager.repository.MembershipRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MembershipService {

    @Autowired
    private MembershipRepository membershipRepository;
    @Autowired
    private UserRepository userRepository;

    public MembershipDto createMembership(MembershipDto membershipDto) {
        Membership membership = new Membership();
        membership.setType(membershipDto.getType());
        membership.setStartDate(membershipDto.getStartDate());
        membership.setEndDate(membershipDto.getEndDate());
        membership.setPaymentStatus(membershipDto.getPaymentStatus());
        User user = userRepository.findById(membershipDto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        membership.setUser(user);
        Membership savedMembership = membershipRepository.save(membership);
        return convertToDto(savedMembership);
    }

    public MembershipDto getMembershipById(Long id) {
        Membership membership = membershipRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Membership not found"));
        return convertToDto(membership);
    }

    public List<MembershipDto> getAllMemberships() {
        List<Membership> memberships = membershipRepository.findAll();
        List<MembershipDto> membershipDtos = new ArrayList<>();
        for (Membership membership : memberships) {
            membershipDtos.add(convertToDto(membership));
        }
        return membershipDtos;
    }

    public MembershipDto updateMembership(Long id, MembershipDto membershipDto) {
        Membership membership = membershipRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Membership not found"));
        membership.setType(membershipDto.getType());
        membership.setStartDate(membershipDto.getStartDate());
        membership.setEndDate(membershipDto.getEndDate());
        membership.setPaymentStatus(membershipDto.getPaymentStatus());
        User user = userRepository.findById(membershipDto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        membership.setUser(user);
        Membership updatedMembership = membershipRepository.save(membership);
        return convertToDto(updatedMembership);
    }
    public void deleteMembership(Long id) {
        membershipRepository.deleteById(id);
    }
    private MembershipDto convertToDto(Membership membership) {
        MembershipDto membershipDto = new MembershipDto();
        membershipDto.setUserId(membership.getUser().getId());
        membershipDto.setType(membership.getType());
        membershipDto.setStartDate(membership.getStartDate());
        membershipDto.setEndDate(membership.getEndDate());
        membershipDto.setPaymentStatus(membership.getPaymentStatus());
        return membershipDto;
    }
    }

    public void deleteMembership(Long id) {
        membershipRepository.deleteById(id);
    }

    public Optional<Membership> getMembershipByUserId(Long userId) {
        return membershipRepository.findByUserId(userId);
    }

    // Additional methods for tracking expiration dates, payment status, and managing member statuses can be added here.
}