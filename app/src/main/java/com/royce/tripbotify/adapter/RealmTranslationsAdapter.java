package com.royce.tripbotify.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.royce.tripbotify.R;
import com.royce.tripbotify.database.RealmTranslation;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link RealmTranslation}
 */
public class RealmTranslationsAdapter extends RecyclerView.Adapter<RealmTranslationsAdapter.ViewHolder> {

    private List<RealmTranslation> mValues;

    public RealmTranslationsAdapter(List<RealmTranslation> mValues) {
        this.mValues = mValues;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_poi_translate, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mInput.setText(holder.mItem.getInput());
        holder.mOutput.setText(holder.mItem.getOutput());
        holder.mLang.setText(holder.mItem.getLanguageCode());
    }

    @Override
    public int getItemCount() {
        return mValues.size();
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
        public final TextView mInput;
        public final TextView mOutput;
        public final TextView mLang;
        public RealmTranslation mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mInput = view.findViewById(R.id.text_input);
            mOutput = view.findViewById(R.id.text_output);
            mLang = view.findViewById(R.id.text_language);
        }

        @Override
        public String toString() {
            return super.toString() + "'";
        }
    }
}

