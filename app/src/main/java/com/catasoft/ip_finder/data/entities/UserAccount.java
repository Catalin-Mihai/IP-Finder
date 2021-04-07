package com.catasoft.ip_finder.data.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.catasoft.ip_finder.data.room.RoomConfig;

@Entity(tableName = RoomConfig.USER_INFO_TABLE)
public class UserAccount {
    @PrimaryKey(autoGenerate = true)
    private long userId;

    private String displayName;
    private String photoUrl;

    public UserAccount(String displayName, String photoUrl) {
        this.displayName = displayName;
        this.photoUrl = photoUrl;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }
}
