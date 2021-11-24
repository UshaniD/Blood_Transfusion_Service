package com.example.bloodtransfusionservice;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class ConfirmCampaignActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    ConfirmCampaignsHolder confirmCampaignsHolder;
    ArrayList<storeCampaignData> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_campaign);

        //Get phone number from session
        SessionManager sessionManager = new SessionManager(this, SessionManager.SESSION_USER_SESSION);
        HashMap<String, String> userDetails = sessionManager.getUserDetailFromSession();
        String phone = userDetails.get(SessionManager.KEY_PHONE_NO);
        Log.d("PHONE NUMBER : ", String.valueOf(phone));

        recyclerView = findViewById(R.id.confirm_campaign_recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        list = new ArrayList<>();
        confirmCampaignsHolder = new ConfirmCampaignsHolder(this, list);
        recyclerView.setAdapter(confirmCampaignsHolder);

        confirmCampaignsHolder.OnRecyclerViewClickListner(new ConfirmCampaignsHolder.OnRecyclerViewClickListner() {
            @Override
            public void onViewMoreClick(int position, String id) {
                Toast.makeText(getApplicationContext(), "Selected ID: " + id, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), ConfirmCampaignDetailsActivity.class);
                intent.putExtra("CampId", id);
                startActivity(intent);
            }
        });

        //Get district
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference1 = firebaseDatabase.getReference("facilitator");
        Query checkmobile = databaseReference1.orderByChild("mobile").equalTo(phone);

        checkmobile.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String district_var = snapshot.child(phone).child("district").getValue(String.class);
                    Log.d("DISTRICT : ", String.valueOf(district_var));

                    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                    DatabaseReference databaseReference = firebaseDatabase.getReference("campaign");
                    databaseReference.addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {
                            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
                            Date date = new Date();
                            String currentDate = dateFormat.format(date);
                            Log.d("Current date", String.valueOf(currentDate));

                            storeCampaignData data = snapshot.getValue(storeCampaignData.class);
                            String dateInDB = data.getDateOfCamp();

                            Date dateNew = null;
                            try {
                                dateNew = (Date) dateFormat.parse(dateInDB);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            SimpleDateFormat newFormat = new SimpleDateFormat("yyyy/MM/dd");
                            String date_ = newFormat.format(dateNew);
                            Log.d("date_", String.valueOf(date_));

                            Date date1 = null;
                            try {
                                date1 = new SimpleDateFormat("yyyy/MM/dd").parse(date_);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            Date date2 = null;
                            try {
                                date2 = new SimpleDateFormat("yyyy/MM/dd").parse(currentDate);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            long diff = date1.getTime() - date2.getTime();

                            TimeUnit time = TimeUnit.DAYS;
                            long diffrence = time.convert(diff, TimeUnit.MILLISECONDS);
                            System.out.println("The difference in days is : " + diffrence);

                            confirmCampaignsHolder.notifyDataSetChanged();
                            if (data.getDistrict().equals(district_var) && data.getStatus().equals("Pending") && diffrence > 0) {
                                list.add(data);
                            }
                        }

                        @Override
                        public void onChildChanged(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {

                        }

                        @Override
                        public void onChildRemoved(@NonNull @NotNull DataSnapshot snapshot) {

                        }

                        @Override
                        public void onChildMoved(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {

                        }

                        @Override
                        public void onCancelled(@NonNull @NotNull DatabaseError error) {

                        }
                    });
                    confirmCampaignsHolder.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

    }

    public void moveToHomeScreen(View view) {
        Intent intent = new Intent(getApplicationContext(), FacilitatorLeftNavActivity.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {

    }
}