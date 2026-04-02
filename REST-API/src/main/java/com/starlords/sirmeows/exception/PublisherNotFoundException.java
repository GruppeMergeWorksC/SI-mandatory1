package com.starlords.sirmeows.exception;

public class PublisherNotFoundException extends RuntimeException {
    private static final String DEFAULT_MESSAGE = "Publisher not found.";

    public PublisherNotFoundException() {
        super(DEFAULT_MESSAGE);
    }
    public PublisherNotFoundException(Throwable cause) {
        super(DEFAULT_MESSAGE, cause);
    }

}
