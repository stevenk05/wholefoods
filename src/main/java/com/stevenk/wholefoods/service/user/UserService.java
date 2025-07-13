package com.stevenk.wholefoods.service.user;

import com.stevenk.wholefoods.dto.UserDTO;
import com.stevenk.wholefoods.exceptions.AlreadyExistsException;
import com.stevenk.wholefoods.exceptions.ResourceNotFoundException;
import com.stevenk.wholefoods.model.User;
import com.stevenk.wholefoods.repository.UserRepository;
import com.stevenk.wholefoods.requests.CreateUserRequest;
import com.stevenk.wholefoods.requests.UserUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService{

    private final UserRepository userRepo;
    private final ModelMapper mapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public User getUserById(Long id) {
        return userRepo.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("User Not Found"));
    }

    @Override
    public User createUser(CreateUserRequest request) {
        return Optional.of(request).filter(user -> !userRepo.existsByEmail(request.getEmail()))
                .map(user -> {
                    User newUser = new User();
                    newUser.setEmail(request.getEmail());
                    newUser.setPassword(passwordEncoder.encode(request.getPassword()));
                    newUser.setFirstname(request.getFirstname());
                    newUser.setLastname(request.getLastname());
                    return userRepo.save(newUser);
                })
                .orElseThrow(()->new AlreadyExistsException("User with " + request.getEmail() + " already exists."));
    }

    @Override
    public User updateUser(UserUpdateRequest request, Long userId) {
        return userRepo.findById(userId).map(user -> {
            user.setFirstname(request.getFirstname());
            user.setLastname(request.getLastname());
            return userRepo.save(user);
        }).orElseThrow(() -> new ResourceNotFoundException("User not found."));
    }

    @Override
    public void deleteUser(Long userId) {
        userRepo.findById(userId).ifPresentOrElse(userRepo::delete,
                () -> {throw new ResourceNotFoundException("User Not Found");
        });
    }

    @Override
    public UserDTO convertToDTO(User user) {
        return mapper.map(user, UserDTO.class);
    }

    @Override
    public User getAuthenticatedUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        return userRepo.findByEmail(email);
    }
}
