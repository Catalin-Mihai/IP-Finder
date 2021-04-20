package com.catasoft.ip_finder.data.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.catasoft.ip_finder.R;
import com.catasoft.ip_finder.data.dao.UserAccountDao;
import com.catasoft.ip_finder.data.entities.UserAccount;
import com.catasoft.ip_finder.data.room.AppRoomDatabase;
import com.catasoft.ip_finder.ui.auth.AuthViewModel;
import com.catasoft.ip_finder.ui.helpers.BaseApp;

import de.rtner.security.auth.spi.SimplePBKDF2;

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

    public boolean checkPassword(String dbPasswordHash, String candidatePassword){
        return new SimplePBKDF2().verifyKeyFormatted(dbPasswordHash, candidatePassword);
    }

    private String hashPassword(String password){
        return new SimplePBKDF2().deriveKeyFormatted(password);
    }

    public void registerLocalUser(UserAccount value, AuthViewModel.AuthViewModelCallback callback){
        AppRoomDatabase.databaseWriteExecutor.execute(() -> {
            // make sure that this will be current user of the application
            value.setCurrentUser(1);

            //get the account by id
            UserAccount dbUserAccount = userAccountDao.getUserAccount(value.getUsername());

            // before register check if user already exists.
            if (dbUserAccount == null){
                // insert user
                // Hash the password before inserting
                value.setPassword(hashPassword(value.getPassword()));
                if(userAccountDao.insert(value) > 0){
                    UserAccount tmp = userAccountDao.getCurrentUserAccount();
                    if(tmp == null){
                        callback.onFailure(BaseApp.getInstance().getString(R.string.user_fetch_error));
                        return;
                    }
                    callback.onSuccess(tmp.getUserId());
                    return;
                }
            }
            callback.onFailure(BaseApp.getInstance().getString(R.string.user_register_error));
        });
    }

    public void loginLocalUser(String username, String password, AuthViewModel.AuthViewModelCallback callback){

        // check if local user exists
        AppRoomDatabase.databaseWriteExecutor.execute(() -> {

            //get the account by id
            UserAccount dbUserAccount = userAccountDao.getUserAccount(username);

            if (dbUserAccount != null && checkPassword(dbUserAccount.getPassword(), password)){
                // refresh current user
                userAccountDao.setCurrentUser(dbUserAccount.getUsername(), dbUserAccount.getPassword(), true);
                UserAccount tmp = userAccountDao.getCurrentUserAccount();
                if(tmp == null){
                    callback.onFailure(BaseApp.getInstance().getString(R.string.user_fetch_error));
                    return;
                }
                callback.onSuccess(tmp.getUserId());
                return;
            }
            callback.onFailure(BaseApp.getInstance().getString(R.string.user_no_exists_error));
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
                    callback.onFailure(BaseApp.getInstance().getString(R.string.user_fetch_error));

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
                    callback.onFailure(BaseApp.getInstance().getString(R.string.user_fetch_error));
                    return;
                }
                callback.onSuccess(tmp.getUserId());
                return;
            }
            callback.onFailure(BaseApp.getInstance().getString(R.string.user_google_error));
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
