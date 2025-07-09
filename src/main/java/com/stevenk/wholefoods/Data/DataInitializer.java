package com.stevenk.wholefoods.Data;

import com.stevenk.wholefoods.model.User;
import com.stevenk.wholefoods.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements ApplicationListener<ApplicationReadyEvent> {

    private final UserRepository userRepo;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        createDefaultUsersIfDontExist();
    }

    private void createDefaultUsersIfDontExist() {
        for(int i = 0; i < 5; i++){
            String email = "user" + i + "@gmail.com";
            if(userRepo.existsByEmail(email)){
                continue;
            }
            User user = new User();
            user.setFirstname("User");
            user.setLastname("#" + i);
            user.setEmail(email);
            user.setPassword("password123");
            userRepo.save(user);
            System.out.println("Created user: " + user.getEmail());
        }
    }
}
