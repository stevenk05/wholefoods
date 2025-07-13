package com.stevenk.wholefoods.security.jwt;

import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

public class AuthTokenFilter extends OncePerRequestFilter {
    @Autowired
    private JWTutils util;
    @Autowired
    private UserDetailsService udService;
    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest rq,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, java.io.IOException {
        try {
            String jwt = parseJWT(rq);
            if(StringUtils.hasText(jwt) && util.validateToken(jwt)){
                String email = util.getUsernameFromToken(jwt);
                UserDetails customer = udService.loadUserByUsername(email);
                Authentication auth = new UsernamePasswordAuthenticationToken
                        (customer, null, customer.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        } catch (JwtException e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write(e.getMessage() + ". This token may be valid or expired.");
            return;
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write(e.getMessage());
        }
        filterChain.doFilter(rq, response);
    }

    private String parseJWT(HttpServletRequest request){
        String header = request.getHeader("Authorization");
        if(StringUtils.hasText(header) && header.startsWith("Bearer ")){
            return header.substring(7);
        }
        return null;
    }
}
