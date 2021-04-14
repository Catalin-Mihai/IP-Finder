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

    public LiveData<UserAccount> getLiveCurrentUserAccount(){ return liveCurrentUserAccount; }

    public void registerLocalUser(UserAccount value, AuthViewModel.AuthViewModelCallback callback){
        AppRoomDatabase.databaseWriteExecutor.execute(() -> {
            // make sure that this will be current user of the application
            value.setCurrentUser(1);

            // before register check if user already exists
            if (!userAccountDao.checkUserAccount(value.getUsername(), value.getPassword(), value.isLocalLogin())){
                // insert user
                if(userAccountDao.insert(value) > 0){
                    UserAccount tmp = userAccountDao.getCurrentUserAccount();
                    if(tmp == null){
                        callback.onFailure("Eroare la preluarea userului");
                        return;
                    }
                    callback.onSuccess(tmp.getUserId());
                    return;
                }
            }
            callback.onFailure("Eroare la inregistrarea userului");
        });
    }

    public void loginLocalUser(String username, String password, AuthViewModel.AuthViewModelCallback callback){
        // check if local user exists
        AppRoomDatabase.databaseWriteExecutor.execute(() -> {
            if (userAccountDao.checkUserAccount(username, password, true)){
                // refresh current user
                userAccountDao.setCurrentUser(username, password, true);
                UserAccount tmp = userAccountDao.getCurrentUserAccount();
                if(tmp == null){
                    callback.onFailure("Eroare la preluarea userului");
                    return;
                }
                callback.onSuccess(tmp.getUserId());
                return;
            }
            callback.onFailure("Username sau parola gresita");
        });
    }

    public void googleLogin(UserAccount value, AuthViewModel.AuthViewModelCallback callback){
        AppRoomDatabase.databaseWriteExecutor.execute(() -> {
            // check if room user exists
            if (userAccountDao.checkUserAccount(value.getUsername(), value.getPassword(), false)){
                // refresh current user
                userAccountDao.setCurrentUser(value.getUsername(), value.getPassword(), false);
                UserAccount tmp = userAccountDao.getCurrentUserAccount();
                if(tmp == null){
                    callback.onFailure("Eroare la preluarea userului");
                    return;
                }
                callback.onSuccess(tmp.getUserId());
                return;
            }

            // if room user does not exists ==> add user
            // make sure that this will be current user of the application
            value.setCurrentUser(1);
            if(userAccountDao.insert(value) > 0){
                UserAccount tmp = userAccountDao.getCurrentUserAccount();
                if(tmp == null){
                    callback.onFailure("Eroare la preluarea userului");
                    return;
                }
                callback.onSuccess(tmp.getUserId());
                return;
            }
            callback.onFailure("Logarea cu Google nu a reusit");
        });
    }

    public void logout(){
        AppRoomDatabase.databaseWriteExecutor.execute(userAccountDao::unsetCurrentUser);
    }

    public void updateCurrentUserIp(String ip){
        AppRoomDatabase.databaseWriteExecutor.execute(() -> {
            userAccountDao.updateCurrentUserIp(ip);
        });
    }

}
