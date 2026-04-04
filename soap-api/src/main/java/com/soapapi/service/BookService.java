package com.soapapi.service;

import com.soapapi.exception.NotFoundException;
import com.soapapi.exception.ValidationException;
import com.soapapi.library.*;
import com.soapapi.repository.AuthorRepository;
import com.soapapi.repository.BookRepository;
import com.soapapi.repository.PublishingCompanyRepository;
import org.springframework.stereotype.Service;

import java.util.Map;

import static com.soapapi.validator.FieldsValidator.validateFields;

@Service
public class BookService {

    BookRepository bookRepository;
    AuthorRepository authorRepository;
    PublishingCompanyRepository publishingCompanyRepository;

    public BookService(BookRepository bookRepository, AuthorRepository authorRepository, PublishingCompanyRepository publishingCompanyRepository) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
        this.publishingCompanyRepository = publishingCompanyRepository;
    }

    public CreateBookResponse createBook(CreateBookRequest request) {
        Map<String, Object> requiredFields = Map.of(
                "title", request.getTitle(),
                "authorId", request.getAuthorId(),
                "publishingYear", request.getPublishingYear(),
                "publishingCompanyId", request.getPublishingCompanyId()
        );

        validateFields(requiredFields);

        boolean authorExists = authorRepository.existsById(request.getAuthorId());
        boolean publishingCompanyExists = publishingCompanyRepository.existsById(request.getPublishingCompanyId());

        CreateBookResponse response = new CreateBookResponse();

        if(authorExists && publishingCompanyExists){
            Book book = new Book();
            book.setTitle(request.getTitle());
            book.setAuthorId(request.getAuthorId());
            book.setPublishingYear(request.getPublishingYear());
            book.setPublishingCompanyId(request.getPublishingCompanyId());

            if(request.getPublishingYear() < 1900){
                throw new ValidationException("Publishing year must be 1900 or above");
            }

            Long bookId = bookRepository.createBook(book);

            response.setBookId(bookId);
        } else {
            throw new NotFoundException("Author or Publishing Company does not exist.");
        }

        return response;
    }

    public GetBookByIdResponse getBookById(Long bookId) {
       if(!bookRepository.existsById(bookId)) {
           throw new NotFoundException("Book with id " + bookId + " not found.");
       }

        Book book = bookRepository.getBookById(bookId);

        GetBookByIdResponse response = new GetBookByIdResponse();
        response.setBook(book);

        return response;
    }

    public UpdateBookResponse updateBook(UpdateBookRequest request) {
        if(!bookRepository.existsById(request.getId())) {
            throw new NotFoundException("Book with id " + request.getId() + " not found.");
        }

        boolean authorFieldProvided = request.getAuthorId() != null;
        boolean publishingCompanyFieldProvided = request.getPublishingCompanyId() != null;
        boolean authorExists = authorRepository.existsById(request.getAuthorId());
        boolean publishingCompanyExists = publishingCompanyRepository.existsById(request.getPublishingCompanyId());

        UpdateBookResponse response = new UpdateBookResponse();

        if((authorFieldProvided && !authorExists) ||
                (publishingCompanyFieldProvided && !publishingCompanyExists)) {
            throw new NotFoundException("Author or Publishing Company does not exist.");
        }

        if(request.getPublishingYear() < 1900){
            throw new ValidationException("Publishing year must be 1900 or above");
        }

        boolean isSuccess = bookRepository.updateBook(
                request.getId(),
                request.getTitle(),
                request.getAuthorId(),
                request.getPublishingYear(),
                request.getPublishingCompanyId()
        );

        response.setSuccess(isSuccess);

        if(!isSuccess) {
            throw new ValidationException("Error updating book. No fields provided for update or at least one of the provided text fields is empty");
        }

        return response;
    }

    public DeleteBookResponse deleteBook(DeleteBookRequest request) {
        if(!bookRepository.existsById(request.getId())) {
            throw new NotFoundException("Book with id " + request.getId() + " not found.");
        }
        DeleteBookResponse response = new DeleteBookResponse();
        response.setSuccess(bookRepository.deleteBook(request.getId()));

        return response;
    }


}
