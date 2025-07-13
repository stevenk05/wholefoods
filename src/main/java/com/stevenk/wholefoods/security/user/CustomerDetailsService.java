package com.stevenk.wholefoods.security.user;

import com.stevenk.wholefoods.model.User;
import com.stevenk.wholefoods.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class CustomerDetailsService implements UserDetailsService {

    private final UserRepository userRepo;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException{
        User user = Optional.ofNullable(userRepo.findByEmail(email))
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return CustomerDetails.buildCustomerDetails(user);
    }
}
