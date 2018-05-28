package com.example.branko.tester;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.Point;
import android.media.Image;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.branko.tester.model.CityInfo;
import com.example.branko.tester.services.CityDetailsIntentService;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Locale;

public class DetailsActivity extends AppCompatActivity {

    private static final String EXTRA_FIRST_CITY = "com.example.branko.tester.first.city";
    private static final String EXTRA_SECOND_CITY = "com.example.branko.tester.second.city";

    private CityInfo mFirstCity;
    private CityInfo mSecondCity;

    private PieChart mFirstCityTrafficPieChart;
    private PieChart mFirstCityCO2PieChart;
    private PieChart mSecondCityTrafficPieChart;
    private PieChart mSecondCityCO2PieChart;

    private BarChart mBarChart;

    private ImageView mFirstCityImageMap;
    private ImageView mSecondCityImageView;

    private SwipeRefreshLayout mSwipeRefreshLayout;

    private TextView mFirstCityName;
    private TextView mSecondCityName;
    private ImageView mLoadingGifImageView;


    // USED
    private BroadcastReceiver mOnShowCityDetailsNotification = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            initComponentsForDisplay();
            mFirstCity = intent.getExtras().getParcelable(EXTRA_FIRST_CITY);
            mSecondCity = intent.getExtras().getParcelable(EXTRA_SECOND_CITY);
            // POPULATE DATA
            populateTextViews();
            populateImageMaps();
            populateCharts(mFirstCity, mFirstCityTrafficPieChart, mFirstCityCO2PieChart);
            populateCharts(mSecondCity, mSecondCityTrafficPieChart, mSecondCityCO2PieChart);
            populateBarChart();
            if (mSwipeRefreshLayout.isRefreshing()) {
                setChartsAfterSwipe();
            }
        }
    };

    // USED
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
        initViewForGif();
        getIntentData();
    }

    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter filter = new IntentFilter(CityDetailsIntentService.ACTION_SHOW_NOTIFICATION);
        registerReceiver(mOnShowCityDetailsNotification, filter);
        startCityDetailsIntentService();
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(mOnShowCityDetailsNotification);
    }

    // USED
    public static Intent newIntent(Context packageContext, CityInfo firstCity, CityInfo secondCity) {
        Intent intent = new Intent(packageContext, DetailsActivity.class);
        Bundle extras = new Bundle();
        extras.putParcelable(EXTRA_FIRST_CITY, firstCity);
        extras.putParcelable(EXTRA_SECOND_CITY, secondCity);
        intent.putExtras(extras);
        return intent;
    }

    // USED
    public static Intent newBroadcastIntent(CityInfo firstCity, CityInfo secondCity, String action) {
        Intent intent = new Intent(action);
        Bundle extras = new Bundle();
        extras.putParcelable(EXTRA_FIRST_CITY, firstCity);
        extras.putParcelable(EXTRA_SECOND_CITY, secondCity);
        intent.putExtras(extras);
        return intent;
    }

    private void initViewForGif() {
        mLoadingGifImageView = findViewById(R.id.loadingGif);
        loadGifIntoGifImageView();
    }

    private void loadGifIntoGifImageView() {
        Glide.with(getApplicationContext())
                .load(R.drawable.preloader)
                .into(mLoadingGifImageView);
    }

    private void initComponentsForDisplay() {
        setContentView(R.layout.activity_details);
        initSwipe();
        initTextViews();
        initPieCharts();
        initBarChart();
        initImageMaps();
    }

    private void initSwipe() {
        mSwipeRefreshLayout = findViewById(R.id.Swipe);
        bindRefreshListenerForSwipe();
    }

    private void initTextViews() {
        mFirstCityName = findViewById(R.id.firstCityName);
        mSecondCityName = findViewById(R.id.secondCityName);
    }

    // USED
    private void getIntentData() {
        Intent intent = getIntent();
        mFirstCity = intent.getParcelableExtra(EXTRA_FIRST_CITY);
        mSecondCity = intent.getParcelableExtra(EXTRA_SECOND_CITY);
    }

    // USED
    private void startCityDetailsIntentService() {
        Intent intent = CityDetailsIntentService.newIntent(DetailsActivity.this, mFirstCity, mSecondCity);
        startService(intent);
    }

    private void initPieCharts() {
        mFirstCityTrafficPieChart = findViewById(R.id.firstCityTrafficPieChart);
        mFirstCityCO2PieChart = findViewById(R.id.firstCityCO2PieChart);
        mSecondCityTrafficPieChart = findViewById(R.id.secondCityTrafficPieChart);
        mSecondCityCO2PieChart = findViewById(R.id.secondCityCO2PieChart);
        bindhartValueSelectedListenerForTrafficPieCharts();
    }

    private void initBarChart() {
        mBarChart = findViewById(R.id.barChart);
    }

    private void initImageMaps() {
        mFirstCityImageMap = findViewById(R.id.firstCityImageMap);
        mSecondCityImageView = findViewById(R.id.secondCityImageMap);
        bindClickEventListenerForFirstCityImageMap();
        bindClickEventListenerForSecondCityImageMap();
    }

    private String getURL(CityInfo cityInfo) {
        return String.format(Locale.US, "https://maps.googleapis.com/maps/api/staticmap?center=%.5f,%.5f&zoom=13&size=500x250&key=AIzaSyAxyzqcHYdOKtwdaOTJKGsA9k9UJaeb1gQ", cityInfo.getLat(), cityInfo.getLon());
    }

    private void populateTextViews() {
        mFirstCityName.setText(mFirstCity.getName());
        mSecondCityName.setText(mSecondCity.getName());
    }

    private void populateCharts(CityInfo cityInfo, PieChart trafficPieChart, PieChart CO2PieChart) {
        populateTrafficPieChart(cityInfo, trafficPieChart);

        populateCO2PieChart(cityInfo, CO2PieChart);

    }

    private void populateTrafficPieChart(CityInfo cityInfo, PieChart trafficPieChart) {
        ArrayList<PieEntry> entries = cityInfo.getTrafficPieEntries();
        ArrayList<Integer> colors = cityInfo.getTrafficColors();

        PieDataSet dataSet = new PieDataSet(entries, "Traffic jam");
        dataSet.setColors(colors);
        dataSet.setSliceSpace(2f);
        PieData data = new PieData(dataSet);
        data.setDrawValues(false);

        trafficPieChart.setData(data);
        trafficPieChart.setUsePercentValues(true);
        trafficPieChart.invalidate();
        trafficPieChart.setCenterTextSize(24);
        trafficPieChart.setCenterTextColor(Color.parseColor("#ffffff"));
        trafficPieChart.setCenterText("");
        trafficPieChart.setHoleColor(Color.parseColor("#00000000"));
        trafficPieChart.setRotationEnabled(false);

        trafficPieChart.setDrawEntryLabels(false);
        trafficPieChart.getDescription().setEnabled(false);
        trafficPieChart.getLegend().setEnabled(false);
        trafficPieChart.setTouchEnabled(true);
    }

    private void populateCO2PieChart(CityInfo cityInfo, PieChart co2PieChart) {
        ArrayList<PieEntry> entries = cityInfo.getCO2PieEntries();
        ArrayList<Integer> colors = cityInfo.getCO2Colors();

        PieDataSet dataSet = new PieDataSet(entries, "CO2");
        dataSet.setColors(colors);
        PieData data = new PieData(dataSet);
        data.setDrawValues(false);

        co2PieChart.setData(data);
        co2PieChart.setUsePercentValues(true);
        co2PieChart.invalidate();
        co2PieChart.setCenterText(String.format("%.1f", cityInfo.getCO2PieChartCenterValue()));
        co2PieChart.setHoleRadius(80f);
        co2PieChart.setCenterTextSize(34);
        co2PieChart.setHoleColor(Color.parseColor("#00000000"));
        co2PieChart.setCenterTextColor(Color.parseColor("#ffffff"));

        co2PieChart.setDrawEntryLabels(false);
        co2PieChart.getDescription().setEnabled(false);
        co2PieChart.getLegend().setEnabled(false);
        co2PieChart.setTouchEnabled(false);
        co2PieChart.setHighlightPerTapEnabled(false);
    }


    private void populateBarChart() {
        ArrayList<BarEntry> firstCityGroup = mFirstCity.getBarEntries();
        ArrayList<BarEntry> secondCityGroup = mSecondCity.getBarEntries();

        BarDataSet firstCitySet = new BarDataSet(firstCityGroup, "Group 1");
        BarDataSet secondCitySet = new BarDataSet(secondCityGroup, "Group 2");

        float groupSpace = 0.1f;
        float barSpace = 0.05f; // x3 DataSet
        float barWidth = 0.4f; // x3 DataSet

        BarData data = new BarData(firstCitySet, secondCitySet);
        data.setBarWidth(barWidth);
        mBarChart.setData(data);
        mBarChart.groupBars(0.5f, groupSpace, barSpace); // perform the "explicit" grouping
        mBarChart.invalidate(); // refresh
    }

    private void populateImageMaps() {
        String firstCityUrl = getURL(mFirstCity);
        String secondCityUrl = getURL(mSecondCity);

        populateMapImage(firstCityUrl, mFirstCityImageMap);
        populateMapImage(secondCityUrl, mSecondCityImageView);
    }

    private void populateMapImage(String url, ImageView image) {
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x - 150;
        double factor = width / 500.0;
        Double x = 500 * factor;
        Double y = 250 * factor;

        Picasso.with(this).load(url).resize(x.intValue(), y.intValue()).into(image);
    }

    private void bindClickEventListenerForFirstCityImageMap() {
        mFirstCityImageMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = MapActivity.newIntent(DetailsActivity.this, mFirstCity.getLat(), mFirstCity.getLon());
                startActivity(intent);
            }
        });
    }

    private void bindClickEventListenerForSecondCityImageMap() {
        mSecondCityImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = MapActivity.newIntent(DetailsActivity.this, mSecondCity.getLat(), mSecondCity.getLon());
                startActivity(intent);
            }
        });
    }

    private void bindhartValueSelectedListenerForTrafficPieCharts() {
        mFirstCityTrafficPieChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                mFirstCityTrafficPieChart.setCenterText(String.format("%s:\n %.2f%%", ((PieEntry) e).getLabel(), ((PieEntry) e).getValue()));
            }

            @Override
            public void onNothingSelected() {
                mFirstCityTrafficPieChart.setCenterText("");
            }
        });

        mSecondCityTrafficPieChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                mSecondCityTrafficPieChart.setCenterText(String.format("%s:\n %.2f%%", ((PieEntry) e).getLabel(), ((PieEntry) e).getValue()));
            }

            @Override
            public void onNothingSelected() {
                mSecondCityTrafficPieChart.setCenterText("");
            }
        });
    }

    private void bindRefreshListenerForSwipe() {
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                startCityDetailsIntentService();
            }
        });
    }

    private void setChartsAfterSwipe() {
        /*if(mFirstCityClicedEntry != null)
            mFirstCityTrafficPieChart.setCenterText(String.format("%s:\n %.2f%%", mFirstCityClicedEntry.getLabel(),mFirstCityClicedEntry.getValue()));
        if(mSecondCityClicedEntry != null)
            mFirstCityTrafficPieChart.setCenterText(String.format("%s:\n %.2f%%", mSecondCityClicedEntry.getLabel(),mSecondCityClicedEntry.getValue()));*/
        mFirstCityTrafficPieChart.highlightValue(null);
        mSecondCityTrafficPieChart.highlightValue(null);
        mSwipeRefreshLayout.setRefreshing(false);
    }
}
