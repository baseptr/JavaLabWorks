package com.esdc.controller;

import com.esdc.dto.LibraryRequest;
import com.esdc.entity.Library;
import com.esdc.service.LibraryService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/libraries")
@AllArgsConstructor
@Validated
public class LibraryController {

    private final LibraryService libraryService;

    @GetMapping
    public ResponseEntity<List<Library>> getAll() {
        return ResponseEntity.ok(libraryService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Library> getById(@PathVariable @Positive Long id) {
        return ResponseEntity.ok(libraryService.getById(id));
    }

    @PostMapping
    public ResponseEntity<Void> create(@RequestBody @Valid LibraryRequest request) {
        libraryService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable @Positive Long id, @RequestBody @Valid LibraryRequest request) {
        libraryService.update(id, request);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable @Positive Long id) {
        libraryService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{libraryId}/books/{bookId}")
    public ResponseEntity<Void> addBook(@PathVariable @Positive Long libraryId, @PathVariable @Positive Long bookId) {
        libraryService.addBook(libraryId, bookId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{libraryId}/books/{bookId}")
    public ResponseEntity<Void> removeBook(@PathVariable @Positive Long libraryId, @PathVariable @Positive Long bookId) {
        libraryService.removeBook(libraryId, bookId);
        return ResponseEntity.noContent().build();
    }
}
