package com.catasoft.ip_finder.ui.main.search;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.catasoft.ip_finder.R;
import com.catasoft.ip_finder.data.entities.SearchInfo;
import com.catasoft.ip_finder.data.repository.SearchInfoRepository;
import com.catasoft.ip_finder.ui.helpers.Utilities;
import com.catasoft.ip_finder.ui.main.MainActivity;

import org.jetbrains.annotations.NotNull;

import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchViewModel extends AndroidViewModel {

    private final SearchInfoRepository searchInfoRepository;

    // Leave this with no initial value. If you put an initial value it will trigger setValue().
    private final MutableLiveData<String> liveToastMessage = new MutableLiveData<>();

    private final MutableLiveData<String> liveIp = new MutableLiveData<>();
    private final MutableLiveData<SearchInfo> liveSearch = new MutableLiveData<>();
    private final MutableLiveData<Boolean> liveGuestSession = new MutableLiveData<>(false);

    public SearchViewModel(@NonNull Application application) {
        super(application);
        searchInfoRepository = new SearchInfoRepository(application);
    }

    public LiveData<SearchInfo> getLiveSearch() {
        return liveSearch;
    }

    public MutableLiveData<String> getLiveIp() {
        return liveIp;
    }

    public LiveData<String> getLiveToastMessage() {
        return liveToastMessage;
    }

    public LiveData<Boolean> getLiveGuestSession(){ return liveGuestSession;}

    public void setLiveGuestSession(boolean value){
        liveGuestSession.postValue(value);
    }

    public void makeRequest(Context context){
        String ip = liveIp.getValue();
        if(ip == null){
            liveToastMessage.postValue(context.getString(R.string.ip_null));
            return;
        }

        if(!Utilities.isValid(ip)){
            liveToastMessage.postValue(context.getString(R.string.ip_wrong));
            return;
        }

        if (!MainActivity.IS_GOOD_INTERNET_CONNECTION.get()){
            liveToastMessage.postValue(context.getString(R.string.no_internet_connexion));
            return;
        }

        searchInfoRepository.makeRequest(ip, new Callback<SearchInfo>() {
            @Override
            public void onResponse(@NotNull Call<SearchInfo> call, @NotNull Response<SearchInfo> response) {
                SearchInfo searchInfo = response.body();
                if(searchInfo == null || searchInfo.getCity() == null){
                    liveToastMessage.postValue(context.getString(R.string.ip_search_unexpected_error));
                    return;
                }
                liveSearch.postValue(searchInfo);
                liveToastMessage.postValue(context.getString(R.string.ip_search_successfully));
            }

            @Override
            public void onFailure(@NotNull Call<SearchInfo> call, @NotNull Throwable t) {
                liveToastMessage.postValue(context.getString(R.string.ip_search_unexpected_error));
            }
        });
    }

    public void saveSearch(Context context){
        SearchInfo value = liveSearch.getValue();
        if(value == null) {
            liveToastMessage.postValue(context.getString(R.string.ip_no_search));
            return;
        }

        Calendar cc = Calendar.getInstance();
        int year = cc.get(Calendar.YEAR);
        int month = cc.get(Calendar.MONTH);
        int day = cc.get(Calendar.DAY_OF_MONTH);
        int hour = cc.get(Calendar.HOUR_OF_DAY);
        int minute = cc.get(Calendar.MINUTE);
        int second = cc.get(Calendar.SECOND);
        value.setCreatedAt(year + "-" + month + "-" + day + " " + hour + ":" + minute + ":" + second);

        // link search with current user
        value.setUserId(MainActivity.CURRENT_USER_ID);

        value.setPreviousUserSearchInfo(0);

        searchInfoRepository.insert(value);
        liveToastMessage.postValue(context.getString(R.string.ip_search_saved));
    }

}