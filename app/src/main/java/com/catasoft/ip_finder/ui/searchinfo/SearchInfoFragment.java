package com.catasoft.ip_finder.ui.searchinfo;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.catasoft.ip_finder.R;
import com.catasoft.ip_finder.data.entities.SearchInfo;
import com.catasoft.ip_finder.databinding.FragmentSearchInfoBinding;
import com.catasoft.ip_finder.databinding.SearchFragmentBinding;
import com.catasoft.ip_finder.ui.search.SearchViewModel;

public class SearchInfoFragment extends Fragment {

    private static final String SEARCH_ITEM = "SEARCH_ITEM";
    private SearchInfo searchInfo;

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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            searchInfo = (SearchInfo) getArguments().getSerializable(SEARCH_ITEM);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentSearchInfoBinding binding = FragmentSearchInfoBinding.inflate(inflater);
        binding.setLifecycleOwner(this);
        binding.setSearchInfo(searchInfo);
        return binding.getRoot();
    }
}