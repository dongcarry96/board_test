package com.example.board.board.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Service
public class FileService {

    @Value("${file.upload.dir}")
    private String uploadDir;

    public String saveFile(MultipartFile multipartFile) throws IOException {
        if(multipartFile == null || multipartFile.isEmpty()) {
            return null;
        }

        File dir = new File(uploadDir).getAbsoluteFile();
        if (!dir.exists()) {
            dir.mkdirs();
        }
        String originalName = multipartFile.getOriginalFilename();
        String extension ="";
        if(originalName != null && originalName.contains(".")) {
            extension = originalName.substring(originalName.lastIndexOf("."));
        }

        String savedFileName = UUID.randomUUID() + extension;
        File dest = new File(dir, savedFileName);
        multipartFile.transferTo(dest);

        return originalName + "::" + savedFileName;
    }

    public void deleteFile(String fileRoot) {
        if (fileRoot == null || fileRoot.isBlank()) {
            return;
        }
        String savedFileName = extractSavedFileName(fileRoot);
        File file = new File(uploadDir, savedFileName);
        if (file.exists()) {
            file.delete();
        }
    }

    public String extractOriginalFileName(String fileRoot) {
        if (fileRoot == null || !fileRoot.contains("::")) {
            return fileRoot;
        }
        return fileRoot.split("::")[0];
    }

    public String extractSavedFileName(String fileRoot) {
        if (fileRoot == null || !fileRoot.contains("::")) {
            return fileRoot;
        }
        return fileRoot.split("::")[1];
    }

    public String getUploadDir() {
        return uploadDir;
    }
}
