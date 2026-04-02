package com.starlords.sirmeows.exception;

public class BookNotFoundException extends RuntimeException {
    private static final String DEFAULT_MESSAGE = "Book not found.";

    public BookNotFoundException() {
        super(DEFAULT_MESSAGE);
    }

    public BookNotFoundException(Throwable cause) {
        super(DEFAULT_MESSAGE, cause);
    }
}
