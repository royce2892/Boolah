package com.royce.tripbotify.core;

import android.os.AsyncTask;
import android.util.Log;

import com.amadeus.Amadeus;
import com.amadeus.Params;
import com.amadeus.exceptions.ResponseException;
import com.amadeus.resources.FlightDestination;
import com.royce.tripbotify.utils.AppConstants;

import java.util.Arrays;

import okhttp3.OkHttpClient;

public class AmadClient {

    private static Amadeus amadeus;

    private static synchronized Amadeus getInstance() {
        if (amadeus == null) {
            AsyncTask.execute(() -> {
                amadeus = Amadeus
                        .builder("hiQb0uhPPNuSGtm5ssxapQ2K8nHvNJ3n", "vanIVyyEeaFqgYGL")
                        .build();
                //token = ApiClient.getAmadeusAccessToken();
                //Log.i(AppConstants.LOG_TAG, token);
                /*try {
                    FlightDestination[] flightDestinations = amadeus.shopping.flightDestinations.get(Params
                            .with("origin", "MAD"));
                    Log.i(AppConstants.LOG_TAG, Arrays.toString(flightDestinations));
                } catch (ResponseException e) {
                    e.printStackTrace();
                }*/
                //return amadeus;
            });
        }
        return amadeus;
    }
}
