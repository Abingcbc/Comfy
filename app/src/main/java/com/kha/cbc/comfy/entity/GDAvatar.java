package com.kha.cbc.comfy.entity;


import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class GDAvatar {

    @Id
    String username;
    String avatarUrl;
    @Generated(hash = 709422856)
    public GDAvatar(String username, String avatarUrl) {
        this.username = username;
        this.avatarUrl = avatarUrl;
    }
    @Generated(hash = 357004300)
    public GDAvatar() {
    }
    public String getUsername() {
        return this.username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getAvatarUrl() {
        return this.avatarUrl;
    }
    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }
    
}
