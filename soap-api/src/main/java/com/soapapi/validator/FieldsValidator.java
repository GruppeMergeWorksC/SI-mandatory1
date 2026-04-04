package com.soapapi.validator;

import com.soapapi.exception.ValidationException;

import java.util.List;
import java.util.Map;

public class FieldsValidator {

    private FieldsValidator() {
    }

    public static void validateFields(Map<String, Object> fields) {
        List<String> missingFields = fields.entrySet().stream()
                .filter(entry -> entry.getValue() == null || entry.getValue().toString().trim().isEmpty())
                .map(Map.Entry::getKey)
                .toList();
    if(!missingFields.isEmpty()){
            throw new ValidationException("Missing or empty required fields: " + String.join(", ", missingFields));
        }
    }
}
