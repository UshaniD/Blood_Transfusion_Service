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

public class BloodDonationHolder extends RecyclerView.Adapter<BloodDonationHolder.BloodDonationViewHolder> {
    Context context;
    ArrayList<storeBloodReceivedData> list;

    private BloodDonationHolder.OnRecyclerViewClickListner listner;

    public interface OnRecyclerViewClickListner {
        void onViewMoreClick(int position, String id);
    }

    public void OnRecyclerViewClickListner(BloodDonationHolder.OnRecyclerViewClickListner listner) {
        this.listner = listner;
    }

    public BloodDonationHolder(Context context, ArrayList<storeBloodReceivedData> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @NotNull
    @Override
    public BloodDonationHolder.BloodDonationViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.donation_data, parent, false);
        return new BloodDonationHolder.BloodDonationViewHolder(v, listner);
    }

    @Override
    public void onBindViewHolder(@NonNull BloodDonationHolder.BloodDonationViewHolder holder, int position) {
        storeBloodReceivedData data = list.get(position);
        holder.id.setText(data.getId());
        holder.donationDate.setText(data.getDate());
        holder.donatedDistrict.setText(data.getDonatedDistrict());
        holder.donatedUnits.setText(data.getReceivedUnits());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class BloodDonationViewHolder extends RecyclerView.ViewHolder {
        TextView id, donationDate, donatedDistrict, donatedUnits;

        public BloodDonationViewHolder(@NotNull View itemView, BloodDonationHolder.OnRecyclerViewClickListner listner) {
            super(itemView);

            id = itemView.findViewById(R.id.txt_donation_id);
            donationDate = itemView.findViewById(R.id.txt_donated_date);
            donatedDistrict = itemView.findViewById(R.id.txt_district_donated);
            donatedUnits = itemView.findViewById(R.id.txt_no_of_units);
        }
    }

}