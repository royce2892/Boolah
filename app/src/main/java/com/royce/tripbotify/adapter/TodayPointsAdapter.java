package com.royce.tripbotify.adapter;

import android.content.Intent;
import android.net.Uri;
import android.os.StrictMode;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amadeus.resources.PointOfInterest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.royce.tripbotify.R;
import com.royce.tripbotify.core.ApiClient;
import com.royce.tripbotify.fragment.DiscoverFragment.OnDiscoverCitiesInteractionListener;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link PointOfInterest} and makes a call to the
 * specified {@link OnDiscoverCitiesInteractionListener}.
 */
public class TodayPointsAdapter extends RecyclerView.Adapter<TodayPointsAdapter.ViewHolder> {

    private final List<PointOfInterest> mValues;

    public TodayPointsAdapter(List<PointOfInterest> items) {
        mValues = items;
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_poi_today, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        // Log.i(AppConstants.LOG_TAG, holder.mItem.toString());
        holder.mName.setText(holder.mItem.getName());
        holder.mTime.setText(position == 0 ? "Now" : position == 1 ? "Next" : "Later");
        holder.mType.setText(holder.mItem.getTags()[0]);
        //Log.i(AppConstants.LOG_TAG, ApiClient.getUnsplashImageURL(holder.mItem.getName()));

        try {
            Glide.with(holder.mImage)
                    .load(ApiClient.getUnsplashImageURL(holder.mItem.getName()))
                    .apply(new RequestOptions().placeholder(R.drawable.city_blr).error(R.drawable.city_blr))
                    .into(holder.mImage);
        } catch (IllegalArgumentException ex) {
            holder.mImage.setImageResource(R.drawable.city_blr);
        }

        holder.mNavigate.setOnClickListener(v -> gotoMap(v, holder.mItem));
        holder.mRemove.setOnClickListener(v -> confirmRemove(v, holder.mItem));
    }

    private void gotoMap(View v, PointOfInterest mItem) {
        Uri gmmIntentUri = Uri.parse("google.navigation:q=" + mItem.getGeoCode().getLatitude() + "," + mItem.getGeoCode().getLongitude());
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        v.getContext().startActivity(mapIntent);

    }

    private void confirmRemove(View view, PointOfInterest mItem) {
        mValues.remove(mItem);
        notifyDataSetChanged();
        Toast.makeText(view.getContext(), mItem.getName() + " removed from your itinerary", Toast.LENGTH_SHORT).show();
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public void removeTopItem() {
        if (mValues.size() > 0) {
            mValues.remove(0);
            notifyDataSetChanged();
        }
    }

    public PointOfInterest getTopItem() {
        if (mValues.size() > 0)
            return mValues.get(0);
        return null;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mName;
        public final TextView mType;
        public final TextView mTime;
        public final ImageView mImage;
        public PointOfInterest mItem;
        public final Button mNavigate;
        public final ImageButton mRemove;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mName = view.findViewById(R.id.poi_name);
            mType = view.findViewById(R.id.poi_type);
            mImage = view.findViewById(R.id.poi_image);
            mTime = view.findViewById(R.id.poi_time);
            mNavigate = view.findViewById(R.id.button_navigate);
            mRemove = view.findViewById(R.id.button_remove);
        }

        @Override
        public String toString() {
            return super.toString() + "'";
        }
    }
}

