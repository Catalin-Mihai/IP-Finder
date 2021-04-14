package com.catasoft.ip_finder;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.catasoft.ip_finder.data.entities.SearchInfo;
import com.catasoft.ip_finder.databinding.ActivityMainBinding;
import com.catasoft.ip_finder.ui.auth.AuthActivity;
import com.catasoft.ip_finder.ui.history.HistoryViewModel;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static long CURRENT_USER_ID = -1;
    private HistoryViewModel historyViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences preferences = getSharedPreferences(AuthActivity.PREFERENCES_KEY, Context.MODE_PRIVATE);
        // check if user is already logged in
        if (!preferences.getBoolean(AuthActivity.LOGGED_IN,false)){
            // user is not logged in ==> redirect it to the login activity
            goToAuthActivity();
            return;
        }

        // user was logged in ==> do the normal stuff for this activity

        // set current user id
        CURRENT_USER_ID = preferences.getLong(AuthActivity.USER_ID,-1);
        if(CURRENT_USER_ID < AuthActivity.FIRST_PRIMARY_KEY){
            Toast.makeText(MainActivity.this, "Unable to login", Toast.LENGTH_LONG).show();
            goToAuthActivity();
            return;
        }

        // set data binding
        ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding.setLifecycleOwner(this);

        // set navigation components
        NavHostFragment navHostFragment =
                (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        if (navHostFragment != null) {
            NavController navController = navHostFragment.getNavController();
            NavigationUI.setupWithNavController(binding.bottomNavigation, navController);
            //NavigationUI.setupActionBarWithNavController(this, navController);
        }

        setViewModel();
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

        historyViewModel.getAllLiveCurrentUserSearches().observe(this, new Observer<List<SearchInfo>>() {
            @Override
            public void onChanged(List<SearchInfo> searches) {
                historyViewModel.checkEmptyMode(searches);
            }
        });
    }

    private void goToAuthActivity(){
        Intent intent = new Intent(this, AuthActivity.class);
        startActivity(intent);
    }

}