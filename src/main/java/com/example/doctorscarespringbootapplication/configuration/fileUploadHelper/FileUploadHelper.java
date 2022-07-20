package com.example.doctorscarespringbootapplication.configuration.fileUploadHelper;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
@Component
public class FileUploadHelper {
    private String uploadDir = "upload";

    public FileUploadHelper() throws IOException {
        var uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectory(uploadPath);
        }
    }

    public String uploadImageFile(MultipartFile multipartFile, String imageName) throws IOException {
        MultipartFile file = multipartFile;
        System.out.println((double) (file.getSize()/(1024.0*1024.0))+" MB");
        System.out.println(file.getContentType());
        System.out.println(file.getName());
        System.out.println(file.getOriginalFilename());
        String uploadFileImgDir = null;
         try {
             Files.copy(multipartFile.getInputStream(), Paths.get(uploadDir+File.separator+imageName), StandardCopyOption.REPLACE_EXISTING);
             uploadFileImgDir = uploadDir+File.separator+imageName;
             return uploadFileImgDir;
         } catch (Exception e) {
             return uploadFileImgDir;
        }
    }
}
