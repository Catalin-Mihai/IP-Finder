package com.catasoft.ip_finder.ui.main;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.catasoft.ip_finder.R;
import com.catasoft.ip_finder.databinding.ActivityMainBinding;
import com.catasoft.ip_finder.ui.auth.AuthActivity;
import com.catasoft.ip_finder.ui.helpers.NetworkBroadcastReceiver;
import com.google.android.material.snackbar.Snackbar;

import java.util.concurrent.atomic.AtomicBoolean;

public class MainActivity extends AppCompatActivity {

    public static long CURRENT_USER_ID = -1;
    public static AtomicBoolean IS_GOOD_INTERNET_CONNECTION = new AtomicBoolean(false);

    private ActivityMainBinding binding;
    private Snackbar snackbar;

    // for broadcast receiver
    private NetworkBroadcastReceiver receiver;
    private IntentFilter filter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences preferences = getSharedPreferences(AuthActivity.LOGIN_STATUS_KEY, Context.MODE_PRIVATE);
        // check if user is already logged in
        if (!preferences.getBoolean(AuthActivity.LOGGED_IN,false)){
            // user is not logged in ==> redirect it to the login activity
            goToAuthActivity();
            return;
        }

        // user was logged in ==> do the normal stuff for this activity

        CURRENT_USER_ID = preferences.getLong(AuthActivity.USER_ID,-1);
        if(CURRENT_USER_ID < AuthActivity.FIRST_PRIMARY_KEY){
            Toast.makeText(MainActivity.this, R.string.main_activity_login_error_1, Toast.LENGTH_LONG).show();
            goToAuthActivity();
            return;
        }

        // set data binding
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding.setLifecycleOwner(this);

        // set navigation components
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        if (navHostFragment != null) {
            NavController navController = navHostFragment.getNavController();
            NavigationUI.setupWithNavController(binding.bottomNavigation, navController);
            // NavigationUI.setupActionBarWithNavController(this, navController);
        }

        initNetworkBroadcastReceiver();
    }

    private void goToAuthActivity(){
        Intent intent = new Intent(this, AuthActivity.class);
        startActivity(intent);
        this.finish();
    }

    @Override
    public void onBackPressed() {
        // disable back from main activity because is not necessary
        //super.onBackPressed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            unregisterReceiver(receiver);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            registerReceiver(receiver, filter);
        }
    }

    private void initNetworkBroadcastReceiver(){
        receiver = new NetworkBroadcastReceiver();
        receiver.setupListener(new NetworkBroadcastReceiver.NetworkListener() {
            @Override
            public void onChange(boolean isGoodConnection) {
                IS_GOOD_INTERNET_CONNECTION.set(isGoodConnection);
                if(!isGoodConnection){
                    showSnackBar();
                }
                else{
                    disableSnackBar();
                }
            }
        });
        filter = new IntentFilter();
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
    }

    private void showSnackBar(){
        snackbar = Snackbar.make(binding.getRoot(),R.string.main_activity_connexion_error_1, Snackbar.LENGTH_INDEFINITE);
        snackbar.setAction(R.string.go_to_settings, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
            }
        });
        snackbar.show();
    }

    private void disableSnackBar(){
        if (snackbar != null){
            snackbar.dismiss();
            snackbar = null;
        }
    }
}