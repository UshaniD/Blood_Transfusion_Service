package com.example.bloodtransfusionservice;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

public class FacilitatorDetailsActivity extends AppCompatActivity {
    TextView txtName, txtMobileNo, txtDistrict, txtEmail, txtLocation, txtAddress;
    Button buttonCall, buttonEmail, buttonBack;
    ImageView backImg;
    String mobile_var;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facilitator_details);

        ActivityCompat.requestPermissions(FacilitatorDetailsActivity.this, new String[]{Manifest.permission.CALL_PHONE}, PackageManager.PERMISSION_GRANTED);

        String mobile_var = getIntent().getStringExtra("mobile");
        Log.d("MOBILE: ", String.valueOf(mobile_var));

        txtName = findViewById(R.id.facilitator_name);
        txtMobileNo = findViewById(R.id.mobile_number);
        txtDistrict = findViewById(R.id.district);
        txtEmail = findViewById(R.id.email);
        txtLocation = findViewById(R.id.location);
        txtAddress = findViewById(R.id.address);
        buttonCall = findViewById(R.id.btn_call);
        buttonEmail = findViewById(R.id.btn_email);
        buttonBack = findViewById(R.id.btn_back);
        backImg = findViewById(R.id.img_back);

        showFacilitatorDetails(mobile_var);
    }

    private void showFacilitatorDetails(String mobile_) {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference("facilitator");
        Query checkFacilitator = databaseReference.orderByChild("mobile").equalTo(mobile_);

        checkFacilitator.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    //Get all user data from donor table
                    String name_var = snapshot.child(mobile_).child("fullname").getValue(String.class);
                    mobile_var = snapshot.child(mobile_).child("mobile").getValue(String.class);
                    String email_var = snapshot.child(mobile_).child("email").getValue(String.class);
                    String district_var = snapshot.child(mobile_).child("district").getValue(String.class);
                    String location_var = snapshot.child(mobile_).child("location").getValue(String.class);
                    String address_var = snapshot.child(mobile_).child("address").getValue(String.class);

                    //Set values
                    txtName.setText(name_var);
                    txtMobileNo.setText(mobile_var);
                    txtEmail.setText(email_var);
                    txtDistrict.setText(district_var);
                    txtLocation.setText(location_var);
                    txtAddress.setText(address_var);

                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

        backImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), FacilitatorInfoActivity.class);
                setResult(Activity.RESULT_CANCELED, intent);
                finish();

            }
        });

        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), FacilitatorLeftNavActivity.class);
                setResult(Activity.RESULT_CANCELED, intent);
                finish();

            }
        });

        buttonCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:+94" + mobile_var));
                startActivity(callIntent);
            }
        });
    }

    public void moveToPreviousScreen(View view) {
        Intent intent = new Intent(getApplicationContext(), FacilitatorInfoActivity.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {

    }
}