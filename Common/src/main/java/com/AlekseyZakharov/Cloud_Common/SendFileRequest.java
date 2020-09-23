package com.AlekseyZakharov.Cloud_Common;

import java.nio.file.Path;

public class SendFileRequest extends Request {
    private String path;
    private byte[] data;

    public SendFileRequest(String path, byte[] data) {
        this.path = path;
        this.data = data;
    }

    public String getPath() {
        return path;
    }

    public byte[] getData() {
        return data;
    }
}


