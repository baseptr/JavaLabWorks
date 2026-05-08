package com.esdc.service.impl;

import com.esdc.dto.BookRequest;
import com.esdc.dto.BookResponse;
import com.esdc.entity.Book;
import com.esdc.entity.Library;
import com.esdc.exception.BookAlreadyExistException;
import com.esdc.exception.BookNotFoundException;
import com.esdc.exception.LibraryNotFoundException;
import com.esdc.mapper.BookMapper;
import com.esdc.service.BookServiceI;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.data.domain.PageImpl;

import java.util.List;

@Service
@Transactional
@AllArgsConstructor
@Profile("jpa")
public class BookService implements BookServiceI {

    @PersistenceContext
    private EntityManager em;
    private final BookMapper bookMapper;

    public List<BookResponse> getBooks(){
        return em.createQuery("from Book", Book.class)
                .getResultList()
                .stream()
                .map(bookMapper::toDto)
                .toList();
    }

    public BookResponse findById(Long id){
        var book = em.find(Book.class, id);
        if(book == null){ throw new BookNotFoundException("Book not found with id " + id); }

        return bookMapper.toDto(book);
    }

    public boolean existsByIsbn(String isbn){
        return !em.createQuery("from Book b where b.isbn = :isbn",Book.class)
                .setParameter("isbn", isbn)
                .getResultList()
                .isEmpty();
    }

    public void save(BookRequest request){
        if(existsByIsbn(request.isbn())){ throw new BookAlreadyExistException("Book with ISBN " + request.isbn() + " already exist"); }
        var library = em.find(Library.class, request.libraryId());
        if (library == null) { throw new LibraryNotFoundException("Library not found with id: " + request.libraryId()); }
        var book = bookMapper.toEntity(request,library);
        em.persist(book);
    }

    public void update(Long id, BookRequest request){
        var book = em.find(Book.class, id);
        if (book == null) { throw new BookNotFoundException("Book not found with id " + id); }
        var library = em.find(Library.class, request.libraryId());
        if (library == null) { throw new LibraryNotFoundException("Library not found with id: " + request.libraryId()); }
        bookMapper.updateEntity(request, book, library);
        em.merge(book);
    }

    public void delete(Long id){
        var book = em.find(Book.class, id);
        if (book == null) { throw new BookNotFoundException("Book not found with id " + id); }
        em.remove(book);
    }

    public Page<BookResponse> getBooks(Pageable pageable) {
        List<BookResponse> content = em.createQuery("from Book", Book.class)
                .setFirstResult((int) pageable.getOffset())
                .setMaxResults(pageable.getPageSize())
                .getResultList()
                .stream()
                .map(bookMapper::toDto)
                .toList();

        long total = em.createQuery("select count(b) from Book b", Long.class)
                .getSingleResult();

        return new PageImpl<>(content, pageable, total);
    }

    public List<BookResponse> findBooksWithCostLessThanAndLibraryName(Double cost, String name) {
        return em.createQuery("select b from Book b join b.library l where b.price < :cost and l.name = :name", Book.class)
                .setParameter("cost", cost)
                .setParameter("name", name)
                .getResultList()
                .stream()
                .map(bookMapper::toDto)
                .toList();
    }
}
