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

public class VerifyUserInfoHolder extends RecyclerView.Adapter<VerifyUserInfoHolder.UserInfoViewHolder> {
    Context context;
    ArrayList<storeData> list;

    public VerifyUserInfoHolder(Context context, ArrayList<storeData> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @NotNull
    @Override
    public VerifyUserInfoHolder.UserInfoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.verify_user_data,parent,false);
        return new VerifyUserInfoHolder.UserInfoViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull VerifyUserInfoHolder.UserInfoViewHolder holder, int position) {
        storeData data = list.get(position);
        holder.phone.setText(data.getMobile());
        holder.donorName.setText(data.getFullname());
        holder.bloodType.setText(data.getBloodGroup());

    }

    @Override
    public int getItemCount() {

        return list.size();
    }

    public static class UserInfoViewHolder extends RecyclerView.ViewHolder{
        TextView phone, donorName, bloodType;

        public UserInfoViewHolder(@NotNull View itemView) {
            super(itemView);

            phone = itemView.findViewById(R.id.txt_phone);
            donorName = itemView.findViewById(R.id.txt_donor_name);
            bloodType = itemView.findViewById(R.id.txt_blood_type);
            
        }

    }

}
