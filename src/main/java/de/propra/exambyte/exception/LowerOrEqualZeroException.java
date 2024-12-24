package de.propra.exambyte.exception;

public class LowerOrEqualZeroException extends RuntimeException {

    public LowerOrEqualZeroException() {
        super();
    }

    public LowerOrEqualZeroException(String message) {
        super(message);
    }

    public LowerOrEqualZeroException(Throwable cause) {
        super(cause);
    }

    public LowerOrEqualZeroException(String message, Throwable cause) {
        super(message, cause);
    }
}
