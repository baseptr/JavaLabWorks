package com.esdc.repository;

import com.esdc.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    boolean existsBookByIsbn(String isbn);
    @Query("select b from Book b join b.library l where b.price < :cost and l.name = :name")
    List<Book> findBooksWithCostLessThanAndLibraryName(@Param("cost") Double cost, @Param("name") String name);
}
