package com.AlekseyZakharov.Cloud_Common;

public class DownloadFileRequest extends Request {
    private String path;
    private byte[] data;

    public DownloadFileRequest(String path) {
        this.path = path;
    }

    public DownloadFileRequest(String path, byte[] data) {
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

