package com.sumalukasz.testing.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sumalukasz.testing.exception.InvalidUuidException;
import com.sumalukasz.testing.repository.GetEmployeeIdByUuidRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class ConvertingEmployeeUuidOnRealOneFilter extends OncePerRequestFilter {

    private static final Pattern URL_PATTERN = Pattern.compile("^/employees/(?<uuid>[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12})(?:/address)?$");
    private static final String UUID_REGEX = "[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}";
    private static final Logger LOGGER = LoggerFactory.getLogger(ConvertingEmployeeUuidOnRealOneFilter.class);
    private final GetEmployeeIdByUuidRepository getEmployeeIdByUuidRepository;
    private final ObjectMapper objectMapper;

    public ConvertingEmployeeUuidOnRealOneFilter(GetEmployeeIdByUuidRepository getEmployeeIdByUuidRepository, ObjectMapper objectMapper) {
        super();
        this.getEmployeeIdByUuidRepository = getEmployeeIdByUuidRepository;
        this.objectMapper = objectMapper;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        LOGGER.info("doFilterInternal");
        Matcher matcher = URL_PATTERN.matcher(request.getServletPath());
        String uuid = null;
        while (matcher.find()) {
            uuid = matcher.group("uuid");
        }
        try {
            long employeeId = getEmployeeIdByUuidRepository.getEmployeeIdByUuid(uuid);
            String pathWithRealId = request.getServletPath().replaceFirst(UUID_REGEX, String.valueOf(employeeId));
            RequestDispatcher dispatcher = request.getRequestDispatcher(pathWithRealId);
            dispatcher.forward(request, response);
        } catch (InvalidUuidException ex) {
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            PrintWriter writer = response.getWriter();
            Map<String, String> errorBody = Map.of("errorMessage", ex.getMessage(), "value", ex.getValue());
            String bodyAsString = objectMapper.writeValueAsString(errorBody);
            writer.write(bodyAsString);
        }
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return !URL_PATTERN.matcher(request.getServletPath()).matches();
    }
}
