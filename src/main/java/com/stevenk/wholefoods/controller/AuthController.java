package com.stevenk.wholefoods.controller;

import com.stevenk.wholefoods.requests.LoginRequest;
import com.stevenk.wholefoods.response.ApiResponse;
import com.stevenk.wholefoods.response.JwtResponse;
import com.stevenk.wholefoods.security.jwt.JWTutils;
import com.stevenk.wholefoods.security.user.CustomerDetails;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/auth")
public class AuthController {
    private final AuthenticationManager authManager;
    private final JWTutils util;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse> login(@Valid @RequestBody LoginRequest rq) {
        try {
            Authentication auth = authManager.authenticate(new UsernamePasswordAuthenticationToken(
                    rq.getEmail(), rq.getPassword()
            ));
            SecurityContextHolder.getContext().setAuthentication(auth);
            String token = util.generateToken(auth);
            CustomerDetails details = (CustomerDetails) auth.getPrincipal();
            JwtResponse jwt = new JwtResponse(details.getId(), token);
            return ResponseEntity.ok(new ApiResponse("Login successful", jwt));
        } catch (AuthenticationException e) {
            return ResponseEntity.status(UNAUTHORIZED).body(new ApiResponse(e.getMessage(), null));
        }
    }
}
