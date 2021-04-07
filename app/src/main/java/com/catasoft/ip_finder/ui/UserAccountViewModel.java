package com.catasoft.ip_finder.ui;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.catasoft.ip_finder.data.entities.UserAccount;
import com.catasoft.ip_finder.data.repository.UserAccountRepository;

public class UserAccountViewModel extends AndroidViewModel {

    protected final UserAccountRepository userAccountRepository;
    private final LiveData<UserAccount> liveUserAccount;

    public UserAccountViewModel(@NonNull Application application) {
        super(application);

        userAccountRepository = new UserAccountRepository(application);

        // one query is enough because LiveData is made i.e. to be automatically notified by room
        // when changes are made in db
        liveUserAccount = userAccountRepository.getLiveUserAccount();
    }

    public void update(UserAccount value) {
        userAccountRepository.update(value);
    }

    public LiveData<UserAccount> getLiveUserAccount(){ return liveUserAccount; }
}
