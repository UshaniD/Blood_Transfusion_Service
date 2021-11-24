package com.example.bloodtransfusionservice;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class UpComingCampaignDetailsActivity extends AppCompatActivity {
    TextView txtCampId, txtOrganizerName, txtMobileNo, txtNic, txtLocation, txtAddress, txtStatus, txtDateCamp;
    DatePicker txtDateOfCamp;
    AutoCompleteTextView txtDistrict;
    Button buttonBack;
    String phone;
    String campId;
    ImageView backImg;
    static final int PICK_MAP_POINT_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_up_coming_campaign_details);

        //Get phone number from session
        SessionManager sessionManager = new SessionManager(this,SessionManager.SESSION_USER_SESSION);
        HashMap<String, String> userDetails = sessionManager.getUserDetailFromSession();
        phone = userDetails.get(SessionManager.KEY_PHONE_NO);
        Log.d("PHONE: ", String.valueOf(phone));

        campId = getIntent().getStringExtra("CampId");
        Log.d("CAMP ID: ", String.valueOf(campId));
        txtCampId = findViewById(R.id.txt_campaign_id);
        txtOrganizerName = findViewById(R.id.txt_organizer_name);
        txtMobileNo = findViewById(R.id.txt_mobile_no);
        txtNic = findViewById(R.id.txt_nic);
        txtDateOfCamp = findViewById(R.id.txt_date_of_camp);
        txtDateCamp = findViewById(R.id.txt_dateOfCamp);
        txtDistrict = findViewById(R.id.districts);
        txtLocation = findViewById(R.id.txt_location);
        txtAddress = findViewById(R.id.txt_address);
        txtStatus = findViewById(R.id.txt_status);
        buttonBack = findViewById(R.id.btn_back);
        backImg = findViewById(R.id.img_back);

        showCampDetails(campId);
    }

    private void showCampDetails(String campId_var) {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference("campaign");
        Query checkCamp = databaseReference.orderByChild("campaignId").equalTo(campId_var);

        checkCamp.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    //Get all user data from donor table
                    String organizerName_var = snapshot.child(campId_var).child("organizerName").getValue(String.class);
                    String phone_var = snapshot.child(campId_var).child("phone").getValue(String.class);
                    String nic_var = snapshot.child(campId_var).child("nic").getValue(String.class);
                    String dateOfCamp_var = snapshot.child(campId_var).child("dateOfCamp").getValue(String.class);
                    String district_var = snapshot.child(campId_var).child("district").getValue(String.class);
                    String location_var = snapshot.child(campId_var).child("location").getValue(String.class);
                    String address_var = snapshot.child(campId_var).child("address").getValue(String.class);
                    String status_var = snapshot.child(campId_var).child("status").getValue(String.class);

                    //Set values
                    txtDateCamp.setVisibility(View.VISIBLE);
                    txtDateOfCamp.setVisibility(View.GONE);
                    buttonBack.setVisibility(View.VISIBLE);
                    txtCampId.setText(campId_var);
                    txtOrganizerName.setText(organizerName_var);
                    txtMobileNo.setText(phone_var);
                    txtNic.setText(nic_var);
                    txtLocation.setText(location_var);
                    txtAddress.setText(address_var);
                    txtStatus.setText(status_var);
                    txtDistrict.setText(district_var);
                    txtDateCamp.setText(dateOfCamp_var);
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
                setResult(Activity.RESULT_CANCELED, intent);
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

    public void moveToPreviousScreen(View view) {
        Intent intent = new Intent(getApplicationContext(), UpComingCampaignActivity.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {

    }
}