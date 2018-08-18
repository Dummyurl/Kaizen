package com.kaizen.models;

public class User {
    private String roomno;
    private String user_id;
    private String sessionid;

    public String getRoomno() {
        return roomno;
    }

    public void setRoomno(String roomno) {
        this.roomno = roomno;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }
    public String getSessionid ()
    {
        return sessionid;
    }

    public void setSessionid (String sessionid)
    {
        this.sessionid = sessionid;
    }
}
