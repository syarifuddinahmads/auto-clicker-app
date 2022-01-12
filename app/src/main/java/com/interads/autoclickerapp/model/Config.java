package com.interads.autoclickerapp.model;

import java.util.ArrayList;
import java.util.Date;

public class Config {
    public int id;
    public String name;
    public String app;
    public Boolean status;
    public Date date;
    public ArrayList<ConfigDetail> configDetail;

    public Config(){

    }

    public Config(String name, String app, Boolean status, Date date) {
        this.name = name;
        this.app = app;
        this.status = status;
        this.date = date;
    }

    public Config(int id,String name, String app, Boolean status, Date date) {
        this.id = id;
        this.name = name;
        this.app = app;
        this.status = status;
        this.date = date;
    }

    public Config(String name, String app, Boolean status, Date date, ArrayList<ConfigDetail> configDetail) {
        this.name = name;
        this.app = app;
        this.status = status;
        this.date = date;
        this.configDetail = configDetail;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public ArrayList<ConfigDetail> getConfigDetail() {
        return configDetail;
    }

    public void setConfigDetail(ArrayList<ConfigDetail> configDetail) {
        this.configDetail = configDetail;
    }
}
