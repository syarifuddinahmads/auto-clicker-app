package com.interads.autoclickerapp.model;

import java.util.ArrayList;

public class Scenario {

    public double x,xx;
    public double y,yy;
    public String type; // type of action (click or swipe)
    public int time,duration;

    public Scenario() {
    }

    public Scenario(int x, int xx, int y, int yy, String type, int time, int duration) {
        this.x = x;
        this.xx = xx;
        this.y = y;
        this.yy = yy;
        this.type = type;
        this.time = time;
        this.duration = duration;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getXx() {
        return xx;
    }

    public void setXx(double xx) {
        this.xx = xx;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getYy() {
        return yy;
    }

    public void setYy(double yy) {
        this.yy = yy;
    }

    public void setYy(int yy) {
        this.yy = yy;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }
}
