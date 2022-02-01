package com.appsdeveloperblog.app.ws.mobileappws.service.impl;

import com.appsdeveloperblog.app.ws.mobileappws.entity.UserEntity;
import com.appsdeveloperblog.app.ws.mobileappws.repo.UserRepository;
import com.appsdeveloperblog.app.ws.mobileappws.service.BusinessService;
import com.appsdeveloperblog.app.ws.mobileappws.service.UserService;
import com.appsdeveloperblog.app.ws.mobileappws.shared.dto.AddressDTO;
import com.appsdeveloperblog.app.ws.mobileappws.shared.dto.UserDto;
import com.appsdeveloperblog.app.ws.mobileappws.shared.utils.RandomIdGenerator;
import com.appsdeveloperblog.app.ws.mobileappws.shared.utils.ValueUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl extends BusinessService implements UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    RandomIdGenerator randomIdGenerator;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public UserDto createUser(UserDto user) {
        validateRequest(user, "invalid input object");

        if (userRepository.existsByEmail(user.getEmail())) throwUserServiceException("user already exists");

        for (int i = 0; i < user.getAddresses().size(); i++) {
            AddressDTO address = user.getAddresses().get(i);
            address.setUserDetails(user);
            address.setAddressId(randomIdGenerator.generateAddressId(30));
            user.getAddresses().set(i, address);
        }

        //UserEntity userEntity = new UserEntity();
        //BeanUtils.copyProperties(user, userEntity);
        ModelMapper modelMapper = new ModelMapper();
        UserEntity userEntity = modelMapper.map(user, UserEntity.class);

        userEntity.setEncryptedPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        userEntity.setUserId(randomIdGenerator.generateUserId(30));

        UserEntity storedUserDetails = userRepository.save(userEntity);

        //UserDto returnValue = new UserDto();
        //BeanUtils.copyProperties(storedUserDetails, returnValue);
        UserDto returnValue = modelMapper.map(storedUserDetails, UserDto.class);

        return returnValue;
    }

    @Override
    public UserDto getUser(String email) {
        validateString(email, "invalid input email");

        UserEntity userEntity = userRepository.findByEmail(email);

        if (ValueUtils.isEmpty(userEntity)) throwNotFound("No user found for email: " + email);

        UserDto returnValue = new UserDto();
        BeanUtils.copyProperties(userEntity, returnValue);
        return returnValue;
    }

    @Override
    public UserDto getUserByUserId(String userId) {
        validateString(userId, "invalid user id");

        UserDto returnValue = new UserDto();
        UserEntity userEntity = userRepository.findByUserId(userId);

        if (ValueUtils.isEmpty(userEntity)) throwNotFound("No user found for user id: " + userId);

        BeanUtils.copyProperties(userEntity, returnValue);

        return returnValue;
    }

    @Override
    public UserDto updateUser(String userId, UserDto userDto) {
        validateString(userId, "invalid user id");

        UserDto returnValue = new UserDto();
        UserEntity userEntity = userRepository.findByUserId(userId);

        if (ValueUtils.isEmpty(userEntity)) throwNotFound("No user found for user id: " + userId);

        userEntity.setFirstName(userDto.getFirstName());
        userEntity.setLastName(userDto.getLastName());

        UserEntity updatedUserDetails = userRepository.save(userEntity);

        BeanUtils.copyProperties(updatedUserDetails, returnValue);

        return returnValue;
    }

    @Override
    public void deleteUser(String userId) {
        UserEntity userEntity = userRepository.findByUserId(userId);

        if (ValueUtils.isEmpty(userEntity)) throwNotFound("No user found for user id: " + userId);

        userRepository.delete(userEntity);
    }

    @Override
    public List<UserDto> getUsers(int page, int limit) {
        List<UserDto> returnValue = new ArrayList<>();

        Pageable pageable = PageRequest.of(page, limit);

        Page<UserEntity> usersPage = userRepository.findAll(pageable);
        List<UserEntity> users = usersPage.getContent();

        for (UserEntity userEntity : users) {
            UserDto userDto = new UserDto();
            BeanUtils.copyProperties(userEntity, userDto);
            returnValue.add(userDto);
        }

        return returnValue;
    }

    @Override
    public List<UserDto> getUsers() {
        List<UserDto> returnValue = new ArrayList<>();

        Iterable<UserEntity> userEntities = userRepository.findAll();

        for (UserEntity userEntity : userEntities) {
            UserDto userDto = new UserDto();
            BeanUtils.copyProperties(userEntity, userDto);
            returnValue.add(userDto);
        }

        return returnValue;
    }

    @Override
    public UserDto updateUserPartially(String userId, JsonPatch patch) throws JsonPatchException, JsonProcessingException {
        validateString(userId, "invalid user id");

        UserDto returnValue = new UserDto();
        UserEntity userEntity = userRepository.findByUserId(userId);

        if (ValueUtils.isEmpty(userEntity)) throwNotFound("No user found for user id: " + userId);

        userEntity = applyPatchToUserEntity(patch, userEntity);

        UserEntity updatedUserDetails = userRepository.save(userEntity);

        BeanUtils.copyProperties(updatedUserDetails, returnValue);

        return returnValue;
    }

    private UserEntity applyPatchToUserEntity(JsonPatch patch, UserEntity userEntity) throws JsonPatchException, JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode patched = patch.apply(objectMapper.convertValue(userEntity, JsonNode.class));
        return objectMapper.treeToValue(patched, UserEntity.class);
    }

    @Override
    public UserDetails loadUserByUsername(String email) {
        validateString(email, "invalid input email");

        UserEntity userEntity = userRepository.findByEmail(email);
        if (ValueUtils.isEmpty(userEntity)) throwNotFound("No user found for email: " + email);

        return new User(userEntity.getEmail(), userEntity.getEncryptedPassword(), new ArrayList<>());
    }
}
