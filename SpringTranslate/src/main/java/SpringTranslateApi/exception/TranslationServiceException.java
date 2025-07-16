package SpringTranslateApi.exception;

public class TranslationServiceException extends RuntimeException {
	 private static final long serialVersionUID = 1L;
    
    public TranslationServiceException(String message) {
        super(message);
    }

    public TranslationServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
