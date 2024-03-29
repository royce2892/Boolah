package com.royce.tripbotify.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import androidx.core.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.royce.tripbotify.R;
import com.royce.tripbotify.activity.AboutMeActivity;
import com.royce.tripbotify.activity.GenericListActivity;
import com.royce.tripbotify.adapter.RealmPointsAdapter;
import com.royce.tripbotify.adapter.RealmTrackedFlightsAdapter;
import com.royce.tripbotify.adapter.RealmTranslationsAdapter;
import com.royce.tripbotify.database.RealmPointOfInterest;
import com.royce.tripbotify.database.RealmTrackFlight;
import com.royce.tripbotify.database.RealmTranslation;
import com.royce.tripbotify.utils.AppConstants;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;
import io.realm.RealmResults;

public class MyWorldFragment extends Fragment {

    @BindView(R.id.list_liked_places)
    RecyclerView mListLikedPlaces;
    @BindView(R.id.list_tracked_flights)
    RecyclerView mListTrackedFlights;
    @BindView(R.id.list_saved_translations)
    RecyclerView mListSavedTranslations;

    private Realm realm;
    private RealmResults<RealmPointOfInterest> points;
    private RealmResults<RealmTranslation> translations;
    private RealmResults<RealmTrackFlight> flights;

    private RealmPointsAdapter mPointsAdapter;
    private RealmTranslationsAdapter mTranslationsAdapter;
    private RealmTrackedFlightsAdapter mFlightsAdapter;

    @OnClick(R.id.more_poi)
    void showMorePOI() {
        startActivity(new Intent(getContext(), GenericListActivity.class).putExtra("type", GenericListActivity.REALM_PLACES));
    }

    @OnClick(R.id.more_flights)
    void showMoreFlights() {
        startActivity(new Intent(getContext(), GenericListActivity.class).putExtra("type", GenericListActivity.REALM_FLIGHTS));
    }

    @OnClick(R.id.more_trans)
    void showMoreTrans() {
        startActivity(new Intent(getContext(), GenericListActivity.class).putExtra("type", GenericListActivity.REALM_TRANSLATIONS));
    }

    @OnClick(R.id.label_4)
    void gotoAboutMe() {
        startActivity(new Intent(getContext(), AboutMeActivity.class));
    }

    public MyWorldFragment() {
    }

    public static MyWorldFragment newInstance() {
        Bundle args = new Bundle();
        MyWorldFragment fragment = new MyWorldFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        realm = Realm.getDefaultInstance();
    }

    private void manipulateAboutMe() {



        //for (RealmTrackFlight flight : flights)
        //  Log.i(AppConstants.LOG_TAG, flight.toString());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_world, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initViews();
    }

    private void initViews() {
      //  manipulateAboutMe();
        Log.i(AppConstants.LOG_TAG,"called");
        points = realm.where(RealmPointOfInterest.class).
                limit(2).
                findAll();

        translations = realm.where(RealmTranslation.class).
                limit(2).
                findAll();

        flights = realm.where(RealmTrackFlight.class).
                limit(2).
                findAll();

        mListLikedPlaces.setLayoutManager(new LinearLayoutManager(getContext()));
        mListSavedTranslations.setLayoutManager(new LinearLayoutManager(getContext()));
        mListTrackedFlights.setLayoutManager(new LinearLayoutManager(getContext()));

        mPointsAdapter = new RealmPointsAdapter(points);
        mListLikedPlaces.setAdapter(mPointsAdapter);
        mTranslationsAdapter = new RealmTranslationsAdapter(translations);
        mListSavedTranslations.setAdapter(mTranslationsAdapter);
        mFlightsAdapter = new RealmTrackedFlightsAdapter(flights);
        mListTrackedFlights.setAdapter(mFlightsAdapter);

        Log.i(AppConstants.LOG_TAG,"refresed with size " + points.size() + translations.size() + flights.size());

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
