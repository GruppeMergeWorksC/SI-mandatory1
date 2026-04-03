package com.soapapi.endpoint;

import com.soapapi.library.*;
import com.soapapi.service.PublishingCompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

@Endpoint
public class PublishingCompanyEndpoint {

    private static final String NAMESPACE_URI = "https://soapapi.com/library";

    PublishingCompanyService publishingCompanyService;

    public PublishingCompanyEndpoint(PublishingCompanyService publishingCompanyService) {
        this.publishingCompanyService = publishingCompanyService;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "createPublishingCompanyRequest")
    @ResponsePayload
    public CreatePublishingCompanyResponse createPublishingCompany(@RequestPayload CreatePublishingCompanyRequest request) {
        return publishingCompanyService.createPublishingCompany(request);
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getPublishingCompanyByIdRequest")
    @ResponsePayload
    public GetPublishingCompanyByIdResponse getPublishingCompanyById(@RequestPayload GetPublishingCompanyByIdRequest request) {
        return publishingCompanyService.getPublishingCompanyById(request.getId());

    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "listPublishingCompaniesRequest")
    @ResponsePayload
    public ListPublishingCompaniesResponse listPublishingCompanies(@RequestPayload ListPublishingCompaniesRequest request) {
        return publishingCompanyService.listPublishingCompanies();
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "updatePublishingCompanyRequest")
    @ResponsePayload
    public UpdatePublishingCompanyResponse updatePublishingCompany(@RequestPayload UpdatePublishingCompanyRequest request) {
        return publishingCompanyService.updatePublishingCompany(request);
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "deletePublishingCompanyRequest")
    @ResponsePayload
    public DeletePublishingCompanyResponse  deletePublishingCompany(@RequestPayload DeletePublishingCompanyRequest request) {
        return publishingCompanyService.deletePublishingCompany(request);
    }
}
