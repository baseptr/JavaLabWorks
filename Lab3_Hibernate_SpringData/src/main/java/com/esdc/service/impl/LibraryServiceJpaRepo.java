package com.esdc.service.impl;

import com.esdc.dto.LibraryRequest;
import com.esdc.dto.LibraryResponse;
import com.esdc.exception.LibraryNotFoundException;
import com.esdc.mapper.LibraryMapper;
import com.esdc.repository.LibraryRepository;
import com.esdc.service.LibraryServiceI;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Profile("jpa-repo")
@Service
@AllArgsConstructor
public class LibraryServiceJpaRepo implements LibraryServiceI {

    private final LibraryRepository libraryRepository;
    private final LibraryMapper libraryMapper;

    @Override
    @Transactional(readOnly = true)
    public List<LibraryResponse> findAll() {
        return libraryRepository.findAll().stream()
                .map(libraryMapper::toDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public LibraryResponse findById(Long id) {
        return libraryRepository.findById(id)
                .map(libraryMapper::toDto)
                .orElseThrow( () -> new LibraryNotFoundException("Library not found with id: " + id));
    }

    @Override
    @Transactional
    public void save(LibraryRequest request) {
        libraryRepository.save(libraryMapper.toEntity(request));
    }

    @Override
    @Transactional
    public void update(Long id, LibraryRequest request) {
        var library = libraryRepository.findById(id)
                .orElseThrow( () -> new LibraryNotFoundException("Library not found with id: " + id));
        libraryMapper.updateEntity(request,library);
        libraryRepository.save(library);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        var library = libraryRepository.findById(id)
                        .orElseThrow(() -> new LibraryNotFoundException("Library not found with id: " + id));
        libraryRepository.delete(library);
    }
}
