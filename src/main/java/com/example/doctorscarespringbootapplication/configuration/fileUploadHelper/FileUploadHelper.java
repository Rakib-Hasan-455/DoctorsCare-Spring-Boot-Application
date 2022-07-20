package com.example.doctorscarespringbootapplication.configuration.fileUploadHelper;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
@Component
public class FileUploadHelper {
    // uploadDir = "G:\\" -> Static path
    public final String UploadDir = new ClassPathResource("/static/images/userProfileImages").getFile().getAbsolutePath();

    public FileUploadHelper() throws IOException {
    }

    public String uploadImageFile(MultipartFile multipartFile, String imageName) throws IOException {
        System.out.println(UploadDir);
        System.out.println(UploadDir+File.separator+imageName);
        MultipartFile file = multipartFile;
        System.out.println((double) (file.getSize()/(1024.0*1024.0))+" MB");
        System.out.println(file.getContentType());
        System.out.println(file.getName());
        System.out.println(file.getOriginalFilename());
        String uploadFileImgDir = null;
         try {
             Files.copy(multipartFile.getInputStream(), Paths.get(UploadDir+File.separator+imageName), StandardCopyOption.REPLACE_EXISTING);
             uploadFileImgDir = UploadDir+File.separator+imageName;
             return uploadFileImgDir;
         } catch (Exception e) {
             return uploadFileImgDir;
        }
    }
}
