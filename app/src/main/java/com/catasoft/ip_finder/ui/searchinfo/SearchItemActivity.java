package com.catasoft.ip_finder.ui.searchinfo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;

import com.catasoft.ip_finder.R;
import com.catasoft.ip_finder.data.entities.SearchInfo;
import com.catasoft.ip_finder.databinding.ActivitySearchItemBinding;
import com.catasoft.ip_finder.ui.main.history.HistoryFragment;

public class SearchItemActivity extends AppCompatActivity {

    private SearchInfo searchInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        searchInfo = (SearchInfo) intent.getSerializableExtra(HistoryFragment.SEARCH_ID);

        // set data binding
        ActivitySearchItemBinding activityBinding = DataBindingUtil.setContentView(this,
                R.layout.activity_search_item);
        activityBinding.setLifecycleOwner(this);

        /*
        setSupportActionBar(findViewById(R.id.));
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.setTitle("Search");

            // show back button on toolbar
            // on back button press, it will navigate to parent activity
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }
        */


        if (savedInstanceState == null) {

            getSupportFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    .add(R.id.searchItemFragment, SearchInfoFragment.newInstance(searchInfo), null)
                    .commit();

            /*
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.searchItemFragment,SearchInfoFragment.newInstance(searchInfo))
                    .commit();

             */
        }

        /*
        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(R.id.searchItemFragment, SearchInfoFragment.newInstance(searchInfo), null);
        transaction.addToBackStack(null);
        transaction.commit();
        */

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}