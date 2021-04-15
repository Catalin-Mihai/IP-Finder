package com.catasoft.ip_finder.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.catasoft.ip_finder.data.room.RoomConfig;
import com.catasoft.ip_finder.data.entities.SearchInfo;

import java.util.List;

@Dao
public interface SearchInfoDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(SearchInfo value);

    @Update(onConflict = OnConflictStrategy.IGNORE)
    void update(SearchInfo value);

    @Delete
    void delete(SearchInfo value);

    @Delete
    void deleteAll(List<SearchInfo> items);

    @Query("DELETE FROM " + RoomConfig.SEARCHES_TABLE + " WHERE previousUserSearchInfo = 1")
    void deletePreviousSearchInfo();

    @Query("SELECT * FROM " + RoomConfig.SEARCHES_TABLE + " WHERE previousUserSearchInfo = 1")
    SearchInfo getPreviousSearchInfo();

    @Query("SELECT * FROM " + RoomConfig.SEARCHES_TABLE + " WHERE searchId = :searchId")
    LiveData<SearchInfo> getLiveSearchInfo(long searchId);

    @Query("SELECT * FROM " + RoomConfig.SEARCHES_TABLE + " WHERE userId = :userId AND userId = :userId")
    LiveData<List<SearchInfo>> getAllLiveCurrentUserSearches(long userId);
}
