package com.crazyostudio.studentcircle.model;

import java.util.ArrayList;

public class RecentLoginsModels {
    private ArrayList<String> time;
    private ArrayList<String> device;
    private ArrayList<String> ip;
    private ArrayList<String> approximateLocation;
    public RecentLoginsModels() {}

    public RecentLoginsModels(ArrayList<String> time, ArrayList<String> device, ArrayList<String> ip, ArrayList<String> approximateLocation) {
        this.time = time;
        this.device = device;
        this.ip = ip;
        this.approximateLocation = approximateLocation;
    }

    public ArrayList<String> getTime() {
        return time;
    }

    public void setTime(ArrayList<String> time) {
        this.time = time;
    }

    public ArrayList<String> getDevice() {
        return device;
    }

    public void setDevice(ArrayList<String> device) {
        this.device = device;
    }

    public ArrayList<String> getIp() {
        return ip;
    }

    public void setIp(ArrayList<String> ip) {
        this.ip = ip;
    }

    public ArrayList<String> getApproximateLocation() {
        return approximateLocation;
    }

    public void setApproximateLocation(ArrayList<String> approximateLocation) {
        this.approximateLocation = approximateLocation;
    }

}
