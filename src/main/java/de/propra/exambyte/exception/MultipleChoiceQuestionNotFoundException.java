package de.propra.exambyte.exception;

public class MultipleChoiceQuestionNotFoundException extends RuntimeException {

    public MultipleChoiceQuestionNotFoundException() {
        super();
    }

    public MultipleChoiceQuestionNotFoundException(String message) {
        super(message);
    }

    public MultipleChoiceQuestionNotFoundException(Throwable cause) {
        super(cause);
    }

    public MultipleChoiceQuestionNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

}
