package com.royce.tripbotify.adapter;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.amadeus.resources.PointOfInterest;
import com.royce.tripbotify.R;
import com.royce.tripbotify.database.RealmPointOfInterest;
import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link RealmPointOfInterest}
 */
public class RealmPointsAdapter extends RecyclerView.Adapter<RealmPointsAdapter.ViewHolder> {

    private List<RealmPointOfInterest> mValues;

    public RealmPointsAdapter(List<RealmPointOfInterest> mValues) {
        this.mValues = mValues;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_poi_realm, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        // Log.i(AppConstants.LOG_TAG, holder.mItem.toString());
        holder.mName.setText(holder.mItem.getName());
        holder.mCity.setText(holder.mItem.getCity());
        holder.mType.setText(holder.mItem.getCategory());
        //Log.i(AppConstants.LOG_TAG, ApiClient.getUnsplashImageURL(holder.mItem.getName()));

        holder.mNavigate.setOnClickListener(v -> {
            gotoMap(v, holder.mItem.getLat(), holder.mItem.getLon());
        });

//        holder.mImage.setImageResource(mValues.get(position).getResId());

       /* holder.mView.setOnClickListener(v -> {
            if (null != mListener) {
                // Notify the active callbacks interface (the activity, if the
                // fragment is attached to one) that an item has been selected.
                mListener.onCityClick(holder.mItem);
            }
        });*/
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    private void gotoMap(View v, double lat, double lng) {
        Uri gmmIntentUri = Uri.parse("google.navigation:q=" + lat + "," + lng);
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        v.getContext().startActivity(mapIntent);
    }


   /* public void removeTopItem() {
        if (mValues.size() > 0) {
            mValues.remove(0);
            notifyDataSetChanged();
        }
    }

    public PointOfInterest getTopItem() {
        if (mValues.size() > 0)
            return mValues.get(0);
        return null;
    }*/

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mName;
        public final TextView mType;
        public final TextView mCity;
        public final Button mNavigate;
        public RealmPointOfInterest mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mName = view.findViewById(R.id.poi_name);
            mType = view.findViewById(R.id.poi_type);
            mCity = view.findViewById(R.id.poi_city);
            mNavigate = view.findViewById(R.id.poi_navi);
        }

        @Override
        public String toString() {
            return super.toString() + "'";
        }
    }
}

