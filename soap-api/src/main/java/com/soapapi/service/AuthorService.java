package com.soapapi.service;

import com.soapapi.exception.ConflictException;
import com.soapapi.exception.NotFoundException;
import com.soapapi.exception.ValidationException;
import com.soapapi.library.*;
import com.soapapi.repository.AuthorRepository;
import com.soapapi.repository.BookRepository;
import org.springframework.stereotype.Service;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.soapapi.validator.FieldsValidator.validateFields;

@Service
public class AuthorService {

    private final AuthorRepository authorRepository;
    private final BookRepository bookRepository;

    public AuthorService(AuthorRepository authorRepository, BookRepository bookRepository) {
        this.authorRepository = authorRepository;
        this.bookRepository = bookRepository;
    }

    public CreateAuthorResponse createAuthor(CreateAuthorRequest request) {
        Map<String, Object> requiredFields = new LinkedHashMap<>();
        requiredFields.put("name", request.getName());
        requiredFields.put("surname", request.getSurname());

        validateFields(requiredFields);

        Author author = new Author();
        author.setName(request.getName());
        author.setSurname(request.getSurname());

        Long authorId = authorRepository.createAuthor(author);

        CreateAuthorResponse response = new CreateAuthorResponse();
        response.setAuthorId(authorId);

        return response;
    }

    public GetAuthorByIdResponse getAuthorById(GetAuthorByIdRequest request) {
        if(!authorRepository.existsById(request.getId())) {
            throw new NotFoundException("Author with id " + request.getId() + " not found.");
        }

        Author author = authorRepository.getAuthorById(request.getId());

        GetAuthorByIdResponse response = new GetAuthorByIdResponse();
        response.setAuthor(author);

        return response;
    }

    public ListAuthorsResponse listAllAuthors() {
        List<Author> authors = authorRepository.getAllAuthors();

        ListAuthorsResponse response = new ListAuthorsResponse();
        response.getAuthors().addAll(authors);

        return response;
    }

    public UpdateAuthorResponse updateAuthor(UpdateAuthorRequest request) {

        if(!authorRepository.existsById(request.getId())) {
            throw new NotFoundException("Author with id " + request.getId() + " not found.");
        }

        boolean isSuccess = authorRepository.updateAuthor(
                request.getId(),
                request.getName(),
                request.getSurname()
        );

        UpdateAuthorResponse response = new UpdateAuthorResponse();
        response.setSuccess(isSuccess);

        if (!isSuccess) {
            throw new ValidationException("Error updating author. No fields provided for update or at least one of the provided text fields is empty");
        }

        return response;
    }

    public DeleteAuthorResponse deleteAuthor(DeleteAuthorRequest request) {
        if(!authorRepository.existsById(request.getId())) {
            throw new NotFoundException("Author with id " + request.getId() + " not found.");
        }
        if(bookRepository.anyDependentOnAuthor(request.getId())) {
            throw new ConflictException("Cannot delete author with existing books.");
        }

        DeleteAuthorResponse response = new DeleteAuthorResponse();
        response.setSuccess(authorRepository.deleteAuthor(request.getId()));

        return response;
    }
}
