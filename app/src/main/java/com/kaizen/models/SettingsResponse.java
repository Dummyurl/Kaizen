package com.kaizen.models;

import java.util.ArrayList;
import java.util.List;

public class SettingsResponse {
    private List<Settings> settings;

    private String responce;

    public List<Settings> getSettings() {

        if (settings == null) {
            settings = new ArrayList<>();
        }

        return settings;
    }

    public void setSettings(List<Settings> settings) {
        this.settings = settings;
    }

    public String getResponce() {
        return responce;
    }

    public void setResponce(String responce) {
        this.responce = responce;
    }

    @Override
    public String toString() {
        return "SettingsResponse{" +
                "settings=" + settings +
                ", responce='" + responce + '\'' +
                '}';
    }
}
