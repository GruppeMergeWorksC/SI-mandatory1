package com.soapapi.service;


import com.soapapi.library.*;
import com.soapapi.repository.PublishingCompanyRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PublishingCompanyService {

    private final PublishingCompanyRepository publishingCompanyRepository;

    public PublishingCompanyService(PublishingCompanyRepository publishingCompanyRepository) {
        this.publishingCompanyRepository = publishingCompanyRepository;
    }

    public CreatePublishingCompanyResponse createPublishingCompany(CreatePublishingCompanyRequest request){
        PublishingCompany pubCo = new PublishingCompany();
        pubCo.setName(request.getName());

        Long pubCoId = publishingCompanyRepository.createPublishingCompany(pubCo);

        CreatePublishingCompanyResponse response = new CreatePublishingCompanyResponse();
        response.setPublishingCompanyId(pubCoId);

        return response;
    }

    public GetPublishingCompanyByIdResponse getPublishingCompanyById(Long id) {
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
        response.setSuccess(
                publishingCompanyRepository.updatePublishingCompany(
                        request.getId(),
                        request.getName()
                )
        );

        return response;
    }

    public DeletePublishingCompanyResponse deletePublishingCompany(DeletePublishingCompanyRequest request){
        DeletePublishingCompanyResponse response = new DeletePublishingCompanyResponse();
        response.setSuccess(publishingCompanyRepository.deletePublishingCompany(request.getId()));

        return response;
    }

}
