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
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.branko.tester.services.CitiesIntentService;

import org.w3c.dom.Text;

import java.util.List;

public class FirstPageActivity extends AppCompatActivity {

    public static String FIRST_CITY = "FirstCity";
    public static String SECOND_CITY = "SecondCity";
    public static String AUTOCOMPLETETEXTVIEWNAME = "autocompleteTextViewName";
    public static String CITIES = "cities";

    private AutoCompleteTextView mAutocompleteTextViewFirstCity;
    private AutoCompleteTextView mAutocompleteTextViewSecondCity;

    private BroadcastReceiver onShowCitiesNotification = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            List<String> autoCompleteData = intent.getStringArrayListExtra(CITIES);
            ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(),android.R.layout.simple_dropdown_item_1line,autoCompleteData);
            String city = intent.getStringExtra(AUTOCOMPLETETEXTVIEWNAME);
            if(city.equals(FIRST_CITY)) {
                mAutocompleteTextViewFirstCity.setAdapter(adapter);
            }
            else if(city.equals(SECOND_CITY)) {
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
        mAutocompleteTextViewSecondCity = findViewById(R.id.secondCityAutoCompleteTextView);
    }

    public void bindTextEventListener(final AutoCompleteTextView autocompleteTextView, final String autocompelteName){
        autocompleteTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.length() > 2) {
                    String input = charSequence.toString().toLowerCase();
                    String output = input.substring(0, 1).toUpperCase() + input.substring(1);
                    Intent intent = CitiesIntentService.newIntent(getApplicationContext(), output);
                    intent.putExtra(AUTOCOMPLETETEXTVIEWNAME, autocompelteName);
                    startService(intent);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

}
