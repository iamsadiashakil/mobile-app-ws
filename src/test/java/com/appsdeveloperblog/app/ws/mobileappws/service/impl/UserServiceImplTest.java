package com.appsdeveloperblog.app.ws.mobileappws.service.impl;

import com.appsdeveloperblog.app.ws.mobileappws.entity.AddressEntity;
import com.appsdeveloperblog.app.ws.mobileappws.entity.UserEntity;
import com.appsdeveloperblog.app.ws.mobileappws.exception.RecordNotFoundException;
import com.appsdeveloperblog.app.ws.mobileappws.repo.UserRepository;
import com.appsdeveloperblog.app.ws.mobileappws.shared.dto.AddressDTO;
import com.appsdeveloperblog.app.ws.mobileappws.shared.dto.UserDto;
import com.appsdeveloperblog.app.ws.mobileappws.shared.utils.RandomIdGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

//service class unit testing
@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {
    private final String USER_ID = "hhtufyn";
    private final String ENCRYPTED_PASSWORD = "74hcgt67jf";
    @Mock
    UserRepository userRepository;

    @Mock
    RandomIdGenerator randomIdGenerator;

    @Mock
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @InjectMocks
    UserServiceImpl userService;

    @Test
    void testGetUser() {
        //Arrange
        UserEntity entity = createUserEntity();
        when(userRepository.findByEmail(anyString())).thenReturn(entity);

        //Action
        UserDto found = userService.getUser("test@test.com");

        //Assert
        assertNotNull(found);
        assertEquals("Sadia", found.getFirstName());
        verify(userRepository).findByEmail(anyString());
    }

    @Test
    void testGetUser_throwNotFoundException() {
        //Arrange
        when(userRepository.findByEmail(anyString())).thenReturn(null);

        //Assert
        assertThrows(RecordNotFoundException.class,
                () -> {
                    userService.getUser("test@test.com");
                });
    }

    @Test
    void testCreateUser() {
        //Arrange
        UserDto user = createUserDto();
        UserEntity entity = createUserEntity();
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(randomIdGenerator.generateAddressId(anyInt())).thenReturn("gsabdasjkadjkhn4565");
        when(randomIdGenerator.generateUserId(anyInt())).thenReturn(USER_ID);
        when(bCryptPasswordEncoder.encode(anyString())).thenReturn(ENCRYPTED_PASSWORD);
        when(userRepository.save(any(UserEntity.class))).thenReturn(entity);

        //Action
        UserDto storedUserDetails = userService.createUser(user);

        //Assert
        assertNotNull(storedUserDetails);
        assertEquals(entity.getFirstName(), storedUserDetails.getFirstName());
        verify(userRepository, times(1)).save(any(UserEntity.class));
        verify(bCryptPasswordEncoder, times(1)).encode(anyString());
        verify(randomIdGenerator, times(storedUserDetails.getAddresses().size())).generateUserId(anyInt());
    }

    private UserEntity createUserEntity() {
        UserEntity entity = new UserEntity();

        AddressEntity addressDTO = new AddressEntity();
        addressDTO.setType("shipping");

        List<AddressEntity> addresses = new ArrayList<>();
        addresses.add(addressDTO);

        entity.setId(1L);
        entity.setFirstName("Sadia");
        entity.setUserId(USER_ID);
        entity.setEncryptedPassword(ENCRYPTED_PASSWORD);
        entity.setEmail("test@test.com");
        entity.setAddresses(addresses);
        /*entity.setEmailVerificationToken("45fe535");*/

        return entity;
    }

    private UserDto createUserDto() {
        UserDto userDto = new UserDto();
        AddressDTO addressDTO = new AddressDTO();
        addressDTO.setType("shipping");

        List<AddressDTO> addresses = new ArrayList<>();
        addresses.add(addressDTO);

        userDto.setId(1L);
        userDto.setFirstName("Sadia");
        userDto.setUserId(USER_ID);
        userDto.setPassword("paw123");
        userDto.setEncryptedPassword(ENCRYPTED_PASSWORD);
        userDto.setEmail("test@test.com");
        userDto.setAddresses(addresses);

        return userDto;
    }
}
