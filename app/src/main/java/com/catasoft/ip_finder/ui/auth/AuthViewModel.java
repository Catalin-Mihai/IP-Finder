package com.catasoft.ip_finder.ui.auth;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.catasoft.ip_finder.R;
import com.catasoft.ip_finder.data.entities.UserAccount;
import com.catasoft.ip_finder.data.repository.UserAccountRepository;

public class AuthViewModel extends AndroidViewModel {

    private final UserAccountRepository userAccountRepository;

    // Leave this with no initial value. If you put an initial value it will trigger setValue().
    private final MutableLiveData<String> liveToastMessage = new MutableLiveData<>();

    private final MutableLiveData<String> liveLocalUsername = new MutableLiveData<>();
    private final MutableLiveData<String> liveLocalPassword = new MutableLiveData<>();

    public AuthViewModel(@NonNull Application application) {
        super(application);
        userAccountRepository = new UserAccountRepository(application);
    }

    public LiveData<String> getLiveToastMessage() { return liveToastMessage; }
    public MutableLiveData<String> getLiveLocalUsername() { return liveLocalUsername; }
    public MutableLiveData<String> getLiveLocalPassword() { return liveLocalPassword; }

    public void localRegister(AuthActivity.AuthActivityCallback callback){
        String username = liveLocalUsername.getValue();
        String password = liveLocalPassword.getValue();

        if(username == null || password == null){
            liveToastMessage.postValue(callback.getActivity().getString(R.string.auth_null_username_or_password));
            return;
        }

        if(username.trim().isEmpty() || password.trim().isEmpty()){
            liveToastMessage.postValue(callback.getActivity().getString(R.string.auth_wrong__username_or_password));
            return;
        }

        callback.getActivity().showLoadingDialog(callback.getActivity().getString(R.string.auth_register_loading_message));
        userAccountRepository.registerLocalUser(new UserAccount(username,password,null,null, 1, true),
                new AuthViewModelCallback(){
                    @Override
                    public void onSuccess(long userId) {
                        callback.goToMainActivity(true, userId);
                    }

                    @Override
                    public void onFailure(String error) {
                        callback.getActivity().dismissLoadingDialog();
                        liveToastMessage.postValue(error);
                    }
                });
    }

    public void localLogin(AuthActivity.AuthActivityCallback callback){
        String username = liveLocalUsername.getValue();
        String password = liveLocalPassword.getValue();

        if(username == null || password == null){
            liveToastMessage.postValue(callback.getActivity().getString(R.string.auth_null_username_or_password));
            return;
        }

        callback.getActivity().showLoadingDialog(callback.getActivity().getString(R.string.auth_login_loading_message));
        userAccountRepository.loginLocalUser(username, password, new AuthViewModelCallback(){
            @Override
            public void onSuccess(long userId) {
                callback.goToMainActivity(true, userId);
            }

            @Override
            public void onFailure(String error) {
                callback.getActivity().dismissLoadingDialog();
                liveToastMessage.postValue(error);
            }
        });
    }

    public void googleLogin(UserAccount userAccount, AuthActivity.AuthActivityCallback callback){
        String username = userAccount.getUsername();
        String password = userAccount.getPassword();

        if(username == null || password == null){
            liveToastMessage.postValue(callback.getActivity().getString(R.string.auth_google_login_error_2));
            return;
        }

        userAccountRepository.googleLogin(userAccount, new AuthViewModelCallback(){
            @Override
            public void onSuccess(long userId) {
                callback.goToMainActivity(false, userId);
            }

            @Override
            public void onFailure(String error) {
                liveToastMessage.postValue(error);
            }
        });

    }

    public interface AuthViewModelCallback {
        void onSuccess(long userId);
        void onFailure(String error);
    }

}
