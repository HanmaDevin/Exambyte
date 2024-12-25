package de.propra.exambyte.exception;

public class NoAnswersMarkedCorrectException extends RuntimeException {

    public NoAnswersMarkedCorrectException() {
        super();
    }

    public NoAnswersMarkedCorrectException(String message) {
        super(message);
    }

    public NoAnswersMarkedCorrectException(Throwable cause) {
        super(cause);
    }

    public NoAnswersMarkedCorrectException(String message, Throwable cause) {
        super(message, cause);
    }
}
