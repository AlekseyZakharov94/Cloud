package com.AlekseyZakharov.Cloud_Common;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

public class FileInfo {
    public enum FileType {
        DIRECTORY("D"), FILE("F");
        private String name;

        public String getName() {
            return name;
        }

        FileType(String name) {
            this.name = name;
        }
    }

    private String fileName;
    private FileType type;
    private long size;
    private LocalDateTime lastModified;

    public String getFileName() {
        return fileName;
    }

    public FileType getType() {
        return type;
    }

    public long getSize() {
        return size;
    }

    public LocalDateTime getLastModified() {
        return lastModified;
    }

    public FileInfo(Path path) {
            this.fileName = path.getFileName().toString();
            try {
                this.size = Files.size(path);
                this.type = Files.isDirectory(path) ? FileType.DIRECTORY : FileType.FILE;
                if (this.type == FileType.DIRECTORY) {
                    this.size = -1L;
                }
                this.lastModified = LocalDateTime.ofInstant(Files.getLastModifiedTime(path).toInstant(),
                        ZoneOffset.ofHours(3));
            } catch (IOException e) {
                e.printStackTrace();
//                throw new RuntimeException("Damaged file");
            }
    }
}
