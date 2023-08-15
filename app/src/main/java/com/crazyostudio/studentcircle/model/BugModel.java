package com.crazyostudio.studentcircle.model;

import java.util.ArrayList;

public class BugModel {
    private String problem,example,model;
    private int width,height;
    private ArrayList<String> images;

    public BugModel(String problem, String example, String model, int width, int height, ArrayList<String> images) {
        this.problem = problem;
        this.example = example;
        this.model = model;
        this.width = width;
        this.height = height;
        this.images = images;
    }

    public BugModel(String problem, String example, ArrayList<String> images) {
        this.problem = problem;
        this.example = example;
        this.images = images;
    }

    public BugModel() {
    }

    public String getProblem() {
        return problem;
    }

    public void setProblem(String problem) {
        this.problem = problem;
    }

    public String getExample() {
        return example;
    }

    public void setExample(String example) {
        this.example = example;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public ArrayList<String> getImages() {
        return images;
    }

    public void setImages(ArrayList<String> images) {
        this.images = images;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}
