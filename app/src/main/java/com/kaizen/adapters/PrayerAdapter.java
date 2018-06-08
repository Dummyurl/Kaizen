package com.kaizen.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kaizen.R;

import java.util.List;

public class PrayerAdapter extends CommonRecyclerAdapter<String> {

    private List<String> prayerNames;

    public PrayerAdapter(List<String> prayerNames) {
        this.prayerNames = prayerNames;
    }

    @Override
    public RecyclerView.ViewHolder onCreateBasicItemViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_prayer, parent, false);

        return new PrayerViewHolder(itemView);
    }

    @Override
    public void onBindBasicItemView(RecyclerView.ViewHolder holder, int position) {
        PrayerViewHolder prayerViewHolder = (PrayerViewHolder) holder;
        prayerViewHolder.bindData(position);
    }

    public class PrayerViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_time, tv_prayer;

        private PrayerViewHolder(View view) {
            super(view);
            tv_time = view.findViewById(R.id.tv_time);
            tv_prayer = view.findViewById(R.id.tv_prayer);
        }

        public void bindData(int position) {
            String time = getItem(position);
            String prayer = prayerNames.get(position);

            tv_time.setText(time);
            tv_prayer.setText(prayer);
        }
    }
}
