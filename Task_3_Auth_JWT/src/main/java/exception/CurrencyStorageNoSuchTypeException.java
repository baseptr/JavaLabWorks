package exception;

public class CurrencyStorageNoSuchTypeException extends RuntimeException {
    public CurrencyStorageNoSuchTypeException(String message) {
        super(message);
    }
}
