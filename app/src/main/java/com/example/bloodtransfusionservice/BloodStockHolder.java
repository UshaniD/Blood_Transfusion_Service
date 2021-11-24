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

public class BloodStockHolder extends RecyclerView.Adapter<BloodStockHolder.StoreViewHolder> {
    Context context;
    ArrayList<storeStockData> list;
    TextView txtRelease;

    private BloodStockHolder.OnRecyclerViewClickListner listner;

    public void notifyItemRemoved() {
    }

    public interface OnRecyclerViewClickListner {
        void onReleaseClick(int position, String id, String unit);
    }

    public void OnRecyclerViewClickListner(BloodStockHolder.OnRecyclerViewClickListner listner) {
        this.listner = listner;
    }

    public BloodStockHolder(Context context, ArrayList<storeStockData> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @NotNull
    @Override
    public StoreViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.blood_stock_data, parent, false);
        return new StoreViewHolder(v, listner);
    }

    @Override
    public void onBindViewHolder(@NonNull StoreViewHolder holder, int position) {
        storeStockData data = list.get(position);
        holder.txtType.setText(data.getBloodGroup());
        holder.txtUnit.setText(data.getUnit());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class StoreViewHolder extends RecyclerView.ViewHolder {
        TextView txtType, txtUnit;

        public StoreViewHolder(@NonNull View itemView, BloodStockHolder.OnRecyclerViewClickListner listner) {
            super(itemView);

            txtType = itemView.findViewById(R.id.txt_blood_type);
            txtUnit = itemView.findViewById(R.id.txt_blood_unit);
            txtRelease = itemView.findViewById(R.id.txt_release);

            if (ViewBloodStockActivity.releaseFlag.equals("true")) {
                txtRelease.setVisibility(View.VISIBLE);
            } else {
                txtRelease.setVisibility(View.INVISIBLE);
            }

            txtRelease.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listner != null && getAdapterPosition() != RecyclerView.NO_POSITION) {
                        String bloodType_var = txtType.getText().toString();
                        String bloodUnit_var = txtUnit.getText().toString();
                        listner.onReleaseClick(getAdapterPosition(), bloodType_var, bloodUnit_var);
                    }
                }
            });
        }
    }
}
