package com.catasoft.ip_finder.ui.main.history;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.catasoft.ip_finder.data.entities.SearchInfo;
import com.catasoft.ip_finder.databinding.HistoryFragmentBinding;
import com.catasoft.ip_finder.ui.searchinfo.SearchItemActivity;

import java.util.List;

public class HistoryFragment extends Fragment {

    public static final String SEARCH_ID = "SEARCH_ID";

    private HistoryFragmentBinding binding;
    private HistoryViewModel mViewModel;
    private HistoryAdapter historyAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = HistoryFragmentBinding.inflate(inflater);
        binding.setLifecycleOwner(this);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(HistoryViewModel.class);

        startRecyclerView();

        // set observers
        mViewModel.getLiveToastMessage().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                Toast.makeText(getContext(), s, Toast.LENGTH_LONG).show();
            }
        });

        mViewModel.getAllLiveCurrentUserSearches().observe(getViewLifecycleOwner(), new Observer<List<SearchInfo>>() {
            @Override
            public void onChanged(List<SearchInfo> searches) {
                historyAdapter.setSearchInfoList(searches);
            }
        });
    }

    private void startRecyclerView(){
        LinearLayoutManager manager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        binding.rvHistory.setLayoutManager(manager);
        binding.rvHistory.addItemDecoration(new ItemDecoration(10));

        historyAdapter = new HistoryAdapter(mViewModel.getAllLiveCurrentUserSearches().getValue(), new HistoryAdapter.AdapterListener(){
            @Override
            public void onItemDelete(SearchInfo value) {
                mViewModel.delete(value);
            }

            @Override
            public void startSearchItemActivity(SearchInfo searchInfo) {
                Intent intent = new Intent(getContext(), SearchItemActivity.class);
                intent.putExtra(SEARCH_ID,searchInfo);
                startActivity(intent);
            }
        });
        binding.rvHistory.setAdapter(historyAdapter);
    }

}