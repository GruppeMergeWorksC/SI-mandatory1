package com.soapapi;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.ws.wsdl.wsdl11.DefaultWsdl11Definition;
import org.springframework.xml.xsd.SimpleXsdSchema;

@Configuration(proxyBeanMethods = false)
public class WebServiceConfig {

    @Bean
    public DefaultWsdl11Definition libraryWsdl(SimpleXsdSchema libraryWs) {
        DefaultWsdl11Definition wsdl11Definition = new DefaultWsdl11Definition();
        wsdl11Definition.setPortTypeName("LibraryPort");
        wsdl11Definition.setLocationUri("/services");
        wsdl11Definition.setTargetNamespace("https://soapapi.com/library");
        wsdl11Definition.setSchema(libraryWs);
        return wsdl11Definition;
    }

}