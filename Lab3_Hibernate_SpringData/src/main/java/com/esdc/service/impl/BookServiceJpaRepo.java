package com.esdc.service.impl;

import com.esdc.dto.BookRequest;
import com.esdc.dto.BookResponse;
import com.esdc.entity.Book;
import com.esdc.entity.Library;
import com.esdc.exception.BookAlreadyExistException;
import com.esdc.exception.BookNotFoundException;
import com.esdc.exception.LibraryNotFoundException;
import com.esdc.mapper.BookMapper;
import com.esdc.repository.BookRepository;
import com.esdc.repository.LibraryRepository;
import com.esdc.service.BookServiceI;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Profile("jpa-repo")
@Service
@AllArgsConstructor
public class BookServiceJpaRepo implements BookServiceI {

    private final BookRepository bookRepository;
    private final LibraryRepository libraryRepository;
    private final BookMapper bookMapper;

    @Override
    @Transactional(readOnly = true)
    public List<BookResponse> getBooks() {
        return bookRepository.findAll().stream()
                .map(bookMapper::toDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public BookResponse findById(Long id) {
        return bookRepository.findById(id)
                .map(bookMapper::toDto)
                .orElseThrow(() -> new BookNotFoundException("Book not found with id " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookResponse> findBooksWithCostLessThanAndLibraryName(Double cost, String name) {
        return bookRepository.findBooksWithCostLessThanAndLibraryName(cost,name).stream()
                .map(bookMapper::toDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByIsbn(String isbn) {
        return bookRepository.existsBookByIsbn(isbn);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<BookResponse> getBooks(Pageable pageable) {
        return bookRepository.findAll(pageable).map(bookMapper::toDto);
    }

    @Override
    @Transactional
    public void save(BookRequest request) {
        if(bookRepository.existsBookByIsbn(request.isbn())){ throw new BookAlreadyExistException("Book with ISBN " + request.isbn() + " already exist"); }
        var library = findLibraryOrThrow(request.libraryId());
        bookRepository.save(bookMapper.toEntity(request,library));

    }

    @Override
    @Transactional
    public void update(Long id, BookRequest request) {
        var book = findBookOrThrow(id);
        var library = findLibraryOrThrow(request.libraryId());
        bookRepository.save(bookMapper.updateEntity(request,book,library));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        bookRepository.delete(findBookOrThrow(id));
    }

    private Book findBookOrThrow(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException("Book not found with id: " + id));
    }

    private Library findLibraryOrThrow(Long id) {
        return libraryRepository.findById(id)
                .orElseThrow(() -> new LibraryNotFoundException("Library not found with id: " + id));
    }
}
