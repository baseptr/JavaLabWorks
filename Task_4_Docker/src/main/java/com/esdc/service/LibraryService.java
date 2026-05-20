package com.esdc.service;

import com.esdc.dto.LibraryRequest;
import com.esdc.dto.LibraryResponse;
import com.esdc.exception.LibraryNotFoundException;
import com.esdc.mapper.LibraryMapper;
import com.esdc.repository.LibraryRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
public class LibraryService {

    private final LibraryRepository libraryRepository;
    private final LibraryMapper libraryMapper;


    @Transactional(readOnly = true)
    public List<LibraryResponse> findAll() {
        return libraryRepository.findAll().stream()
                .map(libraryMapper::toDto)
                .toList();
    }


    @Transactional(readOnly = true)
    public LibraryResponse findById(Long id) {
        return libraryRepository.findById(id)
                .map(libraryMapper::toDto)
                .orElseThrow( () -> new LibraryNotFoundException("Library not found with id: " + id));
    }


    @Transactional
    public void save(LibraryRequest request) {
        libraryRepository.save(libraryMapper.toEntity(request));
    }


    @Transactional
    public void update(Long id, LibraryRequest request) {
        var library = libraryRepository.findById(id)
                .orElseThrow( () -> new LibraryNotFoundException("Library not found with id: " + id));
        libraryMapper.updateEntity(request,library);
        libraryRepository.save(library);
    }


    @Transactional
    public void delete(Long id) {
        var library = libraryRepository.findById(id)
                        .orElseThrow(() -> new LibraryNotFoundException("Library not found with id: " + id));
        libraryRepository.delete(library);
    }
}
