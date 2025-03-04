package com.crazyostudio.studentcircle.model;


import java.time.LocalDate;
import java.util.List;

public class UserInfo {
//    private String name,bio,userImage,userid,mail,number;
    private String id;
    private String token;
    private String username; //
    private String fullName;
    private String bio;
    private String profilePictureUrl;
    private String email;
    private String PhoneNumber;
    private LocalDate dateOfBirth;
    private List<LinksModels> LinksModels;
    private String AccountVisibility;
    private List<String> blockUserId;
    private int followersCount;
    private int followingCount;
    private List<String> mediaIds;
    private boolean isActive;
    private boolean isPrivacy_modeBool;
    private boolean isDeal_notificationBool;
    private boolean isAccount_shipping_notificationBool;

    private long time;


    public UserInfo(String id, String username, String fullName, String bio, String profilePictureUrl, int followersCount, int followingCount, List<String> mediaIds) {
        this.id = id;
        this.username = username;
        this.fullName = fullName;
        this.bio = bio;
        this.profilePictureUrl = profilePictureUrl;
        this.followersCount = followersCount;
        this.followingCount = followingCount;
        this.mediaIds = mediaIds;
    }

    public UserInfo(String id, String username, String fullName, String bio, String profilePictureUrl, int followersCount, int followingCount) {
        this.id = id;
        this.username = username;
        this.fullName = fullName;
        this.bio = bio;
        this.profilePictureUrl = profilePictureUrl;
        this.followersCount = followersCount;
        this.followingCount = followingCount;
    }


//   This is main
    public UserInfo(String id,String token ,String username, String fullName, String bio, String profilePictureUrl, String email, String phoneNumber, LocalDate dateOfBirth, List<LinksModels> profileLink, String accountVisibility, List<String> blockUserId, int followersCount, int followingCount, List<String> mediaIds,long time_) {
        this.id = id;
        this.token = token;
        this.username = username;
        this.fullName = fullName;
        this.bio = bio;
        this.profilePictureUrl = profilePictureUrl;
        this.email = email;
        PhoneNumber = phoneNumber;
        this.dateOfBirth = dateOfBirth;
        LinksModels = profileLink;
        AccountVisibility = accountVisibility;
        this.blockUserId = blockUserId;
        this.followersCount = followersCount;
        this.followingCount = followingCount;
        this.mediaIds = mediaIds;
        this.time = time_;
        isActive = true;
        isPrivacy_modeBool = true;
        isDeal_notificationBool = true;
        isAccount_shipping_notificationBool = true;
    }

    public UserInfo(){}


    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public boolean isPrivacy_modeBool() {
        return isPrivacy_modeBool;
    }

    public void setPrivacy_modeBool(boolean privacy_modeBool) {
        isPrivacy_modeBool = privacy_modeBool;
    }

    public boolean isDeal_notificationBool() {
        return isDeal_notificationBool;
    }

    public void setDeal_notificationBool(boolean deal_notificationBool) {
        isDeal_notificationBool = deal_notificationBool;
    }

    public boolean isAccount_shipping_notificationBool() {
        return isAccount_shipping_notificationBool;
    }

    public void setAccount_shipping_notificationBool(boolean account_shipping_notificationBool) {
        isAccount_shipping_notificationBool = account_shipping_notificationBool;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return PhoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        PhoneNumber = phoneNumber;
    }

    public List<com.crazyostudio.studentcircle.model.LinksModels> getLinksModels() {
        return LinksModels;
    }

    public void setLinksModels(List<com.crazyostudio.studentcircle.model.LinksModels> linksModels) {
        LinksModels = linksModels;
    }

    public String getAccountVisibility() {
        return AccountVisibility;
    }

    public void setAccountVisibility(String accountVisibility) {
        AccountVisibility = accountVisibility;
    }

    public List<String> getBlockUserId() {
        return blockUserId;
    }

    public void setBlockUserId(List<String> blockUserId) {
        this.blockUserId = blockUserId;
    }

    public String getId() {
        return id;
    }




    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getProfilePictureUrl() {
        return profilePictureUrl;
    }

    public void setProfilePictureUrl(String profilePictureUrl) {
        this.profilePictureUrl = profilePictureUrl;
    }

    public int getFollowersCount() {
        return followersCount;
    }

    public void setFollowersCount(int followersCount) {
        this.followersCount = followersCount;
    }

    public int getFollowingCount() {
        return followingCount;
    }

    public void setFollowingCount(int followingCount) {
        this.followingCount = followingCount;
    }

    public List<String> getMediaIds() {
        return mediaIds;
    }

    public void setMediaIds(List<String> mediaIds) {
        this.mediaIds = mediaIds;
    }
}


