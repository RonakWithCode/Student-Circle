package com.crazyostudio.studentcircle.model;

public class Chat_Model {
    private boolean IsImage;
    private String ID,Message,SanderName,SanderBio,SanderImage,SanderStory;
    private long SandTime;
    private boolean isGroup,isRead,IsPDF,IsContact,IsStoryReply;
    private String Filename,FileSize,FilePage;


    public Chat_Model(){}


    public Chat_Model(String ID, String message, String sanderName, String sanderImage, long sandTime, boolean isStoryReply) {
        this.ID = ID;
        Message = message;
        SanderName = sanderName;
        SanderImage = sanderImage;
        SandTime = sandTime;
        IsStoryReply = isStoryReply;
    }

    public Chat_Model(String ID, String message, long sandTime, boolean IsRead) {
        this.ID = ID;
        Message = message;
        SandTime = sandTime;
        isRead = IsRead;
    }


    public Chat_Model(String ID, String message, boolean isImage, String sanderName, String sanderBio, String sanderImage, boolean isGroup) {
        this.ID = ID;
        Message = message;
        IsImage = isImage;
        SanderName = sanderName;
        SanderBio = sanderBio;
        SanderImage = sanderImage;
        this.isGroup = isGroup;
    }

    public Chat_Model(String sanderBio, String sanderImage, String sanderStory, long sandTime, boolean isGroup) {
        SanderBio = sanderBio;
        SanderImage = sanderImage;
        SanderStory = sanderStory;
        SandTime = sandTime;
        this.isGroup = isGroup;
    }

    public Chat_Model(String ID, String message, boolean isImage,boolean IsRead,Boolean isPDF,long sandTime ,String filename) {
        this.ID = ID;
        Message = message;
        IsImage = isImage;
        isRead = IsRead;
        IsPDF = isPDF;
        SandTime = sandTime;
        Filename = filename;
    }

    public Chat_Model(String ID, String message, long sandTime, boolean isImage, boolean isGroup, boolean isRead, boolean isPDF, String filename, String fileSize, String filePage) {
        this.ID = ID;
        Message = message;
        SandTime = sandTime;
        IsImage = isImage;
        this.isGroup = isGroup;
        this.isRead = isRead;
        IsPDF = isPDF;
        Filename = filename;
        FileSize = fileSize;
        FilePage = filePage;
    }
    public Chat_Model(String ID, String message, long sandTime, boolean isImage, boolean isGroup, boolean isRead, boolean isPDF,Boolean isContact ,String filename) {
        this.ID = ID;
        Message = message;
        SandTime = sandTime;
        IsImage = isImage;
        this.isGroup = isGroup;
        this.isRead = isRead;
        IsPDF = isPDF;
        IsContact = isContact;
        Filename = filename;
    }
    public boolean isStoryReply() {
        return IsStoryReply;
    }

    public void setStoryReply(boolean storyReply) {
        IsStoryReply = storyReply;
    }
    public boolean isContact() {
        return IsContact;
    }

    public void setContact(boolean contact) {
        IsContact = contact;
    }
    public String getFilename() {
        return Filename;
    }



    public void setFilename(String filename) {
        Filename = filename;
    }

    public String getFileSize() {
        return FileSize;
    }

    public void setFileSize(String fileSize) {
        FileSize = fileSize;
    }

    public String getFilePage() {
        return FilePage;
    }

    public void setFilePage(String filePage) {
        FilePage = filePage;
    }

    public boolean isPDF() {
        return IsPDF;
    }

    public void setPDF(boolean PDF) {
        IsPDF = PDF;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }
    public boolean isImage() {
        return IsImage;
    }




    public String getSanderName() {
        return SanderName;
    }

    public void setSanderName(String sanderName) {
        SanderName = sanderName;
    }

    public String getSanderBio() {
        return SanderBio;
    }

    public void setSanderBio(String sanderBio) {
        SanderBio = sanderBio;
    }

    public String getSanderImage() {
        return SanderImage;
    }

    public void setSanderImage(String sanderImage) {
        SanderImage = sanderImage;
    }

    public String getSanderStory() {
        return SanderStory;
    }

    public void setSanderStory(String sanderStory) {
        SanderStory = sanderStory;
    }

    public long getSandTime() {
        return SandTime;
    }

    public void setSandTime(long sandTime) {
        SandTime = sandTime;
    }

    public boolean isGroup() {
        return isGroup;
    }

    public void setGroup(boolean group) {
        isGroup = group;
    }

    public void setImage(boolean image) {
        IsImage = image;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

}
