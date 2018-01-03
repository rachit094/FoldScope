package com.example.blackpearl.foldscope.Model;

/**
 * Created by BlackPearl on 20-Nov-17.
 */
public class MyExperimentData {

    public String Title;
    public String Description;
    public String Name;
    public String Time;
    public String Views;
    public String EventPic;
    public String ProfilePic;
    public String Link;
    public String getAllDescription() {

        return AllDescription;
    }

    public void setAllDescription(String allDescription) {

        AllDescription = allDescription;
    }

    public String AllDescription;

    public String getLink() {

        return Link;
    }

    public void setLink(String link) {

        Link = link;
    }

    public String getTitle() {

        return Title;
    }

    public void setTitle(String title) {

        Title = title;
    }

    public String getDescription() {

        return Description;
    }

    public void setDescription(String description) {

        Description = description;
    }

    public String getName() {

        return Name;
    }

    public void setName(String name) {

        Name = name;
    }

    public String getTime() {

        return Time;
    }

    public void setTime(String time) {

        Time = time;
    }

    public String getViews() {

        return Views;
    }

    public void setViews(String views) {

        Views = views;
    }

    public String getEventPic() {

        return EventPic;
    }

    public void setEventPic(String eventPic) {

        EventPic = eventPic;
    }

    public String getProfilePic() {

        return ProfilePic;
    }

    public void setProfilePic(String profilePic) {

        ProfilePic = profilePic;
    }
}
