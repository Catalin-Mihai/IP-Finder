package com.catasoft.ip_finder.ui.auth;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.catasoft.ip_finder.MainActivity;
import com.catasoft.ip_finder.R;
import com.catasoft.ip_finder.data.entities.UserAccount;
import com.catasoft.ip_finder.databinding.ActivityAuthBinding;
import com.catasoft.ip_finder.ui.guest.GuestActivity;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

public class AuthActivity extends AppCompatActivity {

    public final static String PREFERENCES_KEY = "LOGIN_STATUS";
    public final static String LOGGED_IN_ID_KEY = "LOGGED_IN_ID_KEY";
    public final static String LOCAL_LOGGED_IN_ID_KEY = "LOCAL_LOGGED_IN_ID_KEY";
    public final static String GUEST_SESSION_ID_KEY = "GUEST_SESSION_ID_KEY";
    public final static String USER_ID_KEY = "USER_ID_KEY";

    private AuthViewModel authViewModel;

    private GoogleSignInClient mGoogleSignInClient;
    private static final int RC_SIGN_IN = 9001;

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

        binding.btnGoogle.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                // Configure Google Sign In
                GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestIdToken(getString(R.string.default_web_client_id))
                        .requestEmail()
                        .build();

                mGoogleSignInClient = GoogleSignIn.getClient(AuthActivity.this, gso);
                signInWithGoogle();
            }
        });
    }

    private void signInWithGoogle(){
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            // Signed in successfully, show authenticated UI.
            handleRegisterOrLogin(account);

        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w("[google-sign-in]", "signInResult:failed code=" + e.getStatusCode());
            handleRegisterOrLogin(null);
        }
    }

    private void handleRegisterOrLogin(GoogleSignInAccount account){
        if(account == null){
            Toast.makeText(this, "Autentificare cu google nu a reusit", Toast.LENGTH_LONG).show();
            return;
        }

        // create local account for room and login
        UserAccount userAccount = new UserAccount(account.getDisplayName(),"firebase",
                "", "", 1, 1);

        authViewModel.googleLogin(userAccount, new AuthActivityCallback(){
            @Override
            public void goToMainActivity(boolean isLocalLogin, String userId) {
                AuthActivity.this.goToMainActivity(false, userId);
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

    private void addUserInSharedPreferences(boolean isLocalLogin, String userId) {
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
        addUserInSharedPreferences(isLocalLogin, userId);
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