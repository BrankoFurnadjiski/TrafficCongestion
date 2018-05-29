package com.example.branko.tester;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import com.example.branko.tester.model.CityInfo;
import com.example.branko.tester.services.CitiesIntentService;
import com.example.branko.tester.utils.CitiesAdapter;
import com.example.branko.tester.utils.StatusBarChanger;

import java.util.ArrayList;
import java.util.List;


public class CitiesActivity extends AppCompatActivity {

    private final String AUTOCOMPLETEEDITTEXTVIEW = "RANDOM";


    private EditText mCityNameEditText;

    private RecyclerView mRecyclerView;
    private CitiesAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private BroadcastReceiver mOnShowCitiesNotification = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            List<CityInfo> cities = intent.getExtras().getParcelableArrayList(AUTOCOMPLETEEDITTEXTVIEW);
            mAdapter.setCities(cities);
            mAdapter.notifyDataSetChanged();
        }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarChanger.changeColorForStatusBar(this);
        setContentView(R.layout.activity_cities);
        initViews();
        bindEventListenerForEditText();
        mLayoutManager = new LinearLayoutManager(CitiesActivity.this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new CitiesAdapter(CitiesActivity.this);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter citiesFilter = new IntentFilter(CitiesIntentService.ACTION_SHOW_NOTIFICATION);
        registerReceiver(mOnShowCitiesNotification,citiesFilter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(mOnShowCitiesNotification);
}

    private void initViews(){
        mRecyclerView = findViewById(R.id.citiesRecyclerView);
        mCityNameEditText = findViewById(R.id.cityNameEditText);
    }

    private void bindEventListenerForEditText(){
        mCityNameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.length() > 1) {
                    String input = charSequence.toString().toLowerCase();
                    String output = input.substring(0, 1).toUpperCase() + input.substring(1);
                    Intent intent = CitiesIntentService.newIntent(getApplicationContext(), output, AUTOCOMPLETEEDITTEXTVIEW, null);
                    startService(intent);
                }
                if(charSequence.length() == 0 ){
                    mAdapter.getCities().clear();
                    mAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }
}
