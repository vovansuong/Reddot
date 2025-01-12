package com.springboot.app.accounts.service;

import com.springboot.app.dto.response.ServiceResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.stream.Stream;

@Component
public interface StorageService {
    ServiceResponse<String> storeFile(MultipartFile file, String preFilename);

    Stream<Path> loadAll();

    byte[] readFileContent(String filename);

    boolean deleteFile(String filename);

    void deleteAll();
}
