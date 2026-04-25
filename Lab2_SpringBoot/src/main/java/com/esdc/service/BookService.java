package com.esdc.service;

import com.esdc.config.BookStorageConfig;
import com.esdc.dto.BookRequest;
import com.esdc.entity.Book;
import com.esdc.entity.Library;
import com.esdc.exception.BookAlreadyExistException;
import com.esdc.exception.BookNotFoundException;
import com.esdc.exception.LibraryNotFoundException;
import com.esdc.repository.BookRepository;
import com.esdc.repository.LibraryRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class BookService {

    private final BookRepository bookRepository;
    private final LibraryRepository libraryRepository;
    private final BookStorageConfig defaults;

    public List<Book> getAll() {
        return bookRepository.findAll();
    }

    public Book getById(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException("Book not found: " + id));
    }

    public String create(BookRequest request) {
        Library library = request.libraryId() != null
                ? libraryRepository.findById(request.libraryId())
                        .orElseThrow(() -> new LibraryNotFoundException("Library not found: " + request.libraryId()))
                : libraryRepository.getDefault();

        String isbn = Optional.ofNullable(request.isbn()).orElse(defaults.getDefaultIsbn());
        boolean alreadyInLibrary = library.getBooks().stream()
                .anyMatch(b -> b.getIsbn().equals(isbn));
        if (alreadyInLibrary) {
            throw new BookAlreadyExistException("Book with ISBN " + isbn + " already exists in library " + library.getId());
        }

        Book book = Book.builder()
                .title(Optional.ofNullable(request.title()).orElse(defaults.getDefaultTitle()))
                .author(Optional.ofNullable(request.author()).orElse(defaults.getDefaultAuthor()))
                .isbn(isbn)
                .price(Optional.ofNullable(request.price()).orElse(defaults.getDefaultPrice()))
                .genre(Optional.ofNullable(request.genre()).orElse(defaults.getDefaultGenre()))
                .publishYear(Optional.ofNullable(request.publishYear()).orElse(defaults.getDefaultPubYear()))
                .libraryId(library.getId())
                .build();
        bookRepository.create(book);
        log.info("Created book with ISBN {} in library {}", isbn, library.getId());
        return defaults.getMessage();
    }

    public void update(Long id, BookRequest request) {
        bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException("Book not found: " + id));
        Book book = Book.builder()
                .id(id)
                .title(request.title())
                .author(request.author())
                .isbn(request.isbn())
                .price(request.price())
                .genre(request.genre())
                .publishYear(request.publishYear())
                .build();
        bookRepository.update(book);
        log.info("Updated book {}", id);
    }

    public void delete(Long id) {
        bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException("Book not found: " + id));
        bookRepository.delete(id);
        log.info("Deleted book {}", id);
    }
}
