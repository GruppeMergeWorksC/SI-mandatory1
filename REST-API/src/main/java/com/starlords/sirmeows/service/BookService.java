package com.starlords.sirmeows.service;

import com.starlords.sirmeows.entity.Book;
import com.starlords.sirmeows.exception.AuthorNotFoundException;
import com.starlords.sirmeows.exception.BookAlreadyExistsException;
import com.starlords.sirmeows.exception.BookNotFoundException;
import com.starlords.sirmeows.exception.PublisherNotFoundException;
import com.starlords.sirmeows.repo.AuthorRepository;
import com.starlords.sirmeows.repo.BookRepository;
import com.starlords.sirmeows.repo.PublisherRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class BookService {
    private BookRepository bookRepository;
    private AuthorRepository authorRepository;
    private PublisherRepository publisherRepository;

    public List<Book> findAll(int limit, int offset) {
        if (limit <= 0) {
            throw new IllegalArgumentException("limit must be greater than 0");
        }
        if (offset < 0) {
            throw new IllegalArgumentException("offset must be 0 or greater");
        }
        return bookRepository.findAllWithLimitAndOffset(limit, offset);
    }

    public Book findById(Integer id) {
        return bookRepository.findById(id).orElseThrow(BookNotFoundException::new);
    }

    public Book create(Book book) {
        handleBookAlreadyExists(book);

        handleAuthorNotFound(book.getAuthor().getId());

        handlePublisherNotFound(book.getPublisher().getId());

        return bookRepository.save(book);
    }

    public Book update(Integer id, Book patch) {
        var authorId = patch.getAuthor().getId();
        var publisherId = patch.getPublisher().getId();

        var book = bookRepository.findById(id).orElseThrow(() -> new BookNotFoundException(id));

        var author = authorRepository.findById(authorId)
                .orElseThrow(() -> new AuthorNotFoundException(authorId));

        var publisher = publisherRepository.findById(publisherId)
                .orElseThrow(() -> new PublisherNotFoundException(publisherId));

        book.setTitle(patch.getTitle());
        book.setAuthor(author);
        book.setPublisher(publisher);
        book.setPublishingYear(patch.getPublishingYear());

        return bookRepository.save(book);
    }

    private void handlePublisherNotFound(Integer id) {
        if (!publisherRepository.existsById(id)) {
            throw new PublisherNotFoundException(id);
        }
    }

    private void handleAuthorNotFound(Integer id) {
        if (!authorRepository.existsById(id)) {
            throw new AuthorNotFoundException(id);
        }
    }

    private void handleBookAlreadyExists(Book book) {
        var title = book.getTitle();
        var authorId = book.getAuthor().getId();
        var publisherId = book.getPublisher().getId();
        var publishingYear = book.getPublishingYear();

        if(bookRepository.existsByTitleAndAuthorIdAndPublisherIdAndPublishingYear(title, authorId, publisherId, publishingYear)) {
            throw new BookAlreadyExistsException(title, authorId, publisherId, publishingYear);
        }
    }

    public void delete(Integer id) {
        bookRepository.deleteById(id);
    }
}
