package com.catasoft.ip_finder.ui.search;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.catasoft.ip_finder.R;
import com.catasoft.ip_finder.data.entities.SearchInfo;
import com.catasoft.ip_finder.databinding.SearchFragmentBinding;
import com.catasoft.ip_finder.ui.auth.AuthActivity;
import com.catasoft.ip_finder.ui.searchinfo.SearchInfoFragment;

public class SearchFragment extends Fragment {

    private SearchInfoFragment searchInfoFragment;
    private SearchViewModel searchViewModel;

    public static SearchFragment newInstance() {
        return new SearchFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // set data binding
        SearchFragmentBinding binding = SearchFragmentBinding.inflate(inflater);
        binding.setLifecycleOwner(this);

        setViewModel();

        // set view model
        binding.setViewModel(searchViewModel);

        // set listeners
        binding.btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchViewModel.makeRequest();
            }
        });

        binding.btnLove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchViewModel.saveSearch();
            }
        });

        // load search result fragment in this fragment
        if(getActivity() != null){
            searchInfoFragment = SearchInfoFragment.newInstance(null);
            getActivity().getSupportFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    .add(R.id.searchItemFragment, searchInfoFragment, null)
                    .commit();
        }

        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    private void setViewModel(){
        searchViewModel = new ViewModelProvider(this).get(SearchViewModel.class);

        // set visibility for add button
        SharedPreferences preferences = getActivity().getSharedPreferences(AuthActivity.PREFERENCES_KEY, Context.MODE_PRIVATE);
        searchViewModel.setLiveGuestSession(preferences.getBoolean(AuthActivity.GUEST_SESSION,false));

        // set view model observers
        searchViewModel.liveSearch.observe(getViewLifecycleOwner(), new Observer<SearchInfo>() {
            @Override
            public void onChanged(SearchInfo searchInfo) {
                searchInfoFragment.updateValue(searchInfo);
            }
        });

        searchViewModel.getLiveToastMessage().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String message) {
                Toast.makeText(getContext(),message, Toast.LENGTH_SHORT).show();
            }
        });
    }
}