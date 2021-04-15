package com.catasoft.ip_finder.ui.main.search;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

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

    public MutableLiveData<String> liveIp = new MutableLiveData<>();
    public MutableLiveData<SearchInfo> liveSearch = new MutableLiveData<>();

    // Leave this with no initial value. If you put an initial value it will trigger setValue().
    private final MutableLiveData<String> liveToastMessage = new MutableLiveData<>();

    private final MutableLiveData<Boolean> liveGuestSession = new MutableLiveData<>(false);

    public final SearchInfoRepository searchInfoRepository;

    public SearchViewModel(@NonNull Application application) {
        super(application);
        searchInfoRepository = new SearchInfoRepository(application);
    }

    public void makeRequest(Context context){

        String ip = liveIp.getValue();

        if(ip == null){
            liveToastMessage.setValue("Ip-ul este null");
            return;
        }

        if(!Utilities.isValid(ip)){
            liveToastMessage.setValue("Formatul ip-ului nu este corect");
            return;
        }

        if (!MainActivity.isGoodInternetConnection.get()){
            liveToastMessage.setValue("Nu exista conexiune la internet");
            return;
        }

        searchInfoRepository.makeRequest(ip, new Callback<SearchInfo>() {
            @Override
            public void onResponse(@NotNull Call<SearchInfo> call, @NotNull Response<SearchInfo> response) {
                SearchInfo searchInfo = response.body();
                if(searchInfo == null || searchInfo.getCity() == null){
                    liveToastMessage.setValue("Cautarea nu a fost buna");
                    return;
                }
                liveSearch.postValue(searchInfo);
                liveToastMessage.postValue("Cautarea s-a efectuat cu succes");
            }

            @Override
            public void onFailure(@NotNull Call<SearchInfo> call, @NotNull Throwable t) {
                liveToastMessage.postValue("Eroare neasteptata la cautare!");
            }
        });
    }

    public void setLiveSearch(SearchInfo search) {
        this.liveSearch.setValue(search);
    }

    public LiveData<SearchInfo> getLiveSearch() {
        return liveSearch;
    }

    public LiveData<String> getLiveToastMessage() {
        return liveToastMessage;
    }

    public void saveSearch(){
        SearchInfo value = liveSearch.getValue();
        if(value != null){
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
            liveToastMessage.postValue("Cautare salvata!");
        }
    }

    public void setLiveGuestSession(boolean value){
        liveGuestSession.setValue(value);
    }

    public LiveData<Boolean> getLiveGuestSession(){ return liveGuestSession;}
}