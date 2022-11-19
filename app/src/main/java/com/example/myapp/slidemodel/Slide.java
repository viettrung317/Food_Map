package com.example.myapp.slidemodel;

import java.io.Serializable;

public class Slide implements Serializable {
    private int resourceID;
    private String uRl;

    public Slide() {
    }

    public Slide(int resourceID, String uRl) {
        this.resourceID = resourceID;
        this.uRl = uRl;
    }

    public int getResourceID() {
        return resourceID;
    }

    public void setResourceID(int resourceID) {
        this.resourceID = resourceID;
    }

    public String getuRl() {
        return uRl;
    }

    public void setuRl(String uRl) {
        this.uRl = uRl;
    }
}
