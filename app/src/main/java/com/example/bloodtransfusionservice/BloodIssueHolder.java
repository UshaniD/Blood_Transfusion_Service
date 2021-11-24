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

public class BloodIssueHolder extends RecyclerView.Adapter<BloodIssueHolder.bloodIssueViewHolder> {
    Context context;
    ArrayList<storeBloodIssueData> list;

    private BloodIssueHolder.OnRecyclerViewClickListner listner;

    public interface OnRecyclerViewClickListner {
        void onViewMoreClick(int position, String id);
    }

    public void OnRecyclerViewClickListner(BloodIssueHolder.OnRecyclerViewClickListner listner) {
        this.listner = listner;
    }

    public BloodIssueHolder(Context context, ArrayList<storeBloodIssueData> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @NotNull
    @Override
    public bloodIssueViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.blood_issue_data, parent, false);
        return new bloodIssueViewHolder(v, listner);
    }

    @Override
    public void onBindViewHolder(@NonNull bloodIssueViewHolder holder, int position) {
        storeBloodIssueData data = list.get(position);
        holder.bloodGroup.setText(data.getBloodGroup());
        holder.totalUnits.setText(data.getIssuedUnit());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class bloodIssueViewHolder extends RecyclerView.ViewHolder {
        TextView bloodGroup, totalUnits;

        public bloodIssueViewHolder(@NotNull View itemView, OnRecyclerViewClickListner listner) {
            super(itemView);

            bloodGroup = itemView.findViewById(R.id.blood_group);
            totalUnits = itemView.findViewById(R.id.total_units);

        }
    }

}
