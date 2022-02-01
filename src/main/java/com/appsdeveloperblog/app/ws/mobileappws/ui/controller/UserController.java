package com.appsdeveloperblog.app.ws.mobileappws.ui.controller;

import com.appsdeveloperblog.app.ws.mobileappws.service.AddressService;
import com.appsdeveloperblog.app.ws.mobileappws.service.UserService;
import com.appsdeveloperblog.app.ws.mobileappws.shared.dto.AddressDTO;
import com.appsdeveloperblog.app.ws.mobileappws.shared.dto.UserDto;
import com.appsdeveloperblog.app.ws.mobileappws.ui.model.request.UserDetailsRequestModel;
import com.appsdeveloperblog.app.ws.mobileappws.ui.model.response.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
public class UserController implements UserApi {

    @Autowired
    UserService userService;

    @Autowired
    AddressService addressService;

    @Override
    public List<UserRest> getAllUsers() {
        List<UserRest> returnValue = new ArrayList<>();

        List<UserDto> users = userService.getUsers();

        for (UserDto userDto : users) {
            UserRest userModel = new UserRest();
            BeanUtils.copyProperties(userDto, userModel);
            returnValue.add(userModel);
        }

        return returnValue;
    }

    @Override
    public List<UserRest> getUsers(int page, int limit) {
        List<UserRest> returnValue = new ArrayList<>();

        List<UserDto> users = userService.getUsers(page, limit);

        for (UserDto userDto : users) {
            UserRest userModel = new UserRest();
            BeanUtils.copyProperties(userDto, userModel);
            returnValue.add(userModel);
        }

        return returnValue;
    }

    @Override
    public UserRest getUser(String id) {
        UserRest returnValue = new UserRest();

        UserDto userDto = userService.getUserByUserId(id);
        BeanUtils.copyProperties(userDto, returnValue);

        return returnValue;
    }

    @Override
    public UserRest createUser(UserDetailsRequestModel userDetails) {
        UserRest returnValue = new UserRest();

        //Note: BeanUtils is used for shallow copy, it is not best for objects within objects.
        /*UserDto userDto = new UserDto();
        BeanUtils.copyProperties(userDetails, userDto);*/
        ModelMapper modelMapper = new ModelMapper();
        UserDto userDto = modelMapper.map(userDetails, UserDto.class);

        UserDto createdUser = userService.createUser(userDto);
        //BeanUtils.copyProperties(createdUser, returnValue);
        returnValue = modelMapper.map(createdUser, UserRest.class);

        return returnValue;
    }

    @Override
    public UserRest updateUser(String id, UserDetailsRequestModel userDetails) {
        UserRest returnValue = new UserRest();

        UserDto userDto = new UserDto();
        BeanUtils.copyProperties(userDetails, userDto);

        UserDto updateUser = userService.updateUser(id, userDto);
        BeanUtils.copyProperties(updateUser, returnValue);

        return returnValue;
    }

    @Override
    public UserRest updateUserPartially(String id, JsonPatch patch) throws JsonPatchException, JsonProcessingException {
        UserRest returnValue = new UserRest();

        UserDto updateUser = userService.updateUserPartially(id, patch);
        BeanUtils.copyProperties(updateUser, returnValue);

        return returnValue;
    }

    @Override
    public OperationStatusModel deleteUser(String id) {
        OperationStatusModel returnValue = new OperationStatusModel();
        returnValue.setOperationName(RequestOperationName.DELETE.name());

        userService.deleteUser(id);

        returnValue.setOperationResult(RequestOperationStatus.SUCCESS.name());
        return returnValue;
    }

    @Override
    public CollectionModel<AddressRest> getUserAddresses(String id) {
        List<AddressRest> returnValue = new ArrayList<>();

        List<AddressDTO> addresses = addressService.getAddressesByUserId(id);
        //BeanUtils.copyProperties(userDto, returnValue);
        if(addresses!=null&&!addresses.isEmpty()){
            Type listType = new TypeToken<List<AddressRest>>() {
            }.getType();
            returnValue = new ModelMapper().map(addresses, listType);

            for (AddressRest addressRest: returnValue){
                Link selfLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UserController.class).getUserAddress(id,addressRest.getAddressId())).withSelfRel();
                addressRest.add(selfLink);
            }
        }

        Link userLink = WebMvcLinkBuilder.linkTo(UserController.class).slash(id).withRel("user");
        Link selfLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UserController.class).getUserAddresses(id)).withSelfRel();

        return CollectionModel.of(returnValue, userLink, selfLink);
    }

    @Override
    public EntityModel<AddressRest> getUserAddress(String userId,String addressId) {
        /*AddressDTO addressDTO = addressService.getAddressByAddressId(addressId);

        ModelMapper modelMapper=new ModelMapper();

        return modelMapper.map(addressDTO, AddressRest.class);*/
        AddressDTO addressesDto = addressService.getAddressByAddressId(addressId);

        ModelMapper modelMapper = new ModelMapper();
        AddressRest returnValue = modelMapper.map(addressesDto, AddressRest.class);

        //http://localhost:8080/users/<usersId>

        Link userLink = WebMvcLinkBuilder.linkTo(UserController.class).slash(userId).withRel("user");
        Link userAddressesLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UserController.class).getUserAddresses(userId)).withRel("addresses");
        Link selfLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UserController.class).getUserAddress(userId, addressId)).withSelfRel();


        /*returnValue.add(userLink);
        returnValue.add(userAddressesLink);
        returnValue.add(selfLink);*/

        return EntityModel.of(returnValue, Arrays.asList(userLink,userAddressesLink,selfLink));
    }
}
