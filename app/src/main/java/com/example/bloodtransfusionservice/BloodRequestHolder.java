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

public class BloodRequestHolder extends RecyclerView.Adapter<BloodRequestHolder.RequestViewHolder> {
    Context context;
    ArrayList<storeBloodRequestData> list;

    private BloodRequestHolder.OnRecyclerViewClickListner listner;

    public interface OnRecyclerViewClickListner {
        void onViewMoreClick(int position, String id);
    }

    public void OnRecyclerViewClickListner(BloodRequestHolder.OnRecyclerViewClickListner listner) {
        this.listner = listner;
    }

    public BloodRequestHolder(Context context, ArrayList<storeBloodRequestData> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @NotNull
    @Override
    public BloodRequestHolder.RequestViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.blood_request_data, parent, false);
        return new BloodRequestHolder.RequestViewHolder(v, listner);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull BloodRequestHolder.RequestViewHolder holder, int position) {
        storeBloodRequestData data = list.get(position);
        holder.bloodType.setText(data.getBloodType());
        holder.noOfRequests.setText(data.getPhone());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class RequestViewHolder extends RecyclerView.ViewHolder {
        TextView bloodType, noOfRequests;

        public RequestViewHolder(@NotNull View itemView, BloodRequestHolder.OnRecyclerViewClickListner listner) {
            super(itemView);

            bloodType = itemView.findViewById(R.id.blood_group);
            noOfRequests = itemView.findViewById(R.id.total_requests);

        }
    }

}

