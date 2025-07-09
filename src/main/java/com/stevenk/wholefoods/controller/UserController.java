package com.stevenk.wholefoods.controller;

import com.stevenk.wholefoods.dto.UserDTO;
import com.stevenk.wholefoods.exceptions.AlreadyExistsException;
import com.stevenk.wholefoods.exceptions.ResourceNotFoundException;
import com.stevenk.wholefoods.model.User;
import com.stevenk.wholefoods.requests.CreateUserRequest;
import com.stevenk.wholefoods.requests.UserUpdateRequest;
import com.stevenk.wholefoods.response.ApiResponse;
import com.stevenk.wholefoods.service.user.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.*;
@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/users")
public class UserController {
    private final IUserService userService;

    @GetMapping("/getById/{id}")
    public ResponseEntity<ApiResponse> getUserById(@PathVariable Long id) {
        try {
            User user = userService.getUserById(id);
            UserDTO userDTO = userService.convertToDTO(user);
            return ResponseEntity.ok(new ApiResponse("Found.", userDTO));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @PostMapping("/add")
    public ResponseEntity<ApiResponse> createUser(@RequestBody CreateUserRequest rq) {
        try {
            User user = userService.createUser(rq);
            UserDTO userDTO = userService.convertToDTO(user);
            return ResponseEntity.ok(new ApiResponse("User created successfully", userDTO));
        } catch (AlreadyExistsException e) {
            return ResponseEntity.status(CONFLICT).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ApiResponse> updateUser(@PathVariable Long id, @RequestBody UserUpdateRequest rq) {
        try {
            User user = userService.updateUser(rq, id);
            UserDTO userDTO = userService.convertToDTO(user);
            return ResponseEntity.ok(new ApiResponse("User updated successfully", userDTO));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse> deleteUser(@PathVariable Long id) {
        try {
            userService.deleteUser(id);
            return ResponseEntity.ok(new ApiResponse("User deleted successfully", null));
        }
        catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

}
