package com.catasoft.ip_finder.ui.main.home;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.catasoft.ip_finder.R;
import com.catasoft.ip_finder.data.entities.SearchInfo;
import com.catasoft.ip_finder.data.entities.UserAccount;
import com.catasoft.ip_finder.data.repository.SearchInfoRepository;
import com.catasoft.ip_finder.data.repository.UserAccountRepository;
import com.catasoft.ip_finder.ui.auth.AuthActivity;
import com.catasoft.ip_finder.ui.helpers.BaseApp;

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

    public void updateCurrentUserIp(HomeFragment.HomeFragmentCallback callback){
        searchInfoRepository.makeRequest(new Callback<SearchInfo>() {
            @Override
            public void onResponse(@NotNull Call<SearchInfo> call, @NotNull Response<SearchInfo> response) {
                SearchInfo searchInfo = response.body();
                if(searchInfo == null || searchInfo.getQuery() == null){
                    Log.e("[updateCurrentUserIp]", "Received request for current user IP is null");
                    return;
                }

                searchInfoRepository.findPreviousSearchInfo(new HomeViewModelCallback() {
                    @Override
                    public void onSuccess(SearchInfo previousSearchInfo) {
                        String previousIp = previousSearchInfo.getQuery();

                        // add current ip in db
                        userAccountRepository.updateCurrentUserIp(searchInfo.getQuery());
                        // this will be previous IP for the next call of the 'updateCurrentUserIp'
                        searchInfo.setPreviousUserSearchInfo(1);
                        searchInfo.setUserId(AuthActivity.NO_USER);
                        searchInfoRepository.insert(searchInfo);
                        // this will trigger addMarker for google map
                        liveCurrentUserSearchInfo.postValue(searchInfo);

                        // if previous ip != current ip ==> send a notification
                        if(previousIp != null && !previousIp.equals(searchInfo.getQuery())){
                            callback.showIpChangedNotification(
                                    BaseApp.getInstance().getString(R.string.ip_changed_notification_title),
                                    BaseApp.getInstance().getString(R.string.ip_changed_notification_message),
                                    previousSearchInfo, searchInfo
                            );
                        }
                    }

                    @Override
                    public void onFailure() {
                        // here no previous IP was found ==> add current ip in db
                        userAccountRepository.updateCurrentUserIp(searchInfo.getQuery());
                        // this will be previous IP for the next call of the 'updateCurrentUserIp'
                        searchInfo.setPreviousUserSearchInfo(1);
                        searchInfo.setUserId(AuthActivity.NO_USER);
                        searchInfoRepository.insert(searchInfo);
                        // this will trigger addMarker for google map
                        liveCurrentUserSearchInfo.postValue(searchInfo);
                    }
                });
            }

            @Override
            public void onFailure(@NotNull Call<SearchInfo> call, @NotNull Throwable t) {
                Log.e("[updateCurrentUserIp]", "request for current user IP is on failure");
            }
        });
    }

    public interface HomeViewModelCallback {
        void onSuccess(SearchInfo previousSearchInfo);
        void onFailure();
    }

}