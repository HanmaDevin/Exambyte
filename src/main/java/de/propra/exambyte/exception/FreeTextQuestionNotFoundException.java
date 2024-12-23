package de.propra.exambyte.exception;

public class FreeTextQuestionNotFoundException extends RuntimeException {

        public FreeTextQuestionNotFoundException() {
            super();
        }

        public FreeTextQuestionNotFoundException(String message) {
            super(message);
        }

        public FreeTextQuestionNotFoundException(Throwable cause) {
            super(cause);
        }

        public FreeTextQuestionNotFoundException(String message, Throwable cause) {
            super(message, cause);
        }
}
