package com.catasoft.ip_finder.ui.searchinfo;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.catasoft.ip_finder.R;
import com.catasoft.ip_finder.data.entities.SearchInfo;
import com.catasoft.ip_finder.databinding.FragmentSearchInfoBinding;
import com.catasoft.ip_finder.databinding.SearchFragmentBinding;
import com.catasoft.ip_finder.ui.search.SearchViewModel;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

public class SearchInfoFragment extends Fragment {

    private static final String SEARCH_ITEM = "SEARCH_ITEM";
    private final MutableLiveData<SearchInfo> liveSearchInfo = new MutableLiveData<>();

    public SearchInfoFragment() {
        // Required empty public constructor
    }

    public static SearchInfoFragment newInstance(SearchInfo searchInfo) {
        SearchInfoFragment fragment = new SearchInfoFragment();
        Bundle args = new Bundle();
        args.putSerializable(SEARCH_ITEM, searchInfo);
        fragment.setArguments(args);
        return fragment;
    }

    public void updateValue(SearchInfo searchInfo){
        liveSearchInfo.setValue(searchInfo);
        addMarker();
    }

    private void addMarker(){
        if(getActivity() != null && liveSearchInfo.getValue() != null){
            // Get the SupportMapFragment and request notification when the map is ready to be used.
            FragmentManager fragmentManager = getChildFragmentManager();
            SupportMapFragment mapFragment = (SupportMapFragment) fragmentManager.findFragmentById(R.id.infoMap);

            if(mapFragment != null){
                mapFragment.getMapAsync(new OnMapReadyCallback(){

                    @Override
                    public void onMapReady(GoogleMap googleMap) {
                        LatLng position = new LatLng(liveSearchInfo.getValue().getLat(), liveSearchInfo.getValue().getLon());
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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            //Initial value
            liveSearchInfo.setValue((SearchInfo) getArguments().getSerializable(SEARCH_ITEM));
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        addMarker();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentSearchInfoBinding binding = FragmentSearchInfoBinding.inflate(inflater);
        binding.setLifecycleOwner(this);
        binding.setSearchInfo(liveSearchInfo);
        return binding.getRoot();
    }
}