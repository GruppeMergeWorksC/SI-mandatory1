package com.starlords.sirmeows.repo;

import com.starlords.sirmeows.entity.Author;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorRepository extends JpaRepository<Author, Integer> {

    Boolean existsByName(String name);
    Boolean existsByNameAndSurname(String name, String surname);
}
