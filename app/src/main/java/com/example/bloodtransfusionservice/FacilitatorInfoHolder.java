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

public class FacilitatorInfoHolder extends RecyclerView.Adapter<FacilitatorInfoHolder.FacilitatorViewHolder> {
    Context context;
    ArrayList<storeFacilitatorData> list;

    private FacilitatorInfoHolder.OnRecyclerViewClickListner listner;

    public interface OnRecyclerViewClickListner {
        void onViewMoreClick(int position, String id);
    }

    public void OnRecyclerViewClickListner(FacilitatorInfoHolder.OnRecyclerViewClickListner listner) {
        this.listner = listner;
    }

    public FacilitatorInfoHolder(Context context, ArrayList<storeFacilitatorData> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @NotNull
    @Override
    public FacilitatorInfoHolder.FacilitatorViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.facilitator_data, parent, false);
        return new FacilitatorInfoHolder.FacilitatorViewHolder(v, listner);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull FacilitatorInfoHolder.FacilitatorViewHolder holder, int position) {
        storeFacilitatorData data = list.get(position);
        holder.facilitatorName.setText(data.getFullname());
        holder.mobileNumber.setText(data.getMobile());
        holder.district.setText(data.getDistrict());

    }

    @Override
    public int getItemCount() {

        return list.size();
    }

    public static class FacilitatorViewHolder extends RecyclerView.ViewHolder {
        TextView facilitatorName, mobileNumber, district, viewMore;

        public FacilitatorViewHolder(@NotNull View itemView, FacilitatorInfoHolder.OnRecyclerViewClickListner listner) {
            super(itemView);

            facilitatorName = itemView.findViewById(R.id.facilitator_name);
            mobileNumber = itemView.findViewById(R.id.mobile_number);
            district = itemView.findViewById(R.id.district);
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
