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
    String objectId;


    public GDUser(User user) {
        username = user.getUsername();
        sessionToken = user.getSessionToken();
        objectId = user.getComfyUserObjectId();
    }


    @Generated(hash = 1938864627)
    public GDUser(String username, String sessionToken, String objectId) {
        this.username = username;
        this.sessionToken = sessionToken;
        this.objectId = objectId;
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


    public String getObjectId() {
        return this.objectId;
    }


    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }
}
