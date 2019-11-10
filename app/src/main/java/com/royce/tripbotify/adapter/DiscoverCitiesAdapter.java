package com.royce.tripbotify.adapter;

import android.os.StrictMode;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.royce.tripbotify.R;
import com.royce.tripbotify.core.ApiClient;
import com.royce.tripbotify.database.City;
import com.royce.tripbotify.fragment.DiscoverFragment.OnDiscoverCitiesInteractionListener;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link City} and makes a call to the
 * specified {@link OnDiscoverCitiesInteractionListener}.
 */
public class DiscoverCitiesAdapter extends RecyclerView.Adapter<DiscoverCitiesAdapter.ViewHolder> {

    private final List<City> mValues;
    private final OnDiscoverCitiesInteractionListener mListener;

    public DiscoverCitiesAdapter(List<City> items, OnDiscoverCitiesInteractionListener listener) {
        mValues = items;
        mListener = listener;
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_discover_city, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mName.setText(mValues.get(position).getName());
        holder.mImage.setImageResource(mValues.get(position).getResId());

        /*try {
            //getting city image with unsplash
            Glide.with(holder.mImage)
                    .load(ApiClient.getUnsplashImageURL(holder.mItem.getName()))
                    .apply(new RequestOptions().placeholder(R.drawable.city_blr).error(R.drawable.city_blr))
                    .into(holder.mImage);
        } catch (IllegalArgumentException ex) {
            holder.mImage.setImageResource(R.drawable.city_blr);
        }*/

        holder.mView.setOnClickListener(v -> {
            if (null != mListener) {
                mListener.onCityClick(holder.mItem);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mName;
        public final ImageView mImage;
        public City mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mName = (TextView) view.findViewById(R.id.city_name);
            mImage = (ImageView) view.findViewById(R.id.city_image);
        }

        @Override
        public String toString() {
            return super.toString() + "'";
        }
    }
}
