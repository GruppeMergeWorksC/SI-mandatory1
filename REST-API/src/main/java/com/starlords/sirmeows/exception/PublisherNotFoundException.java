package com.starlords.sirmeows.exception;

public class PublisherNotFoundException extends RuntimeException {
    private static final String DEFAULT_MESSAGE = "Publisher not found.";
    private static final String DEFAULT_MESSAGE_ID = "Publisher with id '%d' not found.";

    public PublisherNotFoundException() {
        super(DEFAULT_MESSAGE);
    }
    public PublisherNotFoundException(Throwable cause) {
        super(DEFAULT_MESSAGE, cause);
    }
    public PublisherNotFoundException(Integer id) {
        super(String.format(DEFAULT_MESSAGE_ID, id));
    }
}
