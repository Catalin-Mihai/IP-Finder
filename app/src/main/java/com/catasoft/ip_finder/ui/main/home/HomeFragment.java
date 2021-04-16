package com.catasoft.ip_finder.ui.main.home;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.catasoft.ip_finder.R;
import com.catasoft.ip_finder.data.entities.SearchInfo;
import com.catasoft.ip_finder.databinding.HomeFragmentBinding;
import com.catasoft.ip_finder.ui.auth.AuthActivity;
import com.catasoft.ip_finder.ui.changed.IpChangedActivity;
import com.catasoft.ip_finder.ui.main.MainActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class HomeFragment extends Fragment {

    public static final String PREVIOUS_INFO_ID = "PREVIOUS_INFO_ID";
    public static final String CURRENT_INFO_ID = "CURRENT_INFO_ID";

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
                goToAuthActivity();
            }
        });
        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
        updateCurrentUserIp();
    }

    private void updateCurrentUserIp(){
        homeViewModel.updateCurrentUserIp(new HomeFragmentCallback() {
            @Override
            public void showIpChangedNotification(String title, String message, SearchInfo previousInfo, SearchInfo currentInfo) {
                createIpChangedNotification(title, message, previousInfo, currentInfo);
            }

            @Override
            public HomeFragment getFragment() {
                return HomeFragment.this;
            }
        });
    }

    private void resetSharedPreferences() {
        SharedPreferences preferences = requireActivity().getSharedPreferences(AuthActivity.LOGIN_STATUS_KEY, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(AuthActivity.LOGGED_IN, false);
        editor.putBoolean(AuthActivity.LOCAL_LOGGED_IN, false);
        editor.putBoolean(AuthActivity.GUEST_SESSION, false);
        editor.putLong(AuthActivity.USER_ID, AuthActivity.NO_USER);
        editor.apply();
    }

    private void goToAuthActivity(){
        Intent intent = new Intent(requireContext(), MainActivity.class);
        startActivity(intent);
        requireActivity().finish();
    }

    private void setViewModel(){
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        homeViewModel.getLiveSearchCurrentUser().observe(getViewLifecycleOwner(), new Observer<SearchInfo>() {
            @Override
            public void onChanged(SearchInfo searchInfo) {
                addMarker(searchInfo);
            }
        });
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

    public void createIpChangedNotification(String title, String message, SearchInfo previousInfo, SearchInfo currentInfo){

        Intent notifyIntent = new Intent(requireContext(), IpChangedActivity.class);
        notifyIntent.putExtra(PREVIOUS_INFO_ID,previousInfo);
        notifyIntent.putExtra(CURRENT_INFO_ID,currentInfo);

        // Set the Activity to start in a new, empty task
        notifyIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        // Create the PendingIntent
        PendingIntent notifyPendingIntent = PendingIntent.getActivity(
                requireContext(), 0, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT
        );

        String CHANNEL_ID = "ip-changed-notification-id";
        NotificationCompat.Builder builder = new NotificationCompat.Builder(requireContext(), CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle(title)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                .setContentIntent(notifyPendingIntent)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "ip-changed-notification-channel";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);

            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = requireContext().getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(requireContext());
        // notificationId is a unique int for each notification that you must define
        notificationManager.notify(1, builder.build());
    }

    public interface HomeFragmentCallback {
        void showIpChangedNotification(String title, String message, SearchInfo previousInfo, SearchInfo currentInfo);
        HomeFragment getFragment();
    }

}