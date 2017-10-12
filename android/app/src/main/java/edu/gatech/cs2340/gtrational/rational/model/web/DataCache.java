package edu.gatech.cs2340.gtrational.rational.model.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Robert on 10/11/2017.
 */

public class DataCache {

    /**
     * A mapping of uniqueKey to RatData to store RatData in memory
     */
    private static Map<Integer, WebAPI.RatData> cache = new HashMap<>();

    public static List<WebAPI.RatData> fetchRatData() {
        List<WebAPI.RatData> data = WebAPI.fetchPrelimRatData();
        for (WebAPI.RatData dat : data) {
            cache.put(dat.uniqueKey, dat);
        }
        return data;
    }

    /**
     * A method to return the cached RatData by uniqueKey
     * @param uniqueKey The uniqueKey
     * @return The RatData corresponding to uniqueKey
     */
    public static WebAPI.RatData getRatDataByKey(int uniqueKey) {
        return cache.get(uniqueKey);
    }
}
