package com.royce.tripbotify.adapter;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.amadeus.resources.FlightDate;
import com.amadeus.resources.FlightOffer;
import com.royce.tripbotify.R;
import com.royce.tripbotify.activity.GenericListActivity;
import com.royce.tripbotify.database.City;
import com.royce.tripbotify.database.RealmTrackFlight;
import com.royce.tripbotify.utils.Utils;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link City} and makes a call to the
 * specified {@link}.
 */
public class RealmTrackedFlightsAdapter extends RecyclerView.Adapter<RealmTrackedFlightsAdapter.ViewHolder> {

    private List<RealmTrackFlight> mFlights;


    public RealmTrackedFlightsAdapter(List<RealmTrackFlight> items) {
        mFlights = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_flight_tracked, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        holder.flight = mFlights.get(position);
        holder.mTime.setText(holder.flight.getDate());
        holder.mCode.setText(holder.flight.getAirportCode());
        holder.mPrice.setText(holder.flight.getPrice());
        /*

        Date -> Jul 31, 06:25
    Price -> $72.19
    Departure -> DEL
    Arrival -> BLR
    Code -> UK 811 | Non Stop
         */
    }



    @Override
    public int getItemCount() {
        return mFlights.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        final View mView;
        final TextView mCode;
        final TextView mTime;
        final TextView mPrice;
        final Button mTrack;
        public RealmTrackFlight flight;

        ViewHolder(View view) {
            super(view);
            mView = view;
            mCode = view.findViewById(R.id.item_flight_codes);
            mTime = view.findViewById(R.id.item_flight_time);
            mPrice = view.findViewById(R.id.item_flight_price);
            mTrack = view.findViewById(R.id.button_track);
        }

        @Override
        public String toString() {
            return super.toString() + "'";
        }
    }
}

