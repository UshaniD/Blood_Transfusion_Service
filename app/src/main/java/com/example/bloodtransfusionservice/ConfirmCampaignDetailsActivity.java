package com.example.bloodtransfusionservice;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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

public class ConfirmCampaignDetailsActivity extends AppCompatActivity {
    TextView txtCampId, txtOrganizerName, txtMobileNo, txtNic, txtDateOfCamp, txtDistrict, txtLocation, txtAddress, txtStatus;
    Button buttonApprove, buttonReject;
    String phone_var;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_campaign_details);

        //Get phone number from session
        SessionManager sessionManager = new SessionManager(this, SessionManager.SESSION_USER_SESSION);
        HashMap<String, String> userDetails = sessionManager.getUserDetailFromSession();
        String phone = userDetails.get(SessionManager.KEY_PHONE_NO);
        Log.d("PHONE NUMBER : ", String.valueOf(phone));

        String campId = getIntent().getStringExtra("CampId");
        Log.d("CAMP ID: ", String.valueOf(campId));

        txtCampId = findViewById(R.id.txt_campaign_id);
        txtOrganizerName = findViewById(R.id.txt_organizer_name);
        txtMobileNo = findViewById(R.id.txt_mobile_no);
        txtNic = findViewById(R.id.txt_nic);
        txtDateOfCamp = findViewById(R.id.txt_date_of_camp);
        txtDistrict = findViewById(R.id.txt_district);
        txtLocation = findViewById(R.id.txt_location);
        txtAddress = findViewById(R.id.txt_address);
        txtStatus = findViewById(R.id.txt_status);
        buttonApprove = findViewById(R.id.btn_approve);
        buttonReject = findViewById(R.id.btn_reject);

        ActivityCompat.requestPermissions(ConfirmCampaignDetailsActivity.this, new String[]{android.Manifest.permission.SEND_SMS, Manifest.permission.READ_SMS}, PackageManager.PERMISSION_GRANTED);

        showCampDetails(campId);

        buttonApprove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("phone_var", String.valueOf(phone_var));
                new AlertDialog.Builder(ConfirmCampaignDetailsActivity.this)
                        .setTitle("Campaign Confirmation")
                        .setMessage("By approving this campaign your user ID will be recorded in the system. Want to approve this campaign?")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("campaign").child(campId);
                                Map<String, Object> updates = new HashMap<String, Object>();
                                updates.put("status", "Approved");
                                updates.put("approvedBy", phone);
                                ref.updateChildren(updates);

                                String campId_var = txtCampId.getText().toString();
                                String campDate_var = txtDateOfCamp.getText().toString();
                                String location_var = txtLocation.getText().toString();
                                String address_var = txtAddress.getText().toString();
                                String mobileNo_var = txtMobileNo.getText().toString();
                                Toast.makeText(getApplicationContext(), "Campaign Approved!", Toast.LENGTH_SHORT).show();
                                Log.d("phone_var", String.valueOf(phone_var));

                                //Build message
                                String message = "Your campaign request has been approved. Camp Id: " + campId_var + " Camp date: " + campDate_var;
                                Log.d("Message", String.valueOf(message));

                                SmsManager smsManager = SmsManager.getDefault();
                                smsManager.sendTextMessage("+94" + phone_var, null, message, null, null);

                                Intent intent = new Intent(getApplicationContext(), ConfirmCampaignActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                finish();
                            }
                        })
                        .setNegativeButton(android.R.string.no, null).show();
            }
        });

        buttonReject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("phone_var", String.valueOf(phone_var));
                new AlertDialog.Builder(ConfirmCampaignDetailsActivity.this)
                        .setTitle("Confirm Rejection Campaign")
                        .setMessage("By rejecting this campaign your user ID will be recorded in the system. Want to reject this campaign?")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("campaign").child(campId);
                                Map<String, Object> updates = new HashMap<String, Object>();
                                updates.put("status", "Rejected");
                                updates.put("approvedBy", phone);
                                ref.updateChildren(updates);

                                String campId_var = txtCampId.getText().toString();
                                String campDate_var = txtDateOfCamp.getText().toString();
                                String location_var = txtLocation.getText().toString();
                                String address_var = txtAddress.getText().toString();
                                String mobileNo_var = txtMobileNo.getText().toString();
                                Toast.makeText(getApplicationContext(), "Campaign Rejected!", Toast.LENGTH_SHORT).show();
                                Log.d("phone_var", String.valueOf(phone_var));

                                //Build message
                                String message = "Your campaign request has been rejected. Camp Id: " + campId_var + " Camp date: " + campDate_var;
                                Log.d("Message", String.valueOf(message));

                                SmsManager smsManager = SmsManager.getDefault();
                                smsManager.sendTextMessage("+94" + phone_var, null, message, null, null);

                                Intent intent = new Intent(getApplicationContext(), ConfirmCampaignActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                finish();
                            }
                        })
                        .setNegativeButton(android.R.string.no, null).show();
            }
        });

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
                    phone_var = snapshot.child(campId_var).child("phone").getValue(String.class);
                    Log.d("phone_var", String.valueOf(phone_var));
                    String nic_var = snapshot.child(campId_var).child("nic").getValue(String.class);
                    String dateOfCamp_var = snapshot.child(campId_var).child("dateOfCamp").getValue(String.class);
                    String district_var = snapshot.child(campId_var).child("district").getValue(String.class);
                    String location_var = snapshot.child(campId_var).child("location").getValue(String.class);
                    String address_var = snapshot.child(campId_var).child("address").getValue(String.class);
                    String status_var = snapshot.child(campId_var).child("status").getValue(String.class);

                    //Set values
                    txtCampId.setText(campId_var);
                    txtOrganizerName.setText(organizerName_var);
                    txtMobileNo.setText(phone_var);
                    txtNic.setText(nic_var);
                    txtDateOfCamp.setText(dateOfCamp_var);
                    txtDistrict.setText(district_var);
                    txtLocation.setText(location_var);
                    txtAddress.setText(address_var);
                    txtStatus.setText(status_var);
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }

    public void moveToPreviousScreen(View view) {
        Intent intent = new Intent(getApplicationContext(), ConfirmCampaignActivity.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {

    }
}