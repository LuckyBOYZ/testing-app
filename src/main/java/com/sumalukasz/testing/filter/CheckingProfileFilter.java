package com.sumalukasz.testing.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Map;

@SuppressWarnings("NullableProblems")
@Component
public class CheckingProfileFilter extends OncePerRequestFilter {

    @Value("${spring.profiles.active:none}")
    private String activeProfile;
    private final ObjectMapper objectMapper;
    private static final Logger LOGGER = LoggerFactory.getLogger(CheckingProfileFilter.class);

    public CheckingProfileFilter(ObjectMapper objectMapper) {
        super();
        this.objectMapper = objectMapper;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        LOGGER.info("doFilterInternal");
        if (!activeProfile.equals("test")) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            Map<String, Object> errorBody = Map.of(
                    "errorMessage", "No access to resources with current profile",
                    "activeProfile", activeProfile);
            String body = objectMapper.writeValueAsString(errorBody);
            response.getWriter().write(body);
            response.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
            response.setHeader(HttpHeaders.CONTENT_LENGTH, String.valueOf(body.length()));
        } else {
            filterChain.doFilter(request, response);
        }
    }
}
