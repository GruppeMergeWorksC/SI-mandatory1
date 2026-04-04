package com.soapapi.service;


import com.soapapi.exception.NotFoundException;
import com.soapapi.exception.ValidationException;
import com.soapapi.library.*;
import com.soapapi.repository.BookRepository;
import com.soapapi.repository.PublishingCompanyRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import static com.soapapi.validator.FieldsValidator.validateFields;

@Service
public class PublishingCompanyService {

    private final PublishingCompanyRepository publishingCompanyRepository;
    private final BookRepository bookRepository;

    public PublishingCompanyService(PublishingCompanyRepository publishingCompanyRepository, BookRepository bookRepository) {
        this.publishingCompanyRepository = publishingCompanyRepository;
        this.bookRepository = bookRepository;
    }

    public CreatePublishingCompanyResponse createPublishingCompany(CreatePublishingCompanyRequest request){
        Map<String, Object> requiredFields = Map.of(
                "name", request.getName()
        );

        validateFields(requiredFields);

        PublishingCompany pubCo = new PublishingCompany();
        pubCo.setName(request.getName());

        Long pubCoId = publishingCompanyRepository.createPublishingCompany(pubCo);

        CreatePublishingCompanyResponse response = new CreatePublishingCompanyResponse();
        response.setPublishingCompanyId(pubCoId);

        return response;
    }

    public GetPublishingCompanyByIdResponse getPublishingCompanyById(Long id) {
        if(!publishingCompanyRepository.existsById(id)) {
            throw new NotFoundException("Publishing company with id " + id + " not found.");
        }

        PublishingCompany pubCo = publishingCompanyRepository.getPublishingCompanyById(id);

        GetPublishingCompanyByIdResponse response = new GetPublishingCompanyByIdResponse();
        response.setPublishingCompany(pubCo);

        return response;
    }

    public ListPublishingCompaniesResponse listPublishingCompanies(){
        List<PublishingCompany> pubCos = publishingCompanyRepository.getAllPublishingCompanies();

        ListPublishingCompaniesResponse response = new ListPublishingCompaniesResponse();
        response.getPublishingCompanies().addAll(pubCos);

        return response;
    }

    public UpdatePublishingCompanyResponse updatePublishingCompany(UpdatePublishingCompanyRequest request){
        UpdatePublishingCompanyResponse response = new UpdatePublishingCompanyResponse();
        if(!publishingCompanyRepository.existsById(request.getId())) {
            throw new NotFoundException("Publishing company with id " + request.getId() + " not found.");
        }

        boolean isSuccess = publishingCompanyRepository.updatePublishingCompany(
                request.getId(),
                request.getName()
        );

        response.setSuccess(isSuccess);

        if(!isSuccess){
            throw new ValidationException("Error updating publishing company. No fields provided for update or at least one of the provided text fields is empty");
        }

        return response;
    }

    public DeletePublishingCompanyResponse deletePublishingCompany(DeletePublishingCompanyRequest request){
        if(!publishingCompanyRepository.existsById(request.getId())) {
            throw new NotFoundException("Publishing company with id " + request.getId() + " not found.");
        }
        if(bookRepository.anyDependentOnPublishingCompany(request.getId())) {
            throw new IllegalStateException("Cannot delete publishing company with existing books.");
        }

        DeletePublishingCompanyResponse response = new DeletePublishingCompanyResponse();
        response.setSuccess(publishingCompanyRepository.deletePublishingCompany(request.getId()));

        return response;
    }

}
