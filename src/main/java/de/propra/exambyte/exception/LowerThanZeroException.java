package de.propra.exambyte.exception;

public class LowerThanZeroException extends RuntimeException {

    public LowerThanZeroException() {
        super();
    }

    public LowerThanZeroException(String message) {
        super(message);
    }

    public LowerThanZeroException(Throwable cause) {
        super(cause);
    }

    public LowerThanZeroException(String message, Throwable cause) {
        super(message, cause);
    }
}
