package com.example.branko.tester;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.branko.tester.model.CityInfo;
import com.example.branko.tester.services.CitiesIntentService;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class FirstPageActivity extends AppCompatActivity {

    public static String FIRST_CITY = "FirstCity";
    public static String SECOND_CITY = "SecondCity";
    public static String AUTOCOMPLETETEXTVIEWNAME = "autocompleteTextViewName";
    public static String CITIES = "cities";

    private AutoCompleteTextView mAutocompleteTextViewFirstCity;
    private AutoCompleteTextView mAutocompleteTextViewSecondCity;
    private Button mCompareButton;
    private CityInfo mFirstCity;
    private CityInfo mSecondCity;

    private List<CityInfo> mFirstCityAutocompleteData;
    private List<CityInfo> mSecondCityAutocompleteData;

    private BroadcastReceiver onShowCitiesNotification = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            List<CityInfo> cities = intent.getExtras().getParcelableArrayList(CITIES);
            String city = intent.getStringExtra(AUTOCOMPLETETEXTVIEWNAME);
            List<String> autoCompleteData = fillAutoCompleteData(cities);
            ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(),R.layout.autocomplete_dropdown_item,autoCompleteData);
            if(city.equals(FIRST_CITY)) {
                mFirstCityAutocompleteData = cities;
                mAutocompleteTextViewFirstCity.setAdapter(adapter);
            }
            else if(city.equals(SECOND_CITY)) {
                mSecondCityAutocompleteData = cities;
                mAutocompleteTextViewSecondCity.setAdapter(adapter);
            }

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_page);
        initResources();
        bindTextEventListener(mAutocompleteTextViewFirstCity, FIRST_CITY);
        bindTextEventListener(mAutocompleteTextViewSecondCity, SECOND_CITY);
        bindItemClickEventListenerForFirstCity();
        bindItemClickEventListenerForSecondCity();
        bindComapreClickEventListener();
    }

    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter filter = new IntentFilter(CitiesIntentService.ACTION_SHOW_NOTIFICATION);
        registerReceiver(onShowCitiesNotification,filter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(onShowCitiesNotification);
    }

    private void initResources(){
        mAutocompleteTextViewFirstCity = findViewById(R.id.firstCityAutoCompleteTextView);
        mAutocompleteTextViewFirstCity.setThreshold(3);
        mAutocompleteTextViewSecondCity = findViewById(R.id.secondCityAutoCompleteTextView);
        mAutocompleteTextViewSecondCity.setThreshold(3);
        mCompareButton = findViewById(R.id.compareButton);
        mFirstCity = null;
        mSecondCity = null;
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
                if(charSequence.length() > 2) {
                    String input = charSequence.toString().toLowerCase();
                    String output = input.substring(0, 1).toUpperCase() + input.substring(1);
                    Intent intent = CitiesIntentService.newIntent(getApplicationContext(), output);
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

    private void bindComapreClickEventListener() {
        mCompareButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                hideKeyboard();
                if(mFirstCity != null && mSecondCity != null) {
                    Intent intent = DetailsActivity.newIntent(FirstPageActivity.this,mFirstCity,mSecondCity);
                    startActivity(intent);
                }
                else{
                    Toast.makeText(FirstPageActivity.this, "You must choose two cities!", Toast.LENGTH_SHORT).show();
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
}
