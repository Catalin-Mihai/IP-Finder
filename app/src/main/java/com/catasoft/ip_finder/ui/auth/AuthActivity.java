package com.catasoft.ip_finder.ui.auth;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.catasoft.ip_finder.MainActivity;
import com.catasoft.ip_finder.R;
import com.catasoft.ip_finder.databinding.ActivityAuthBinding;
import com.catasoft.ip_finder.ui.guest.GuestActivity;

public class AuthActivity extends AppCompatActivity {

    public final static String PREFERENCES_KEY = "LOGIN_STATUS";
    public final static String LOGGED_IN_ID_KEY = "LOGGED_IN_ID_KEY";
    public final static String LOCAL_LOGGED_IN_ID_KEY = "LOCAL_LOGGED_IN_ID_KEY";
    public final static String GUEST_SESSION_ID_KEY = "GUEST_SESSION_ID_KEY";
    public final static String USER_ID_KEY = "USER_ID_KEY";

    private AuthViewModel authViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // set data binding
        ActivityAuthBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_auth);
        binding.setLifecycleOwner(this);
        setViewModel();
        binding.setViewModel(authViewModel);

        // set listeners
        binding.btnLocalLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                authViewModel.localLogin(new AuthActivityCallback() {
                    @Override
                    public void goToMainActivity(boolean isLocalLogin, String userId) {
                        AuthActivity.this.goToMainActivity(isLocalLogin, userId);
                    }
                });
            }
        });

        binding.btnLocalRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                authViewModel.localRegister(new AuthActivityCallback() {
                    @Override
                    public void goToMainActivity(boolean isLocalLogin, String userId) {
                        AuthActivity.this.goToMainActivity(isLocalLogin, userId);
                    }
                });
            }
        });

        binding.btnGuest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setGuestSharedPreferences();
                AuthActivity.this.goToGuestActivity();
            }
        });
    }

    private void setViewModel(){
        // set ViewModel
        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);

        // set observers
        authViewModel.getLiveToastMessage().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                Toast.makeText(AuthActivity.this, s, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void addLocalUserInSharedPreferences(boolean isLocalLogin, String userId) {
        SharedPreferences preferences = getSharedPreferences(PREFERENCES_KEY, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(LOGGED_IN_ID_KEY, true);
        editor.putBoolean(LOCAL_LOGGED_IN_ID_KEY, isLocalLogin);
        editor.putBoolean(GUEST_SESSION_ID_KEY, false);
        editor.putString(USER_ID_KEY, userId);
        editor.apply();
    }

    private void setGuestSharedPreferences() {
        SharedPreferences preferences = getSharedPreferences(PREFERENCES_KEY, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(LOGGED_IN_ID_KEY, false);
        editor.putBoolean(LOCAL_LOGGED_IN_ID_KEY, false);
        editor.putBoolean(GUEST_SESSION_ID_KEY, true);
        editor.putString(USER_ID_KEY, "guest");
        editor.apply();
    }

    private void goToMainActivity(boolean isLocalLogin, String userId){
        addLocalUserInSharedPreferences(isLocalLogin, userId);
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private void goToGuestActivity(){
        Intent intent = new Intent(this, GuestActivity.class);
        startActivity(intent);
    }

    public interface AuthActivityCallback {
        void goToMainActivity(boolean isLocalLogin, String userId);
    }
}