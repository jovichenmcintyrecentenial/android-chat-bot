package com.zv.geochat.model;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class ChatMessage {
    private String id;
    private String userName;
    private String body;
    private int datetime;

    public int getDatetime() {
        return datetime;
    }

    public String getFormattedDate(){
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/YYYY");

        Date currentDate = new Date(datetime*1000L);
        //get phone timezone
        sdf.setTimeZone(TimeZone.getDefault());
        String formattedDate = sdf.format(currentDate);
        return formattedDate;
    }

    public boolean isOlderThan24Hours(){
        Date nowDate = new Date(System.currentTimeMillis());
        Date dateOfMessage = new Date(datetime*1000L);

        //calculate hours elapse
        long secs = (nowDate.getTime() - dateOfMessage.getTime())/1000;
        int hours = (int)(secs / 3600);

        if(hours>24){
            return true;
        }
        return false;
    }

    public String getGetTime(){
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm");

        Date currentDate = new Date(datetime);
        //get phone timezone
        sdf.setTimeZone(TimeZone.getDefault());
        String formattedDate = sdf.format(currentDate);
        return formattedDate;
    }

    public void setDatetime(int datetime) {
        this.datetime = datetime;
    }

    public ChatMessage() {
    }

    public ChatMessage(String userName, String body) {
        this.userName = userName;
        this.body = body;
    }

    public ChatMessage(String id, String userName, String body) {
        this.id = id;
        this.userName = userName;
        this.body = body;
    }


    public String getUserName() {
        return userName;
    }

    public String getBody() {
        return body;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setBody(String body) {
        this.body = body;
    }

    @Override
    public String toString() {
        return "ChatMessage{" +
                "id='" + id + '\'' +
                ", userName='" + userName + '\'' +
                ", body='" + body + '\'' +
                '}';
    }
}
