package com.catasoft.ip_finder.data.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.catasoft.ip_finder.data.dao.UserAccountDao;
import com.catasoft.ip_finder.data.entities.UserAccount;
import com.catasoft.ip_finder.data.room.AppRoomDatabase;
import com.catasoft.ip_finder.ui.auth.AuthViewModel;

public class UserAccountRepository {

    private final UserAccountDao userAccountDao;
    private final LiveData<UserAccount> liveCurrentUserAccount;

    public UserAccountRepository(Application application) {
        // no need for db instance in class because communication will be made using dao interface
        AppRoomDatabase db = AppRoomDatabase.getDatabaseInstance(application);

        // this is used to communicate with db
        userAccountDao = db.userAccountDao();

        // one query is enough because LiveData is made i.e. to be automatically notified by room
        // when changes are made in db
        liveCurrentUserAccount = userAccountDao.getLiveCurrentUserAccount();
    }

    public void updateCurrentUserAccount(UserAccount value) {
        UserAccount currentUserAccount = liveCurrentUserAccount.getValue();
        if(currentUserAccount == null || value == null){
            return;
        }

        // update current user
        currentUserAccount.setUsername(value.getUsername());
        currentUserAccount.setIp(value.getIp());
        currentUserAccount.setPhotoUrl(value.getPhotoUrl());
        currentUserAccount.setCurrentUser(1);

        AppRoomDatabase.databaseWriteExecutor.execute(() -> {
            userAccountDao.update(currentUserAccount);
        });
    }

    public LiveData<UserAccount> getLiveCurrentUserAccount(){ return liveCurrentUserAccount; }

    public void registerLocalUser(UserAccount value, AuthViewModel.AuthViewModelCallback callback){
        // make sure that this will be current user of the application
        value.setCurrentUser(1);

        // add user
        AppRoomDatabase.databaseWriteExecutor.execute(() -> {
            if(userAccountDao.insert(value) > 0){
                callback.onSuccess();
                return;
            }
            callback.onFailure();
        });
    }

    public void localLogin(String username, String password, AuthViewModel.AuthViewModelCallback callback){
        // check if local user exists
        AppRoomDatabase.databaseWriteExecutor.execute(() -> {
            if (userAccountDao.checkLocalUserAccount(username, password)){
                userAccountDao.setCurrentUser(username, password);
                callback.onSuccess();
                return;
            }
            callback.onFailure();
        });
    }

    public void logout(){
        AppRoomDatabase.databaseWriteExecutor.execute(userAccountDao::unsetCurrentUser);
    }
}
