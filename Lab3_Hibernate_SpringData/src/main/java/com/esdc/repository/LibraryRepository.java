package com.esdc.repository;

import com.esdc.entity.Library;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LibraryRepository extends JpaRepository<Library,Long> {
    @EntityGraph(attributePaths = "books")
    @NonNull
    List<Library> findAll();
}
