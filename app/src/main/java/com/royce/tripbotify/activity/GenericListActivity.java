package com.royce.tripbotify.activity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.amadeus.Amadeus;
import com.amadeus.Params;
import com.amadeus.exceptions.ResponseException;
import com.amadeus.referenceData.Locations;
import com.amadeus.resources.AirTraffic;
import com.amadeus.resources.FlightDate;
import com.amadeus.resources.FlightOffer;
import com.amadeus.resources.Location;
import com.amadeus.resources.SearchedDestination;
import com.royce.tripbotify.R;
import com.royce.tripbotify.adapter.FlightsAdapter;
import com.royce.tripbotify.adapter.RealmPointsAdapter;
import com.royce.tripbotify.adapter.RealmTrackedFlightsAdapter;
import com.royce.tripbotify.adapter.RealmTranslationsAdapter;
import com.royce.tripbotify.database.RealmPointOfInterest;
import com.royce.tripbotify.database.RealmTrackFlight;
import com.royce.tripbotify.database.RealmTranslation;
import com.royce.tripbotify.utils.AppConstants;
import com.royce.tripbotify.utils.PreferenceManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

public class GenericListActivity extends AppCompatActivity {

    public static int TYPE_FLIGHT_CHEAPEST_FARES = 1;
    public static int TYPE_FLIGHT_FARES = 2;
    public static int REALM_PLACES = 3;
    public static int REALM_TRANSLATIONS = 4;
    public static int REALM_FLIGHTS = 5;

    private RecyclerView mList;
    private int type;
    // fallback to BARCELONA if no desti code
    private String airportCode = "BCN", iata = "", date = "2019-08-01";
    private List<FlightDate> dates = new ArrayList<>();
    private List<FlightOffer> offers = new ArrayList<>();
    private FlightsAdapter mFlightsAdapter;
    private TextView mTip;
    private ProgressBar mProgressBar;
    private String tip = "Pro Tip : ";

    private Realm realm;
    private RealmResults<RealmPointOfInterest> points;
    private RealmResults<RealmTranslation> translations;
    private RealmResults<RealmTrackFlight> flights;

