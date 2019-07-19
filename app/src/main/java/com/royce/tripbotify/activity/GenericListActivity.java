package com.royce.tripbotify.activity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;

import com.amadeus.Amadeus;
import com.amadeus.Params;
import com.amadeus.exceptions.ResponseException;
import com.amadeus.resources.FlightDate;
import com.amadeus.resources.FlightOffer;
import com.royce.tripbotify.R;
import com.royce.tripbotify.adapter.FlightsAdapter;
import com.royce.tripbotify.utils.AppConstants;
import com.royce.tripbotify.utils.PreferenceManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GenericListActivity extends AppCompatActivity {

    public static int TYPE_FLIGHT_CHEAPEST_FARES = 1;
    public static int TYPE_FLIGHT_FARES = 2;

    private RecyclerView mList;
    private int type;
    // fallback to BARCELONA if no desti code
    private String airportCode = "BCN", iata = "", date = "2019-08-01";
    private List<FlightDate> dates = new ArrayList<>();
    private List<FlightOffer> offers = new ArrayList<>();
    private FlightsAdapter mFlightsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generic_list);
        mList = findViewById(R.id.list);
        mList.setLayoutManager(new LinearLayoutManager(this));
        iata = PreferenceManager.getInstance(this).getString(AppConstants.PREFS_IATA);
        // fallback to Bombay if no origin code
        if (iata.contentEquals(""))
            iata = "IAD";
        getIntentData();
        getData();
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

                    /*for (FlightDate date : flightDates)
                        Log.i(AppConstants.LOG_TAG, date.toString());
*/
                    dates = new ArrayList<>(Arrays.asList(flightDates));

                } else if (type == TYPE_FLIGHT_CHEAPEST_FARES) {
                    FlightOffer[] flightOffers = amadeus.shopping.flightOffers.get(Params
                            .with("origin", iata)
                            .and("destination", airportCode)
                            .and("departureDate", date));
                    //for (FlightOffer offer : flightOffers)
                    //   Log.i(AppConstants.LOG_TAG, offer.toString());
                    offers = new ArrayList<>(Arrays.asList(flightOffers));
                }

                runOnUiThread(this::setAdapter);

                /*Period[] busiestPeriods = amadeus.travel.analytics.airTraffic.busiestPeriod.get(Params
                        .with("cityCode", airportCode)
                        .and("period", "2018")
                        .and("direction", BusiestPeriod.ARRIVING));

                //runOnUiThread(() -> mProTip.setText(Utils.getBusiestPeriodString(name, busiestPeriods)));*/


            } catch (ResponseException e) {
                /*if(!iata.contentEquals("IAD")) {
                    iata = "IAD";
                    getData();
                }*/
                Log.i(AppConstants.LOG_TAG, e.getCode() + e.getDescription());
            }
        });
    }

    private void setAdapter() {
        if (dates.size() == 0)
            mFlightsAdapter = new FlightsAdapter(offers, date,iata, airportCode);
        else
            mFlightsAdapter = new FlightsAdapter(dates, iata, airportCode);
        mList.setAdapter(mFlightsAdapter);
    }

    private void getIntentData() {
        type = getIntent().getIntExtra("type", TYPE_FLIGHT_FARES);
        airportCode = getIntent().getStringExtra("code");
        date = getIntent().getStringExtra("date");
//        Log.i(AppConstants.LOG_TAG,date);
        if (getSupportActionBar() != null) {
            if (type == TYPE_FLIGHT_FARES)
                getSupportActionBar().setTitle("Cheapest Flights");
            else if (type == TYPE_FLIGHT_CHEAPEST_FARES)
                getSupportActionBar().setTitle("Upcoming fares");
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
