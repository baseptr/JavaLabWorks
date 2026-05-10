package exception;

public class CurrencyStorageEmptyException extends RuntimeException {
    public CurrencyStorageEmptyException(String message) {
        super(message);
    }
}
