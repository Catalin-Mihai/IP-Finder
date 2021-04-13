package com.catasoft.ip_finder.ui.search;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.catasoft.ip_finder.R;
import com.catasoft.ip_finder.data.entities.SearchInfo;
import com.catasoft.ip_finder.databinding.HomeFragmentBinding;
import com.catasoft.ip_finder.databinding.SearchFragmentBinding;
import com.catasoft.ip_finder.ui.home.HomeViewModel;
import com.catasoft.ip_finder.ui.searchinfo.SearchInfoFragment;

public class SearchFragment extends Fragment {

    SearchInfoFragment searchInfoFragment;
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

        binding.btnLove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewModel.saveSearch();
            }
        });

        if(getActivity() != null){
            searchInfoFragment = SearchInfoFragment.newInstance(null);
            getActivity().getSupportFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    .add(R.id.searchItemFragment, searchInfoFragment, null)
                    .commit();
        }

        mViewModel.liveSearch.observe(getViewLifecycleOwner(), new Observer<SearchInfo>() {
            @Override
            public void onChanged(SearchInfo searchInfo) {
                searchInfoFragment.updateValue(searchInfo);
            }
        });

        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

}