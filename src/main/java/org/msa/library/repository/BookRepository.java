package org.msa.library.repository;

import org.msa.library.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {


    Optional<Book> findByName(String name);

    List<Book> findTop10ByOrderByCreatedAtDesc();
}
