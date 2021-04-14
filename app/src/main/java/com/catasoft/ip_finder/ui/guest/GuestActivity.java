package com.catasoft.ip_finder.ui.guest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;

import com.catasoft.ip_finder.R;
import com.catasoft.ip_finder.databinding.ActivityGuestBinding;
import com.catasoft.ip_finder.databinding.ActivityMainBinding;

public class GuestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // set data binding
        ActivityGuestBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_guest);
        binding.setLifecycleOwner(this);
    }
}