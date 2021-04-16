package com.catasoft.ip_finder.ui.changed;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.catasoft.ip_finder.R;
import com.catasoft.ip_finder.data.entities.SearchInfo;
import com.catasoft.ip_finder.databinding.ActivityIpChangedBinding;
import com.catasoft.ip_finder.ui.main.home.HomeFragment;

public class IpChangedActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // set data binding
        ActivityIpChangedBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_ip_changed);
        binding.setLifecycleOwner(this);

        Intent intent = getIntent();
        SearchInfo previousInfo = (SearchInfo) intent.getSerializableExtra(HomeFragment.PREVIOUS_INFO_ID);
        SearchInfo currentInfo = (SearchInfo) intent.getSerializableExtra(HomeFragment.CURRENT_INFO_ID);

        binding.setPreviousInfo(previousInfo);
        binding.setCurrentInfo(currentInfo);
    }
}