package id.co.mii.sitego.controller;

import id.co.mii.sitego.model.response.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class ErrorController {

    @ExceptionHandler(ResponseStatusException.class)
    public ErrorResponse response(ResponseStatusException exception, HttpServletResponse response) {
        response.setStatus(exception.getStatus().value());

        return ErrorResponse.builder()
            .messages(Collections.singletonList(exception.getMessage()))
            .status(exception.getStatus().value())
            .timestamp(LocalDateTime.now())
            .build();
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse response(ConstraintViolationException exception) {
        List<String> messages = exception.getConstraintViolations()
            .stream()
            .map(constraintViolation -> constraintViolation.getPropertyPath() + " " + constraintViolation.getMessage())
            .collect(Collectors.toList());

        return ErrorResponse.builder()
            .messages(messages)
            .status(HttpStatus.BAD_REQUEST.value())
            .timestamp(LocalDateTime.now())
            .build();
    }

    @ExceptionHandler(BadCredentialsException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorResponse response(BadCredentialsException exception) {
        return ErrorResponse.builder()
            .messages(Collections.singletonList(exception.getMessage()))
            .status(HttpStatus.UNAUTHORIZED.value())
            .timestamp(LocalDateTime.now())
            .build();
    }

    @ExceptionHandler(InternalAuthenticationServiceException.class)
    public ErrorResponse response(InternalAuthenticationServiceException exception, HttpServletResponse response) {

        if (exception.getCause() instanceof ResponseStatusException) {
            ResponseStatusException responseStatusException = (ResponseStatusException) exception.getCause();
            response.setStatus(responseStatusException.getStatus().value());

            return ErrorResponse.builder()
                .messages(Collections.singletonList(responseStatusException.getReason()))
                .status(responseStatusException.getStatus().value())
                .timestamp(LocalDateTime.now())
                .build();
        }

        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        return ErrorResponse.builder()
            .messages(Collections.singletonList(exception.getMessage()))
            .status(HttpStatus.UNAUTHORIZED.value())
            .timestamp(LocalDateTime.now())
            .build();
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse response(Exception exception) {
        return ErrorResponse.builder()
            .messages(Collections.singletonList(exception.getMessage()))
            .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
            .timestamp(LocalDateTime.now())
            .build();
    }
}
