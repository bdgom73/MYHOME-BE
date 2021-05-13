package com.myhome.server.Service;

import lombok.NoArgsConstructor;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.HashMap;

@NoArgsConstructor
public class FileUpload {

    private final String CLASSPATH = "E:/study/weather/client/public/static";
    private String PATHNAME = "/board/image/";
    private final String ABSOLUTE_PATH = CLASSPATH+PATHNAME;

    public HashMap<String,String> Save(MultipartFile file){
        String originalFileName = file.getOriginalFilename();
        String safe_pathname = (ABSOLUTE_PATH + System.currentTimeMillis() + originalFileName).trim();
        String save_pathname = (PATHNAME + System.currentTimeMillis() + originalFileName).trim();
        try {
            file.transferTo(new File(safe_pathname));
            HashMap<String,String> value = new HashMap<>();
            value.put("ABSOLUTE_PATH",safe_pathname);
            value.put("PATH",save_pathname);
            return value;
        }catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public byte[] fetch(String url){
        Image image = new ImageIcon(url).getImage();

        BufferedImage bi = new BufferedImage(image.getWidth(null),image.getHeight(null),BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = bi.createGraphics();
        g2d.drawImage(image,0,0,null);
        g2d.dispose();
        ByteArrayOutputStream baos = null;
        try{
            baos = new ByteArrayOutputStream();
            ImageIO.write(bi,"png",baos);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try{
                baos.close();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        byte[] encodeImg = Base64.encodeBase64(bais.readAllBytes());
        return encodeImg;
    }

    public void setPATHNAME(String PATHNAME) {
        this.PATHNAME = PATHNAME;
    }
}
