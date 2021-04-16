package com.catasoft.ip_finder.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.catasoft.ip_finder.data.entities.UserAccount;
import com.catasoft.ip_finder.data.room.RoomConfig;

@Dao
public interface UserAccountDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long insert(UserAccount value);

    @Query("SELECT * FROM " + RoomConfig.USER_INFO_TABLE + " WHERE currentUser = 1")
    LiveData<UserAccount> getLiveCurrentUserAccount();

    @Query("SELECT * FROM " + RoomConfig.USER_INFO_TABLE + " WHERE currentUser = 1")
    UserAccount getCurrentUserAccount();

    @Query("UPDATE " + RoomConfig.USER_INFO_TABLE + " SET currentUser = 0 WHERE currentUser = 1")
    void unsetCurrentUser();

    @Query("UPDATE " + RoomConfig.USER_INFO_TABLE + " SET currentUser = 1 WHERE username = :username" +
            " AND password = :password AND localLogin = :localLogin")
    void setCurrentUser(String username, String password, boolean localLogin);

    @Query("UPDATE " + RoomConfig.USER_INFO_TABLE + " SET ip = :ip WHERE currentUser = 1")
    void updateCurrentUserIp(String ip);

    @Query("SELECT EXISTS(SELECT * FROM " + RoomConfig.USER_INFO_TABLE +
            " WHERE username = :username AND password = :password AND localLogin = :localLogin)")
    boolean checkUserAccount(String username, String password, boolean localLogin);
}
