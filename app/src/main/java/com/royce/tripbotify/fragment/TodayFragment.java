package com.royce.tripbotify.fragment;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.amadeus.Amadeus;
import com.amadeus.Params;
import com.amadeus.exceptions.ResponseException;
import com.amadeus.resources.Location;
import com.amadeus.resources.PointOfInterest;
import com.google.gson.Gson;
import com.here.android.mpa.common.GeoCoordinate;
import com.here.android.mpa.common.GeoPosition;
import com.here.android.mpa.common.PositioningManager;
import com.royce.tripbotify.R;
import com.royce.tripbotify.adapter.NewTodayAdapter;
import com.royce.tripbotify.core.ApiClient;
import com.royce.tripbotify.database.RealmPlace;
import com.royce.tripbotify.utils.AppConstants;
import com.royce.tripbotify.utils.PreferenceManager;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static android.support.v4.content.ContextCompat.checkSelfPermission;

public class TodayFragment extends Fragment {

    //    private SupportMapFragment mapFragment;
    private boolean paused = false;
    //  private Map map;
    private PositioningManager posManager;
    private Amadeus amadeus;
    private Geocoder geocoder;
    //private String city = "";
    private List includedCityList = Arrays.asList("San Francisco", "New York", "Paris", "Dallas",
            "Bangalore", "London", "Barcelona", "Berlin");

    private RecyclerView mList;
    private TextView mCityName;
    //    private TodayPointsAdapter mAdapter;
    private NewTodayAdapter mAdapter;
    //    private List<PointOfInterest> mItems = new ArrayList<>();
    private List<RealmPlace> mItems = new ArrayList<>();
    private double latitude, longitude;
    private Set<String> set;

    public TodayFragment() {
    }

