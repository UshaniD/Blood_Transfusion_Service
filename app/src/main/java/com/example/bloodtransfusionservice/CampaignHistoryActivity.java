package com.example.bloodtransfusionservice;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
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

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class CampaignHistoryActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    CampaignsHolder campaignsHolder;
    ArrayList<storeCampaignData> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_campaign_history);

        /* Get phone number from session */
        SessionManager sessionManager = new SessionManager(this, SessionManager.SESSION_USER_SESSION);
        HashMap<String, String> userDetails = sessionManager.getUserDetailFromSession();
        String phone = userDetails.get(SessionManager.KEY_PHONE_NO);
        Log.d("PHONE NUMBER : ", String.valueOf(phone));

        recyclerView = findViewById(R.id.campaign_history_recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        list = new ArrayList<>();
        campaignsHolder = new CampaignsHolder(this, list);
        recyclerView.setAdapter(campaignsHolder);

        campaignsHolder.OnRecyclerViewClickListner(new CampaignsHolder.OnRecyclerViewClickListner() {
            @Override
            public void onViewMoreClick(int position, String id) {
                Toast.makeText(getApplicationContext(), "POSITION: " + position + " ID: " + id, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), CampaignDetailsActivity.class);
                intent.putExtra("CampId", id);
                startActivity(intent);
            }
        });

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference("campaign");
        Query checkmobile = databaseReference.orderByChild("phone").equalTo(phone);

        checkmobile.addListenerForSingleValueEvent(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        storeCampaignData data = dataSnapshot.getValue(storeCampaignData.class);
                        list.add(data);
                        Collections.sort(list, new Comparator<storeCampaignData>() {
                            @Override
                            public int compare(storeCampaignData o1, storeCampaignData o2) {
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd", Locale.ENGLISH);
                                Date d1 = null;
                                Date d2 = null;
                                try {
                                    d1 = sdf.parse(String.valueOf(o2.getDateOfCamp()));
                                    d2 = sdf.parse(String.valueOf(o1.getDateOfCamp()));
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }

                                if (d1 != null && d1.after(d2)) {
                                    return -1;
                                } else {
                                    return 1;
                                }
                            }

                        });
                    }
                    campaignsHolder.notifyDataSetChanged();
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