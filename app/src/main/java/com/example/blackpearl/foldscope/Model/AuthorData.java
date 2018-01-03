package com.example.blackpearl.foldscope.Model;

/**
 * Created by BlackPearl on 23-Nov-17.
 */
public class AuthorData {

    public String Name;
    public String Pic_Url;
    public String UserId;
    public String Slug;

    public String getName() {

        return Name;
    }

    public void setName(String name) {

        Name = name;
    }

    public String getPic_Url() {

        return Pic_Url;
    }

    public void setPic_Url(String pic_Url) {

        Pic_Url = pic_Url;
    }

    public String getUserId() {

        return UserId;
    }

    public void setUserId(String userId) {

        UserId = userId;
    }

    public String getSlug() {

        return Slug;
    }

    public void setSlug(String slug) {

        Slug = slug;
    }
}
