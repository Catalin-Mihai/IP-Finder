package com.catasoft.ip_finder;

import android.app.DownloadManager;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;
import androidx.navigation.ui.NavigationUI.*;

import com.catasoft.ip_finder.data.api.ApiBuilder;
import com.catasoft.ip_finder.data.entities.SearchInfo;
import com.catasoft.ip_finder.databinding.ActivityMainBinding;
import com.catasoft.ip_finder.ui.history.HistoryViewModel;
import com.catasoft.ip_finder.ui.search.SearchViewModel;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private HistoryViewModel historyViewModel;
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
//        binding.setLifecycleOwner(this);
//
//        NavHostFragment navHostFragment =
//                (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
//        if (navHostFragment != null) {
//            NavController navController = navHostFragment.getNavController();
//
//            NavigationUI.setupWithNavController(binding.bottomNavigation, navController);
//
////            NavigationUI.setupActionBarWithNavController(this, navController);
//        }

        setViewModel();

        Call<SearchInfo> call = ApiBuilder.getInstance().getSearchInfo("79.112.238.196");
        call.enqueue(new Callback<SearchInfo>() {
            @Override
            public void onResponse(@NotNull Call<SearchInfo> call, @NotNull Response<SearchInfo> response) {
                SearchInfo searchInfo = response.body();
                historyViewModel.insert(searchInfo);
            }

            @Override
            public void onFailure(@NotNull Call<SearchInfo> call, @NotNull Throwable t) {

            }
        });

    }

    private void setViewModel(){
        // set ViewModel
        historyViewModel = new ViewModelProvider(this).get(HistoryViewModel.class);

        // set observers
        historyViewModel.getLiveToastMessage().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                Toast.makeText(MainActivity.this, s, Toast.LENGTH_LONG).show();
            }
        });

        historyViewModel.getAllLiveSearches().observe(this, new Observer<List<SearchInfo>>() {
            @Override
            public void onChanged(List<SearchInfo> searches) {
                historyViewModel.checkEmptyMode(searches);
            }
        });
    }
}