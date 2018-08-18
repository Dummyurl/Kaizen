package com.kaizen.models;

public class DescriptionResponse {

    private String message;

    private String description;

    private boolean responce;

    public String getMessage ()
    {
        return message;
    }

    public void setMessage (String message)
    {
        this.message = message;
    }

    public String getDescription ()
    {
        return description;
    }

    public void setDescription (String description)
    {
        this.description = description;
    }
    public boolean isResponce() {
        return responce;
    }
    public void setResponce(boolean responce) {
        this.responce = responce;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [message = "+message+", description = "+description+"]";
    }
}
