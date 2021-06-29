package com.myhome.server.Data;

import lombok.Data;

@Data
public class FileResult {

    private String url;
    private String Filename;

    public FileResult(String url, String filename) {
        this.url = url;
        Filename = filename;
    }
}
