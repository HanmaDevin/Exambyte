package de.propra.exambyte.exception;

public class DuplicateAnswerException extends RuntimeException {
    public DuplicateAnswerException() {
        super();
    }

    public DuplicateAnswerException(String message) {
        super(message);
    }
    public DuplicateAnswerException(Throwable cause) {
        super(cause);
    }

    public DuplicateAnswerException(String message, Throwable cause) {
        super(message, cause);
    }
}
