package com.spade.sociallogin;

/**
 * Created by Ayman Abouzaid on 5/30/17.
 */

public class SocialUser {

    private String name;
    private String emailAddress;
    private String userId;
    private String userToken;
    private String userPhoto;

    public SocialUser(String name, String emailAddress, String userId, String userToken, String userPhoto) {
        this.name = name;
        this.emailAddress = emailAddress;
        this.userId = userId;
        this.userToken = userToken;
        this.userPhoto = userPhoto;
    }

    public SocialUser(String name, String emailAddress) {
        this.name = name;
        this.emailAddress = emailAddress;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserToken() {
        return userToken;
    }

    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }

    public String getUserPhoto() {
        return userPhoto;
    }

    public void setUserPhoto(String userPhoto) {
        this.userPhoto = userPhoto;
    }
}
