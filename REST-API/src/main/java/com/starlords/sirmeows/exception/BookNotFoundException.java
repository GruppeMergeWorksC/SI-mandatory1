package com.starlords.sirmeows.exception;

public class BookNotFoundException extends RuntimeException {
    private static final String DEFAULT_MESSAGE = "Book not found.";
    private static final String DEFAULT_MESSAGE_ID = "Book with id '%d' not found.";

    public BookNotFoundException() {
        super(DEFAULT_MESSAGE);
    }

    public BookNotFoundException(Integer id) {
        super(String.format(DEFAULT_MESSAGE_ID, id));
    }

    public BookNotFoundException(Throwable cause) {
        super(DEFAULT_MESSAGE, cause);
    }
}
