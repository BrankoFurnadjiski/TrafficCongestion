package com.example.branko.tester;

import android.content.Intent;
import android.graphics.Color;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;

import com.example.branko.tester.services.CitiesIntentService;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLayout();

        setToolbar();
        setActionBar();

        setupPieChart(R.id.mainPieChart);
        setupPieChart(R.id.sidePieChart);
        Log.d("CitiesIntentService","before method");
        Intent i = new Intent(getApplicationContext(),CitiesIntentService.class);
        startService(i);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);
        return true;
    }

    private void setLayout() {
        setContentView(R.layout.activity_main);
        mDrawerLayout = findViewById(R.id.drawer_layout);
    }

    private void setToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    private void setActionBar() {
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);
    }

    private void setupPieChart(int id){
        float values[] = new float[]{300.0f, 2080.0f, 3329.0f, 129897.3f};
        String colors[] = new String[]{"brown", "red", "orange", "green"};

        List<PieEntry> listPieEntries = new ArrayList<>();

        for(int i = 0; i < values.length; ++i) {
            listPieEntries.add(new PieEntry(values[i],colors[i]));
        }

        PieDataSet dataSet = new PieDataSet(listPieEntries, "Traffic jam");
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        PieData data = new PieData(dataSet);

        PieChart pieChart = findViewById(id);
        pieChart.setData(data);
        pieChart.setUsePercentValues(true);
    }


}
