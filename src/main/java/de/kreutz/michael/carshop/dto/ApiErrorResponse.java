package de.kreutz.michael.carshop.dto;

import lombok.Builder;
import lombok.Value;
import org.springframework.http.HttpStatus;

@Value
@Builder(toBuilder = true)
public class ApiErrorResponse {

    HttpStatus status;
    String message;
}
