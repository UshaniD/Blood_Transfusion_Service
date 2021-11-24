package com.example.bloodtransfusionservice;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;

public class RequestBloodHistoryActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    RequestBloodHolder requestBloodHolder;
    ArrayList<storeBloodRequestData> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_blood_history);

        //Get phone number from session
        SessionManager sessionManager = new SessionManager(this,SessionManager.SESSION_USER_SESSION);
        HashMap<String,String> userDetails =  sessionManager.getUserDetailFromSession();
        String phone = userDetails.get(SessionManager.KEY_PHONE_NO);
        Log.d("====PHONE NUMBER : ", String.valueOf(phone));

        recyclerView = findViewById(R.id.request_history_recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        list = new ArrayList<>();
        requestBloodHolder = new RequestBloodHolder(this, list);
        recyclerView.setAdapter(requestBloodHolder);

        requestBloodHolder.OnRecyclerViewClickListner(new RequestBloodHolder.OnRecyclerViewClickListner() {
            @Override
            public void onViewMoreClick(int position, String id) {
                Toast.makeText(getApplicationContext(), "POSITION: "+position+" ID: "+id, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), RequestBloodDetailsActivity.class);
                intent.putExtra("RequestId", id);
                startActivity(intent);
            }
        });

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference("request");
        Query checkmobile = databaseReference.orderByChild("phone").equalTo(phone);

        checkmobile.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                        storeBloodRequestData data = dataSnapshot.getValue(storeBloodRequestData.class);
                        list.add(data);
                    }
                    requestBloodHolder.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

    }

    public void moveToHomeScreen(View view) {
        Intent intent = new Intent(getApplicationContext(), LeftNavActivity.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {

    }

}