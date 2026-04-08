package com.starlords.sirmeows.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class AuthorAlreadyExistsException extends RuntimeException {
  private static final String DEFAULT_MESSAGE = "Author already exists";
  private static final String NAME_MESSAGE = "Author with name '%s' already exists";
  private static final String NAME_SURNAME_MESSAGE = "Author with name '%s' and surname '%s' already exists";

    public AuthorAlreadyExistsException() {
        super(DEFAULT_MESSAGE);
    }

    public AuthorAlreadyExistsException(Throwable cause) {
      super(DEFAULT_MESSAGE, cause);
    }
    public AuthorAlreadyExistsException(String name) {
      super(String.format(NAME_MESSAGE, name));
    }

  public AuthorAlreadyExistsException(String name, String surname) {
    super(String.format(NAME_SURNAME_MESSAGE, name, surname));
  }
}
