package com.soapapi.service;

import com.soapapi.exception.NotFoundException;
import com.soapapi.exception.ValidationException;
import com.soapapi.library.*;
import com.soapapi.repository.AuthorRepository;
import com.soapapi.repository.BookRepository;
import com.soapapi.repository.PublishingCompanyRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Map;

import static com.soapapi.utils.FieldsValidator.validateFields;

@Service
public class BookService {

    private static final Logger log = LoggerFactory.getLogger(BookService.class);
    BookRepository bookRepository;
    AuthorRepository authorRepository;
    PublishingCompanyRepository publishingCompanyRepository;

    public BookService(BookRepository bookRepository, AuthorRepository authorRepository, PublishingCompanyRepository publishingCompanyRepository) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
        this.publishingCompanyRepository = publishingCompanyRepository;
    }

    public CreateBookResponse createBook(CreateBookRequest createBookRequest) {
        Map<String, Object> requiredFields = Map.of(
                "title", createBookRequest.getTitle(),
                "authorId", createBookRequest.getAuthorId(),
                "publishingYear", createBookRequest.getPublishingYear(),
                "publishingCompanyId", createBookRequest.getPublishingCompanyId()
        );

        validateFields(requiredFields);

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

        if((authorFieldProvided && !authorExists) || (publishingCompanyFieldProvided && !publishingCompanyExists)) {
            throw new NotFoundException("Author or Publishing Company does not exist.");
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
            throw new ValidationException("Error updating book. No fields provided for update.");
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
