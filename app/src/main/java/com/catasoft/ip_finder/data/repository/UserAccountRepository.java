package com.catasoft.ip_finder.data.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.catasoft.ip_finder.data.dao.UserAccountDao;
import com.catasoft.ip_finder.data.entities.UserAccount;
import com.catasoft.ip_finder.data.room.AppRoomDatabase;

public class UserAccountRepository {

    private final UserAccountDao userAccountDao;
    private final LiveData<UserAccount> liveUserAccount;

    public UserAccountRepository(Application application) {
        // no need for db instance in class because communication will be made using dao interface
        AppRoomDatabase db = AppRoomDatabase.getDatabaseInstance(application);

        // this is used to communicate with db
        userAccountDao = db.userAccountDao();

        // one query is enough because LiveData is made i.e. to be automatically notified by room
        // when changes are made in db
        liveUserAccount = userAccountDao.getLiveUserAccount();
    }

    public void update(UserAccount value) {
        AppRoomDatabase.databaseWriteExecutor.execute(() -> {
            userAccountDao.update(value);
        });
    }

    public LiveData<UserAccount> getLiveUserAccount(){ return liveUserAccount; }
}
