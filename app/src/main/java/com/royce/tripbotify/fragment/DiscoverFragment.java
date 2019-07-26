package com.royce.tripbotify.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.royce.tripbotify.R;
import com.royce.tripbotify.adapter.DiscoverCitiesAdapter;
import com.royce.tripbotify.database.City;

import java.util.ArrayList;
import java.util.List;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnDiscoverCitiesInteractionListener}
 * interface.
 */
public class DiscoverFragment extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 2;
    private OnDiscoverCitiesInteractionListener mListener;
    private List<City> cities = new ArrayList<>(8);

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public DiscoverFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static DiscoverFragment newInstance(int columnCount) {
        DiscoverFragment fragment = new DiscoverFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
        if (cities.size() == 0)
            initData();
    }

    private void initData() {
        cities.add(new City("Bangalore", "BLR", 13.023577, 12.923210, 77.642256, 77.536856, R.drawable.city_blr, "hi"));
        cities.add(new City("Barcelona", "BCN", 41.42, 41.347463, 2.228208, 2.11, R.drawable.city_barcelona,"es"));
        cities.add(new City("Berlin", "BER", 52.541755, 52.490569, 13.457198, 13.354201, R.drawable.city_berlin, "de"));
        cities.add(new City("Dallas", "DFW", 32.806993, 32.740310, -96.737293, -96.836857, R.drawable.city_dallas, "en"));
        cities.add(new City("London", "LCY", 51.520180, 51.484703, -0.061048, -0.169882, R.drawable.city_london, "en"));
        cities.add(new City("New York", "JFK", 40.792027, 40.697607, -73.942847, -74.058204, R.drawable.city_ny, "en"));
        cities.add(new City("Paris", "CDG", 48.91, 48.80, 2.46, 2.25, R.drawable.city_paris, "fr"));
        cities.add(new City("San Francisco", "SFO", 37.810980, 37.732007, -122.370076, -122.483716, R.drawable.city_sfo, "en"));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_discover_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            recyclerView.setAdapter(new DiscoverCitiesAdapter(cities, mListener));
        }
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnDiscoverCitiesInteractionListener) {
            mListener = (OnDiscoverCitiesInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnDiscoverCitiesInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnDiscoverCitiesInteractionListener {
        // TODO: Update argument type and name
        void onCityClick(City item);
    }
}
