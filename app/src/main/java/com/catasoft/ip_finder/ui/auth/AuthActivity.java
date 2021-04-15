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
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.catasoft.ip_finder.R;
import com.catasoft.ip_finder.data.entities.UserAccount;
import com.catasoft.ip_finder.databinding.ActivityAuthBinding;
import com.catasoft.ip_finder.ui.guest.GuestActivity;
import com.catasoft.ip_finder.ui.helpers.LoadingDialog;
import com.catasoft.ip_finder.ui.main.MainActivity;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

public class AuthActivity extends AppCompatActivity {

    public final static String PREFERENCES_KEY = "LOGIN_STATUS";
    public final static String LOGGED_IN = "LOGGED_IN";
    public final static String LOCAL_LOGGED_IN = "LOCAL_LOGGED_IN";
    public final static String GUEST_SESSION = "GUEST_SESSION";
    public final static String USER_ID = "USER_ID";
    public final static long FIRST_PRIMARY_KEY = 1;
    public final static long NO_USER = -1;

    private AuthViewModel authViewModel;

    private GoogleSignInClient mGoogleSignInClient;
    private static final int RC_SIGN_IN = 9001;

    private DialogFragment loadingDialog;

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
                    public void goToMainActivity(boolean isLocalLogin, long userId) {
                        AuthActivity.this.goToMainActivity(isLocalLogin, userId);
                    }

                    @Override
                    public AuthActivity getActivity() {
                        return AuthActivity.this;
                    }
                });
            }
        });

        binding.btnLocalRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                authViewModel.localRegister(new AuthActivityCallback() {
                    @Override
                    public void goToMainActivity(boolean isLocalLogin, long userId) {
                        AuthActivity.this.goToMainActivity(isLocalLogin, userId);
                    }

                    @Override
                    public AuthActivity getActivity() {
                        return AuthActivity.this;
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
            Toast.makeText(this, "Autentificarea cu google nu a reusit", Toast.LENGTH_LONG).show();
            return;
        }

        authViewModel.googleLogin(new UserAccount(account.getDisplayName(),"firebase","", "",
                        1, false), new AuthActivityCallback(){
            @Override
            public void goToMainActivity(boolean isLocalLogin, long userId) {
                AuthActivity.this.goToMainActivity(false, userId);
            }

            @Override
            public AuthActivity getActivity() {
                return AuthActivity.this;
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

    private void addUserInSharedPreferences(boolean isLocalLogin, long userId) {
        SharedPreferences preferences = getSharedPreferences(PREFERENCES_KEY, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(LOGGED_IN, true);
        editor.putBoolean(LOCAL_LOGGED_IN, isLocalLogin);
        editor.putBoolean(GUEST_SESSION, false);
        editor.putLong(USER_ID, userId);
        editor.apply();
    }

    private void setGuestSharedPreferences() {
        SharedPreferences preferences = getSharedPreferences(PREFERENCES_KEY, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(LOGGED_IN, false);
        editor.putBoolean(LOCAL_LOGGED_IN, false);
        editor.putBoolean(GUEST_SESSION, true);
        editor.putLong(USER_ID, NO_USER);
        editor.apply();
    }

    private void goToMainActivity(boolean isLocalLogin, long userId){
        addUserInSharedPreferences(isLocalLogin, userId);
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        this.finish();
    }

    private void goToGuestActivity(){
        Intent intent = new Intent(this, GuestActivity.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        // disable back from main activity because is not necessary
        //super.onBackPressed();
    }

    @Override
    protected void onStop() {
        dismissLoadingDialog();
        super.onStop();
    }

    public void showLoadingDialog(String message){
        loadingDialog = new LoadingDialog(message);
        loadingDialog.show(getSupportFragmentManager(), "AuthActivity");
    }

    public void dismissLoadingDialog(){
        if(loadingDialog != null){
            loadingDialog.dismiss();
            loadingDialog = null;
        }
    }

    public interface AuthActivityCallback {
        void goToMainActivity(boolean isLocalLogin, long userId);
        AuthActivity getActivity();
    }
}