    public static TodayFragment newInstance() {
        Bundle args = new Bundle();
        TodayFragment fragment = new TodayFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        set = PreferenceManager.getInstance(getContext()).getSet(AppConstants.PREFS_TAGS);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_today, container, false);
        //  mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.mapfragment);
        //return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mList = view.findViewById(R.id.today_list);
        //mCityName = view.findViewById(R.id.city_name);
        mList.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        //initMap();
        /*if (geocoder == null)
            geocoder = new Geocoder(getContext(), Locale.getDefault());
        if (posManager == null) {
            posManager = PositioningManager.getInstance();
            posManager.addListener(
                    new WeakReference<>(positionListener));
        }*/
        chechGPSPermission();
        if (mItems.size() == 0)
            getData(0.0, 0.0);
        else
            setAdapter();
    }

    private void getData(double v, double v1) {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                JSONArray array = ApiClient.getSamplePlaces();
                if (array != null)
                    for (int i = 0; i < array.length(); i++) {
                        try {
                            mItems.add(new Gson().fromJson(array.getJSONObject(i).toString(), RealmPlace.class));
                        } catch (Exception ex) {
                            Log.i(AppConstants.LOG_TAG, "mathew django -> " + ex.getLocalizedMessage());
                        }
                    }
                    if(mItems.size()!=0 && getActivity()!=null)
                        getActivity().runOnUiThread(() -> setAdapter());

            }
        });
    }

    private void getPointsOfInterest(double lat, double lon) {

        AsyncTask.execute(() -> {
            if (amadeus == null)
                amadeus = Amadeus
                        .builder("hiQb0uhPPNuSGtm5ssxapQ2K8nHvNJ3n", "vanIVyyEeaFqgYGL")
                        .build();
            try {
                latitude = lat == 0.0 ? 12.924210 : lat;
                longitude = lat == 0.0 ? 77.537856 : lon;
                PointOfInterest[] pointsOfInterest = amadeus.referenceData.locations.pointsOfInterest.get(Params
                        .with("latitude", latitude)
                        .and("longitude", longitude));

                addItemsToArray(pointsOfInterest);
                if (getActivity() != null)
                    getActivity().runOnUiThread(this::setAdapter);

                while (mItems.size() < 40) {
                    latitude += 0.02;
                    longitude += 0.02;
                    PointOfInterest[] s = amadeus.referenceData.locations.pointsOfInterest.get(Params
                            .with("latitude", latitude)
                            .and("longitude", longitude));
                    //Log.i(AppConstants.LOG_TAG, s[s.length - 1].toString());
                    addItemsToArray(s);
                }
//                Log.i(AppConstants.LOG_TAG, "size -> " + pointsOfInterest.length);
            } catch (ResponseException ex) {
                //todo check response ex once, else user barcelona and build up
                Log.i(AppConstants.LOG_TAG + "140", ex.getDescription());
            }
        });
    }

    private void addItemsToArray(PointOfInterest[] pointsOfInterest) {
        //AI LOGIC
        if (set.isEmpty()) {
//            mItems.addAll(Arrays.asList(pointsOfInterest));
            return;
        }
        for (PointOfInterest pointOfInterest : pointsOfInterest) {
            String[] tags = pointOfInterest.getTags();
            for (String tag : tags)
                if (set.contains(tag)) {
  //                  mItems.add(pointOfInterest);
                    break;
                }
        }
        //mItems.addAll(Arrays.asList(pointsOfInterest));
    }

    private void setAdapter() {
        if (mAdapter == null)
            mAdapter = new NewTodayAdapter(mItems);
        mList.setAdapter(mAdapter);
    }

    private void chechGPSPermission() {
        if (getContext() != null)
            if (checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            } //else
        //Log.i(AppConstants.LOG_TAG, "PERMISSION GRANTED");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED && requestCode == 1)
            getCurrentPosition();
    }

    /*@Override
    public void onStart() {
        super.onStart();
        paused = false;
        //getCurrentPosition();
    }*/

    private void getCurrentPosition() {
        if (posManager != null) {
            boolean started = posManager.start(
                    PositioningManager.LocationMethod.NETWORK);
            Log.i(AppConstants.LOG_TAG, "started -> " + started);
        }
    }

    /*@Override
    public void onStop() {
        super.onStop();
        if (posManager != null) {
            posManager.stop();
        }
        paused = true;
    }*/

    /*@Override
    public void onDestroy() {
        if (posManager != null) {
            // Cleanup
            posManager.removeListener(
                    positionListener);
        }
        //map = null;
        super.onDestroy();
    }
*/
    private void initMap() {

       /* if (mapFragment != null) {
            mapFragment.init(error -> {
                if (error == OnEngineInitListener.Error.NONE) {
                    // now the map is ready to be used
                    map = mapFragment.getMap();
                    // ...
                } else {
                    Log.i(AppConstants.LOG_TAG, error.getDetails());
                }
            });
        }*/
    }

    private PositioningManager.OnPositionChangedListener positionListener = new
            PositioningManager.OnPositionChangedListener() {

                public void onPositionUpdated(PositioningManager.LocationMethod method,
                                              GeoPosition position, boolean isMapMatched) {
                    if (!paused) {
                        getNearestAirport(position.getCoordinate());
                        /*map.setCenter(position.getCoordinate(),
                                Map.Animation.LINEAR);
                        map.getPositionIndicator().setVisible(true);
                        *///try {
                            /*if (city.contentEquals("")) {
                                city = geocoder.getFromLocation(position.getCoordinate().getLatitude(), position.getCoordinate().getLongitude(),
                                        1).get(0).getLocality();*/
                        //if (getActivity() != null)
                        //    getActivity().runOnUiThread(() -> mCityName.setText(city));
                        //todo uncomment
                                /*if (includedCityList.contains(city))
                                    getPointsOfInterest(position.getCoordinate().getLatitude(), position.getCoordinate().getLongitude());
                                else*/
                        //getPointsOfInterest(0.0, 0.0);
                        // Log.i(AppConstants.LOG_TAG, city);
                        //  }
                        /*} catch (IOException e) {
//                            getPointsOfInterest(0.0, 0.0);
                            Log.i(AppConstants.LOG_TAG, e.getLocalizedMessage());
                        }*/
                        // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                    }
                }

                public void onPositionFixChanged(PositioningManager.LocationMethod method,
                                                 PositioningManager.LocationStatus status) {
                }
            };

    private void getNearestAirport(GeoCoordinate coordinate) {
        AsyncTask.execute(() -> {
            if (amadeus == null)
                amadeus = Amadeus
                        .builder("hiQb0uhPPNuSGtm5ssxapQ2K8nHvNJ3n", "vanIVyyEeaFqgYGL")
                        .build();

            try {
                Location[] locations = amadeus.referenceData.locations.airports.get(Params
                        .with("latitude", coordinate.getLatitude())
                        .and("longitude", coordinate.getLongitude()));
                if (locations != null) {
                    Log.i(AppConstants.LOG_TAG, "code -> " + locations[0].getIataCode());
                    PreferenceManager.getInstance(getContext()).put(AppConstants.PREFS_IATA, locations[0].getIataCode());
                } else
                    Log.i(AppConstants.LOG_TAG, " fetch nearest airport -> 0 locs");
            } catch (ResponseException ex) {
                Log.i(AppConstants.LOG_TAG + "284", ex.getDescription());
            }
        });
    }


}
