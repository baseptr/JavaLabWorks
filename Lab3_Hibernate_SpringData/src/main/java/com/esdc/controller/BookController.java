package com.esdc.controller;

import com.esdc.dto.BookRequest;
import com.esdc.dto.BookResponse;
import com.esdc.service.BookServiceI;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/books")
@AllArgsConstructor
@Validated
public class BookController {

    private final BookServiceI bookService;

    @GetMapping
    public ResponseEntity<List<BookResponse>> findAll() {
        return ResponseEntity.ok(bookService.getBooks());
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookResponse> findById(@PathVariable @Positive Long id){
        return ResponseEntity.ok(bookService.findById(id));
    }

    @GetMapping("/book")
    public ResponseEntity<List<BookResponse>> findByLessThanCostAndNameLib(@RequestParam Double cost, @RequestParam @NotBlank String name){
        return ResponseEntity.ok(bookService.findBooksWithCostLessThanAndLibraryName(cost, name));
    }

    @GetMapping("/paged")
    public ResponseEntity<Page<BookResponse>> findAllPaged(@ParameterObject @PageableDefault(sort = "title", size = 5) Pageable pageable) {
        return ResponseEntity.ok(bookService.getBooks(pageable));
    }

    @PostMapping
    public ResponseEntity<Void> create(@RequestBody @Valid BookRequest request){
        bookService.save(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@RequestBody @Valid BookRequest request, @PathVariable @Positive Long id){
        bookService.update(id,request);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable @Positive Long id){
        bookService.delete(id);
        return ResponseEntity.ok().build();
    }
}
