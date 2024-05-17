package com.github.dimafour.restaurantvoting.config;

import com.github.dimafour.restaurantvoting.error.*;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ValidationException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.NestedExceptionUtils;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.lang.NonNull;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.firewall.RequestRejectedException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.io.FileNotFoundException;
import java.net.URI;
import java.nio.file.AccessDeniedException;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.springframework.http.HttpStatus.*;
import static org.springframework.web.ErrorResponse.*;

@RestControllerAdvice
@AllArgsConstructor
@Slf4j
public class RestExceptionHandler {
    static final Map<Class<? extends Throwable>, HttpStatus> HTTP_STATUS_MAP = new LinkedHashMap<>() {
        {
            put(NotFoundException.class, NOT_FOUND);
            put(AuthenticationException.class, UNAUTHORIZED);
            put(FileNotFoundException.class, NOT_FOUND);
            put(UpdateRestrictionException.class, FORBIDDEN);
            put(NoHandlerFoundException.class, NOT_FOUND);
            put(DataConflictException.class, CONFLICT);
            put(IllegalRequestDataException.class, BAD_REQUEST);
            put(AppException.class, INTERNAL_SERVER_ERROR);
            put(UnsupportedOperationException.class, INTERNAL_SERVER_ERROR);
            put(EntityNotFoundException.class, CONFLICT);
            put(DataIntegrityViolationException.class, CONFLICT);
            put(IllegalArgumentException.class, UNPROCESSABLE_ENTITY);
            put(ValidationException.class, UNPROCESSABLE_ENTITY);
            put(HttpRequestMethodNotSupportedException.class, BAD_REQUEST);
            put(ServletRequestBindingException.class, BAD_REQUEST);
            put(RequestRejectedException.class, BAD_REQUEST);
            put(AccessDeniedException.class, FORBIDDEN);
        }
    };

    @NonNull
    private static Throwable getRootCause(@NonNull Throwable t) {
        Throwable rootCause = NestedExceptionUtils.getRootCause(t);
        return rootCause != null ? rootCause : t;
    }

    @ExceptionHandler(BindException.class)
    ProblemDetail bindException(BindException ex, HttpServletRequest request) {
        Map<String, String> invalidParams = getErrorMap(ex.getBindingResult());
        String path = request.getRequestURI();
        log.warn("BindException with invalidParams {} at request {}", invalidParams, path);
        return createProblemDetail(ex, path, UNPROCESSABLE_ENTITY, "BindException", Map.of("invalid_params", invalidParams));
    }

    @ExceptionHandler(Exception.class)
    ProblemDetail exception(Exception ex, HttpServletRequest request) {
        String path = request.getRequestURI();
        Class<? extends Exception> exClass = ex.getClass();
        HttpStatus status = HTTP_STATUS_MAP.entrySet().stream()
                .filter(entry -> entry.getKey().isAssignableFrom(exClass))
                .findAny().map(Map.Entry::getValue).orElse(INTERNAL_SERVER_ERROR);
        Throwable root = getRootCause(ex);
        log.error("Exception {} at request {}", root, path);
        return createProblemDetail(ex, path, status, ex.getMessage(), Map.of());
    }

    private Map<String, String> getErrorMap(BindingResult result) {
        Map<String, String> invalidParams = new LinkedHashMap<>();
        for (ObjectError error : result.getGlobalErrors()) {
            invalidParams.put(error.getObjectName(), error.getDefaultMessage());
        }
        for (FieldError error : result.getFieldErrors()) {
            invalidParams.put(error.getField(), error.getDefaultMessage());
        }
        return invalidParams;
    }

    private ProblemDetail createProblemDetail(Exception ex, String path, HttpStatus status, String defaultDetail, @NonNull Map<String, Object> additionalParams) {
        ProblemDetail pd = builder(ex, status, defaultDetail)
                .instance(URI.create(path))
                .build().getBody();
        additionalParams.forEach(pd::setProperty);
        return pd;
    }
}