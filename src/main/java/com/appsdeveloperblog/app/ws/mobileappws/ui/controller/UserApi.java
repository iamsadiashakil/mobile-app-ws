package com.appsdeveloperblog.app.ws.mobileappws.ui.controller;

import com.appsdeveloperblog.app.ws.mobileappws.ui.model.request.UserDetailsRequestModel;
import com.appsdeveloperblog.app.ws.mobileappws.ui.model.response.AddressRest;
import com.appsdeveloperblog.app.ws.mobileappws.ui.model.response.OperationStatusModel;
import com.appsdeveloperblog.app.ws.mobileappws.ui.model.response.UserRest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/users")// http://localhost:8080/userss
public interface UserApi {

    @GetMapping(produces = {MediaType.APPLICATION_XML_VALUE,
            MediaType.APPLICATION_JSON_VALUE})
    public List<UserRest> getAllUsers();

    @GetMapping(path = "/limit", produces = {MediaType.APPLICATION_XML_VALUE,
            MediaType.APPLICATION_JSON_VALUE})
    public List<UserRest> getUsers(@RequestParam(value = "page", defaultValue = "0") int page,
                                   @RequestParam(value = "limit", defaultValue = "25") int limit);

    @GetMapping(path = "/{id}", produces = {MediaType.APPLICATION_XML_VALUE,
            MediaType.APPLICATION_JSON_VALUE})
    public UserRest getUser(@PathVariable String id);

    @PostMapping(
            consumes = {MediaType.APPLICATION_XML_VALUE,
                    MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_XML_VALUE,
                    MediaType.APPLICATION_JSON_VALUE})
    public UserRest createUser(@RequestBody UserDetailsRequestModel userDetails);

    @PutMapping(path = "/{id}",
            consumes = {MediaType.APPLICATION_XML_VALUE,
                    MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_XML_VALUE,
                    MediaType.APPLICATION_JSON_VALUE})
    public UserRest updateUser(@PathVariable String id, @RequestBody UserDetailsRequestModel userDetails);

    @PatchMapping(path = "/{id}",
            consumes = {MediaType.APPLICATION_XML_VALUE,
                    MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_XML_VALUE,
                    MediaType.APPLICATION_JSON_VALUE})
    public UserRest updateUserPartially(@PathVariable String id, @RequestBody JsonPatch patch) throws JsonPatchException, JsonProcessingException;

    @DeleteMapping(path = "/{id}", produces = {MediaType.APPLICATION_XML_VALUE,
            MediaType.APPLICATION_JSON_VALUE})
    public OperationStatusModel deleteUser(@PathVariable String id);

    @GetMapping(path = "/{id}/addresses", produces = {MediaType.APPLICATION_XML_VALUE,
            MediaType.APPLICATION_JSON_VALUE})
    public CollectionModel<AddressRest> getUserAddresses(@PathVariable String id);

    @GetMapping(path = "/{userId}/addresses/{addressId}", produces = {MediaType.APPLICATION_XML_VALUE,
            MediaType.APPLICATION_JSON_VALUE})
    public EntityModel<AddressRest> getUserAddress(@PathVariable String userId, @PathVariable String addressId);

}
