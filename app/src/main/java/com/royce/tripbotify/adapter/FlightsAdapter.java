package com.royce.tripbotify.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.amadeus.resources.FlightDate;
import com.amadeus.resources.FlightOffer;
import com.royce.tripbotify.R;
import com.royce.tripbotify.activity.GenericListActivity;
import com.royce.tripbotify.database.City;
import com.royce.tripbotify.database.RealmTrackFlight;
import com.royce.tripbotify.utils.Utils;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmObject;

/**
 * {@link RecyclerView.Adapter} that can display a {@link City} and makes a call to the
 * specified {@link}.
 */
public class FlightsAdapter extends RecyclerView.Adapter<FlightsAdapter.ViewHolder> {

    private List<FlightDate> mDates;
    private List<FlightOffer> mOffers;
    private int type;
    private String iata, airportCode, date;
    private Realm realm;

    public FlightsAdapter(List<FlightDate> items, String iata, String airportCode) {
        mDates = items;
        this.iata = iata;
        this.airportCode = airportCode;
        type = GenericListActivity.TYPE_FLIGHT_FARES;
        realm = Realm.getDefaultInstance();
    }

    public FlightsAdapter(List<FlightOffer> items, String date, String iata, String airportCode) {
        mOffers = items;
        this.iata = iata;
        this.date = Utils.getMonthDay(date);
        this.airportCode = airportCode;
        type = GenericListActivity.TYPE_FLIGHT_CHEAPEST_FARES;
        realm = Realm.getDefaultInstance();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_flight, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        if (type == 1) {
            //cheapest fares for a given date
            holder.mOffer = mOffers.get(position);
            FlightOffer.Segment departure = holder.mOffer.getOfferItems()[0].getServices()[0].getSegments()[0];
            holder.mTime.setText(Utils.getJustDate(departure.getFlightSegment().getDeparture().getAt()));
            String nonStop = holder.mOffer.getOfferItems()[0].getServices()[0].getSegments().length == 1 ? "Non Stop" :
                    holder.mOffer.getOfferItems()[0].getServices()[0].getSegments().length + " stops";
            holder.mReturnPlaceHolder.setText(departure.getFlightSegment().getCarrierCode() + " " +
                    departure.getFlightSegment().getNumber() + " | " + nonStop);
            //holder.mReturnTime.setText(Utils.getJustDate(returnFlight.getFlightSegment().getDeparture().getAt()));
            double price = holder.mOffer.getOfferItems()[0].getPrice().getTotal();
            if (price > 2000)
                price = price / 68.80;
            holder.mPrice.setText("$".concat(price + ""));
            holder.mTrack.setOnClickListener(v -> saveToRealm(v.getContext(), date + ", "
                    + holder.mTime.getText().toString(), holder.mPrice.getText().toString(), holder.mReturnPlaceHolder.getText().toString()));
        } else {
            // cheapest dates
            holder.mDate = mDates.get(position);
            holder.mTime.setText(Utils.getJustDate(holder.mDate.getDepartureDate()));
            holder.mReturnTime.setText(Utils.getJustDate(holder.mDate.getReturnDate()));
            String price = holder.mDate.getPrice().toString().substring(holder.mDate.getPrice().toString().indexOf("=") + 1);
            price = price.substring(0, price.length() - 1);
            try {
                double pp = Double.valueOf(price);
                if (pp > 2000)
                    pp = pp / 68.80;
                holder.mPrice.setText("$".concat(pp + ""));
            } catch (NumberFormatException ex) {
                holder.mPrice.setText("$".concat(price));
            }
            holder.mTrack.setOnClickListener(v -> saveToRealm(v.getContext(), holder.mTime.getText().toString() + " - "
                    + holder.mReturnTime.getText().toString(), holder.mPrice.getText().toString(), null));
        }
    }

    private void saveToRealm(Context context, String date, String price, String flightCode) {
        realm.beginTransaction();
        RealmTrackFlight flight = realm.createObject(RealmTrackFlight.class);
        flight.setDate(date);
        flight.setDepartureCode(iata);
        flight.setArrivalCode(airportCode);
        flight.setPrice(price);
        if (flightCode != null)
            flight.setFlightCode(flightCode);
        realm.commitTransaction();
        Toast.makeText(context, "You're now tracking the fare of the selected flight, view it from MY WORLD page", Toast.LENGTH_SHORT).show();
    }

    @Override
    public int getItemCount() {
        return mDates == null ? mOffers.size() : mDates.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        final View mView;
        final TextView mPrice;
        final TextView mTime;
        final TextView mReturnTime;
        final TextView mReturnPlaceHolder;
        final TextView mDeparturePlaceHolder;
        FlightOffer mOffer;
        final Button mTrack;
        FlightDate mDate;

        ViewHolder(View view) {
            super(view);
            mView = view;
            mPrice = view.findViewById(R.id.item_flight_price);
            mTime = view.findViewById(R.id.item_flight_time);
            mReturnTime = view.findViewById(R.id.item_flight_return_time);
            mReturnPlaceHolder = view.findViewById(R.id.item_flight_return_placeholder);
            mDeparturePlaceHolder = view.findViewById(R.id.item_flight_departure_placeholder);
            mTrack = view.findViewById(R.id.button_track);
        }

        @Override
        public String toString() {
            return super.toString() + "'";
        }
    }
}
    
