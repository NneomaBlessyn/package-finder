package com.nneomablessyn.packagefinder.commons.utils;

import com.nneomablessyn.packagefinder.commons.dto.ApiError;
import com.nneomablessyn.packagefinder.exceptions.ConflictingResourceException;
import com.nneomablessyn.packagefinder.exceptions.CustomException;
import com.nneomablessyn.packagefinder.exceptions.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.InvalidParameterException;

@RestControllerAdvice
public class RestErrorHandler {

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException e) {
        final String error = "Malformed JSON request";
        return buildResponseEntity(new ApiError(HttpStatus.BAD_REQUEST, error, e));
    }

    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Object> handleParameterBindingException(BindException e) {
        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST);
        ObjectError error = e.getAllErrors().get(0);
        apiError.setMessage(error.getDefaultMessage());
        return buildResponseEntity(apiError);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Object> handleMissingParams(final MissingServletRequestParameterException ex) {
        final String name = ex.getParameterName();
        final String responseMessage = String.format(" %s parameter is missing", name);
        final ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST);
        apiError.setMessage(responseMessage);
        return buildResponseEntity(apiError);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Object> handleMethodArgumentNotValid(final MethodArgumentNotValidException ex) {
        final ObjectError error = ex.getBindingResult().getAllErrors().get(0);
        final String responseMessage = error.getDefaultMessage();
        final ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST);
        apiError.setMessage(responseMessage);
        return buildResponseEntity(apiError);
    }

    @ExceptionHandler(InvalidParameterException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Object> handleInvalidParameterException(final InvalidParameterException ex) {
        final ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST);
        apiError.setMessage(ex.getMessage());
        return buildResponseEntity(apiError);
    }

    @ExceptionHandler({NotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<Object> handleNotFoundException(final NotFoundException ex) {
        final ApiError apiError = new ApiError(HttpStatus.NOT_FOUND);
        apiError.setMessage(ex.getMessage());
        return buildResponseEntity(apiError);
    }

    @ExceptionHandler(ConflictingResourceException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<Object> handleConflictingResourceException(final ConflictingResourceException ex) {
        final ApiError apiError = new ApiError(HttpStatus.CONFLICT);
        apiError.setMessage(ex.getMessage());
        return buildResponseEntity(apiError);
    }

    @ExceptionHandler(CustomException.class)
    public void handleCustomException(HttpServletResponse res, CustomException ex) throws IOException {
        res.sendError(ex.getHttpStatus().value(), ex.getMessage());
    }

    private ResponseEntity<Object> buildResponseEntity(final ApiError apiError) {
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }

}
