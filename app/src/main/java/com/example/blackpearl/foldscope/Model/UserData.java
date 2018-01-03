package com.example.blackpearl.foldscope.Model;

/**
 * Created by BlackPearl on 13-Dec-17.
 */
public class UserData {

    public String UserId;

    public String getUserId() {

        return UserId;
    }

    public void setUserId(String userId) {

        UserId = userId;
    }

    public String getUserName() {

        return UserName;
    }

    public void setUserName(String userName) {

        UserName = userName;
    }

    public String getSlug() {

        return Slug;
    }

    public void setSlug(String slug) {

        Slug = slug;
    }

    public String getAvatar() {

        return Avatar;
    }

    public void setAvatar(String avatar) {

        Avatar = avatar;
    }

    public String UserName;
    public String Slug;
    public String Avatar;
}
