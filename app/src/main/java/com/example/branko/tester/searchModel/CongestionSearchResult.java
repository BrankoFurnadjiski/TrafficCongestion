package com.example.branko.tester.searchModel;

import com.example.branko.tester.model.TrafficCongestion;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Branko on 4/16/2018.
 */

public class CongestionSearchResult {
    private TrafficCongestion city1;
    private TrafficCongestion city2;

    public List<TrafficCongestion> getSearchResult(){
        return new ArrayList<>(Arrays.asList(city1,city2));
    }
}
