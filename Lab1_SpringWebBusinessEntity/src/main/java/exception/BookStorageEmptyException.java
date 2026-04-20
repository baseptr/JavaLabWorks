package exception;

public class BookStorageEmptyException extends RuntimeException {
    public BookStorageEmptyException(String message) {
        super(message);
    }
}
