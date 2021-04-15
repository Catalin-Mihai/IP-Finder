package com.catasoft.ip_finder.ui.main.home;

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
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.catasoft.ip_finder.ui.main.MainActivity;
import com.catasoft.ip_finder.R;
import com.catasoft.ip_finder.data.entities.SearchInfo;
import com.catasoft.ip_finder.databinding.HomeFragmentBinding;
import com.catasoft.ip_finder.ui.auth.AuthActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

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
    public void onStart() {
        super.onStart();
        homeViewModel.updateCurrentUserIp();

        homeViewModel.getLiveSearchCurrentUser().observe(getViewLifecycleOwner(), new Observer<SearchInfo>() {
            @Override
            public void onChanged(SearchInfo searchInfo) {
                addMarker(searchInfo);
            }
        });
    }

    private void resetSharedPreferences() {
        SharedPreferences preferences = getActivity().getSharedPreferences(AuthActivity.PREFERENCES_KEY, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(AuthActivity.LOGGED_IN, false);
        editor.putBoolean(AuthActivity.LOCAL_LOGGED_IN, false);
        editor.putBoolean(AuthActivity.GUEST_SESSION, false);
        editor.putString(AuthActivity.USER_ID, "");
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

    private void addMarker(SearchInfo searchInfo){
        if(getActivity() != null && searchInfo != null){
            // Get the SupportMapFragment and request notification when the map is ready to be used.
            FragmentManager fragmentManager = getChildFragmentManager();
            SupportMapFragment mapFragment = (SupportMapFragment) fragmentManager.findFragmentById(R.id.map);

            if(mapFragment != null){
                mapFragment.getMapAsync(new OnMapReadyCallback(){
                    @Override
                    public void onMapReady(GoogleMap googleMap) {
                        LatLng position = new LatLng(searchInfo.getLat(), searchInfo.getLon());
                        googleMap.addMarker(new MarkerOptions().position(position));
                        CameraPosition cameraPosition = new CameraPosition.Builder()
                                .target(position)           // Center Set
                                .zoom(11.0f)                // Zoom
                                .bearing(90)                // Orientation of the camera to east
                                .tilt(30)                   // Tilt of the camera to 30 degrees
                                .build();                   // Creates a CameraPosition from the builder
                        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                    }
                });
            }
        }
    }

}