package com.example.bloodtransfusionservice;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
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

public class UpComingCampaignActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    CampaignsHolder campaignsHolder;
    ArrayList<storeCampaignData> list;
    ImageView backImg;
    static final int PICK_MAP_POINT_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_up_coming_campaign);

        //Get phone number from session
        SessionManager sessionManager = new SessionManager(this, SessionManager.SESSION_USER_SESSION);
        HashMap<String, String> userDetails = sessionManager.getUserDetailFromSession();
        String phone = userDetails.get(SessionManager.KEY_PHONE_NO);
        Log.d("PHONE NUMBER : ", String.valueOf(phone));

        recyclerView = findViewById(R.id.upcoming_campaign_recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        list = new ArrayList<>();
        campaignsHolder = new CampaignsHolder(this, list);
        recyclerView.setAdapter(campaignsHolder);
        backImg = findViewById(R.id.img_back);

        campaignsHolder.OnRecyclerViewClickListner(new CampaignsHolder.OnRecyclerViewClickListner() {
            @Override
            public void onViewMoreClick(int position, String id) {
                Toast.makeText(getApplicationContext(), "POSITION: " + position + " ID: " + id, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), UpComingCampaignDetailsActivity.class);
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
                            storeCampaignData data = snapshot.getValue(storeCampaignData.class);
                            campaignsHolder.notifyDataSetChanged();
                            String campDate = data.getDateOfCamp();
                            Log.d("Camp date", String.valueOf(campDate));

                            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
                            Date date = new Date();
                            String currentDate = dateFormat.format(date);
                            Log.d("Current date", String.valueOf(currentDate));

                            Date date1 = null;
                            try {
                                date1 = new SimpleDateFormat("yyyy/MM/dd").parse(campDate);
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

                            if (data.getDistrict().equals(district_var) && data.getStatus().equals("Approved") && diffrence > 0) {
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
                            Toast.makeText(getApplicationContext(), "No upcoming campaigns", Toast.LENGTH_SHORT).show();
                        }
                    });
                    campaignsHolder.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

        backImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), FacilitatorLeftNavActivity.class);
                startActivity(intent);
                finish();

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_MAP_POINT_REQUEST) {
            if (resultCode == Activity.RESULT_CANCELED) {

            }
        }
    }

    public void moveToHomeScreen(View view) {
        Intent intent = new Intent(getApplicationContext(), FacilitatorLeftNavActivity.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {

    }

}