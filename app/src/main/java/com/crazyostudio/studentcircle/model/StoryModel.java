package com.crazyostudio.studentcircle.model;

public class StoryModel {
    private String id,StoryHolderDP,StoryHolderName,StoryUri,StoryColor,StoryType,StoryShortMsg;
    private long StoryStartTime;
    private boolean isImage;

    public StoryModel(){}
    public StoryModel(String StoryHolderName, boolean isImage ,String storyUri, String storyType, String storyShortMsg, long storyStartTime) {
        StoryUri = storyUri;
        StoryType = storyType;
        StoryShortMsg = storyShortMsg;
        StoryStartTime = storyStartTime;
    }

    public StoryModel(String ID,String HolderDP,String HolderName,String storyColor, String storyType, String storyShortMsg, long storyStartTime) {
        id  = ID;
        StoryHolderDP = HolderDP;
        StoryHolderName = HolderName;
        StoryColor = storyColor;
        StoryType = storyType;
        StoryShortMsg = storyShortMsg;
        StoryStartTime = storyStartTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStoryHolderDP() {
        return StoryHolderDP;
    }

    public void setStoryHolderDP(String storyHolderDP) {
        StoryHolderDP = storyHolderDP;
    }

    public boolean isImage() {
        return isImage;
    }

    public String getStoryHolderName() {
        return StoryHolderName;
    }

    public void setStoryHolderName(String storyHolderName) {
        StoryHolderName = storyHolderName;
    }

    public void setImage(boolean image) {
        isImage = image;
    }

    public String getStoryColor() {
        return StoryColor;
    }

    public void setStoryColor(String storyColor) {
        StoryColor = storyColor;
    }

    public String getStoryUri() {
        return StoryUri;
    }

    public void setStoryUri(String storyUri) {
        StoryUri = storyUri;
    }

    public String getStoryType() {
        return StoryType;
    }

    public void setStoryType(String storyType) {
        StoryType = storyType;
    }

    public String getStoryShortMsg() {
        return StoryShortMsg;
    }

    public void setStoryShortMsg(String storyShortMsg) {
        StoryShortMsg = storyShortMsg;
    }

    public long getStoryStartTime() {
        return StoryStartTime;
    }

    public void setStoryStartTime(long storyStartTime) {
        StoryStartTime = storyStartTime;
    }
}
