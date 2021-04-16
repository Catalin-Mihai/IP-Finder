package com.catasoft.ip_finder.data.room;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.catasoft.ip_finder.data.dao.SearchInfoDao;
import com.catasoft.ip_finder.data.dao.UserAccountDao;
import com.catasoft.ip_finder.data.entities.SearchInfo;
import com.catasoft.ip_finder.data.entities.UserAccount;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(
        entities = {SearchInfo.class, UserAccount.class},
        version = 1, exportSchema = false
)
public abstract class AppRoomDatabase extends RoomDatabase {

    public abstract SearchInfoDao searchInfoDao();
    public abstract UserAccountDao userAccountDao();

    private static volatile AppRoomDatabase instance;
    private static final int NUMBER_OF_THREADS = 4;

    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static synchronized AppRoomDatabase getDatabaseInstance(final Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    AppRoomDatabase.class, RoomConfig.DATABASE_NAME)
                    .build();
        }

        return instance;
    }
}