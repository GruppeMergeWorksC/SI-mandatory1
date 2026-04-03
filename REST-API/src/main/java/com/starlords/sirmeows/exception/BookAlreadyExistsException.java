package com.starlords.sirmeows.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BookAlreadyExistsException extends RuntimeException {
    private static final String DEFAULT_MESSAGE = "Book with title '%s', author id '%d', '%d' publisherId, '%d' publishingYear already exists";

    public BookAlreadyExistsException(String title, Integer authorId, Integer publisherId, Integer publishingYear) {
        super(String.format(DEFAULT_MESSAGE, title, authorId, publisherId, publishingYear));
    }
}
