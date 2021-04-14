package com.catasoft.ip_finder.ui.history;

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

    // By default, the empty mode will be set to false in order to avoid showing him before
    // the first loading of the adapter list.
    private final MutableLiveData<Boolean> liveEmptyMode = new MutableLiveData<>(false);

    // Leave this with no initial value. If you put an initial value it will trigger setValue().
    private final MutableLiveData<String> liveToastMessage = new MutableLiveData<>();

    public HistoryViewModel(@NonNull Application application) {
        super(application);
        searchInfoRepository = new SearchInfoRepository(application);
    }

    public LiveData<String> getLiveToastMessage() { return liveToastMessage; }
    public LiveData<Boolean> getLiveEmptyMode() { return liveEmptyMode; }

    public void insert(SearchInfo value) {
        searchInfoRepository.insert(value);
    }

    public void update(SearchInfo value) {
        searchInfoRepository.update(value);
    }

    public void delete(SearchInfo value) {
        searchInfoRepository.delete(value);
    }

    public void deleteAll(List<SearchInfo> items){
        searchInfoRepository.deleteAll(items);
    }

    public LiveData<SearchInfo> getLiveSearchInfo(long searchId){ return searchInfoRepository.getLiveSearchInfo(searchId); }

    public LiveData<List<SearchInfo>> getAllLiveCurrentUserSearches(){ return searchInfoRepository.getAllLiveCurrentUserSearches(); }

    public void checkEmptyMode(@NonNull List<SearchInfo> currentList) {
        if(currentList.isEmpty()){
            liveEmptyMode.setValue(true);
            return;
        }
        liveEmptyMode.setValue(false);
    }
}