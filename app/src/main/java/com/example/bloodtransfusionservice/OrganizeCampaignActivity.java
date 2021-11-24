package com.example.bloodtransfusionservice;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class OrganizeCampaignActivity extends AppCompatActivity {
    AutoCompleteTextView districts;
    Button buttonSubmit, buttonCancel;
    Button buttonChooseLocation;
    TextView txtPlace, txtAddress;
    DatePicker datePicker_dateOfCampaign;
    static final int PICK_MAP_POINT_REQUEST = 1;
    String latitude;
    String longitude;
    String longAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_organize_campaign);

        //Get phone number from session
        SessionManager sessionManager = new SessionManager(this,SessionManager.SESSION_USER_SESSION);
        HashMap<String, String> userDetails = sessionManager.getUserDetailFromSession();
        String phone = userDetails.get(SessionManager.KEY_PHONE_NO);
        Log.d("====PHONE NUMBER : ", String.valueOf(phone));

        buttonSubmit = findViewById(R.id.btn_campaign_submit);
        buttonCancel = findViewById(R.id.btn_cancel);
        buttonChooseLocation = findViewById(R.id.btn_choose_location);
        districts = findViewById(R.id.districts);
        txtPlace = findViewById(R.id.txt_place);
        txtAddress = findViewById(R.id.txt_address);
        datePicker_dateOfCampaign = findViewById(R.id.input_date_of_campaign);

        String[] district_options = {"Colombo", "Kalutara", "Gampaha", "Matale", "Kandy", "Nuwara Eliya", "Matara", "Galle", "Hambantota",
                "Badulla", "Monaragala", "Kegalle", "Ratnapura", "Auradhapura", "Polonnaruwa", "Trincomalee", "Batticaloa", "Ampara", "Puttalam", "Kurunegala",
                "Jaffna", "Kilinochchi", "Mannar", "Mulaitivu", "Vavuniya"};

        ArrayAdapter arrayAdapter1 = new ArrayAdapter(this, R.layout.format_dropdown, district_options);
        districts.setText(arrayAdapter1.getItem(0).toString(), false);
        districts.setAdapter(arrayAdapter1);

        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String districts_var = districts.getText().toString();
                String place_var = txtPlace.getText().toString();
                String address_var = txtAddress.getText().toString();
                String isUpdateCampaign = "false";

                int day = datePicker_dateOfCampaign.getDayOfMonth();
                int month = datePicker_dateOfCampaign.getMonth() + 1;
                int year = datePicker_dateOfCampaign.getYear();
                String date_ = "" + year + "/" + month + "/" + day;
                Log.d("============Date:", String.valueOf(date_));

                DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
                Date date = new Date();
                String currentDate = dateFormat.format(date);
                Log.d("Current date", String.valueOf(currentDate));

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
                long date_diffrence = time.convert(diff, TimeUnit.MILLISECONDS);
                System.out.println("The difference in days is : " + date_diffrence);


                FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                DatabaseReference reference = firebaseDatabase.getReference("donor");
                Query checkmobileNo = reference.orderByChild("mobile").equalTo(phone);

                checkmobileNo.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            String organizerName_var = snapshot.child(phone).child("fullname").getValue(String.class);
                            String nic_var = snapshot.child(phone).child("nic").getValue(String.class);
                            if (date_diffrence >= 90) {
                                if (!place_var.isEmpty()) {
                                    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                                    DatabaseReference reference = firebaseDatabase.getReference("campaign");
                                    Query checkCampaign = reference.orderByChild("phone").equalTo(phone);

                                    checkCampaign.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                                            if (snapshot.exists()) {
                                                Query checkDate = reference.orderByChild("dateOfCamp").equalTo(date_);
                                                checkDate.addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                                                        if (snapshot.exists()) {
                                                            Toast.makeText(OrganizeCampaignActivity.this, "You already have created a campaign on same date.", Toast.LENGTH_SHORT).show();
                                                        } else {
                                                            Intent intent = new Intent(OrganizeCampaignActivity.this, VerifyOrganizeCampaignActivity.class);
                                                            intent.putExtra("organizerName", organizerName_var);
                                                            intent.putExtra("nic", nic_var);
                                                            intent.putExtra("district", districts_var);
                                                            intent.putExtra("place", place_var);
                                                            intent.putExtra("address", address_var);
                                                            intent.putExtra("mobile", phone);
                                                            intent.putExtra("campaignDate", date_);
                                                            intent.putExtra("latitude", latitude);
                                                            intent.putExtra("longitude", longitude);
                                                            String campaignId = UUID.randomUUID().toString();
                                                            intent.putExtra("campaignId", campaignId);
                                                            intent.putExtra("isUpdateCampaign", isUpdateCampaign);
                                                            String status = "Pending";
                                                            intent.putExtra("status", status);

                                                            startActivityForResult(intent, PICK_MAP_POINT_REQUEST);
                                                        }
                                                    }

                                                    @Override
                                                    public void onCancelled(@NonNull @NotNull DatabaseError error) {

                                                    }
                                                });

                                            } else {
                                                Intent intent = new Intent(OrganizeCampaignActivity.this, VerifyOrganizeCampaignActivity.class);
                                                intent.putExtra("organizerName", organizerName_var);
                                                intent.putExtra("nic", nic_var);
                                                intent.putExtra("district", districts_var);
                                                intent.putExtra("place", place_var);
                                                intent.putExtra("address", address_var);
                                                intent.putExtra("mobile", phone);
                                                intent.putExtra("campaignDate", date_);
                                                intent.putExtra("latitude", latitude);
                                                intent.putExtra("longitude", longitude);
                                                String campaignId = UUID.randomUUID().toString();
                                                intent.putExtra("campaignId", campaignId);
                                                intent.putExtra("isUpdateCampaign", isUpdateCampaign);
                                                String status = "Pending";
                                                intent.putExtra("status", status);
                                                startActivityForResult(intent, PICK_MAP_POINT_REQUEST);
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull @NotNull DatabaseError error) {

                                        }
                                    });
                                } else {
                                    Toast.makeText(getApplicationContext(), "Please select a location", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(OrganizeCampaignActivity.this, "Request should submit 90 days prior to the campaign date.", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(OrganizeCampaignActivity.this, "User: +94" + phone + " is not exists.", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });

            }
        });

        buttonChooseLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickPointOnMap();
            }
        });

    }

    private void pickPointOnMap() {
        String activity = "createCampaign";
        Intent pickPointIntent = new Intent(OrganizeCampaignActivity.this, ChooseLocationActivity.class);
        pickPointIntent.putExtra("locationActivity", activity);
        startActivityForResult(pickPointIntent, PICK_MAP_POINT_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_MAP_POINT_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {
                String locationAddressLong = data.getStringExtra("myLocation");
                longAddress = data.getStringExtra("longAddress");
                latitude = data.getStringExtra("latitude");
                longitude = data.getStringExtra("longitude");
                txtPlace.setText(String.format("%s", locationAddressLong));
                txtAddress.setText(String.format("%s", longAddress));
            }

        }
    }

    public void moveToHomeScreen(View view) {
        Intent intent = new Intent(getApplicationContext(), LeftNavActivity.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {

    }
}