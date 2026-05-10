package exception;

public class LibraryStorageEmptyException extends RuntimeException {
    public LibraryStorageEmptyException(String message) {
        super(message);
    }
}
