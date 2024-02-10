package com.assignment.exception;

import com.assignment.common.HttpResponseDto;
import com.assignment.common.ResponseMessageDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    private final static String ERROR="error";

    @ExceptionHandler(UnProcessableEntityException.class)
    public ResponseEntity<ResponseMessageDto> handleUnProcessableException(UnProcessableEntityException unProcessableEntityException) {
        HttpResponseDto errorResponse = new HttpResponseDto(HttpStatus.UNPROCESSABLE_ENTITY, unProcessableEntityException.getMessage());
        return new ResponseEntity<>(new ResponseMessageDto(ERROR, errorResponse), HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ResponseMessageDto> handleNotFoundException(NotFoundException notFoundException){
        HttpResponseDto  errorResponse= new HttpResponseDto(HttpStatus.NOT_FOUND, notFoundException.getMessage());
        return new ResponseEntity<>(new ResponseMessageDto(ERROR, errorResponse), HttpStatus.NOT_FOUND);
    }
}
