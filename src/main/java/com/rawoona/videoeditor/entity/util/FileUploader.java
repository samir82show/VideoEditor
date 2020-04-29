package com.rawoona.videoeditor.entity.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import javax.servlet.http.Part;

public class FileUploader {

    private static Part uploadedFile;
    private static String urlPath;
    private static String filesystemPath;

    public static void upload() {
        String fileName = getFileName();
        try {
            InputStream inputStream = uploadedFile.getInputStream();
            File absolutePathFile = new File(getFilesystemPath() + fileName);
            FileOutputStream fileOutputStream = new FileOutputStream(absolutePathFile);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) > 0) {
                fileOutputStream.write(buffer, 0, length);
            }
            fileOutputStream.close();
            inputStream.close();
        } catch (Exception e) {
            e.getStackTrace();
        }
    }

    public static void setUrlPath(String urlPath) {
        FileUploader.urlPath = urlPath;
    }

    public static String getUrlPath() {
        return FileUploader.urlPath;
    }

    public static void setFilesystemPath(String filesystemPath) {
        FileUploader.filesystemPath = filesystemPath;
    }

    public static String getFilesystemPath() {
        return FileUploader.filesystemPath;
    }

    public static String getFileName() {
        if (!uploadedFile.getSubmittedFileName().equals("")) {
            return uploadedFile.getSubmittedFileName();
        }
        return null;
    }

    public static String getFileAppUrl() {
        return urlPath + getFileName();
    }

    public static void setUploadedFile(Part uploadedFile) {
        FileUploader.uploadedFile = uploadedFile;
    }

    public static Part getUploadedFile() {
        return FileUploader.uploadedFile;
    }

}
