package com.catasoft.ip_finder.ui.home;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.catasoft.ip_finder.MainActivity;
import com.catasoft.ip_finder.databinding.HomeFragmentBinding;
import com.catasoft.ip_finder.ui.auth.AuthActivity;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        // set data binding
        HomeFragmentBinding binding = HomeFragmentBinding.inflate(inflater);
        binding.setLifecycleOwner(this);

        // set view model
        setViewModel();
        binding.setViewModel(homeViewModel);

        // set listeners
        binding.btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                homeViewModel.logout();
                resetSharedPreferences();
            }
        });

        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    private void resetSharedPreferences() {
        SharedPreferences preferences = getActivity().getSharedPreferences(AuthActivity.PREFERENCES_KEY, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(AuthActivity.LOGGED_IN_ID_KEY, false);
        editor.putBoolean(AuthActivity.LOCAL_LOGGED_IN_ID_KEY, false);
        editor.putBoolean(AuthActivity.GUEST_SESSION_ID_KEY, false);
        editor.putString(AuthActivity.USER_ID_KEY, "");
        editor.apply();

        goToAuthActivity();
    }

    private void goToAuthActivity(){
        Intent intent = new Intent(getContext(), MainActivity.class);
        startActivity(intent);
    }

    private void setViewModel(){
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
    }

}