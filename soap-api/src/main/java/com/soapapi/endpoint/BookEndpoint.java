package com.soapapi.endpoint;

import com.soapapi.library.*;
import com.soapapi.service.BookService;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

@Endpoint
public class BookEndpoint {

    private static final String NAMESPACE_URI = "https://soapapi.com/library";

    BookService bookService;

    public BookEndpoint(BookService bookService) {
        this.bookService = bookService;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "createBookRequest")
    @ResponsePayload
    public CreateBookResponse createBookRequest(@RequestPayload CreateBookRequest createBookRequest) {
        return bookService.createBook(createBookRequest);
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getBookByIdRequest")
    @ResponsePayload
    public GetBookByIdResponse getBookByIdRequest(@RequestPayload GetBookByIdRequest getBookByIdRequest) {
        return bookService.getBookById(getBookByIdRequest.getId());
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "updateBookRequest")
    @ResponsePayload
    public UpdateBookResponse updateBookRequest(@RequestPayload UpdateBookRequest updateBookRequest) {
        return bookService.updateBook(updateBookRequest);
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "deleteBookRequest")
    @ResponsePayload
    public DeleteBookResponse deleteBookRequest(@RequestPayload DeleteBookRequest deleteBookRequest) {
        return bookService.deleteBook(deleteBookRequest);
    }
}
