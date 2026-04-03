package com.starlords.sirmeows.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class PublisherAlreadyExistsException extends RuntimeException {
    private static final String DEFAULT_MESSAGE = "Publisher already exists";
    private static final String NAME_MESSAGE = "Publisher with name '%s' already exists";

    public PublisherAlreadyExistsException() {
        super(DEFAULT_MESSAGE);
    }

    public PublisherAlreadyExistsException(Throwable cause) {
        super(DEFAULT_MESSAGE, cause);
    }

    public PublisherAlreadyExistsException(String name) {
        super(String.format(NAME_MESSAGE, name));
    }
}
