package de.propra.exambyte.exception;

public class NoTestActiveException extends RuntimeException {
    public NoTestActiveException(String message) {
        super(message);
    }

    public NoTestActiveException(Throwable cause) {
        super(cause);
    }

    public NoTestActiveException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoTestActiveException() {
        super();
    }
}
