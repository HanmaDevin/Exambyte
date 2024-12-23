package de.propra.exambyte.exception;

public class EmptyInputException extends RuntimeException {

    public EmptyInputException() {
        super();
    }

    public EmptyInputException(String message) {
        super(message);
    }

    public EmptyInputException(Throwable cause) {
        super(cause);
    }

    public EmptyInputException(String message, Throwable cause) {
        super(message, cause);
    }
}
