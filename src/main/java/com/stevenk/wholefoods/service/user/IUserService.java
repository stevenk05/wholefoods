package com.stevenk.wholefoods.service.user;

import com.stevenk.wholefoods.dto.UserDTO;
import com.stevenk.wholefoods.model.User;
import com.stevenk.wholefoods.requests.CreateUserRequest;
import com.stevenk.wholefoods.requests.UserUpdateRequest;

public interface IUserService {
    User getUserById(Long id);
    User createUser(CreateUserRequest request);
    User updateUser(UserUpdateRequest request, Long userId);
    void deleteUser(Long userId);

    UserDTO convertToDTO(User user);

    User getAuthenticatedUser();
}
