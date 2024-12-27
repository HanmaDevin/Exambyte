package de.propra.exambyte.exception;

public class InsufficientAnswersException extends RuntimeException {
    public InsufficientAnswersException() {
        super();
    }

    public InsufficientAnswersException(String message) {
        super(message);
    }

    public InsufficientAnswersException(Throwable cause) {
        super(cause);
    }

    public InsufficientAnswersException(String message, Throwable cause) {
        super(message, cause);
    }
}
