package com.scotiabank.challengue.mock;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.scotiabank.challengue.application.dto.CreateStudentRequestDTO;
import com.scotiabank.challengue.application.dto.SearchStudentsRequestDTO;
import com.scotiabank.challengue.application.dto.SearchStudentResponseDTO;

import java.io.IOException;
import java.io.InputStream;

public class StudentTestDataFactory {
    private static final StudentTestDataFactory INSTANCE = new StudentTestDataFactory();
    private final ObjectMapper objectMapper;

    private StudentTestDataFactory() {
        objectMapper = new ObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public static StudentTestDataFactory getInstance() {
        return INSTANCE;
    }

    public CreateStudentRequestDTO getCreateStudentRequestMock() throws IOException {
        return readJson("mock/request/getCreateStudentRequest.json", CreateStudentRequestDTO.class);
    }
    public SearchStudentsRequestDTO getSearchStudentsRequestMock() throws IOException {
        return readJson("mock/request/getSearchStudentsRequest.json", SearchStudentsRequestDTO.class);
    }


    public SearchStudentResponseDTO getSearchStudentsResponseMock() throws IOException {
        return readJson("mock/response/getSearchStudentsResponse.json", SearchStudentResponseDTO.class);
    }


    private <T> T readJson(String path, Class<T> clazz) throws IOException {
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(path)) {
            if (inputStream == null) {
                throw new IOException("File not found: " + path);
            }
            return objectMapper.readValue(inputStream, clazz);
        }
    }
}
