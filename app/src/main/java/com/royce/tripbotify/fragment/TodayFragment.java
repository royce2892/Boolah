package com.royce.tripbotify.fragment;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.amadeus.Amadeus;
import com.amadeus.Params;
import com.amadeus.exceptions.ResponseException;
import com.amadeus.resources.PointOfInterest;
import com.here.android.mpa.common.GeoPosition;
import com.here.android.mpa.common.OnEngineInitListener;
import com.here.android.mpa.common.PositioningManager;
import com.here.android.mpa.mapping.Map;
import com.here.android.mpa.mapping.SupportMapFragment;
import com.royce.tripbotify.R;
import com.royce.tripbotify.utils.AppConstants;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import static android.support.v4.content.ContextCompat.checkSelfPermission;

public class TodayFragment extends Fragment {

    private SupportMapFragment mapFragment;
    private boolean paused = false;
    private Map map;
    private PositioningManager posManager;
    private Amadeus amadeus;
    private Geocoder geocoder;
    private String city = "";
    private List includedCityList = Arrays.asList("San Francisco", "New York", "Paris", "Dallas",
            "Bangalore", "London", "Barcelona", "Berlin");

    public TodayFragment() {
    }

    public static TodayFragment newInstance() {
        Bundle args = new Bundle();
        TodayFragment fragment = new TodayFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_today, container, false);
        mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.mapfragment);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initMap();
        if (geocoder == null)
            geocoder = new Geocoder(getContext(), Locale.getDefault());
        if (posManager == null) {
            posManager = PositioningManager.getInstance();
            posManager.addListener(
                    new WeakReference<>(positionListener));
        }
        chechGPSPermission();
        //getPointsOfInterest(0.0, 0.0);
    }

    private void getPointsOfInterest(double lat, double lon) {

        AsyncTask.execute(() -> {
            if (amadeus == null)
                amadeus = Amadeus
                        .builder("hiQb0uhPPNuSGtm5ssxapQ2K8nHvNJ3n", "vanIVyyEeaFqgYGL")
                        .build();

            try {
                PointOfInterest[] pointsOfInterest = amadeus.referenceData.locations.pointsOfInterest.get(Params
                        .with("latitude", lat == 0.0 ? "41.39715" : lat)
                        .and("longitude", lon == 0.0 ? "2.160873" : lon));
                Log.i(AppConstants.LOG_TAG, "size -> " + pointsOfInterest.length);
            } catch (ResponseException ex) {
                //todo check response ex once, else user barcelona and build up
                Log.i(AppConstants.LOG_TAG, ex.getDescription());
            }
        });
    }

    private void chechGPSPermission() {
        if (getContext() != null)
            if (checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            } else
                Log.i(AppConstants.LOG_TAG, "PERMISSION GRANTED");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED && requestCode == 1)
            getCurrentPosition();
    }

    @Override
    public void onStart() {
        super.onStart();
        paused = false;
        getCurrentPosition();
    }

    private void getCurrentPosition() {
        if (posManager != null) {
            boolean started = posManager.start(
                    PositioningManager.LocationMethod.NETWORK);
            Log.i(AppConstants.LOG_TAG, "started -> " + started);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (posManager != null) {
            posManager.stop();
        }
        paused = true;
    }

    @Override
    public void onDestroy() {
        if (posManager != null) {
            // Cleanup
            posManager.removeListener(
                    positionListener);
        }
        map = null;
        super.onDestroy();
    }

    private void initMap() {

        if (mapFragment != null) {
            mapFragment.init(error -> {
                if (error == OnEngineInitListener.Error.NONE) {
                    // now the map is ready to be used
                    map = mapFragment.getMap();
                    // ...
                } else {
                    Log.i(AppConstants.LOG_TAG, error.getDetails());
                }
            });
        }
    }

    private PositioningManager.OnPositionChangedListener positionListener = new
            PositioningManager.OnPositionChangedListener() {

                public void onPositionUpdated(PositioningManager.LocationMethod method,
                                              GeoPosition position, boolean isMapMatched) {
                    if (!paused && map != null) {
                        map.setCenter(position.getCoordinate(),
                                Map.Animation.LINEAR);
                        map.getPositionIndicator().setVisible(true);
                        try {
                            if (city.contentEquals("")) {
                                city = geocoder.getFromLocation(position.getCoordinate().getLatitude(), position.getCoordinate().getLongitude(),
                                        1).get(0).getLocality();
                                if (includedCityList.contains(city))
                                    getPointsOfInterest(position.getCoordinate().getLatitude(), position.getCoordinate().getLongitude());
                                else
                                    getPointsOfInterest(0.0, 0.0);
                                // Log.i(AppConstants.LOG_TAG, city);
                            }
                        } catch (IOException e) {
                            getPointsOfInterest(0.0, 0.0);
                            Log.i(AppConstants.LOG_TAG, e.getLocalizedMessage());
                        }
                        // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                    }
                }

                public void onPositionFixChanged(PositioningManager.LocationMethod method,
                                                 PositioningManager.LocationStatus status) {
                }
            };


}
