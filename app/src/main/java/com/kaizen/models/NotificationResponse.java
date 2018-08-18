package com.kaizen.models;

import java.util.List;

public class NotificationResponse {

    private List<Notifications> notifications;

    private String responce;

    public List<Notifications> getNotifications ()
    {
        return notifications;
    }

    public void setNotifications (List<Notifications> notifications)
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
