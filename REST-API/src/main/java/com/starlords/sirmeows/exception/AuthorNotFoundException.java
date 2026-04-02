package com.starlords.sirmeows.exception;

public class AuthorNotFoundException extends RuntimeException {
    private static final String DEFAULT_MESSAGE = "Author not found.";

    public AuthorNotFoundException() {
        super(DEFAULT_MESSAGE);
    }

    public AuthorNotFoundException(Throwable cause) {
        super(DEFAULT_MESSAGE, cause);
    }
}


