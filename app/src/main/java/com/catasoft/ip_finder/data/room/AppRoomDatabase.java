package com.catasoft.ip_finder.data.room;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.catasoft.ip_finder.data.entities.SearchInfo;
import com.catasoft.ip_finder.data.entities.UserAccount;
import com.catasoft.ip_finder.data.dao.SearchInfoDao;
import com.catasoft.ip_finder.data.dao.UserAccountDao;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Create the room database for local storage
 *
 * https://developer.android.com/codelabs/android-room-with-a-view#7
 * */
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
                    .addCallback(roomDatabaseCallback)
                    .build();
        }

        return instance;
    }

    private static final RoomDatabase.Callback roomDatabaseCallback = new RoomDatabase.Callback() {

        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);

            databaseWriteExecutor.execute(() -> {
                SearchInfoDao dao = instance.searchInfoDao();

                SearchInfo a = new SearchInfo("Romania","Buc","Bucuresti",
                        1.52,1.62,"timezone","RCS","192.568");

                SearchInfo b = new SearchInfo("Romania","Iasi","Iasi",
                        1.52,1.62,"timezone","RCS","192.568");

                dao.insert(a);
                dao.insert(b);


                UserAccountDao userDao = instance.userAccountDao();
                userDao.insert(new UserAccount("Guest","url phot"));
            });
        }
    };
}