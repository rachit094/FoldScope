package com.example.blackpearl.foldscope.Model;

/**
 * Created by BlackPearl on 29-Nov-17.
 */
public class TokenData {

    public String Token;
    public String Email;

    public String getToken() {

        return Token;
    }

    public void setToken(String token) {

        Token = token;
    }

    public String getEmail() {

        return Email;
    }

    public void setEmail(String email) {

        Email = email;
    }

    public String getNiceName() {

        return NiceName;
    }

    public void setNiceName(String niceName) {

        NiceName = niceName;
    }

    public String getDisplayName() {

        return DisplayName;
    }

    public void setDisplayName(String displayName) {

        DisplayName = displayName;
    }

    public String NiceName;
    public String DisplayName;
}
