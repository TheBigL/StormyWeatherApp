package com.lebanmohamed.stormy.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.lebanmohamed.stormy.R;
import com.lebanmohamed.stormy.databinding.HourlyListItemBinding;
import com.lebanmohamed.stormy.weather.Hour;

import java.util.List;

public class HourlyAdapter extends RecyclerView.Adapter<HourlyAdapter.ViewHolder>
{
    private List<Hour> hours;
    private Context context;


    public HourlyAdapter(List<Hour> hours, Context context)
    {
        this.context = context;
        this.hours = hours;
    }



    @NonNull
    @Override
    public HourlyAdapter.ViewHolder onCreateViewHolder(@NonNull  ViewGroup parent, int viewType)
    {
        HourlyListItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.hourly_list_item, parent, false);


        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position)
    {
        Hour hour = hours.get(position);
        holder.hourlyListItemBinding.setHour(hour);

    }



    @Override
    public int getItemCount() {
        return hours.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        public HourlyListItemBinding hourlyListItemBinding;


        public ViewHolder()
        {
            super(hourlyLayoutItemBinding.getRoot());
            hourlyListItemBinding = hourlyLayoutItemBinding;
        }
    }
}
