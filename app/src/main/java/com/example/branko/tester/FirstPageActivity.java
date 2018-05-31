package com.example.branko.tester;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import com.example.branko.tester.model.CityInfo;
import com.example.branko.tester.services.CitiesIntentService;
import com.example.branko.tester.utils.AlertDialogBuilder;
import com.example.branko.tester.utils.InternetBroadcastReceiver;
import com.example.branko.tester.utils.StatusBarChanger;
import com.example.branko.tester.utils.TrafficFetchr;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class FirstPageActivity extends AppCompatActivity {

    public static String FIRST_CITY = "FirstCity";
    public static String SECOND_CITY = "SecondCity";
    public static String AUTOCOMPLETETEXTVIEWNAME = "autocompleteTextViewName";
    public static String CITIES = "cities";
    private static final int MY_PERMISSIONS_REQUEST_INTERNET = 666;

    private AutoCompleteTextView mAutocompleteTextViewFirstCity;
    private AutoCompleteTextView mAutocompleteTextViewSecondCity;

    private Button mCompareButton;

    private CityInfo mFirstCity;
    private CityInfo mSecondCity;

    private List<CityInfo> mFirstCityAutocompleteData;
    private List<CityInfo> mSecondCityAutocompleteData;

    private ArrayAdapter<String> mAdapter;

    private AlertDialog mAlertDialog;


    private BroadcastReceiver mOnInternetStateChangeNotification = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            AlertDialogBuilder.displayAlertDialogFirstActivity  (context, mAlertDialog);
        }
    };

    private BroadcastReceiver mOnShowCitiesNotification = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            List<CityInfo> cities = intent.getExtras().getParcelableArrayList(CITIES);
            String city = intent.getStringExtra(AUTOCOMPLETETEXTVIEWNAME);
            List<String> autoCompleteData = fillAutoCompleteData(cities);
            mAdapter.clear();
            mAdapter.addAll(autoCompleteData);
            mAdapter.notifyDataSetChanged();
            if(city.equals(FIRST_CITY)) {
                mFirstCityAutocompleteData = cities;
                mAutocompleteTextViewFirstCity.setAdapter(mAdapter);
            }
            else if(city.equals(SECOND_CITY)) {
                mSecondCityAutocompleteData = cities;
                mAutocompleteTextViewSecondCity.setAdapter(mAdapter);
            }

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarChanger.changeColorForStatusBar(this);
        setContentView(R.layout.activity_first_page);
        initResources();
        bindTextEventListener(mAutocompleteTextViewFirstCity, FIRST_CITY);
        bindTextEventListener(mAutocompleteTextViewSecondCity, SECOND_CITY);
        bindItemClickEventListenerForFirstCity();
        bindItemClickEventListenerForSecondCity();
        bindCompareClickEventListener();
        checkForInternetPermission();
    }

    @Override
    protected void onStart() {
        super.onStart();
        InternetBroadcastReceiver.registerReceiver(mOnInternetStateChangeNotification, FirstPageActivity.this);
        IntentFilter citiesFilter = new IntentFilter(CitiesIntentService.ACTION_SHOW_NOTIFICATION);
        registerReceiver(mOnShowCitiesNotification,citiesFilter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(mOnShowCitiesNotification);
        InternetBroadcastReceiver.unregisterReceiver(mOnInternetStateChangeNotification, FirstPageActivity.this);
    }

    private void initResources(){
        initAlertDialog();
        mAutocompleteTextViewFirstCity = findViewById(R.id.firstCityAutoCompleteTextView);
        mAutocompleteTextViewSecondCity = findViewById(R.id.secondCityAutoCompleteTextView);
        mAdapter = new ArrayAdapter<>(getApplicationContext(),R.layout.autocomplete_dropdown_item);
        mCompareButton = findViewById(R.id.compareButton);
        mFirstCity = null;
        mSecondCity = null;
    }

    private void initAlertDialog() {
        mAlertDialog = AlertDialogBuilder.initAlertDialog(this,FirstPageActivity.this);
        mAlertDialog.setCanceledOnTouchOutside(false);
    }

    public void bindTextEventListener(final AutoCompleteTextView autocompleteTextView, final String autocompleteName){
        autocompleteTextView.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(autocompleteName.equals(FIRST_CITY)){
                    mFirstCity = null;
                }
                if(autocompleteName.equals(SECOND_CITY)){
                    mSecondCity = null;
                }
                if(charSequence.length() > 1 ) {
                    String input = charSequence.toString().toLowerCase();
                    String output = input.substring(0, 1).toUpperCase() + input.substring(1);
                    Intent intent = CitiesIntentService.newIntent(getApplicationContext(), output, CITIES, AUTOCOMPLETETEXTVIEWNAME);
                    intent.putExtra(AUTOCOMPLETETEXTVIEWNAME, autocompleteName);
                    startService(intent);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    public List<String> fillAutoCompleteData(List<CityInfo> cities) {
        List<String> autocompleteData = new ArrayList<>();
        for(CityInfo city : cities){
            autocompleteData.add(city.toString());
        }
        return autocompleteData;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mAutocompleteTextViewFirstCity.clearFocus();
        mAutocompleteTextViewSecondCity.clearFocus();
        hideKeyboard();
        return true;
    }

    private void bindItemClickEventListenerForFirstCity() {
        mAutocompleteTextViewFirstCity.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                hideKeyboard();
                mFirstCity = mFirstCityAutocompleteData.get(i);
            }
        });
    }

    private void bindItemClickEventListenerForSecondCity() {
        mAutocompleteTextViewSecondCity.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                hideKeyboard();
                mSecondCity = mSecondCityAutocompleteData.get(i);
            }
        });
    }

    private void bindCompareClickEventListener() {
        mCompareButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                hideKeyboard();
                if(mFirstCity != null && mSecondCity != null) {
                    if(mFirstCity.getName().equals(mSecondCity.getName())){
                        Toast.makeText(FirstPageActivity.this, getString(R.string.first_page_toast_info_different), Toast.LENGTH_SHORT).show();
                    } else {
                        Intent intent = DetailsActivity.newIntent(FirstPageActivity.this, mFirstCity, mSecondCity);
                        startActivity(intent);
                    }
                }
                else{
                    Toast.makeText(FirstPageActivity.this, getString(R.string.first_page_toast_info), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void hideKeyboard() {
        View view = FirstPageActivity.this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(),0);
        }
    }

    private void checkForInternetPermission(){
        if(ContextCompat.checkSelfPermission(FirstPageActivity.this, Manifest.permission.INTERNET)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(FirstPageActivity.this,new String[] {Manifest.permission.INTERNET}, MY_PERMISSIONS_REQUEST_INTERNET);
        }
    }



}
