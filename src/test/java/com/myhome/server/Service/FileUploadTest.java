package com.myhome.server.Service;

import com.myhome.server.Data.FileResult;
import jdk.jfr.ContentType;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.tomcat.util.http.fileupload.FileItemFactory;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.apache.tomcat.util.http.fileupload.disk.DiskFileItemFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.annotation.Rollback;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.imageio.ImageIO;
import javax.transaction.Transactional;
import java.awt.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Rollback(false)
class FileUploadTest {


    @Autowired
    AWS_S3Upload aws_s3Upload;

    @Test
    public void test() throws IOException {
        FileUpload fileUpload = new FileUpload();
        fileUpload.setPATHNAME("/static/images/");
        File file = new File("E:/download/birdright.jpg");

        FileItem fileItem =  new DiskFileItem("mainFile", Files.probeContentType(file.toPath()), false, file.getName(), (int) file.length(), file.getParentFile());

        try {
            InputStream input = new FileInputStream(file);
            OutputStream os = fileItem.getOutputStream();
            IOUtils.copy(input, os);
        } catch (IOException ex) {
            // do something.
        }

        MultipartFile multipartFile = new CommonsMultipartFile(fileItem);


//        String s = fileUpload.ImageToBase64(multipartFile);

        FileResult upload = aws_s3Upload.uploadTest(file, "/");
        System.out.println("upload = " + upload.getUrl());

    }
}