package com.kaizen.models;

public class RequestResponse {
    private String message;
    private String error;
    private boolean responce;
    private String sessionid;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public boolean isResponce() {
        return responce;
    }

    public void setResponce(boolean responce) {
        this.responce = responce;
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
