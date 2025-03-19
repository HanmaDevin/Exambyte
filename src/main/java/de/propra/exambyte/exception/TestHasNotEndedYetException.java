package de.propra.exambyte.exception;

public class TestHasNotEndedYetException extends RuntimeException{
    public TestHasNotEndedYetException(String message) {
        super(message);
    }

    public TestHasNotEndedYetException(String message, Throwable cause) {
        super(message, cause);
    }

    public TestHasNotEndedYetException(Throwable cause) {
        super(cause);
    }

    public TestHasNotEndedYetException() {
        super();
    }
}
