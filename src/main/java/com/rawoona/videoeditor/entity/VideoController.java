/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rawoona.videoeditor.entity;

import com.rawoona.videoeditor.entity.util.FileUploader;
import javax.inject.Named;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;

@Named(value = "videoController")
@SessionScoped
public class VideoController implements Serializable {

    private Video video;
    private String fileAppUrl;

    @PostConstruct
    public void init() {
        video = new Video();
    }

    public Video getVideo() {
        return video;
    }

    public String getFileAppUrl() {
        return fileAppUrl;
    }

    public String uploadVideo() {
        if (isVideo()) {
            FileUploader.setUploadedFile(video.getVideo());
            FileUploader.setFilesystemPath("d:/uploads/");
            FileUploader.setUrlPath("uploads/");
            video.setName(FileUploader.getFileName());
            video.setConcatPathName(FileUploader.getFileAppUrl());
            FileUploader.upload();
            return "display?faces-redirect=true";
        }
        return "index?error=true";
    }

    public void printSomething() {
        System.out.println("printSomethingprintSomethingprintSomething!!!" + hidden2);
    }

    private boolean isVideo() {
        return video.getVideo() != null;
    }

    private int hidden2;

    public int getHidden2() {
        return hidden2;
    }

    public void setHidden2(int hidden2) {
        this.hidden2 = hidden2;
    }


}
