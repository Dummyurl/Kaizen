package com.kaizen.models;

import java.util.List;

public class NotificationResponse {

    private List<NotificationResponse> notifications;

    private String responce;

    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<NotificationResponse> getNotifications ()
    {
        return notifications;
    }

    public void setNotifications (List<NotificationResponse> notifications)
    {
        this.notifications = notifications;
    }

    public String getResponce ()
    {
        return responce;
    }

    public void setResponce (String responce)
    {
        this.responce = responce;
    }


    @Override
    public String toString()
    {
        return "ClassPojo [notifications = "+notifications+", responce = "+responce+"]";
    }
}
