package com.catasoft.ip_finder.data.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.catasoft.ip_finder.data.room.RoomConfig;

@Entity(tableName = RoomConfig.USER_INFO_TABLE)
public class UserAccount {
    @PrimaryKey(autoGenerate = true)
    private long userId;

    private String username;
    private String password;
    private String photoUrl;
    private String ip;
    private int currentUser;
    private int firebaseLogin;

    public UserAccount(String username, String password, String photoUrl, String ip, int currentUser, int firebaseLogin) {
        this.username = username;
        this.password = password;
        this.photoUrl = photoUrl;
        this.ip = ip;
        this.currentUser = currentUser;
        this.firebaseLogin = firebaseLogin;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(int currentUser) {
        this.currentUser = currentUser;
    }

    public int getFirebaseLogin() {
        return firebaseLogin;
    }

    public void setFirebaseLogin(int firebaseLogin) {
        this.firebaseLogin = firebaseLogin;
    }
}
