package com.soapapi.endpoint;

import com.soapapi.library.*;
import com.soapapi.service.AuthorService;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

@Endpoint
public class AuthorEndpoint {

    private static final String NAMESPACE_URI = "https://soapapi.com/library";

    private final AuthorService authorService;

    public AuthorEndpoint(AuthorService authorService) {
        this.authorService = authorService;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "createAuthorRequest")
    @ResponsePayload
    public CreateAuthorResponse createAuthor(@RequestPayload CreateAuthorRequest request) {
        return authorService.createAuthor(request);
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getAuthorByIdRequest")
    @ResponsePayload
    public GetAuthorByIdResponse getAuthorById(@RequestPayload GetAuthorByIdRequest request) {
        return authorService.getAuthorById(request);
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "listAuthorsRequest")
    @ResponsePayload
    public ListAuthorsResponse getAllAuthors(@RequestPayload ListAuthorsRequest request) {
        return authorService.getAllAuthors();
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "updateAuthorRequest")
    @ResponsePayload
    public UpdateAuthorResponse updateAuthor(@RequestPayload UpdateAuthorRequest request) {
        return authorService.updateAuthor(request);
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "deleteAuthorRequest")
    @ResponsePayload
    public DeleteAuthorResponse deleteAuthor(@RequestPayload DeleteAuthorRequest request) {
        return authorService.deleteAuthor(request);
    }
}
