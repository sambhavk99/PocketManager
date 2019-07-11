package com.example.pokktmanager;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

public class CompareActivity extends AppCompatActivity implements CompareHomeFragment.OnDateRangeSendListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compare);

        if(findViewById(R.id.compare_fragment_container)!=null){
            if(savedInstanceState!=null)
            {
                return;
            }

            getSupportFragmentManager().beginTransaction().add(R.id.compare_fragment_container,new CompareHomeFragment(),null).commit();
        }
    }

    @Override
    public void OnDateSend(String date1, String date2, String date3, String date4) {

        CompareResultFragment compareResultFragment = new CompareResultFragment();
        Bundle bundle = new Bundle();
        String[] arr = {date1,date2,date3,date4};
        bundle.putStringArray("DateRanges",arr);
        compareResultFragment.setArguments(bundle);

        FragmentManager fragmentManager =getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.compare_fragment_container,compareResultFragment,null).addToBackStack(null).commit();

    }
}
