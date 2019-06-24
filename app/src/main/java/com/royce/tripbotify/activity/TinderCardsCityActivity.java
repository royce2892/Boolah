package com.royce.tripbotify.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import com.amadeus.Amadeus;
import com.amadeus.Params;
import com.amadeus.exceptions.ResponseException;
import com.amadeus.resources.Period;
import com.amadeus.resources.PointOfInterest;
import com.amadeus.travel.analytics.airTraffic.BusiestPeriod;
import com.royce.tripbotify.R;
import com.royce.tripbotify.adapter.PointOfInterestAdapter;
import com.royce.tripbotify.utils.AppConstants;
import com.royce.tripbotify.utils.PreferenceManager;
import com.royce.tripbotify.utils.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import swipeable.com.layoutmanager.OnItemSwiped;
import swipeable.com.layoutmanager.SwipeableLayoutManager;
import swipeable.com.layoutmanager.SwipeableTouchHelperCallback;
import swipeable.com.layoutmanager.touchelper.ItemTouchHelper;

public class TinderCardsCityActivity extends AppCompatActivity {

    private List<PointOfInterest> mItems = new ArrayList<>();
    private PointOfInterestAdapter mAdapter;
    private RecyclerView mDeckLayout;
    private String name, airportCode;
    private double south, west;
    private TextView mProTip;
    private PreferenceManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_point_of_interest);
        mDeckLayout = findViewById(R.id.list);
        mProTip = findViewById(R.id.pro_tip);
        addSwipeHelper();
        getIntentData();
        getPoints();
        manager = PreferenceManager.getInstance(this);

        findViewById(R.id.button_add_find_cheapest_dates).setOnClickListener(v -> startActivity(new Intent(TinderCardsCityActivity.this,
                GenericListActivity.class).putExtra("type", GenericListActivity.TYPE_FLIGHT_CHEAPEST_FARES).putExtra("code", airportCode)));

        //ask for date //todo
        findViewById(R.id.button_find_lowest_fares).setOnClickListener(v -> startActivity(new Intent(TinderCardsCityActivity.this,
                GenericListActivity.class).putExtra("type", GenericListActivity.TYPE_FLIGHT_FARES).putExtra("code", airportCode)));

    }

    private void getIntentData() {
        name = getIntent().getStringExtra("name");
        south = getIntent().getDoubleExtra("south", 12.97321);
        west = getIntent().getDoubleExtra("west", 77.586856);
        south += 0.02;
        west += 0.02;
       // south = round(south, 6);
       // west = round(west, 6);
        Log.i(AppConstants.LOG_TAG, south + " - " + west);
        airportCode = getIntent().getStringExtra("airportCode");
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(name);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    private void addSwipeHelper() {
        SwipeableTouchHelperCallback swipeableTouchHelperCallback =
                new SwipeableTouchHelperCallback(new OnItemSwiped() {
                    @Override
                    public void onItemSwiped() {
                        mAdapter.removeTopItem();
                    }

                    @Override
                    public void onItemSwipedLeft() {
                        saveFirstToPrefs();
                    }

                    @Override
                    public void onItemSwipedRight() {
                    }

                    @Override
                    public void onItemSwipedUp() {
                    }

                    @Override
                    public void onItemSwipedDown() {
                    }
                });

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(swipeableTouchHelperCallback);
        itemTouchHelper.attachToRecyclerView(mDeckLayout);
        mDeckLayout.setLayoutManager(new SwipeableLayoutManager().
                setAngle(25).
                setAnimationDuratuion(500));

    }

    private void saveFirstToPrefs() {
        for (String tag : mItems.get(0).getTags()) {
            manager.increment(tag);
            //     Log.i(AppConstants.LOG_TAG, manager.getInt(tag) + " tag " + tag);
            manager.putInSet(AppConstants.PREFS_TAGS, tag);
            //     Log.i(AppConstants.LOG_TAG, manager.getSet(AppConstants.PREFS_TAGS).toString());
        }
    }

    private void setAdapter() {
        mAdapter = new PointOfInterestAdapter(mItems);
        mDeckLayout.setAdapter(mAdapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            onBackPressed();
        return super.onOptionsItemSelected(item);
    }

    private double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }


    private void getPoints() {
        AsyncTask.execute(() -> {
            Amadeus amadeus = Amadeus
                    .builder("hiQb0uhPPNuSGtm5ssxapQ2K8nHvNJ3n", "vanIVyyEeaFqgYGL")
                    .build();
            try {

                PointOfInterest[] pointsOfInterest = amadeus.referenceData.locations.pointsOfInterest.get(Params
                        .with("latitude", south +"")
                        .and("longitude", west + ""));

                mItems = new ArrayList<>(Arrays.asList(pointsOfInterest));
                runOnUiThread(this::setAdapter);

                Period[] busiestPeriods = amadeus.travel.analytics.airTraffic.busiestPeriod.get(Params
                        .with("cityCode", airportCode)
                        .and("period", "2018")
                        .and("direction", BusiestPeriod.ARRIVING));

                runOnUiThread(() -> mProTip.setText(Utils.getBusiestPeriodString(name, busiestPeriods)));

                //todo uncomment
                /*while (mItems.size() < 100) {
                    south += 0.02;
                    west += 0.02;
                    PointOfInterest[] s = amadeus.referenceData.locations.pointsOfInterest.get(Params
                            .with("latitude", south)
                            .and("longitude", west));
                    //Log.i(AppConstants.LOG_TAG, s[s.length - 1].toString());
                    mItems.addAll(Arrays.asList(s));
                }*/
                //Log.i(AppConstants.LOG_TAG, pointsOfInterest[0].toString());
            } catch (ResponseException e) {
                Log.i(AppConstants.LOG_TAG, e.getCode() + e.getDescription());
            }
        });
    }

}