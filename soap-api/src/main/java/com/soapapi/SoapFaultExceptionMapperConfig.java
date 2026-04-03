package com.soapapi;

import com.soapapi.exception.ConflictException;
import com.soapapi.exception.NotFoundException;
import com.soapapi.exception.ValidationException;
import com.soapapi.library.ConflictFault;
import com.soapapi.library.NotFoundFault;
import com.soapapi.library.ValidationFault;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBElement;
import jakarta.xml.bind.Marshaller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.ws.soap.SoapFault;
import org.springframework.ws.soap.SoapFaultDetail;
import org.springframework.ws.soap.server.endpoint.SoapFaultMappingExceptionResolver;
import org.springframework.ws.soap.server.endpoint.SoapFaultDefinition;

import javax.xml.namespace.QName;
import java.util.Properties;

@Configuration
public class SoapFaultExceptionMapperConfig {

    public static final String NS = "https://soapapi.com/library";
    private static final Logger log = LoggerFactory.getLogger(SoapFaultExceptionMapperConfig.class);

    @Bean
    public SoapFaultMappingExceptionResolver soapFaultMappingExceptionResolver() {
        SoapFaultMappingExceptionResolver resolver = new SoapFaultMappingExceptionResolver() {
            @Override
            protected void customizeFault(Object endpoint, Exception ex, SoapFault fault) {
                try {
                    SoapFaultDetail detail = fault.addFaultDetail();

                    JAXBContext ctx = JAXBContext.newInstance(
                            ValidationFault.class, NotFoundFault.class, ConflictFault.class
                    );
                    Marshaller marshaller = ctx.createMarshaller();

                    if (ex instanceof ValidationException) {
                        ValidationFault vf = new ValidationFault();
                        vf.setMessage(ex.getMessage());

                        JAXBElement<ValidationFault> el =
                                new JAXBElement<>(new QName(NS, "ValidationFault"), ValidationFault.class, vf);

                        marshaller.marshal(el, detail.getResult());

                    } else if (ex instanceof NotFoundException) {
                        NotFoundFault nf = new NotFoundFault();
                        nf.setMessage(ex.getMessage());

                        JAXBElement<NotFoundFault> el =
                                new JAXBElement<>(new QName(NS, "NotFoundFault"), NotFoundFault.class, nf);

                        marshaller.marshal(el, detail.getResult());

                    } else if (ex instanceof ConflictException) {
                        ConflictFault cf = new ConflictFault();
                        cf.setMessage(ex.getMessage());

                        JAXBElement<ConflictFault> el =
                                new JAXBElement<>(new QName(NS, "ConflictFault"), ConflictFault.class, cf);

                        marshaller.marshal(el, detail.getResult());
                    }
                } catch (Exception marshallingProblem) {
                   log.error("Error marshalling SOAP fault detail", marshallingProblem);
                }
            }
        };

        Properties mappings = new Properties();
        mappings.setProperty(ValidationException.class.getName(), SoapFaultDefinition.CLIENT.toString());
        mappings.setProperty(NotFoundException.class.getName(), SoapFaultDefinition.CLIENT.toString());
        mappings.setProperty(ConflictException.class.getName(), SoapFaultDefinition.CLIENT.toString());
        resolver.setExceptionMappings(mappings);

        SoapFaultDefinition defaultDef = new SoapFaultDefinition();
        defaultDef.setFaultCode(SoapFaultDefinition.SERVER);
        resolver.setDefaultFault(defaultDef);

        resolver.setOrder(1);
        return resolver;
    }
}