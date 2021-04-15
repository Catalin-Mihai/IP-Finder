package com.catasoft.ip_finder.ui.main.home;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.catasoft.ip_finder.data.entities.SearchInfo;
import com.catasoft.ip_finder.data.entities.UserAccount;
import com.catasoft.ip_finder.data.repository.SearchInfoRepository;
import com.catasoft.ip_finder.data.repository.UserAccountRepository;

import org.jetbrains.annotations.NotNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeViewModel extends AndroidViewModel {

    private final UserAccountRepository userAccountRepository;
    private final SearchInfoRepository searchInfoRepository;

    private final LiveData<UserAccount> liveCurrentUser;
    private final MutableLiveData<SearchInfo> liveCurrentUserSearchInfo = new MutableLiveData<>();

    public HomeViewModel(@NonNull Application application) {
        super(application);
        searchInfoRepository = new SearchInfoRepository(application);
        userAccountRepository = new UserAccountRepository(application);
        liveCurrentUser = userAccountRepository.getLiveCurrentUserAccount();
    }

    public LiveData<UserAccount> getLiveCurrentUser() {
        return liveCurrentUser;
    }

    public LiveData<SearchInfo> getLiveSearchCurrentUser() {
        return liveCurrentUserSearchInfo;
    }

    public void logout(){
        userAccountRepository.logout();
    }

    public void updateCurrentUserIp(){

        searchInfoRepository.makeRequest(new Callback<SearchInfo>() {
            @Override
            public void onResponse(@NotNull Call<SearchInfo> call, @NotNull Response<SearchInfo> response) {
                SearchInfo searchInfo = response.body();
                if(searchInfo == null || searchInfo.getQuery() == null){
                    Log.e("[makeRequest]", "Request-ul pt ip-ul userului curent nu este bun");
                    return;
                }

                userAccountRepository.updateCurrentUserIp(searchInfo.getQuery());
                liveCurrentUserSearchInfo.setValue(searchInfo);
            }

            @Override
            public void onFailure(@NotNull Call<SearchInfo> call, @NotNull Throwable t) {
                Log.e("[makeRequest]", "Request-ul pt ip-ul userului curent este on failure");
            }
        });
    }

}