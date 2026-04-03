package com.starlords.sirmeows.exception;

public class AuthorNotFoundException extends RuntimeException {
    private static final String DEFAULT_MESSAGE = "Author not found";
    private static final String DEFAULT_MESSAGE_ID = "Author with id '%d' not found.";

    public AuthorNotFoundException(Integer id) {
        super(String.format(DEFAULT_MESSAGE_ID, id));
    }

    public AuthorNotFoundException(Throwable cause) {
        super(DEFAULT_MESSAGE, cause);
    }

    public AuthorNotFoundException() {
        super(DEFAULT_MESSAGE);
    }
}


