package com.esdc.service.impl;

import com.esdc.dto.LibraryRequest;
import com.esdc.dto.LibraryResponse;
import com.esdc.entity.Library;
import com.esdc.exception.LibraryNotFoundException;
import com.esdc.mapper.LibraryMapper;
import com.esdc.service.LibraryServiceI;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Service
@AllArgsConstructor
@Profile("jpa")
public class LibraryService implements LibraryServiceI {

    @PersistenceContext
    private EntityManager em;
    private final LibraryMapper libraryMapper;

    public List<LibraryResponse> findAll() {
        return em.createQuery("from Library l left join fetch l.books", Library.class)
                .getResultList()
                .stream()
                .map(libraryMapper::toDto)
                .toList();
    }

    public LibraryResponse findById(Long id) {
        var library = em.find(Library.class, id);
        if (library == null) { throw new LibraryNotFoundException("Library not found with id: " + id); }
        return libraryMapper.toDto(library);
    }

    public void save(LibraryRequest request) {
        em.persist(libraryMapper.toEntity(request));
    }

    public void update(Long id, LibraryRequest request) {
        var library = em.find(Library.class, id);
        if (library == null) { throw new LibraryNotFoundException("Library not found with id: " + id); }
        libraryMapper.updateEntity(request, library);
        em.merge(library);
    }

    public void delete(Long id) {
        var library = em.find(Library.class, id);
        if (library == null) { throw new LibraryNotFoundException("Library not found with id: " + id); }
        em.remove(library);
    }

}
