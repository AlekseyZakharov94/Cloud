package com.AlekseyZakharov.Cloud_Common;

public class DeleteFileRequest extends Request{
    private String path;

    public DeleteFileRequest(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }
}
