package com.interads.autoclickerapp.model;

public class AppInstalled {
    String name;
    String packageName;

    public AppInstalled(String name, String packageName) {
        this.name = name;
        this.packageName = packageName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }
}
