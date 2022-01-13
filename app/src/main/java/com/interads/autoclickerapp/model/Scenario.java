package com.interads.autoclickerapp.model;

import java.util.ArrayList;

public class Scenario {

    public int x,xx;
    public int y,yy;
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

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getXx() {
        return xx;
    }

    public void setXx(int xx) {
        this.xx = xx;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getYy() {
        return yy;
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
