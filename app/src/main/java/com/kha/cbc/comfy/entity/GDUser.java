package com.kha.cbc.comfy.entity;

import com.kha.cbc.comfy.model.User;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

@Entity
public class GDUser {

    @Id
    String username;
    String sessionToken;


    public GDUser(User user) {
        username = user.getUsername();
        sessionToken = user.getSessionToken();
    }


    @Generated(hash = 1833295239)
    public GDUser(String username, String sessionToken) {
        this.username = username;
        this.sessionToken = sessionToken;
    }


    @Generated(hash = 1014226889)
    public GDUser() {
    }


    public String getUsername() {
        return this.username;
    }


    public void setUsername(String username) {
        this.username = username;
    }


    public String getSessionToken() {
        return this.sessionToken;
    }


    public void setSessionToken(String sessionToken) {
        this.sessionToken = sessionToken;
    }
}
