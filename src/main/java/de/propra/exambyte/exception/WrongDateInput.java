package de.propra.exambyte.exception;

public class WrongDateInput extends RuntimeException {

    public WrongDateInput() {
        super();
    }

    public WrongDateInput(String message) {
        super(message);
    }

    public WrongDateInput(Throwable cause) {
        super(cause);
    }

    public WrongDateInput(String message, Throwable cause) {
        super(message, cause);
    }

}
