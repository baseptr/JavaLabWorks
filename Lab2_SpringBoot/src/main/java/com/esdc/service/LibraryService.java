package com.esdc.service;

import com.esdc.dto.LibraryRequest;
import com.esdc.entity.Book;
import com.esdc.entity.Library;
import com.esdc.exception.BookNotFoundException;
import com.esdc.exception.LibraryNotFoundException;
import com.esdc.repository.BookRepository;
import com.esdc.repository.LibraryRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class LibraryService {

    private final LibraryRepository libraryRepository;
    private final BookRepository bookRepository;

    public List<Library> getAll() {
        return libraryRepository.findAll();
    }

    public Library getById(Long id) {
        return libraryRepository.findById(id)
                .orElseThrow(() -> new LibraryNotFoundException("Library not found: " + id));
    }

    public void create(LibraryRequest request) {
        Library library = Library.builder()
                .name(request.name())
                .address(request.address())
                .foundedYear(request.foundedYear())
                .books(new ArrayList<>())
                .build();
        libraryRepository.create(library);
        log.info("Created library '{}'", request.name());
    }

    public void update(Long id, LibraryRequest request) {
        Library existing = libraryRepository.findById(id)
                .orElseThrow(() -> new LibraryNotFoundException("Library not found: " + id));
        Library updated = Library.builder()
                .id(id)
                .name(request.name())
                .address(request.address())
                .foundedYear(request.foundedYear())
                .books(existing.getBooks())
                .build();
        libraryRepository.update(updated);
        log.info("Updated library {}", id);
    }

    public void delete(Long id) {
        libraryRepository.findById(id)
                .orElseThrow(() -> new LibraryNotFoundException("Library not found: " + id));
        libraryRepository.delete(id);
        log.info("Deleted library {}", id);
    }

    public void addBook(Long libraryId, Long bookId) {
        Library library = libraryRepository.findById(libraryId)
                .orElseThrow(() -> new LibraryNotFoundException("Library not found: " + libraryId));
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new BookNotFoundException("Book not found: " + bookId));
        book.setLibraryId(libraryId);
        bookRepository.update(book);
        library.getBooks().add(book);
        log.info("Added book {} to library {}", bookId, libraryId);
    }

    public void removeBook(Long libraryId, Long bookId) {
        Library library = libraryRepository.findById(libraryId)
                .orElseThrow(() -> new LibraryNotFoundException("Library not found: " + libraryId));
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new BookNotFoundException("Book not found: " + bookId));
        book.setLibraryId(null);
        bookRepository.update(book);
        library.getBooks().removeIf(b -> b.getId().equals(bookId));
        log.info("Removed book {} from library {}", bookId, libraryId);
    }
}
