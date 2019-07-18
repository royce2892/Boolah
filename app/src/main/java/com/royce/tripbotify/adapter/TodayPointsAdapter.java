package com.royce.tripbotify.adapter;

import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

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
            //todo uncomment
            Glide.with(holder.mImage)
                    .load(ApiClient.getUnsplashImageURL(holder.mItem.getName()))
                    .apply(new RequestOptions().placeholder(R.drawable.city_blr).error(R.drawable.city_blr))
                    .into(holder.mImage);
        } catch (IllegalArgumentException ex) {
            holder.mImage.setImageResource(R.drawable.city_blr);
        }
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

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mName = view.findViewById(R.id.poi_name);
            mType = view.findViewById(R.id.poi_type);
            mImage = view.findViewById(R.id.poi_image);
            mTime = view.findViewById(R.id.poi_time);
        }

        @Override
        public String toString() {
            return super.toString() + "'";
        }
    }
}

