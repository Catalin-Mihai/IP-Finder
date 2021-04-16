package com.catasoft.ip_finder.ui.main.history;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.catasoft.ip_finder.data.entities.SearchInfo;
import com.catasoft.ip_finder.data.repository.SearchInfoRepository;

import java.util.List;

public class HistoryViewModel extends AndroidViewModel {

    protected final SearchInfoRepository searchInfoRepository;

    // Leave this with no initial value. If you put an initial value it will trigger setValue().
    private final MutableLiveData<String> liveToastMessage = new MutableLiveData<>();

    public HistoryViewModel(@NonNull Application application) {
        super(application);
        searchInfoRepository = new SearchInfoRepository(application);
    }

    public LiveData<String> getLiveToastMessage() { return liveToastMessage; }

    public void insert(SearchInfo value) {
        searchInfoRepository.insert(value);
    }

    public void delete(SearchInfo value) {
        searchInfoRepository.delete(value);
    }

    public LiveData<List<SearchInfo>> getAllLiveCurrentUserSearches(){ return searchInfoRepository.getAllLiveCurrentUserSearches(); }
}