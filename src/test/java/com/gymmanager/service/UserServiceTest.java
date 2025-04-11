package com.gymmanager.service;

import com.gymmanager.dto.UserDto;
import com.gymmanager.model.Room;
import com.gymmanager.model.User;
import com.gymmanager.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Test
    void testRegisterUser() {
        //given
        UserDto userDto = new UserDto();
        userDto.setUsername("testuser");
        userDto.setPassword("password");
        userDto.setFirstName("Test");
        userDto.setLastName("User");
        userDto.setEmail("test@example.com");
        userDto.setRole("MEMBER");


        User user = new User();
        user.setId(1L);
        user.setUsername(userDto.getUsername());
        user.setPassword("encodedPassword"); // Mocked encoding
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setEmail(userDto.getEmail());
        user.setRole(userDto.getRole());

        //when
        when(bCryptPasswordEncoder.encode(userDto.getPassword())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);

        UserDto registeredUser = userService.registerUser(userDto);

        //then
        assertNotNull(registeredUser);
        assertEquals(user.getUsername(), registeredUser.getUsername());
        assertEquals(user.getFirstName(), registeredUser.getFirstName());
        assertEquals(user.getLastName(), registeredUser.getLastName());
        assertEquals(user.getEmail(), registeredUser.getEmail());
        assertEquals(user.getRole(), registeredUser.getRole());
        verify(bCryptPasswordEncoder).encode(userDto.getPassword());
        verify(userRepository).save(user);
        verify(userRepository).save(any(User.class));
    }

    @Test
    void testGetUserDetails() {
        User user = new User();
        user.setId(1L);
        user.setUsername("testuser");
        user.setPassword("encodedPassword");
        user.setFirstName("Test");
        user.setLastName("User");
        user.setEmail("test@example.com");
        user.setRole("MEMBER");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        UserDto userDetails = userService.getUserDetails(1L);

        assertNotNull(userDetails);
        assertEquals(user.getUsername(), userDetails.getUsername());
        assertEquals(user.getFirstName(), userDetails.getFirstName());
        assertEquals(user.getLastName(), userDetails.getLastName());
        assertEquals(user.getEmail(), userDetails.getEmail());
        assertEquals(user.getRole(), userDetails.getRole());
        verify(userRepository).findById(1L);
    }

    @Test
    void testUpdateUserDetails() {
        UserDto userDto = new UserDto();
        userDto.setUsername("testuser");
        userDto.setFirstName("Updated");
        userDto.setLastName("User");
        userDto.setEmail("updated@example.com");

        User user = new User();
        user.setId(1L);
        user.setUsername("testuser");
        user.setPassword("encodedPassword");
        user.setFirstName("Test");
        user.setLastName("User");
        user.setEmail("test@example.com");
        user.setRole("MEMBER");

        User updatedUser = new User();
        updatedUser.setFirstName(userDto.getFirstName());
        updatedUser.setLastName(userDto.getLastName());
        updatedUser.setEmail(userDto.getEmail());

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(updatedUser);

        UserDto updatedUser = userService.updateUserDetails(1L, userDto);
        assertEquals(userDto.getLastName(), updatedUser.getLastName());
        assertEquals(userDto.getEmail(), updatedUser.getEmail());
        verify(userRepository).findById(1L);
        verify(userRepository).save(any(User.class));
    }

    @Test
    void testUpdateUserRole() {
        User user = new User();
        user.setId(1L);
        user.setUsername("testuser");
        user.setPassword("encodedPassword");
        user.setFirstName("Test");
        user.setLastName("User");
        user.setEmail("test@example.com");
        user.setRole("MEMBER");

        User updatedUser = new User();
        updatedUser.setRole("ADMIN");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(updatedUser);

        UserDto updatedUser = userService.updateUserRole(1L, "ADMIN");
        assertEquals("ADMIN", updatedUser.getRole());
        verify(userRepository).findById(1L);
        verify(userRepository).save(any(User.class));
    }
}