package com.catasoft.ip_finder.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.catasoft.ip_finder.data.entities.UserAccount;
import com.catasoft.ip_finder.data.room.RoomConfig;

@Dao
public interface UserAccountDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long insert(UserAccount value);

    @Update(onConflict = OnConflictStrategy.IGNORE)
    void update(UserAccount value);

    @Query("SELECT * FROM " + RoomConfig.USER_INFO_TABLE + " WHERE currentUser = 1")
    LiveData<UserAccount> getLiveCurrentUserAccount();

    @Query("UPDATE " + RoomConfig.USER_INFO_TABLE + " SET currentUser = 0 WHERE currentUser = 1")
    void unsetCurrentUser();

    @Query("UPDATE " + RoomConfig.USER_INFO_TABLE + " SET currentUser = 1 WHERE username = :username AND password = :password")
    void setCurrentUser(String username, String password);

    @Query("SELECT EXISTS(SELECT * FROM " + RoomConfig.USER_INFO_TABLE +
            " WHERE username = :username AND password = :password)")
    boolean checkLocalUserAccount(String username, String password);
}
