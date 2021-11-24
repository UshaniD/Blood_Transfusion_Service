package com.example.bloodtransfusionservice;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;

import com.facebook.share.model.ShareHashtag;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

public class RequestBloodDetailsActivity extends AppCompatActivity {
    TextView txtRequestId, txtPatientName, txtBloodType, txtNic, txtLocation, txtAddress, txtStatus, txtDate;
    AutoCompleteTextView txtDistrict;
    Button buttonShare, buttonBack;
    ShareDialog shareDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_blood_details);

        String RequestId = getIntent().getStringExtra("RequestId");
        Log.d("REQUEST ID: ", String.valueOf(RequestId));

        txtRequestId = findViewById(R.id.txt_request_id);
        txtPatientName = findViewById(R.id.txt_patient_name);
        txtBloodType = findViewById(R.id.txt_blood_type);
        txtNic = findViewById(R.id.txt_nic);
        txtDate = findViewById(R.id.txt_request_date);
        txtDistrict = findViewById(R.id.districts);
        txtLocation = findViewById(R.id.txt_location);
        txtAddress = findViewById(R.id.txt_address);
        buttonShare = findViewById(R.id.btn_share);
        buttonBack = findViewById(R.id.btn_back);

        showRequest(RequestId);
    }

    private void showRequest(String requestId_var) {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference("request");
        Query checkCamp = databaseReference.orderByChild("requestId").equalTo(requestId_var);

        checkCamp.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    //Get all user data from donor table
                    String request_var = snapshot.child(requestId_var).child("requestId").getValue(String.class);
                    String patientName_var = snapshot.child(requestId_var).child("patientName").getValue(String.class);
                    String bloodType_var = snapshot.child(requestId_var).child("bloodType").getValue(String.class);
                    String nic_var = snapshot.child(requestId_var).child("nic").getValue(String.class);
                    String date_var = snapshot.child(requestId_var).child("date").getValue(String.class);
                    String location_var = snapshot.child(requestId_var).child("location").getValue(String.class);
                    String address_var = snapshot.child(requestId_var).child("address").getValue(String.class);
                    String district_var = snapshot.child(requestId_var).child("district").getValue(String.class);

                    //Set values
                    txtRequestId.setText(request_var);
                    txtPatientName.setText(patientName_var);
                    txtBloodType.setText(bloodType_var);
                    txtNic.setText(nic_var);
                    txtLocation.setText(location_var);
                    txtAddress.setText(address_var);
                    txtDate.setText(date_var);
                    txtDistrict.setText(district_var);

                    shareDialog = new ShareDialog(RequestBloodDetailsActivity.this);
                    buttonShare.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            final ShareLinkContent content = new ShareLinkContent.Builder()
                                    .setQuote("Blood Type: "+bloodType_var+" \nPatient's Name: "+patientName_var+"\nAddress: "+address_var+"\nDistrict: "+district_var+"\nRequested Date: "+date_var)
                                    .setContentUrl(Uri.parse(location_var))
                                    .setShareHashtag(new ShareHashtag.Builder()
                                            .setHashtag("#BTS #DonateBlood #SaveLife")
                                            .build())
                                    .build();
                            ShareDialog shareDialog = new ShareDialog(RequestBloodDetailsActivity.this);
                            shareDialog.show(content);

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }

    public void moveToPreviousScreen(View view) {
        Intent intent = new Intent(getApplicationContext(), RequestBloodHistoryActivity.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {

    }
}