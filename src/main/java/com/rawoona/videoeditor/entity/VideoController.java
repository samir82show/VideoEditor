/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rawoona.videoeditor.entity;

import com.rawoona.utils.OSCommandExecuter;
import com.rawoona.videoeditor.entity.util.FileUploader;
import java.io.IOException;
import javax.inject.Named;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

@Named(value = "videoController")
@SessionScoped
public class VideoController implements Serializable {

    private Video video;
    private String fileAppUrl;
    private int startTime;
    private int endTime;
    private String cuttingOutput;
    private String downloadLink;
    private final String absPath = "/tmp/uploads/";
//    private final String absPath = "d:/uploads/";

    @PostConstruct
    public void init() {
        video = new Video();
    }

    public VideoController() {
//        video = new Video();
    }

    public Video getVideo() {
        return video;
    }

    public void setFileAppUrl(String fileAppUrl) {
        this.fileAppUrl = fileAppUrl;
    }

    public String getFileAppUrl() {
        return fileAppUrl;
    }

    public String uploadVideo() {
        if (isVideo()) {
            fileAppUrl = "testDemoText";
            FileUploader.setUploadedFile(video.getVideo());
            FileUploader.setFilesystemPath(absPath);
            FileUploader.setUrlPath("uploads/");
            video.setName(FileUploader.getFileName());
            video.setConcatPathName(FileUploader.getFileAppUrl());
            FileUploader.upload();
            System.out.println("uploadVideo video.getConcatPathName(): " + video.getConcatPathName());
            return "display?faces-redirect=true";
        }
        System.out.println("uploadVideo is not video: " + video.getConcatPathName());
        return "index";
    }

    private boolean isVideo() {
        return video.getVideo() != null;
    }

    private long getEpocTime() throws ParseException {
        Date today = Calendar.getInstance().getTime();
        SimpleDateFormat crunchifyFormat = new SimpleDateFormat("MMM dd yyyy HH:mm:ss.SSS zzz");
        String currentTime = crunchifyFormat.format(today);
        Date date = crunchifyFormat.parse(currentTime);
        long epochTime = date.getTime();
        return epochTime;
    }

    public String generateRandomName() throws ParseException {
        String nonSpacedName = removeNameSpaces();
        long epochTime = getEpocTime();
        return String.format("%s", epochTime) + nonSpacedName;
    }

    private String removeNameSpaces() {
        return video.getName().replaceAll("\\s+", "");
    }

    public String getCuttingOutput() {
        return cuttingOutput;
    }

    public String getDownloadLink() {
        return downloadLink;
    }

    public String runCommand() throws IOException, ParseException {
        getHiddenParams();
        if (endTime > startTime) {
            String outputfile = absPath + generateRandomName();
            downloadLink = outputfile.replace("/tmp/", "");
            String videoPath = absPath + video.getName();
            String startTimeString = convertSecondsToTime(startTime);
            String endTimeString = convertSecondsToTime(endTime);
            List<String> cmdList = new ArrayList<>();
            cmdList.add("ffmpeg");
            cmdList.add("-i");
            cmdList.add(videoPath);
            cmdList.add("-c:av");
            cmdList.add("copy");
            cmdList.add("-ss");
            cmdList.add(startTimeString);
            cmdList.add("-to");
            cmdList.add(endTimeString);
            cmdList.add(outputfile);
            OSCommandExecuter.setOSCommand(cmdList);
            OSCommandExecuter.RunOSCommand();
            cuttingOutput = OSCommandExecuter.getOutput();
            System.out.println("output: " + cuttingOutput);
            if (OSCommandExecuter.getExitCode() == 0) {
                return "download?faces-redirect=true";
            }
        } else {
            cuttingOutput = String.format("End time [%s] should be greater than start time [%s]", endTime, startTime);
            return "error-page?faces-redirect=true";
        }
        return null;
    }

    private String convertSecondsToTime(int totalSeconds) {
        int seconds = 0;
        int minutes = 0;
        int hours = 0;
        int hours_remainder = 0;
        hours = totalSeconds / 3600;
        hours_remainder = totalSeconds % 3600;
        if (hours_remainder < 60) {
            seconds = hours_remainder % 60;
        } else if (hours_remainder > 60) {
            minutes = hours_remainder / 60;
            seconds = hours_remainder % 60;
        } else {
            minutes = hours_remainder / 60;
        }

        return String.format("%02d:%02d:%02d", hours, minutes, seconds);

    }

    private void getHiddenParams() {
        try {
            fetchStartEndTimes();
        } catch (Exception e) {
            showException(e);
        }
    }

    private void fetchStartEndTimes() {
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        startTime = Integer.parseInt(ec.getRequestParameterMap().get("hiddenstartTtime"));
        endTime = Integer.parseInt(ec.getRequestParameterMap().get("hiddenendTime"));
    }

    private void showException(Exception e) {
        System.out.println(e.getMessage());
    }

}
