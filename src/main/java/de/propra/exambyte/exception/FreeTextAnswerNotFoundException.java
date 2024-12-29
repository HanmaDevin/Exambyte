package de.propra.exambyte.exception;

public class FreeTextAnswerNotFoundException extends RuntimeException {
  public FreeTextAnswerNotFoundException() {
    super();
  }

  public FreeTextAnswerNotFoundException(String message) {
    super(message);
  }

  public FreeTextAnswerNotFoundException(Throwable cause) {
    super(cause);
  }

  public FreeTextAnswerNotFoundException(String message, Throwable cause) {
    super(message, cause);
  }
}
