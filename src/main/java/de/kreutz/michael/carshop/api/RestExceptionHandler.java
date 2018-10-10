package de.kreutz.michael.carshop.api;

import de.kreutz.michael.carshop.dto.ApiErrorResponse;
import de.kreutz.michael.carshop.exception.CarAlreadyExistsException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(EmptyResultDataAccessException.class)
    @ResponseStatus(NOT_FOUND)
    public ApiErrorResponse handleNotFoundException(final EmptyResultDataAccessException exception) {

        return ApiErrorResponse.builder()
            .status(NOT_FOUND)
            .message(exception.getLocalizedMessage())
            .build();
    }

    @ExceptionHandler(CarAlreadyExistsException.class)
    @ResponseStatus(BAD_REQUEST)
    public ApiErrorResponse handleCarAlreadyExistsException(final CarAlreadyExistsException exception) {

        return ApiErrorResponse.builder()
            .status(BAD_REQUEST)
            .message(exception.getLocalizedMessage())
            .build();
    }
}
