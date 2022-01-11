package com.interads.autoclickerapp.model;

import java.util.Date;

public class Config {
    public String name;
    public String app;
    public Boolean status;
    public Date date;

    public Config(String name, String app, Boolean status, Date date) {
        this.name = name;
        this.app = app;
        this.status = status;
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getApp() {
        return app;
    }

    public void setApp(String app) {
        this.app = app;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
