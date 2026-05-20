package com.esdc.mapper;

import com.esdc.dto.BookResponse;
import com.esdc.dto.LibraryRequest;
import com.esdc.dto.LibraryResponse;
import com.esdc.entity.Library;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
public class LibraryMapper {

    private final BookMapper bookMapper;

    public LibraryResponse toDto(Library library) {
        List<BookResponse> books = library.getBooks().stream()
                .map(bookMapper::toDto)
                .toList();
        return new LibraryResponse(
                library.getId(),
                library.getName(),
                library.getAddress(),
                library.getFoundedYear(),
                books
        );
    }

    public Library toEntity(LibraryRequest request) {
        return new Library(null, request.name(), request.address(), request.foundedYear(), null);
    }

    public void updateEntity(LibraryRequest request, Library library) {
        library.setName(request.name());
        library.setAddress(request.address());
        library.setFoundedYear(request.foundedYear());
    }
}
