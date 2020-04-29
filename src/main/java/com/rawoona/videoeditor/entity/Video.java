package com.rawoona.videoeditor.entity;

import java.io.Serializable;
import javax.servlet.http.Part;

public class Video implements Serializable {

    private Part video;
    private String url;
    private String name;
    private int startTime;
    private int endTime;
    private String concatFilePath;

    public Part getVideo() {
        return video;
    }

    public int getStartTime() {
        return startTime;
    }

    public void setStartTime(int startTime) {
        this.startTime = startTime;
        System.out.println("setStartTime: " + startTime);
    }

    public int getEndTime() {
        return endTime;
    }

    public void setEndTime(int endTime) {
        this.endTime = endTime;
    }

    public void setVideo(Part video) {
        this.video = video;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setConcatPathName(String concatFilePath) {
        this.concatFilePath = concatFilePath;
    }

    public String getConcatPathName() {
        return concatFilePath;
    }

    public Video() {
    }

    @Override
    public String toString() {
        return "Video{" + "video=" + video + ", url=" + url + ", name=" + name + '}';
    }

}
