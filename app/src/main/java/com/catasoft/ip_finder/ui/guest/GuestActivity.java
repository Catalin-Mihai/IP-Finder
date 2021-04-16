package com.catasoft.ip_finder.ui.guest;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.catasoft.ip_finder.R;
import com.catasoft.ip_finder.databinding.ActivityGuestBinding;

public class GuestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // set data binding
        ActivityGuestBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_guest);
        binding.setLifecycleOwner(this);
    }
}