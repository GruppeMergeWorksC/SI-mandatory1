package com.starlords.sirmeows.service;

import com.starlords.sirmeows.entity.Author;
import com.starlords.sirmeows.exception.AuthorAlreadyExistsException;
import com.starlords.sirmeows.exception.AuthorNotFoundException;
import com.starlords.sirmeows.repo.AuthorRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;

@AllArgsConstructor
@Service
public class AuthorService {
    private AuthorRepository authorRepository;

    public Collection<Author> findAll() {
        return authorRepository.findAll();
    }

    public Author findById(Integer id) {
        return authorRepository.findById(id).orElseThrow(AuthorNotFoundException::new);
    }

    public Author create(Author author) {
        handleAuthorExists(author);

        return authorRepository.save(author);
    }

    private void handleAuthorExists(Author author) {
        // Handle Author exists, accounting for authors with no surname
        var name = author.getName();
        var surname = author.getSurname();

        if (surname == null && authorRepository.existsByName(name)) {
            throw new AuthorAlreadyExistsException(name);
        }
        if (authorRepository.existsByNameAndSurname(name, surname)) {
            throw new AuthorAlreadyExistsException(name, surname);
        }
    }

    public Author update(Author author) {
        return authorRepository.save(author);
    }

    public void delete(Integer id) {
        authorRepository.deleteById(id);
    }
}
