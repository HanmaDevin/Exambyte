package de.propra.exambyte.exception;

public class WrongDateInputException extends RuntimeException {

    public WrongDateInputException() {
        super();
    }

    public WrongDateInputException(String message) {
        super(message);
    }

    public WrongDateInputException(Throwable cause) {
        super(cause);
    }

    public WrongDateInputException(String message, Throwable cause) {
        super(message, cause);
    }

}
