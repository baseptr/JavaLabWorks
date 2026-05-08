package com.esdc.controller;

import com.esdc.dto.LibraryRequest;
import com.esdc.dto.LibraryResponse;
import com.esdc.service.LibraryServiceI;
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

    private final LibraryServiceI libraryService;

    @GetMapping
    public ResponseEntity<List<LibraryResponse>> findAll() {
        return ResponseEntity.ok(libraryService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<LibraryResponse> findById(@PathVariable @Positive Long id) {
        return ResponseEntity.ok(libraryService.findById(id));
    }

    @PostMapping
    public ResponseEntity<Void> create(@RequestBody @Valid LibraryRequest request) {
        libraryService.save(request);
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
        return ResponseEntity.ok().build();
    }
}
