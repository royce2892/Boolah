package com.royce.tripbotify.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.amadeus.Amadeus;
import com.amadeus.Params;
import com.amadeus.exceptions.ResponseException;
import com.amadeus.resources.PointOfInterest;
import com.royce.tripbotify.R;
import com.royce.tripbotify.adapter.HomePagerAdapter;
import com.royce.tripbotify.database.City;
import com.royce.tripbotify.fragment.DiscoverFragment;
import com.royce.tripbotify.utils.AppConstants;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ViewPagerWithTabsActivity extends AppCompatActivity implements DiscoverFragment.OnDiscoverCitiesInteractionListener {

    @BindView(R.id.viewpager)
    ViewPager mPager;
    @BindView(R.id.sliding_tabs)
    TabLayout mTabs;

    private String token;
    private Amadeus amadeus;
    private String[] TITLE = {"Today","Explore","My World"};
    //private List<FlightDestination> experiences = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        if (getSupportActionBar() != null)
            getSupportActionBar().hide();
//            getSupportActionBar().setTitle(TITLE[0]);
        setupPagerAndInitTabs();
    }


    private void getAccessToken() {
        AsyncTask.execute(() -> {
            amadeus = Amadeus
                    .builder("hiQb0uhPPNuSGtm5ssxapQ2K8nHvNJ3n", "vanIVyyEeaFqgYGL")
                    .build();
            try {
                /*FlightDestination[] flightDestinations = amadeus.shopping.flightDestinations.get(Params
                        .with("origin", "MAD"));
                //Log.i(AppConstants.LOG_TAG, Arrays.toString(flightDestinations));

// Flight Check-in Links
                CheckinLink[] checkinLinks = amadeus.referenceData.urls.checkinLinks.get(Params
                        .with("airlineCode", "BA"));

// Airline Code LookUp
                Airline[] airlines = amadeus.referenceData.airlines.get(Params
                        .with("airlineCodes", "BA"));

// Airport & City Search (autocomplete)
// Find all the cities and airports starting by the keyword 'LON'
                Location[] locations = amadeus.referenceData.locations.get(Params
                        .with("keyword", "LON")
                        .and("subType", Locations.ANY));
// Get a specific city or airport based on its id
                Location location = amadeus.referenceData
                        .location("ALHR").get();

// Airport Nearest Relevant (for London)
                Location[] locationsrel = amadeus.referenceData.locations.airports.get(Params
                        .with("latitude", 0.1278)
                        .and("longitude", 51.5074));

// Flight Most Searched Destinations
// Which were the most searched flight destinations from Madrid in August 2017?
                SearchedDestination searchedDestination = amadeus.travel.analytics.airTraffic.searchedByDestination.get(Params
                        .with("originCityCode", "MAD")
                        .and("destinationCityCode", "NYC")
                        .and("searchPeriod", "2019-05")
                        .and("marketCountryCode", "ES"));
// How many people in Spain searched for a trip from Madrid to New-York in September 2017?
                Search[] search = amadeus.travel.analytics.airTraffic.searched.get(Params
                        .with("originCityCode", "MAD")
                        .and("searchPeriod", "2017-08")
                        .and("marketCountryCode", "ES"));

// Flight Most Booked Destinations
                AirTraffic[] airTraffics = amadeus.travel.analytics.airTraffic.booked.get(Params
                        .with("originCityCode", "MAD")
                        .and("period", "2017-08"));

// Flight Most Traveled Destinations
                AirTraffic[] airTraffics2 = amadeus.travel.analytics.airTraffic.traveled.get(Params
                        .with("originCityCode", "MAD")
                        .and("period", "2017-01"));



// Hotel Search API
// Get list of hotels by city code
                HotelOffer[] offers = amadeus.shopping.hotelOffers.get(Params
                        .with("cityCode", "MAD"));
// Get list of offers for a specific hotel
                HotelOffer hotelOffer = amadeus.shopping.hotelOffersByHotel.get(Params.with("hotelId", "BGLONBGB"));
// Confirm the availability of a specific offer
                HotelOffer offer = amadeus.shopping.hotelOffer("4BA070CE929E135B3268A9F2D0C51E9D4A6CF318BA10485322FA2C7E78C7852E").get();
*/
// Points of Interest
// What are the popular places in Barcelona (based a geo location and a radius)
                PointOfInterest[] pointsOfInterest = amadeus.referenceData.locations.pointsOfInterest.get(Params
                        .with("latitude", "41.39715")
                        .and("longitude", "2.160873"));

                Log.i(AppConstants.LOG_TAG, pointsOfInterest.length + " size of array");
                Log.i(AppConstants.LOG_TAG, pointsOfInterest[0].toString());


// What are the popular places in Barcelona? (based on a square)
                PointOfInterest[] pointsOfInterest2 = amadeus.referenceData.locations.pointsOfInterest.bySquare.get(Params
                        .with("north", "41.397158")
                        .and("west", "2.160873")
                        .and("south", "41.394582")
                        .and("east", "2.177181"));


            } catch (ResponseException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void onCityClick(City item) {
        Log.i(AppConstants.LOG_TAG, item.toString());
        startActivity(new Intent(this, TinderCardsCityActivity.class).
                putExtra("south", item.getSouth()).
                putExtra("airportCode", item.getAirportCode()).
                putExtra("languageCode", item.getLanguageCode()).
                putExtra("west", item.getWest()).
                putExtra("name", item.getName()));
    }

    private void setupPagerAndInitTabs() {
        mTabs.addTab(mTabs.newTab().setIcon(R.drawable.ic_today_selected)/*setText("Today")*/);
        mTabs.addTab(mTabs.newTab().setIcon(R.drawable.ic_explore)/*.setText("Explore")*/);
        mTabs.addTab(mTabs.newTab().setIcon(R.drawable.ic_favorite)/*.setText("My World")*/);

        HomePagerAdapter pagerAdapter = new HomePagerAdapter(getSupportFragmentManager());

//        mPager.setOffscreenPageLimit(1);
        mPager.setAdapter(pagerAdapter);
        mPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabs));


        mTabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mPager.setCurrentItem(tab.getPosition());
                setToolTitle(tab.getPosition());
                //setToolbarTitle(tab.getPosition());
                //     tab.getIcon().setColorFilter(getResources().getColor(R.color.colorAccent), PorterDuff.Mode.SRC_IN);
                switch (tab.getPosition()) {
                    default:
                    case 0:
                        tab.setIcon(R.drawable.ic_today_selected);
                        break;
                    case 1:
                        tab.setIcon(R.drawable.ic_explore_selected);
                        break;
                    case 2:
                        tab.setIcon(R.drawable.ic_favorite_selected);
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                //   tab.getIcon().setColorFilter(getResources().getColor(R.color.colorPrimaryDark), PorterDuff.Mode.SRC_IN);
                switch (tab.getPosition()) {
                    default:
                    case 0:
                        tab.setIcon(R.drawable.ic_today);
                        break;
                    case 1:
                        tab.setIcon(R.drawable.ic_explore);
                        break;
                    case 2:
                        tab.setIcon(R.drawable.ic_favorite);
                        break;
                }
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        mPager.setCurrentItem(1);

    }

    private void setToolTitle(int position) {
        if(getSupportActionBar() != null)
            getSupportActionBar().setTitle(TITLE[position]);
    }
}
