package com.interads.autoclickerapp.model;

public class ConfigDetail {

    public Config config;
    public int idConfig;
    public String x;
    public String y;
    public String xX;
    public String yY;
    public String type;
    public int orderConfig;
    public int duration;
    public int time;

    public ConfigDetail(Config config, int idConfig, String x, String y,String xX, String yY, String type, int orderConfig, int duration, int time) {
        this.config = config;
        this.idConfig = idConfig;
        this.x = x;
        this.y = y;
        this.xX = xX;
        this.yY = yY;
        this.type = type;
        this.orderConfig = orderConfig;
        this.duration = duration;
        this.time = time;
    }

    public Config getConfig() {
        return config;
    }

    public void setConfig(Config config) {
        this.config = config;
    }

    public String getX() {
        return x;
    }

    public void setX(String x) {
        this.x = x;
    }

    public String getY() {
        return y;
    }

    public void setY(String y) {
        this.y = y;
    }

    public String getxX() {
        return xX;
    }

    public void setxX(String xX) {
        this.xX = xX;
    }

    public String getyY() {
        return yY;
    }

    public void setyY(String yY) {
        this.yY = yY;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getOrderConfig() {
        return orderConfig;
    }

    public void setOrderConfig(int orderConfig) {
        this.orderConfig = orderConfig;
    }

    public int getIdConfig() {
        return idConfig;
    }

    public void setIdConfig(int idConfig) {
        this.idConfig = idConfig;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }
}
