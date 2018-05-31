package com.example.branko.tester;

import android.annotation.SuppressLint;
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
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.branko.tester.model.CityInfo;
import com.example.branko.tester.services.CitiesIntentService;
import com.example.branko.tester.utils.CitiesAdapter;
import com.example.branko.tester.utils.StatusBarChanger;

import java.util.ArrayList;
import java.util.List;


public class CitiesActivity extends AppCompatActivity {

    private final String AUTOCOMPLETEEDITTEXTVIEW = "RANDOM";
    private static final String EXTRA_FIRST_CITY = "com.example.branko.tester.first.city";
    private static final String EXTRA_SECOND_CITY = "com.example.branko.tester.second.city";

    private CityInfo mFirstCity;
    private CityInfo mSecodnCity;

    private EditText mCityNameEditText;

    private ImageView mCloseSearchImageView;

    private RecyclerView mRecyclerView;

    private CitiesAdapter mAdapter;

    private RecyclerView.LayoutManager mLayoutManager;

    private BroadcastReceiver mOnShowCitiesNotification = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            List<CityInfo> cities = intent.getExtras().getParcelableArrayList(AUTOCOMPLETEEDITTEXTVIEW);
            cities = filterData(cities);
            mAdapter.setCities(cities);
            mAdapter.notifyDataSetChanged();
        }
    };

    private List<CityInfo> filterData(List<CityInfo> cities) {
        CityInfo notNull;
        if(mFirstCity == null){
            notNull = mSecodnCity;
        } else {
            notNull = mFirstCity;
        }
        for(int i = 0; i < cities.size(); ++i) {
            if(notNull.toString().equals(cities.get(i).toString())){
                cities.remove(i);
            }
        }
        return cities;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarChanger.changeColorForStatusBar(this);
        setContentView(R.layout.activity_cities);
        getIntentData();
        initViews();
        initAdapterComponents();
        bindEventListenerForEditText();
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

    private void getIntentData() {
        mFirstCity = getIntent().getParcelableExtra(EXTRA_FIRST_CITY);
        mSecodnCity = getIntent().getParcelableExtra(EXTRA_SECOND_CITY);
    }

    private void initViews(){
        mRecyclerView = findViewById(R.id.citiesRecyclerView);
        mCityNameEditText = findViewById(R.id.cityNameEditText);
        mCloseSearchImageView = findViewById(R.id.closeSearchCities);
        bindClickListenerToCloseSearchImageView();
    }

    private void initAdapterComponents() {
        mLayoutManager = new LinearLayoutManager(CitiesActivity.this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new CitiesAdapter(CitiesActivity.this);
        mRecyclerView.setAdapter(mAdapter);
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

    private void bindClickListenerToCloseSearchImageView() {
        mCloseSearchImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CitiesActivity.this.finish();
            }
        });
    }

    public static Intent newIntent(Context packageContext, CityInfo firstCity, CityInfo secondCity) {
        Intent intent = new Intent(packageContext, CitiesActivity.class);
        Bundle extras = new Bundle();
        extras.putParcelable(EXTRA_FIRST_CITY, firstCity);
        extras.putParcelable(EXTRA_SECOND_CITY, secondCity);
        intent.putExtras(extras);
        return intent;
    }

    public CityInfo getmFirstCity() {
        return mFirstCity;
    }

    public CityInfo getmSecodnCity() {
        return mSecodnCity;
    }

    public void hideKeyboard() {
        View view = CitiesActivity.this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(),0);
        }
    }
}
