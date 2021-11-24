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

public class RequestBloodHolder extends RecyclerView.Adapter<RequestBloodHolder.RequestViewHolder> {
    Context context;
    ArrayList<storeBloodRequestData> list;

    private RequestBloodHolder.OnRecyclerViewClickListner listner;

    public interface OnRecyclerViewClickListner{
        void onViewMoreClick(int position, String id);
    }

    public void OnRecyclerViewClickListner (RequestBloodHolder.OnRecyclerViewClickListner listner){
        this.listner = listner;
    }

    public RequestBloodHolder(Context context, ArrayList<storeBloodRequestData> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @NotNull
    @Override
    public RequestViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.request_data,parent,false);
        return new RequestViewHolder(v, listner);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull RequestViewHolder holder, int position) {
        storeBloodRequestData data = list.get(position);
        holder.requestId.setText(data.getRequestId());
        holder.requestDate.setText(data.getDate());
        holder.bloodType.setText(data.getBloodType());

    }

    @Override
    public int getItemCount() {

        return list.size();
    }

    public static class RequestViewHolder extends RecyclerView.ViewHolder{
        TextView requestId, requestDate, bloodType, viewMore;

        public RequestViewHolder(@NotNull View itemView, RequestBloodHolder.OnRecyclerViewClickListner listner) {
            super(itemView);

            requestId = itemView.findViewById(R.id.txt_request_id);
            requestDate = itemView.findViewById(R.id.txt_request_date);
            bloodType = itemView.findViewById(R.id.txt_blood_group);
            viewMore = itemView.findViewById(R.id.txt_view_more);

            viewMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listner!=null && getAdapterPosition()!=RecyclerView.NO_POSITION){
                        String requestId_var = requestId.getText().toString();
                        listner.onViewMoreClick(getAdapterPosition(),requestId_var);
                    }
                }
            });
        }
    }

}

