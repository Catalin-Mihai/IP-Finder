package com.catasoft.ip_finder.data.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.catasoft.ip_finder.data.api.ApiBuilder;
import com.catasoft.ip_finder.data.dao.SearchInfoDao;
import com.catasoft.ip_finder.data.entities.SearchInfo;
import com.catasoft.ip_finder.data.room.AppRoomDatabase;
import com.catasoft.ip_finder.ui.main.MainActivity;
import com.catasoft.ip_finder.ui.main.home.HomeViewModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

public class SearchInfoRepository {

    private final SearchInfoDao searchInfoDao;
    private final LiveData<List<SearchInfo>> liveCurrentUserSearchInfoList;

    public SearchInfoRepository(Application application) {
        // no need for db instance in class because communication will be made using dao interface
        AppRoomDatabase db = AppRoomDatabase.getDatabaseInstance(application);

        // this is used to communicate with db
        searchInfoDao = db.searchInfoDao();

        // one query is enough because LiveData is made i.e. to be automatically notified by room
        // when changes are made in db
        liveCurrentUserSearchInfoList = searchInfoDao.getAllLiveCurrentUserSearches(MainActivity.CURRENT_USER_ID);
    }

    public void insert(SearchInfo value) {
        AppRoomDatabase.databaseWriteExecutor.execute(() -> {
                searchInfoDao.insert(value);
        });
    }

    public void delete(SearchInfo value) {
        AppRoomDatabase.databaseWriteExecutor.execute(() -> {
            searchInfoDao.delete(value);
        });
    }

    public void makeRequest(String query, Callback<SearchInfo> callback){
        Call<SearchInfo> call = ApiBuilder.getInstance().getSearchInfo(query);
        call.enqueue(callback);
    }

    public void makeRequest(Callback<SearchInfo> callback){
        Call<SearchInfo> call = ApiBuilder.getInstance().getCurrentUserInfo();
        call.enqueue(callback);
    }

    public LiveData<List<SearchInfo>> getAllLiveCurrentUserSearches(){ return liveCurrentUserSearchInfoList; }

    public void findPreviousSearchInfo(HomeViewModel.HomeViewModelCallback callback){
        AppRoomDatabase.databaseWriteExecutor.execute(() -> {
            SearchInfo searchInfo = searchInfoDao.getPreviousSearchInfo();
            if(searchInfo != null){
                searchInfoDao.deletePreviousSearchInfo();
                callback.onSuccess(searchInfo);
                return;
            }
            callback.onFailure();
        });
    }
}
