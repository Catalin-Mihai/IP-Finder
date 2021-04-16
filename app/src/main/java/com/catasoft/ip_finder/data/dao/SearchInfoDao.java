package com.catasoft.ip_finder.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.catasoft.ip_finder.data.entities.SearchInfo;
import com.catasoft.ip_finder.data.room.RoomConfig;

import java.util.List;

@Dao
public interface SearchInfoDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(SearchInfo value);

    @Delete
    void delete(SearchInfo value);

    @Query("DELETE FROM " + RoomConfig.SEARCHES_TABLE + " WHERE previousUserSearchInfo = 1")
    void deletePreviousSearchInfo();

    @Query("SELECT * FROM " + RoomConfig.SEARCHES_TABLE + " WHERE previousUserSearchInfo = 1")
    SearchInfo getPreviousSearchInfo();

    @Query("SELECT * FROM " + RoomConfig.SEARCHES_TABLE + " WHERE userId = :userId AND userId = :userId")
    LiveData<List<SearchInfo>> getAllLiveCurrentUserSearches(long userId);
}
