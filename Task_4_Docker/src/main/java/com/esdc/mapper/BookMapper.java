package com.esdc.mapper;

import com.esdc.dto.BookRequest;
import com.esdc.dto.BookResponse;
import com.esdc.entity.Book;
import com.esdc.entity.Library;
import org.springframework.stereotype.Component;

@Component
public class BookMapper {

    public BookResponse toDto(Book book) {
        return new BookResponse(
                book.getId(),
                book.getTitle(),
                book.getAuthor(),
                book.getIsbn(),
                book.getPrice(),
                book.getGenre(),
                book.getPublishYear(),
                book.getLibrary().getId()
        );
    }

    public Book toEntity(BookRequest request, Library library) {
        return new Book(null
                ,request.title()
                ,request.author()
                ,request.isbn()
                ,request.price()
                ,request.genre()
                ,request.publishYear()
                ,library);
    }

    public Book updateEntity(BookRequest request, Book book, Library library) {
        book.setTitle(request.title());
        book.setAuthor(request.author());
        book.setIsbn(request.isbn());
        book.setPrice(request.price());
        book.setGenre(request.genre());
        book.setPublishYear(request.publishYear());
        book.setLibrary(library);
        return book;
    }



}
