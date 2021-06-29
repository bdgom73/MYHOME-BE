package com.myhome.server.Service;

import lombok.NoArgsConstructor;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.*;

@NoArgsConstructor
public class FileUpload {

    private String CLASSPATH = "E:/study/weather/client/public";
    private String PATHNAME = "/static/board/image/";
//    String ABSOLUTE_PATH = CLASSPATH+PATHNAME;

    public Map<String,String> Save(MultipartFile file){
        String rs = RandomString();
        long date = System.currentTimeMillis();
        String originalFileName = file.getOriginalFilename();
        String ext = originalFileName.split("[.]")[1];
        String safe_pathname = (CLASSPATH+PATHNAME +date+ rs+"."+ext).trim();
        String save_pathname = (PATHNAME+date + rs+"."+ext).trim();
        try {
            file.transferTo(new File(safe_pathname));
            Map<String,String> value = new HashMap<>();
            value.put("ABSOLUTE_PATH",safe_pathname);
            value.put("PATH",save_pathname);
            value.put("FILENAME",date+ rs+"."+ext);

            return value;
        }catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public File multipartToFile(MultipartFile multipartFile) throws IOException {
        File file = new File(multipartFile.getOriginalFilename());
        multipartFile.transferTo(file);
        return file;
    }
    public String ImageToBase64(MultipartFile multipartFile) throws IOException {

        String originalFilename = multipartFile.getOriginalFilename();
        String ext = originalFilename.split("[.]")[1];
        ByteArrayOutputStream baos = null;
        InputStream inputStream = null;
        ByteArrayInputStream  bais = null;
        try {
            String url = "data:image/"+ext+";base64, ";
            baos = new ByteArrayOutputStream();
            byte[] bytes = Base64.encodeBase64(multipartFile.getBytes(),false);
            inputStream = new ByteArrayInputStream(bytes);
            int len = 0 ;
            while((len = inputStream.read(bytes)) != -1) {
                baos.write(bytes, 0, len);
            }
           return url+baos.toString();
        } catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }

    public String RandomString(){
        StringBuffer temp = new StringBuffer();
        Random rnd = new Random();
        for (int i = 0; i < 20; i++) {
            int rIndex = rnd.nextInt(3);
            switch (rIndex) {
                case 0:
                    // a-z
                    temp.append((char) ((int) (rnd.nextInt(26)) + 97));
                    break;
                case 1:
                    // A-Z
                    temp.append((char) ((int) (rnd.nextInt(26)) + 65));
                    break;
                case 2:
                    // 0-9
                    temp.append((rnd.nextInt(10)));
                    break;
            }
        }
        return temp.toString();

    }
    public String RandomString(int len){
        StringBuffer temp = new StringBuffer();
        Random rnd = new Random();
        for (int i = 0; i < len; i++) {
            int rIndex = rnd.nextInt(3);
            switch (rIndex) {
                case 0:
                    // a-z
                    temp.append((char) ((int) (rnd.nextInt(26)) + 97));
                    break;
                case 1:
                    // A-Z
                    temp.append((char) ((int) (rnd.nextInt(26)) + 65));
                    break;
                case 2:
                    // 0-9
                    temp.append((rnd.nextInt(10)));
                    break;
            }
        }
        return temp.toString();

    }
    public String RandomNumberString(int len){
        StringBuffer temp = new StringBuffer();
        Random rnd = new Random();
        for (int i = 0; i < len; i++) {
            temp.append((rnd.nextInt(10)));
        }
        return temp.toString();
    }
    public String RandomNumberString(){
        StringBuffer temp = new StringBuffer();
        Random rnd = new Random();
        for (int i = 0; i < 20; i++) {
            temp.append((rnd.nextInt(10)));
        }
        return temp.toString();

    }
    public void SizeSave(MultipartFile file, String docName){
        String[] filenameList = new String[5];
        filenameList[0] = "default";
        filenameList[1] = "480x360";
        filenameList[2] = "120x90";
        filenameList[3] = "1280x720";
        filenameList[4] = "1920x1080";
        String originalFileName = file.getOriginalFilename();
        String ext = originalFileName.split("[.]")[1];
        String safe_pathname = CLASSPATH+PATHNAME+docName+"/default."+ext;
        try {
            String path = CLASSPATH+PATHNAME+docName;
            File Folder = new File(path);
            if (!Folder.exists()) { Folder.mkdirs(); }

            for (String s : filenameList) {
                safe_pathname = CLASSPATH+PATHNAME+docName+"/"+s+"."+ext;
                if(s == "default"){
                    file.transferTo(new File(safe_pathname));
                }else{
                    String[] ss = s.split("x");
                    ImageResize(file,safe_pathname, Integer.parseInt(ss[0]) , Integer.parseInt(ss[1]));
                }

            }

        }catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void ImageResize(MultipartFile file , String filename, int width, int height) throws IOException {
        // 원본 이미지 가져오기
        File file1 = new File(file.getOriginalFilename());
        file1.createNewFile();
        FileOutputStream fos = new FileOutputStream(file1);
        fos.write(file.getBytes());
        fos.close();
        Image image = ImageIO.read(file1);

        // 이미지 리사이즈
        // Image.SCALE_DEFAULT : 기본 이미지 스케일링 알고리즘 사용
        // Image.SCALE_FAST    : 이미지 부드러움보다 속도 우선
        // Image.SCALE_REPLICATE : ReplicateScaleFilter 클래스로 구체화 된 이미지 크기 조절 알고리즘
        // Image.SCALE_SMOOTH  : 속도보다 이미지 부드러움을 우선
        // Image.SCALE_AREA_AVERAGING  : 평균 알고리즘 사용

        // 새 이미지  저장하기
        Image resizeImage = null;
        BufferedImage newImage = null;
        if(width == 0 && height == 0){
            resizeImage = image.getScaledInstance(image.getWidth(null), image.getHeight(null), Image.SCALE_AREA_AVERAGING);
            newImage = new BufferedImage(resizeImage.getWidth(null), resizeImage.getHeight(null), BufferedImage.TYPE_INT_RGB);
        }else{
            resizeImage = image.getScaledInstance(width, height, Image.SCALE_AREA_AVERAGING);
            newImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        }

        Graphics g = newImage.getGraphics();
        g.drawImage(resizeImage, 0, 0, null);
        g.dispose();
        String[] split = file.getOriginalFilename().split("[.]");

        ImageIO.write(newImage, split[1], new File(filename));
    }
    public void setPATHNAME(String PATHNAME) {
        this.PATHNAME = PATHNAME;
    }
}
