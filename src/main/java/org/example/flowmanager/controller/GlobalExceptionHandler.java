package org.example.flowmanager.controller;

import lombok.extern.slf4j.Slf4j;
import org.example.flowmanager.exception.FileConvertionFailedException;
import org.example.flowmanager.exception.FileNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler(FileNotFoundException.class)
    public ProblemDetail handleNotFound(FileNotFoundException e) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, e.getMessage());
    }

    @ExceptionHandler(FileConvertionFailedException.class)
    public ProblemDetail handleNotFound(FileConvertionFailedException e) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.UNPROCESSABLE_ENTITY, e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ProblemDetail handlerOther(Exception e) {
        log.error("Не обработанная ошибка", e);
        return ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, "Внутрення ошибка сервера");
    }
}
