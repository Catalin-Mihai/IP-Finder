package com.catasoft.ip_finder.ui.home;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.catasoft.ip_finder.data.entities.UserAccount;
import com.catasoft.ip_finder.data.repository.UserAccountRepository;

public class HomeViewModel extends AndroidViewModel {

    private final UserAccountRepository userAccountRepository;

    private final LiveData<UserAccount> liveCurrentUser;

    public HomeViewModel(@NonNull Application application) {
        super(application);
        userAccountRepository = new UserAccountRepository(application);
        liveCurrentUser = userAccountRepository.getLiveCurrentUserAccount();
    }

    public LiveData<UserAccount> getLiveCurrentUser() {
        return liveCurrentUser;
    }

    public void logout(){
        userAccountRepository.logout();
    }


}