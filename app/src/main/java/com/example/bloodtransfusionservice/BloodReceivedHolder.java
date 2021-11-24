package com.example.bloodtransfusionservice;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class BloodReceivedHolder extends RecyclerView.Adapter<BloodReceivedHolder.bloodReceivedViewHolder> {
    Context context;
    ArrayList<storeBloodReceivedData> list;

    private BloodReceivedHolder.OnRecyclerViewClickListner listner;

    public interface OnRecyclerViewClickListner {
        void onViewMoreClick(int position, String id);
    }

    public void OnRecyclerViewClickListner(BloodReceivedHolder.OnRecyclerViewClickListner listner) {
        this.listner = listner;
    }

    public BloodReceivedHolder(Context context, ArrayList<storeBloodReceivedData> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @NotNull
    @Override
    public BloodReceivedHolder.bloodReceivedViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.blood_received_data, parent, false);
        return new BloodReceivedHolder.bloodReceivedViewHolder(v, listner);
    }

    @Override
    public void onBindViewHolder(@NonNull BloodReceivedHolder.bloodReceivedViewHolder holder, int position) {
        storeBloodReceivedData data = list.get(position);
        holder.bloodGroup.setText(data.getBloodGroup());
        holder.totalUnits.setText(data.getReceivedUnits());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class bloodReceivedViewHolder extends RecyclerView.ViewHolder {
        TextView bloodGroup, totalUnits;

        public bloodReceivedViewHolder(@NotNull View itemView, BloodReceivedHolder.OnRecyclerViewClickListner listner) {
            super(itemView);

            bloodGroup = itemView.findViewById(R.id.blood_group);
            totalUnits = itemView.findViewById(R.id.total_units);

        }
    }

}
