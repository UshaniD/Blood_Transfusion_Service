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

public class DonorInforHolder extends RecyclerView.Adapter<DonorInforHolder.DonorInfoViewHolder> {
    Context context;
    ArrayList<storeData> list;

    private DonorInforHolder.OnRecyclerViewClickListner listner;

    public interface OnRecyclerViewClickListner {
        void onViewMoreClick(int position, String id);
    }

    public void OnRecyclerViewClickListner(DonorInforHolder.OnRecyclerViewClickListner listner) {
        this.listner = listner;
    }

    public DonorInforHolder(Context context, ArrayList<storeData> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @NotNull
    @Override
    public DonorInforHolder.DonorInfoViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.donor_data, parent, false);
        return new DonorInforHolder.DonorInfoViewHolder(v, listner);
    }


    @Override
    public void onBindViewHolder(@NonNull DonorInfoViewHolder holder, int position) {
        storeData data = list.get(position);
        holder.donorName.setText(data.getFullname());
        holder.mobileNumber.setText(data.getMobile());
        holder.district.setText(data.getDistrict());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class DonorInfoViewHolder extends RecyclerView.ViewHolder {
        TextView donorName, mobileNumber, district, viewMore;

        public DonorInfoViewHolder(@NotNull View itemView, DonorInforHolder.OnRecyclerViewClickListner listner) {
            super(itemView);

            donorName = itemView.findViewById(R.id.txt_donor_name);
            mobileNumber = itemView.findViewById(R.id.txt_donor_mobile);
            district = itemView.findViewById(R.id.txt_district);
            viewMore = itemView.findViewById(R.id.txt_view_more);

            viewMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listner != null && getAdapterPosition() != RecyclerView.NO_POSITION) {
                        String mobileNo_var = mobileNumber.getText().toString();
                        listner.onViewMoreClick(getAdapterPosition(), mobileNo_var);
                    }
                }
            });
        }
    }

}
