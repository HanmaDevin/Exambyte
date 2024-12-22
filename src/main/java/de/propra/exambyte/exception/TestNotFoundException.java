package de.propra.exambyte.exception;

public class TestNotFoundException extends RuntimeException {

    public TestNotFoundException() {
        super();
    }

    public TestNotFoundException(String message) {
        super(message);
    }

    public TestNotFoundException(Throwable cause) {
        super(cause);
    }

    public TestNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