    /* private RealmPointsAdapter mPointsAdapter;
     private RealmTranslationsAdapter mTranslationsAdapter;
     private RealmTrackedFlightsAdapter mFlightsRealmAdapter;
 */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generic_list);
        mList = findViewById(R.id.list);
        mProgressBar = findViewById(R.id.progress_bar);
        mList.setLayoutManager(new LinearLayoutManager(this));
        mTip = findViewById(R.id.list_tip);
        mTip.setVisibility(View.GONE);
        iata = PreferenceManager.getInstance(this).getString(AppConstants.PREFS_IATA);
        // fallback to Bombay if no origin code
        if (iata.contentEquals(""))
            iata = "IAD";
        getIntentData();
        mProgressBar.setVisibility(View.VISIBLE);
        if (type < 3)
            getData();
        else
            getRealmData();
    }

    private void getRealmData() {
        realm = Realm.getDefaultInstance();
        if (type == REALM_PLACES) {
            points = realm.where(RealmPointOfInterest.class).
                    findAll();
            //mPointsAdapter = new RealmPointsAdapter(points);
            mList.setAdapter(new RealmPointsAdapter(points));
        } else if (type == REALM_TRANSLATIONS) {
            translations = realm.where(RealmTranslation.class).
                    findAll();
            //  mTranslationsAdapter = new RealmTranslationsAdapter(translations);
            mList.setAdapter(new RealmTranslationsAdapter(translations));
        } else {
            flights = realm.where(RealmTrackFlight.class).
                    findAll();
            // mFlightsRealmAdapter = new RealmTrackedFlightsAdapter(flights);
            mList.setAdapter(new RealmTrackedFlightsAdapter(flights));
        }
        mProgressBar.setVisibility(View.GONE);
        realm.executeTransaction(realm -> {
            RealmResults<RealmTrackFlight> result = realm.where(RealmTrackFlight.class).
                    equalTo("date", "Time Date Parse Error").findAll();
            Log.i(AppConstants.LOG_TAG, "found rows -> " + result.asJSON());
            result.deleteAllFromRealm();
        });
    }

    private void getData() {
        AsyncTask.execute(() -> {
            Amadeus amadeus = Amadeus
                    .builder("hiQb0uhPPNuSGtm5ssxapQ2K8nHvNJ3n", "vanIVyyEeaFqgYGL")
                    .build();
            try {

                if (type == TYPE_FLIGHT_FARES) {
                    FlightDate[] flightDates = amadeus.shopping.flightDates.get(Params
                            .with("origin", iata)
                            .and("destination", airportCode));

                    dates = new ArrayList<>(Arrays.asList(flightDates));

                } else if (type == TYPE_FLIGHT_CHEAPEST_FARES) {
                    FlightOffer[] flightOffers = amadeus.shopping.flightOffers.get(Params
                            .with("origin", iata)
                            .and("destination", airportCode)
                            .and("departureDate", date));
                    offers = new ArrayList<>(Arrays.asList(flightOffers));
                }


                runOnUiThread(this::setAdapter);

            } catch (ResponseException e) {
                runOnUiThread(() -> mProgressBar.setVisibility(View.GONE));
                Log.i(AppConstants.LOG_TAG, e.getCode() + e.getDescription());
            }
            try {
                SearchedDestination searchedDestination = amadeus.travel.analytics.airTraffic.searchedByDestination.get(Params
                        .with("originCityCode", iata)
                        //.and("destinationCityCode", airportCode)
                        .and("searchPeriod", "2017-08")
                        .and("marketCountryCode", "ES"));

                Log.i(AppConstants.LOG_TAG, searchedDestination.toString());


            } catch (ResponseException e) {
                Log.i(AppConstants.LOG_TAG, e.getDescription());
            }

            try {
                AirTraffic[] airTraffics = amadeus.travel.analytics.airTraffic.booked.get(Params
                        .with("originCityCode", iata)
                        .and("period", "2017-08"));
                getMostBooked(amadeus, airTraffics, true);
            } catch (ResponseException e) {
                Log.i(AppConstants.LOG_TAG, e.getDescription());
            }

            try {

                AirTraffic[] airTraffics2 = amadeus.travel.analytics.airTraffic.traveled.get(Params
                        .with("originCityCode", iata)
                        .and("period", "2017-08"));
                getMostBooked(amadeus, airTraffics2, false);
            } catch (ResponseException e) {
                Log.i(AppConstants.LOG_TAG, e.getDescription());
            }
        });
    }

    private void getMostBooked(Amadeus amadeus, AirTraffic[] airTraffics, boolean isBooked) {
        if (airTraffics == null || airTraffics.length == 0)
            return;
        String tag = isBooked ? " booked " : " travelled ";
        String message = "The most" + tag + "destinations from your hometown for the last month was ";
        for (int i = 0; i < airTraffics.length; i++) {
            message += getCity(amadeus, airTraffics[i].getDestination());
            if (i < 2)
                message += ", ";
            else if (i == 3)
                message += " and";
            else
                break;
        }
        message += " in that order \n";
        tip += message;
        Log.i(AppConstants.LOG_TAG, message);
        runOnUiThread(() -> new Handler().postDelayed(() -> {
            mTip.setText(tip);
            mTip.setVisibility(View.VISIBLE);
        }, 1000));

    }

    private String getCity(Amadeus amadeus, String code) {
        try {
            return amadeus.referenceData.locations.get(Params
                    .with("keyword", code)
                    .and("subType", Locations.ANY))[0].getAddress().getCityName();
        } catch (ResponseException ignored) {
            return "Not deciphered";
        }
    }

    private void setAdapter() {
        if (dates.size() == 0)
            mFlightsAdapter = new FlightsAdapter(offers, date, iata, airportCode);
        else
            mFlightsAdapter = new FlightsAdapter(dates, iata, airportCode);
        mList.setAdapter(mFlightsAdapter);
        mProgressBar.setVisibility(View.GONE);
    }

    private void getIntentData() {
        type = getIntent().getIntExtra("type", TYPE_FLIGHT_FARES);
        airportCode = getIntent().getStringExtra("code");
        date = getIntent().getStringExtra("date");
//        Log.i(AppConstants.LOG_TAG,date);
        if (getSupportActionBar() != null) {
            if (type == TYPE_FLIGHT_FARES)
                getSupportActionBar().setTitle("Cheapest Flight Dates");
            else if (type == TYPE_FLIGHT_CHEAPEST_FARES)
                getSupportActionBar().setTitle("Fares");
            else if (type == REALM_TRANSLATIONS)
                getSupportActionBar().setTitle("Saved Translations");
            else if (type == REALM_PLACES)
                getSupportActionBar().setTitle("Liked Places");
            else
                getSupportActionBar().setTitle("Tracked Flights");
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            onBackPressed();
        return super.onOptionsItemSelected(item);
    }

}
