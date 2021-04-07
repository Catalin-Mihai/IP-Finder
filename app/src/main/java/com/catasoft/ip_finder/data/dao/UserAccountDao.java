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
    void insert(UserAccount value);

    @Update(onConflict = OnConflictStrategy.IGNORE)
    void update(UserAccount value);

    @Query("SELECT * FROM " + RoomConfig.USER_INFO_TABLE + " LIMIT 1")
    LiveData<UserAccount> getLiveUserAccount();
}
