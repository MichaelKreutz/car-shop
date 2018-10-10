package de.kreutz.michael.carshop.exception;

public class CarAlreadyExistsException extends RuntimeException {

    public CarAlreadyExistsException(final String message) {
        super(message);
    }
}
