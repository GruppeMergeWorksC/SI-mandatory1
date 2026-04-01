package com.soapapi.service;

import com.soapapi.library.*;
import com.soapapi.repository.AuthorRepository;
import com.soapapi.repository.BookRepository;
import com.soapapi.repository.PublishingCompanyRepository;

public class BookService {

    BookRepository bookRepository;
    AuthorRepository authorRepository;
    PublishingCompanyRepository publishingCompanyRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public CreateBookResponse createBook(CreateBookRequest createBookRequest) {
        boolean authorExists = authorRepository.existsById(createBookRequest.getAuthorId());
        boolean publishingCompanyExists = publishingCompanyRepository.existsById(createBookRequest.getPublishingCompanyId());

        CreateBookResponse response = new CreateBookResponse();

        if(authorExists && publishingCompanyExists){
            Book book = new Book();
            book.setTitle(createBookRequest.getTitle());
            book.setAuthorId(createBookRequest.getAuthorId());
            book.setPublishingYear(createBookRequest.getPublishingYear());
            book.setPublishingCompanyId(createBookRequest.getPublishingCompanyId());

            Long bookId = bookRepository.createBook(book);


            response.setBookId(bookId);
        } else {
            throw new IllegalArgumentException("Author or Publishing Company does not exist.");
        }

        return response;
    }

    public GetBookByIdResponse getBookById(Long bookId) {
        Book book = bookRepository.getBookById(bookId);

        GetBookByIdResponse response = new GetBookByIdResponse();
        response.setBook(book);

        return response;
    }

    public UpdateBookResponse updateBook(UpdateBookRequest request) {
        boolean authorExists = authorRepository.existsById(request.getAuthorId());
        boolean publishingCompanyExists = publishingCompanyRepository.existsById(request.getPublishingCompanyId());

        UpdateBookResponse response = new UpdateBookResponse();

        if(authorExists && publishingCompanyExists){
           response.setSuccess(bookRepository.updateBook(
                    request.getId(),
                    request.getTitle(),
                    request.getAuthorId(),
                    request.getPublishingYear(),
                    request.getPublishingCompanyId()
            ));
        } else {
            throw new IllegalArgumentException("Author or Publishing Company does not exist.");
        }

        return response;
    }

    public DeleteBookResponse deleteBook(Long bookId) {
        DeleteBookResponse response = new DeleteBookResponse();
        response.setSuccess(bookRepository.deleteBook(bookId));

        return response;
    }


}
