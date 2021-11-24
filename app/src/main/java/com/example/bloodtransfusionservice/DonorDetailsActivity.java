package com.example.bloodtransfusionservice;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class DonorDetailsActivity extends AppCompatActivity {
    TextView txtName, txtMobileNo, txtDistrict, txtEmail, txtGender, txtBloodGroup, txtLastDateDonated, txtNoOfDonations, txtLocation, txtAddress, txtUserStatus, txtDiseases, txtListOfDiseases;
    Button buttonCall, buttonDeactivate, buttonActivate, buttonBack;
    ImageView backImg;
    String mobile_var;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donor_details);

        ActivityCompat.requestPermissions(DonorDetailsActivity.this, new String[]{android.Manifest.permission.SEND_SMS, Manifest.permission.READ_SMS}, PackageManager.PERMISSION_GRANTED);

        ActivityCompat.requestPermissions(DonorDetailsActivity.this, new String[]{Manifest.permission.CALL_PHONE}, PackageManager.PERMISSION_GRANTED);

        String mobile_var = getIntent().getStringExtra("mobile");
        Log.d("MOBILE: ", String.valueOf(mobile_var));

        txtName = findViewById(R.id.donor_name);
        txtMobileNo = findViewById(R.id.mobile_number);
        txtDistrict = findViewById(R.id.district);
        txtEmail = findViewById(R.id.email);
        txtGender = findViewById(R.id.gender);
        txtBloodGroup = findViewById(R.id.blood_group);
        txtLastDateDonated = findViewById(R.id.date_last_donated);
        txtNoOfDonations = findViewById(R.id.no_of_donations);
        txtLocation = findViewById(R.id.location);
        txtAddress = findViewById(R.id.address);
        txtUserStatus = findViewById(R.id.user_status);
        buttonCall = findViewById(R.id.btn_call);
        buttonDeactivate = findViewById(R.id.btn_deactivate);
        buttonActivate = findViewById(R.id.btn_activate);
        backImg = findViewById(R.id.img_back);
        buttonBack = findViewById(R.id.btn_back);
        txtDiseases = findViewById(R.id.diseases);
        txtListOfDiseases = findViewById(R.id.txt_diseases);

        showFacilitatorDetails(mobile_var);
    }

    private void showFacilitatorDetails(String mobile_) {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference("donor");
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
                    String gender_var = snapshot.child(mobile_).child("gender").getValue(String.class);
                    String bloodGroup_var = snapshot.child(mobile_).child("bloodGroup").getValue(String.class);
                    String dateLastDonated_var = snapshot.child(mobile_).child("dateLastDonated").getValue(String.class);
                    String noOfDonations_var = snapshot.child(mobile_).child("noOfDonations").getValue(String.class);
                    String location_var = snapshot.child(mobile_).child("location").getValue(String.class);
                    String address_var = snapshot.child(mobile_).child("address").getValue(String.class);
                    String userStatus_var = snapshot.child(mobile_).child("userStatus").getValue(String.class);
                    String diseases_var = snapshot.child(mobile_).child("diseases").getValue(String.class);
                    String diseases_list_var = snapshot.child(mobile_).child("diseasesList").getValue(String.class);

                    //Set values
                    txtName.setText(name_var);
                    txtMobileNo.setText(mobile_var);
                    txtEmail.setText(email_var);
                    txtDistrict.setText(district_var);
                    txtGender.setText(gender_var);
                    txtBloodGroup.setText(bloodGroup_var);
                    txtLastDateDonated.setText(dateLastDonated_var);
                    txtNoOfDonations.setText(noOfDonations_var);
                    txtLocation.setText(location_var);
                    txtAddress.setText(address_var);
                    txtUserStatus.setText(userStatus_var);
                    txtDiseases.setText(diseases_var);
                    txtListOfDiseases.setText(diseases_list_var);

                    if (userStatus_var.equals("active")) {
                        buttonDeactivate.setVisibility(View.VISIBLE);
                    } else {
                        buttonActivate.setVisibility(View.VISIBLE);
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

        buttonDeactivate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                DatabaseReference databaseReference = firebaseDatabase.getReference("donor").child(mobile_var);

                Intent intent = new Intent(getApplicationContext(), DeactivateReasonActivity.class);
                intent.putExtra("deactivateID", mobile_var);
                startActivity(intent);

                /*new AlertDialog.Builder(DonorDetailsActivity.this)
                        .setTitle("Donor Deactivation")
                        .setMessage("Want to deactivate Donor ID: +94" + mobile_var)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                String status = "inactive";
                                DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("donor").child(mobile_var);
                                Map<String, Object> updates = new HashMap<String, Object>();
                                updates.put("userStatus", status);
                                ref.updateChildren(updates);

                                //Send SMS
                                //Build message
                                String message = "Your userID: "+mobile_var+" has been deactivated.";
                                Log.d("Message", String.valueOf(message));

                                SmsManager smsManager = SmsManager.getDefault();
                                smsManager.sendTextMessage("+94" + mobile_var, null, message, null, null);

                                Intent intent = new Intent(getApplicationContext(), ViewDonorActivity.class);
                                startActivity(intent);

                            }
                        })
                        .setNegativeButton(android.R.string.no, null).show();*/
            }
        });

        buttonActivate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                DatabaseReference databaseReference = firebaseDatabase.getReference("donor").child(mobile_var);

                new AlertDialog.Builder(DonorDetailsActivity.this)
                        .setTitle("Donor activation")
                        .setMessage("Want to activate Donor ID: +94" + mobile_var)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                String status = "active";
                                DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("donor").child(mobile_var);
                                Map<String, Object> updates = new HashMap<String, Object>();
                                updates.put("userStatus", status);
                                ref.updateChildren(updates);

                                //Build message
                                String message = "Your userID: "+mobile_var+" has been activated.";
                                Log.d("Message", String.valueOf(message));

                                SmsManager smsManager = SmsManager.getDefault();
                                smsManager.sendTextMessage("+94" + mobile_var, null, message, null, null);

                                Intent intent = new Intent(getApplicationContext(), ViewDonorActivity.class);
                                startActivity(intent);

                            }
                        })
                        .setNegativeButton(android.R.string.no, null).show();
            }
        });

        backImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ViewDonorActivity.class);
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
        Intent intent = new Intent(getApplicationContext(), ViewDonorActivity.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {

    }
}