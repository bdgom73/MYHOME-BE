package com.myhome.server.Data;

import lombok.Data;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Data
public class FileInfo {

    private String MimeType;
    private byte[] Data;
    private String DataUrl;

    public FileInfo(String mimeType, byte[] data) {
        MimeType = mimeType;
        Data = data;
        DataUrl = "data:"+mimeType+";base64,";
    }
}
