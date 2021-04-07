package com.catasoft.ip_finder.ui.search;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.catasoft.ip_finder.data.entities.SearchInfo;
import com.catasoft.ip_finder.data.repository.SearchInfoRepository;

import org.jetbrains.annotations.NotNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchViewModel extends AndroidViewModel {

    public MutableLiveData<String> liveIp = new MutableLiveData<>();
    public MutableLiveData<SearchInfo> liveSearch = new MutableLiveData<>();

    // Leave this with no initial value. If you put an initial value it will trigger setValue().
    private final MutableLiveData<String> liveToastMessage = new MutableLiveData<>();

    public final SearchInfoRepository searchInfoRepository;

    public SearchViewModel(@NonNull Application application) {
        super(application);
        searchInfoRepository = new SearchInfoRepository(application);
    }

    public void makeRequest(){

        String ip = liveIp.getValue();

        //TODO: validare IP cu regex rule

        searchInfoRepository.makeRequest(ip, new Callback<SearchInfo>() {
            @Override
            public void onResponse(@NotNull Call<SearchInfo> call, @NotNull Response<SearchInfo> response) {
                SearchInfo searchInfo = response.body();
                liveSearch.postValue(searchInfo);
                //TODO: Verifica daca raspunsul e bun (192.168.0.1 nu este ok. Trebuie facut handle cand nu este cu succes)
                //TODO: Verifica sa ai acces la retea!
                //TODO: Camp de tip status pentru SearchInfo
            }

            @Override
            public void onFailure(@NotNull Call<SearchInfo> call, @NotNull Throwable t) {
                liveToastMessage.postValue("Eroare la fetch!");
            }
        });
    }

    public void setLiveSearch(SearchInfo search) {
        this.liveSearch.setValue(search);
    }

    public LiveData<SearchInfo> getLiveSearch() {
        return liveSearch;
    }
}