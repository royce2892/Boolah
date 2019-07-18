package com.royce.tripbotify.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.amadeus.resources.FlightDate;
import com.amadeus.resources.FlightOffer;
import com.royce.tripbotify.R;
import com.royce.tripbotify.activity.GenericListActivity;
import com.royce.tripbotify.database.City;
import com.royce.tripbotify.utils.Utils;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link City} and makes a call to the
 * specified {@link}.
 */
public class FlightsAdapter extends RecyclerView.Adapter<FlightsAdapter.ViewHolder> {

    private List<FlightDate> mDates;
    private List<FlightOffer> mOffers;
    private int type;
    // private final OnFlightsInteractionListener mListener;

    public FlightsAdapter(List<FlightDate> items, boolean placeHolder) {
        mDates = items;
        type = GenericListActivity.TYPE_FLIGHT_FARES;
    }

    public FlightsAdapter(List<FlightOffer> items) {
        mOffers = items;
        type = GenericListActivity.TYPE_FLIGHT_CHEAPEST_FARES;
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
            holder.mOffer = mOffers.get(position);
            FlightOffer.Segment departure = holder.mOffer.getOfferItems()[0].getServices()[0].getSegments()[0];
            holder.mTime.setText(Utils.getJustDate(departure.getFlightSegment().getDeparture().getAt()));
            String nonStop = holder.mOffer.getOfferItems()[0].getServices()[0].getSegments().length == 1 ? "Non Stop" :
                    holder.mOffer.getOfferItems()[0].getServices()[0].getSegments().length + " stops";
            holder.mReturnPlaceHolder.setText(departure.getFlightSegment().getCarrierCode() + " " +
                    departure.getFlightSegment().getNumber() + " | " + nonStop);
            //holder.mReturnTime.setText(Utils.getJustDate(returnFlight.getFlightSegment().getDeparture().getAt()));
            holder.mPrice.setText("$".concat((holder.mOffer.getOfferItems()[0].getPrice().getTotal()) + ""));
        } else {
            holder.mDate = mDates.get(position);
            holder.mTime.setText(Utils.getJustDate(holder.mDate.getDepartureDate()));
            holder.mReturnTime.setText(Utils.getJustDate(holder.mDate.getReturnDate()));
            String price = holder.mDate.getPrice().toString().substring(holder.mDate.getPrice().toString().indexOf("=") + 1);
            price = price.substring(0, price.length() - 1);
            holder.mPrice.setText("$".concat(price));
        }

        /*holder.mView.setOnClickListener(v -> {
            if (null != mListener) {
                // Notify the active callbacks interface (the activity, if the
                // fragment is attached to one) that an item has been selected.
                mListener.onCityClick(holder.mItem);
            }
        });*/
    }

    @Override
    public int getItemCount() {
        return mDates == null ? mOffers.size() : mDates.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mPrice;
        public final TextView mTime;
        public final TextView mReturnTime;
        public final TextView mReturnPlaceHolder;
        public final TextView mDeparturePlaceHolder;
        public FlightOffer mOffer;
        public FlightDate mDate;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mPrice = view.findViewById(R.id.item_flight_price);
            mTime = view.findViewById(R.id.item_flight_time);
            mReturnTime = view.findViewById(R.id.item_flight_return_time);
            mReturnPlaceHolder = view.findViewById(R.id.item_flight_return_placeholder);
            mDeparturePlaceHolder = view.findViewById(R.id.item_flight_departure_placeholder);
        }

        @Override
        public String toString() {
            return super.toString() + "'";
        }
    }
}
    
