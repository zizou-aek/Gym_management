import com.gymmanager.dto.MembershipDto;
import com.gymmanager.model.Membership;
import com.gymmanager.model.User;
import com.gymmanager.repository.MembershipRepository;
import com.gymmanager.repository.UserRepository;
import com.gymmanager.service.MembershipService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MembershipServiceTest {

    @InjectMocks
    private MembershipService membershipService;

    @Mock
    private MembershipRepository membershipRepository;

    @Mock
    private UserRepository userRepository;

    @Test
    void testCreateMembership() {
        // a) Create a MembershipDto object with test data, including a userId.
        MembershipDto membershipDto = new MembershipDto();
        membershipDto.setUserId(1L);
        membershipDto.setType("monthly");
        membershipDto.setStartDate(LocalDate.now());
        membershipDto.setEndDate(LocalDate.now().plusMonths(1));
        membershipDto.setPaymentStatus("paid");
        // b) Create a User object with test data.
        User user = new User();
        user.setId(1L);
        // c) Create a Membership object with the same test data as the MembershipDto.
        Membership membership = new Membership();
        membership.setType(membershipDto.getType());
        membership.setStartDate(membershipDto.getStartDate());
        membership.setEndDate(membershipDto.getEndDate());
        membership.setPaymentStatus(membershipDto.getPaymentStatus());
        membership.setUser(user);
        // d) Mock the UserRepository.findById() method to return an Optional containing the User object.
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        // e) Mock the MembershipRepository.save() method to return the Membership object.
        when(membershipRepository.save(any(Membership.class))).thenReturn(membership);
        // f) Call the createMembership() method of the MembershipService.
        MembershipDto createdMembership = membershipService.createMembership(membershipDto);
        // g) Assert that the returned MembershipDto has the correct attributes.
        assertNotNull(createdMembership);
        assertEquals(membership.getType(), createdMembership.getType());
        assertEquals(membership.getStartDate(), createdMembership.getStartDate());
        assertEquals(membership.getEndDate(), createdMembership.getEndDate());
        assertEquals(membership.getPaymentStatus(), createdMembership.getPaymentStatus());
        assertEquals(membershipDto.getUserId(), createdMembership.getUserId());
        // h) Verify that the MembershipRepository.save() method was called once with the correct Membership object.
        verify(membershipRepository, times(1)).save(any(Membership.class));
    }

    @Test
    void testGetAllMemberships() {
        // a) Create a User object with test data.
        User user1 = new User();
        user1.setId(1L);
        // b) Create a list of Membership objects with test data, associating them with the User.
        Membership membership1 = new Membership();
        membership1.setType("monthly");
        membership1.setStartDate(LocalDate.now());
        membership1.setEndDate(LocalDate.now().plusMonths(1));
        membership1.setPaymentStatus("paid");
        membership1.setUser(user1);
        // b) Create a list of Membership objects with test data, associating them with the User.
        User user2 = new User();
        user2.setId(2L);
        // b) Create a list of Membership objects with test data, associating them with the User.
        Membership membership2 = new Membership();
        membership2.setType("yearly");
        membership2.setStartDate(LocalDate.now());
        membership2.setEndDate(LocalDate.now().plusYears(1));
        membership2.setPaymentStatus("unpaid");
        membership2.setUser(user2);
        // b) Create a list of Membership objects with test data, associating them with the User.
        List<Membership> memberships = Arrays.asList(membership1, membership2);
        // c) Mock the MembershipRepository.findAll() method to return the list of Membership objects.
        when(membershipRepository.findAll()).thenReturn(memberships);
        // d) Call the getAllMemberships() method of the MembershipService.
        List<MembershipDto> membershipDtos = membershipService.getAllMemberships();
        // e) Assert that the returned list of MembershipDtos has the correct size and attributes.
        assertEquals(2, membershipDtos.size());
        assertEquals(membership1.getType(), membershipDtos.get(0).getType());
        assertEquals(membership1.getStartDate(), membershipDtos.get(0).getStartDate());
        assertEquals(membership1.getEndDate(), membershipDtos.get(0).getEndDate());
        assertEquals(membership1.getPaymentStatus(), membershipDtos.get(0).getPaymentStatus());
        assertEquals(user1.getId(), membershipDtos.get(0).getUserId());
        assertEquals(membership2.getType(), membershipDtos.get(1).getType());
        assertEquals(membership2.getStartDate(), membershipDtos.get(1).getStartDate());
        assertEquals(membership2.getEndDate(), membershipDtos.get(1).getEndDate());
        assertEquals(membership2.getPaymentStatus(), membershipDtos.get(1).getPaymentStatus());
        assertEquals(user2.getId(), membershipDtos.get(1).getUserId());

    }

    @Test
    void testGetMembershipById() {
        // a) Create a User object with test data.
        User user = new User();
        user.setId(1L);
        // b) Create a Membership object with test data, associated with the User.
        Membership membership = new Membership();
        membership.setType("monthly");
        membership.setStartDate(LocalDate.now());
        membership.setEndDate(LocalDate.now().plusMonths(1));
        membership.setPaymentStatus("paid");
        membership.setUser(user);
        // c) Mock the MembershipRepository.findById() method to return an Optional containing the Membership object.
        when(membershipRepository.findById(1L)).thenReturn(Optional.of(membership));
        // d) Call the getMembershipById() method of the MembershipService.
        MembershipDto membershipDto = membershipService.getMembershipById(1L);
        // e) Assert that the returned MembershipDto has the correct attributes.
        assertNotNull(membershipDto);
        assertEquals(membership.getType(), membershipDto.getType());
        assertEquals(membership.getStartDate(), membershipDto.getStartDate());
        assertEquals(membership.getEndDate(), membershipDto.getEndDate());
        assertEquals(membership.getPaymentStatus(), membershipDto.getPaymentStatus());
        assertEquals(user.getId(), membershipDto.getUserId());

        when(membershipRepository.findById(2L)).thenReturn(Optional.empty());
        assertThrows(NoSuchElementException.class, () -> membershipService.getMembershipById(2L));
    }

    @Test
    void testUpdateMembership() {
        // a) Create a User object with initial test data.
        User initialUser = new User();
        initialUser.setId(1L);
        // b) Create a Membership object with initial test data, associated with the User.
        Membership initialMembership = new Membership();
        initialMembership.setId(1L);
        initialMembership.setType("monthly");
        initialMembership.setStartDate(LocalDate.now());
        initialMembership.setEndDate(LocalDate.now().plusMonths(1));
        initialMembership.setPaymentStatus("paid");
        initialMembership.setUser(initialUser);
        // c) Create a MembershipDto object with updated test data, including a userId.
        MembershipDto membershipDto = new MembershipDto();
        membershipDto.setId(1L);
        membershipDto.setUserId(1L);
        membershipDto.setType("yearly");
        membershipDto.setStartDate(LocalDate.now());
        membershipDto.setEndDate(LocalDate.now().plusYears(1));
        membershipDto.setPaymentStatus("unpaid");
        // d) Create a User object with the updated userId.
        User user = new User();
        user.setId(1L);
        // e) Create a Membership object with the updated test data, associated with the new User.
        Membership membership = new Membership();
        membership.setId(1L);
        membership.setType(membershipDto.getType());
        membership.setStartDate(membershipDto.getStartDate());
        membership.setEndDate(membershipDto.getEndDate());
        membership.setPaymentStatus(membershipDto.getPaymentStatus());
        membership.setUser(user);
        // f) Mock the MembershipRepository.findById() method to return an Optional containing the initial Membership object.
        when(membershipRepository.findById(1L)).thenReturn(Optional.of(initialMembership));
        // g) Mock the UserRepository.findById() method to return an Optional containing the new User object.
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        // h) Mock the MembershipRepository.save() method to return the updated Membership object.
        when(membershipRepository.save(any(Membership.class))).thenReturn(membership);
        // i) Call the updateMembership() method of the MembershipService.
        MembershipDto updatedMembership = membershipService.updateMembership(1L, membershipDto);
        // j) Assert that the returned MembershipDto has the correct updated attributes.
        assertNotNull(updatedMembership);
        assertEquals(membershipDto.getType(), updatedMembership.getType());
        assertEquals(membershipDto.getStartDate(), updatedMembership.getStartDate());
        assertEquals(membershipDto.getEndDate(), updatedMembership.getEndDate());
        assertEquals(membershipDto.getPaymentStatus(), updatedMembership.getPaymentStatus());
        assertEquals(user.getId(), updatedMembership.getUserId());
        // k) Verify that the MembershipRepository.save() method was called once with the correct Membership object.
        verify(membershipRepository, times(1)).save(any(Membership.class));

    }

    @Test
    void testDeleteMembership() {
        // Given
        Long membershipId = 1L;

        // When
        doNothing().when(membershipRepository).deleteById(membershipId);
        membershipService.deleteMembership(membershipId);

        // Then
        verify(membershipRepository).deleteById(membershipId);
    }
}