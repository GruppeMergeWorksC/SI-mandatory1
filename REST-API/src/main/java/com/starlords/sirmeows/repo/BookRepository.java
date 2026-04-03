package com.starlords.sirmeows.repo;

import com.starlords.sirmeows.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface BookRepository extends JpaRepository<Book, Integer> {
    @Query(value = "select * from book limit :limit offset :offset", nativeQuery = true)
    List<Book> findAllWithLimitAndOffset(@Param("limit") int limit, @Param("offset") int offset);

    Boolean existsByTitleAndAuthorIdAndPublisherIdAndPublishingYear(String title, Integer authorId, Integer publisherId, Integer publishingYear);
}
