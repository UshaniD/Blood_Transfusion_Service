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
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

public class ViewDonorActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    DonorInforHolder donorInforHolder;
    ArrayList<storeData> list;
    AutoCompleteTextView districts;
    TextInputLayout txtName, txtMobile;
    Button buttonSearch;
    public static String district_var;
    public static String name_var;
    public static String mobile_var;
    static String releaseFlag = "";
    ImageView backImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_donor);

        //Get phone number from session
        SessionManager sessionManager = new SessionManager(this,SessionManager.SESSION_USER_SESSION);
        HashMap<String, String> userDetails = sessionManager.getUserDetailFromSession();
        String phone = userDetails.get(SessionManager.KEY_PHONE_NO);
        Log.d("PHONE NUMBER : ", String.valueOf(phone));

        recyclerView = findViewById(R.id.view_donor);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        list = new ArrayList<>();
        donorInforHolder = new DonorInforHolder(this, list);
        recyclerView.setAdapter(donorInforHolder);
        districts = findViewById(R.id.districts);
        txtName = findViewById(R.id.input_name);
        txtMobile = findViewById(R.id.input_mobile);
        buttonSearch = findViewById(R.id.btn_search);
        backImg = findViewById(R.id.img_back);

        String[] district_options = {"", "Colombo", "Kalutara", "Gampaha", "Matale", "Kandy", "Nuwara Eliya", "Matara", "Galle", "Hambantota",
                "Badulla", "Monaragala", "Kegalle", "Ratnapura", "Auradhapura", "Polonnaruwa", "Trincomalee", "Batticaloa", "Ampara", "Puttalam", "Kurunegala",
                "Jaffna", "Kilinochchi", "Mannar", "Mulaitivu", "Vavuniya"};
        ArrayAdapter arrayAdapter1 = new ArrayAdapter(this, R.layout.format_dropdown, district_options);
        districts.setText(arrayAdapter1.getItem(0).toString(), false);
        districts.setAdapter(arrayAdapter1);

        buttonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                releaseFlag = "";
                recyclerView.removeAllViewsInLayout();
                recyclerView.setVisibility(View.INVISIBLE);
                list.clear();
                district_var = districts.getText().toString();
                name_var = txtName.getEditText().getText().toString();
                mobile_var = txtMobile.getEditText().getText().toString();

                Log.d("district_var : ", String.valueOf(district_var));
                Log.d("name_var : ", String.valueOf(name_var));
                Log.d("mobile_var : ", String.valueOf(mobile_var));

                FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                DatabaseReference databaseReference = firebaseDatabase.getReference("donor");
                databaseReference.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                        storeData data = snapshot.getValue(storeData.class);
                        donorInforHolder.notifyDataSetChanged();

                        Log.d("Name : ", String.valueOf(data.getFullname()));
                        Log.d("District : ", String.valueOf(data.getDistrict()));
                        Log.d("Mobile : ", String.valueOf(data.getMobile()));
                        recyclerView.setVisibility(View.VISIBLE);

                        if (name_var.isEmpty() && mobile_var.isEmpty() && district_var.isEmpty()) {
                            Toast.makeText(getApplicationContext(), "Please add at least one criteria!", Toast.LENGTH_SHORT).show();
                        } else if (name_var.isEmpty() && mobile_var.isEmpty()) {
                            if (data.getDistrict().toLowerCase(Locale.ROOT).contains(district_var.toLowerCase(Locale.ROOT))) {
                                list.add(data);
                            } else {
                               // Toast.makeText(getApplicationContext(), "No data available for selected criteria!", Toast.LENGTH_SHORT).show();
                            }
                        } else if (name_var.isEmpty()) {
                            if (data.getDistrict().toLowerCase(Locale.ROOT).contains(district_var.toLowerCase(Locale.ROOT)) && data.getMobile().toLowerCase(Locale.ROOT).contains(mobile_var.toLowerCase(Locale.ROOT))) {
                                list.add(data);
                            } else {
                               // Toast.makeText(getApplicationContext(), "No data available for selected criteria!", Toast.LENGTH_SHORT).show();
                            }
                        } else if (mobile_var.isEmpty() && district_var.isEmpty()) {
                            if (data.getFullname().toLowerCase(Locale.ROOT).contains(name_var.toLowerCase(Locale.ROOT))) {
                                list.add(data);
                            } else {
                              //  Toast.makeText(getApplicationContext(), "No data available for selected criteria!", Toast.LENGTH_SHORT).show();
                            }
                        } else if (mobile_var.isEmpty()) {
                            if (data.getFullname().toLowerCase(Locale.ROOT).contains(name_var.toLowerCase(Locale.ROOT)) && data.getDistrict().toLowerCase(Locale.ROOT).contains(district_var.toLowerCase(Locale.ROOT))) {
                                list.add(data);
                            } else {
                               // Toast.makeText(getApplicationContext(), "No data available for selected criteria!", Toast.LENGTH_SHORT).show();
                            }
                        } else if (name_var.isEmpty() && district_var.isEmpty()) {
                            if (data.getMobile().toLowerCase(Locale.ROOT).contains(mobile_var.toLowerCase(Locale.ROOT))) {
                                list.add(data);
                            } else {
                               // Toast.makeText(getApplicationContext(), "No data available for selected criteria!", Toast.LENGTH_SHORT).show();
                            }
                        } else if (district_var.isEmpty()) {
                            if (data.getFullname().toLowerCase(Locale.ROOT).contains(name_var.toLowerCase(Locale.ROOT)) && data.getMobile().toLowerCase(Locale.ROOT).contains(mobile_var.toLowerCase(Locale.ROOT))) {
                                list.add(data);
                            } else {
                              //  Toast.makeText(getApplicationContext(), "No data available for selected criteria!", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            if (data.getFullname().toLowerCase(Locale.ROOT).contains(name_var.toLowerCase(Locale.ROOT)) && data.getMobile().toLowerCase(Locale.ROOT).contains(mobile_var.toLowerCase(Locale.ROOT)) && data.getDistrict().toLowerCase(Locale.ROOT).contains(district_var.toLowerCase(Locale.ROOT))) {
                                list.add(data);
                            } else {
                               // Toast.makeText(getApplicationContext(), "No data available for selected criteria!", Toast.LENGTH_SHORT).show();
                            }
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
                donorInforHolder.notifyDataSetChanged();


            }
        });

        donorInforHolder.OnRecyclerViewClickListner(new DonorInforHolder.OnRecyclerViewClickListner() {
            @Override
            public void onViewMoreClick(int position, String id) {
                Toast.makeText(getApplicationContext(), "POSITION: " + position + " Mobile: " + id, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), DonorDetailsActivity.class);
                intent.putExtra("mobile", id);
                startActivity(intent);
            }
        });

        backImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), FacilitatorLeftNavActivity.class);
                setResult(Activity.RESULT_CANCELED, intent);
                finish();

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