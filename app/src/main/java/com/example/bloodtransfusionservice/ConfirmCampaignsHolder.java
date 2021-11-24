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

public class ConfirmCampaignsHolder extends RecyclerView.Adapter<ConfirmCampaignsHolder.CampViewHolder> {
    Context context;
    ArrayList<storeCampaignData> list;

    private OnRecyclerViewClickListner listner;

    public interface OnRecyclerViewClickListner {
        void onViewMoreClick(int position, String id);
    }

    public void OnRecyclerViewClickListner(OnRecyclerViewClickListner listner) {
        this.listner = listner;
    }

    public ConfirmCampaignsHolder(Context context, ArrayList<storeCampaignData> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @NotNull
    @Override
    public CampViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.campaign_data, parent, false);
        return new CampViewHolder(v, listner);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull CampViewHolder holder, int position) {
        storeCampaignData data = list.get(position);
        holder.campId.setText(data.getCampaignId());
        holder.campDate.setText(data.getDateOfCamp());
        holder.campStatus.setText(data.getStatus());
    }

    @Override
    public int getItemCount() {

        return list.size();
    }

    public static class CampViewHolder extends RecyclerView.ViewHolder {
        TextView campId, campDate, campStatus, viewMore;

        public CampViewHolder(@NonNull View itemView, OnRecyclerViewClickListner listner) {
            super(itemView);

            campId = itemView.findViewById(R.id.txt_campaign_id);
            campDate = itemView.findViewById(R.id.txt_campaign_date);
            campStatus = itemView.findViewById(R.id.txt_campaign_status);
            viewMore = itemView.findViewById(R.id.txt_view_more);

            viewMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listner != null && getAdapterPosition() != RecyclerView.NO_POSITION) {
                        String campId_var = campId.getText().toString();
                        listner.onViewMoreClick(getAdapterPosition(), campId_var);
                    }
                }
            });
        }
    }

}
