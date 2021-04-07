package com.catasoft.ip_finder.ui.home;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.catasoft.ip_finder.data.entities.UserAccount;
import com.catasoft.ip_finder.data.repository.SearchInfoRepository;
import com.catasoft.ip_finder.data.repository.UserAccountRepository;
import com.google.firebase.auth.FirebaseUser;

public class HomeViewModel extends AndroidViewModel {

    private final UserAccountRepository userAccountRepository;

    private final LiveData<UserAccount> liveUser;

    public HomeViewModel(@NonNull Application application) {
        super(application);
        userAccountRepository = new UserAccountRepository(application);
        liveUser = userAccountRepository.getLiveUserAccount();
    }

    public LiveData<UserAccount> getLiveUser() {
        return liveUser;
    }
}