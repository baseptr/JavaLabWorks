package com.esdc.service;

import com.esdc.dto.LibraryRequest;
import com.esdc.dto.LibraryResponse;

import java.util.List;

public interface LibraryServiceI {
    List<LibraryResponse> findAll();
    LibraryResponse findById(Long id);
    void save(LibraryRequest request);
    void update(Long id, LibraryRequest request);
    void delete(Long id);
}
