package com.ejlchina.project.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletResponse;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public String handlerIllegalArgs(IllegalArgumentException e, HttpServletResponse response) {
        response.setStatus(HttpStatus.FORBIDDEN.value());
        return e.getMessage();
    }

}
