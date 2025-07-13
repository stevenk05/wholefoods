package com.stevenk.wholefoods.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

@Component
public class AuthEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException)
                        throws IOException, ServletException
    {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        final Map<String, Object> map = new HashMap<>();
        //map.put("status", HttpServletResponse.SC_UNAUTHORIZED);
        map.put("error", "Unauthorized");
        map.put("message", authException.getMessage() + ". You may login and try again.");
        //map.put("path", request.getServletPath());

        final ObjectMapper mapper = new ObjectMapper();
        try (PrintWriter writer = response.getWriter()) {
            writer.write(mapper.writeValueAsString(map));
            writer.flush();
        }


    }
}
