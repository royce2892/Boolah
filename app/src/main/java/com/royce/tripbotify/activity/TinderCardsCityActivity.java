package com.royce.tripbotify.activity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.app.AppCompatActivity;

import com.amadeus.Amadeus;
import com.amadeus.Params;
import com.amadeus.exceptions.ResponseException;
import com.amadeus.resources.Period;
import com.amadeus.resources.PointOfInterest;
import com.amadeus.travel.analytics.airTraffic.BusiestPeriod;
import com.royce.tripbotify.R;
import com.royce.tripbotify.adapter.PointOfInterestAdapter;
import com.royce.tripbotify.database.RealmPointOfInterest;
import com.royce.tripbotify.utils.AppConstants;
import com.royce.tripbotify.utils.PreferenceManager;
import com.royce.tripbotify.utils.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import io.realm.Realm;
import swipeable.com.layoutmanager.OnItemSwiped;
import swipeable.com.layoutmanager.SwipeableLayoutManager;
import swipeable.com.layoutmanager.SwipeableTouchHelperCallback;
import swipeable.com.layoutmanager.touchelper.ItemTouchHelper;

public class TinderCardsCityActivity extends AppCompatActivity {

    private List<PointOfInterest> mItems = new ArrayList<>();
    private PointOfInterestAdapter mAdapter;
    private RecyclerView mDeckLayout;
    private ProgressBar mProgressBar;
    private String name, airportCode, languageCode;
    private double south, west;
    private TextView mProTip;
    private PreferenceManager manager;
    private Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_point_of_interest);
        realm = Realm.getDefaultInstance();
        mDeckLayout = findViewById(R.id.list);
        mProTip = findViewById(R.id.pro_tip);
        mProgressBar = findViewById(R.id.progress_bar);
        addSwipeHelper();
        getIntentData();
        getPoints();
        manager = PreferenceManager.getInstance(this);

        findViewById(R.id.button_add_find_cheapest_dates).setOnClickListener(v -> showChooseDateDialog());

        findViewById(R.id.button_find_lowest_fares).setOnClickListener(v -> startActivity(new Intent(TinderCardsCityActivity.this,
                GenericListActivity.class).putExtra("type", GenericListActivity.TYPE_FLIGHT_FARES).putExtra("code", airportCode)));

        Button mBoolah = findViewById(R.id.button_add_city_to_list);
        mBoolah.setText("Learn ".concat(name) + "'s local language");

        //todo replace with favorite
        mBoolah.setOnClickListener(v -> startActivity(new Intent(TinderCardsCityActivity.this, TranslateActivity.class).
                putExtra("languageCode", languageCode)));

        mProgressBar.setVisibility(View.VISIBLE);

        checkAndShowTinderDialog();
    }

    private void checkAndShowTinderDialog() {
        if(PreferenceManager.getInstance(this).getBoolean(AppConstants.PREFS_TINDER_SHOWN))
            return;
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_tinder);
        dialog.findViewById(R.id.button_dismiss).setOnClickListener(v -> {
            dialog.dismiss();
            PreferenceManager.getInstance(TinderCardsCityActivity.this).put(AppConstants.PREFS_TINDER_SHOWN,true);
        });
        dialog.show();
    }

    private void showChooseDateDialog() {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog dialog = new DatePickerDialog(this, (view, year, monthOfYear, dayOfMonth) -> {
            Calendar newDate = Calendar.getInstance();
            monthOfYear++;
            String month = monthOfYear < 10 ? "0" + monthOfYear : monthOfYear + "";
            String day = dayOfMonth < 10 ? "0" + dayOfMonth : dayOfMonth + "";
            gotoFaresSearchActivity(year + "-" + month + "-" + day);
            newDate.set(year, monthOfYear, dayOfMonth);
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        dialog.show();
    }

    private void gotoFaresSearchActivity(String date) {
        startActivity(new Intent(TinderCardsCityActivity.this,
                GenericListActivity.class).putExtra("type", GenericListActivity.TYPE_FLIGHT_CHEAPEST_FARES).
                putExtra("code", airportCode).putExtra("date", date));
    }

    private void getIntentData() {
        name = getIntent().getStringExtra("name");
        languageCode = getIntent().getStringExtra("languageCode");
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
                    }

                    @Override
                    public void onItemSwipedLeft() {
                        Log.i(AppConstants.LOG_TAG, "item swipe left called");
                        saveFirstToPrefs();
                        Log.i(AppConstants.LOG_TAG, "item swipe left done");
                    }

                    @Override
                    public void onItemSwipedRight() {
                        Toast.makeText(TinderCardsCityActivity.this,"Oops, Boolah is sorry you didn't like that suggestion from me",Toast.LENGTH_SHORT).show();
                        mAdapter.removeTopItem();
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
            Log.i(AppConstants.LOG_TAG, manager.getInt(tag) + " tag " + tag);
            manager.putInSet(AppConstants.PREFS_TAGS, tag);
            Log.i(AppConstants.LOG_TAG, manager.getSet(AppConstants.PREFS_TAGS).toString());
        }
        if (mAdapter.getTopItem() != null) {
            Log.i(AppConstants.LOG_TAG, "realm save called for " + mAdapter.getTopItem().getName());
            realm.beginTransaction();
            RealmPointOfInterest point = realm.createObject(RealmPointOfInterest.class);
            PointOfInterest pointOfInterest = mAdapter.getTopItem();
            point.setName(pointOfInterest.getName());
            point.setType(pointOfInterest.getType());
            point.setCategory(pointOfInterest.getCategory());
            point.setSubType(pointOfInterest.getSubType());
            point.setLat(pointOfInterest.getGeoCode().getLatitude());
            point.setLon(pointOfInterest.getGeoCode().getLongitude());
            point.setCity(name);
            point.setTags(pointOfInterest.getTags());
            realm.commitTransaction();
            Toast.makeText(this,"Bingo! Boolah have saved " + point.getName() +" to your liked places",Toast.LENGTH_SHORT).show();
          //  Log.i(AppConstants.LOG_TAG, "realm save done for " + mAdapter.getTopItem().getName());
            mAdapter.removeTopItem();
        }
    }

    private void setAdapter() {
        mAdapter = new PointOfInterestAdapter(mItems);
        mDeckLayout.setAdapter(mAdapter);
        mProgressBar.setVisibility(View.GONE);
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
                        .with("latitude", south + "")
                        .and("longitude", west + ""));

                mItems = new ArrayList<>(Arrays.asList(pointsOfInterest));
                runOnUiThread(this::setAdapter);

                Period[] busiestPeriods = amadeus.travel.analytics.airTraffic.busiestPeriod.get(Params
                        .with("cityCode", airportCode)
                        .and("period", "2018")
                        .and("direction", BusiestPeriod.ARRIVING));

                runOnUiThread(() -> mProTip.setText(Utils.getBusiestPeriodString(name, busiestPeriods)));

                //todo uncomment
                while (mItems.size() < 40) {
                    south += 0.02;
                    west += 0.02;
                    PointOfInterest[] s = amadeus.referenceData.locations.pointsOfInterest.get(Params
                            .with("latitude", south)
                            .and("longitude", west));
                    //Log.i(AppConstants.LOG_TAG, s[s.length - 1].toString());
                    mItems.addAll(Arrays.asList(s));
                }
                //Log.i(AppConstants.LOG_TAG, pointsOfInterest[0].toString());
            } catch (ResponseException e) {
                runOnUiThread(() -> mProgressBar.setVisibility(View.GONE));
                Log.i(AppConstants.LOG_TAG, e.getCode() + e.getDescription());
            }
        });
    }

}