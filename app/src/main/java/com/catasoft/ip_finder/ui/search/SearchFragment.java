package com.catasoft.ip_finder.ui.search;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.catasoft.ip_finder.R;
import com.catasoft.ip_finder.databinding.HomeFragmentBinding;
import com.catasoft.ip_finder.databinding.SearchFragmentBinding;
import com.catasoft.ip_finder.ui.home.HomeViewModel;

public class SearchFragment extends Fragment {

    private SearchViewModel mViewModel;

    public static SearchFragment newInstance() {
        return new SearchFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        SearchFragmentBinding binding = SearchFragmentBinding.inflate(inflater);
        binding.setLifecycleOwner(this);
        mViewModel = new ViewModelProvider(this).get(SearchViewModel.class);
        binding.setViewModel(mViewModel);

        binding.btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewModel.makeRequest();
            }
        });

        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(SearchViewModel.class);
        // TODO: Use the ViewModel


    }

}