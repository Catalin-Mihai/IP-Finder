package com.catasoft.ip_finder.ui.auth;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.catasoft.ip_finder.data.entities.UserAccount;
import com.catasoft.ip_finder.data.repository.UserAccountRepository;

public class AuthViewModel extends AndroidViewModel {

    private final UserAccountRepository userAccountRepository;
    private final LiveData<UserAccount> liveCurrentUserAccount;

    // Leave this with no initial value. If you put an initial value it will trigger setValue().
    private final MutableLiveData<String> liveToastMessage = new MutableLiveData<>();

    private final MutableLiveData<String> liveLocalUsername = new MutableLiveData<>();
    private final MutableLiveData<String> liveLocalPassword = new MutableLiveData<>();

    public AuthViewModel(@NonNull Application application) {
        super(application);

        userAccountRepository = new UserAccountRepository(application);

        // one query is enough because LiveData is made i.e. to be automatically notified by room
        // when changes are made in db
        liveCurrentUserAccount = userAccountRepository.getLiveCurrentUserAccount();
    }

    public LiveData<String> getLiveToastMessage() { return liveToastMessage; }

    public MutableLiveData<String> getLiveLocalUsername() { return liveLocalUsername; }
    public MutableLiveData<String> getLiveLocalPassword() { return liveLocalPassword; }

    public void updateCurrentUserAccount(UserAccount value) {
        userAccountRepository.updateCurrentUserAccount(value);
    }

    public LiveData<UserAccount> getLiveCurrentUserAccount(){ return liveCurrentUserAccount; }

    public void localRegister(UserAccount userAccount, AuthActivity.AuthActivityCallback callback){
        String username = userAccount.getUsername();
        String password = userAccount.getPassword();

        addUser(username, password, callback, 0);
    }

    public void localRegister(AuthActivity.AuthActivityCallback callback){
        String username = liveLocalUsername.getValue();
        String password = liveLocalPassword.getValue();

        addUser(username, password, callback, 1);
    }

    private void addUser(String username, String password, AuthActivity.AuthActivityCallback callback, int firebaseLogin){

        if(username == null || password == null){
            liveToastMessage.setValue("Username-ul sau parola este null");
            return;
        }

        userAccountRepository.addUser(new UserAccount(username,password,"","", 1, firebaseLogin),
            new AuthViewModelCallback(){
                @Override
                public void onSuccess() {
                    callback.goToMainActivity(firebaseLogin == 0, username);
                }

                @Override
                public void onFailure() {
                    liveToastMessage.postValue("Inregistrarea nu a putut fi efectuata");
                }
            });
    }

    public void localLogin(UserAccount userAccount, AuthActivity.AuthActivityCallback callback){
        String username = userAccount.getUsername();
        String password = userAccount.getPassword();

        login(username, password, callback, 1);
    }

    public void localLogin(AuthActivity.AuthActivityCallback callback){
        String username = liveLocalUsername.getValue();
        String password = liveLocalPassword.getValue();

        login(username, password, callback, 0);
    }

    private void login(String username, String password, AuthActivity.AuthActivityCallback callback, int firebaseLogin){
        if(username == null || password == null){
            liveToastMessage.setValue("Username-ul sau parola este null");
            return;
        }

        userAccountRepository.login(username, password, firebaseLogin, new AuthViewModelCallback(){
            @Override
            public void onSuccess() {
                callback.goToMainActivity(true, username);
            }

            @Override
            public void onFailure() {
                liveToastMessage.postValue("Contul asociat acestor credentiale nu exista");
            }
        });
    }

    public interface AuthViewModelCallback {
        void onSuccess();
        void onFailure();
    }
}
