package com.catasoft.ip_finder.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseUser;

public class HomeViewModel extends ViewModel {
    private MutableLiveData<FirebaseUser> liveUser;

    public LiveData<FirebaseUser> getLiveUser() {
        return liveUser;
    }
}