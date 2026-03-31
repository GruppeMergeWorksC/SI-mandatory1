package com.soapapi.service;

import com.soapapi.library.*;
import com.soapapi.repository.AuthorRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthorService {

    private final AuthorRepository authorRepository;

    public AuthorService(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    public CreateAuthorResponse createAuthor(CreateAuthorRequest request) {
        Author author = new Author();
        author.setName(request.getName());
        author.setSurname(request.getSurname());

        Long authorId = authorRepository.createAuthor(author);

        CreateAuthorResponse response = new CreateAuthorResponse();
        response.setAuthorId(authorId);

        return response;
    }

    public GetAuthorByIdResponse getAuthorById(GetAuthorByIdRequest request) {
        Author author = authorRepository.getAuthorById(request.getId());

        GetAuthorByIdResponse response = new GetAuthorByIdResponse();
        response.setAuthor(author);

        return response;
    }

    public ListAuthorsResponse getAllAuthors() {
        List<Author> authors = authorRepository.getAllAuthors();

        ListAuthorsResponse response = new ListAuthorsResponse();
        response.getAuthors().addAll(authors);

        return response;
    }

    public UpdateAuthorResponse updateAuthor(UpdateAuthorRequest request) {
        UpdateAuthorResponse response = new UpdateAuthorResponse();
        response.setSuccess(authorRepository.updateAuthor(
                request.getId(),
                request.getName(),
                request.getSurname()
        ));

        return response;
    }

    public DeleteAuthorResponse deleteAuthor(DeleteAuthorRequest request) {
        DeleteAuthorResponse response = new DeleteAuthorResponse();
        response.setSuccess(authorRepository.deleteAuthor(request.getId()));

        return response;
    }
}
