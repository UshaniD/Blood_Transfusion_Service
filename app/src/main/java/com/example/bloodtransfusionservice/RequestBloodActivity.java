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
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
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

public class RequestBloodActivity extends AppCompatActivity {
    AutoCompleteTextView districts, bloodGroups;
    TextInputLayout txtPatientName;
    Button buttonSubmit, buttonCancel, buttonChooseLocation;
    TextView txtPlace, txtAddress;
    ProgressBar progressBar;
    static final int PICK_MAP_POINT_REQUEST = 1;
    String longAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_blood);

        //Get phone number from session
        SessionManager sessionManager = new SessionManager(this, SessionManager.SESSION_USER_SESSION);
        HashMap<String, String> userDetails = sessionManager.getUserDetailFromSession();
        String phone = userDetails.get(SessionManager.KEY_PHONE_NO);
        Log.d("====PHONE NUMBER : ", String.valueOf(phone));

        buttonSubmit = findViewById(R.id.btn_request_submit);
        buttonCancel = findViewById(R.id.btn_cancel);
        buttonChooseLocation = findViewById(R.id.btn_choose_location);
        districts = findViewById(R.id.districts);
        bloodGroups = findViewById(R.id.blood_type);
        txtPatientName = findViewById(R.id.input_patient_name);
        txtPlace = findViewById(R.id.txt_place);
        txtAddress = findViewById(R.id.txt_address);
        progressBar = findViewById(R.id.progress);

        ActivityCompat.requestPermissions(RequestBloodActivity.this, new String[]{Manifest.permission.SEND_SMS, Manifest.permission.READ_SMS}, PackageManager.PERMISSION_GRANTED);

        String[] district_options = {"Colombo", "Kalutara", "Gampaha", "Matale", "Kandy", "Nuwara Eliya", "Matara", "Galle", "Hambantota",
                "Badulla", "Monaragala", "Kegalle", "Ratnapura", "Auradhapura", "Polonnaruwa", "Trincomalee", "Batticaloa", "Ampara", "Puttalam", "Kurunegala",
                "Jaffna", "Kilinochchi", "Mannar", "Mulaitivu", "Vavuniya"};
        ArrayAdapter arrayAdapter1 = new ArrayAdapter(this, R.layout.format_dropdown, district_options);
        districts.setText(arrayAdapter1.getItem(0).toString(), false);
        districts.setAdapter(arrayAdapter1);

        String[] blood_groups_option = {"A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-"};
        ArrayAdapter arrayAdapter2 = new ArrayAdapter(this, R.layout.format_dropdown, blood_groups_option);
        bloodGroups.setText(arrayAdapter2.getItem(0).toString(), false);
        bloodGroups.setAdapter(arrayAdapter2);

        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String patientName_var = txtPatientName.getEditText().getText().toString();
                String districts_var = districts.getText().toString();
                String bloodGroup_var = bloodGroups.getText().toString();
                String place_var = txtPlace.getText().toString();
                String address_var = txtAddress.getText().toString();
                String requestId = UUID.randomUUID().toString();
                String status = "Pending";

                FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                DatabaseReference reference = firebaseDatabase.getReference("donor");
                Query checkmobileNo = reference.orderByChild("mobile").equalTo(phone);

                checkmobileNo.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            String requesterName_var = snapshot.child(phone).child("fullname").getValue(String.class);
                            String nic_var = snapshot.child(phone).child("nic").getValue(String.class);
                            if (!patientName_var.isEmpty()) {
                                txtPatientName.setError(null);
                                txtPatientName.setErrorEnabled(false);
                                if (patientName_var.matches("^[\\p{L} .'-]+$")) {
                                    txtPatientName.setError(null);
                                    txtPatientName.setErrorEnabled(false);
                                    if (!place_var.isEmpty()) {
                                        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
                                        Date date = new Date();
                                        String currentDate = dateFormat.format(date);
                                        Log.d("Current date", String.valueOf(currentDate));

                                        new AlertDialog.Builder(RequestBloodActivity.this)
                                                .setTitle("Request Blood Confirmation")
                                                .setMessage("This will send SMS to all the available donors which match your requirement. Do you want to proceed?")
                                                .setIcon(android.R.drawable.ic_dialog_alert)
                                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int whichButton) {
                                                        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                                                        DatabaseReference reference = firebaseDatabase.getReference("request");
                                                        storeBloodRequestData storeBloodRequestData = new storeBloodRequestData(requestId, requesterName_var, patientName_var, nic_var, bloodGroup_var, districts_var, place_var, address_var, phone, status, currentDate);
                                                        reference.child(requestId).setValue(storeBloodRequestData);
                                                        Toast.makeText(getApplicationContext(), "Blood request created successfully", Toast.LENGTH_SHORT).show();

                                                        FirebaseDatabase firebaseDatabase1 = FirebaseDatabase.getInstance();
                                                        DatabaseReference reference1 = firebaseDatabase1.getReference("donor");
                                                        Query checkmobileNo = reference1.orderByChild("mobile");

                                                        checkmobileNo.addListenerForSingleValueEvent(new ValueEventListener() {
                                                            @Override
                                                            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                                                                if (snapshot.exists()) {
                                                                    String bloodTypeFromDb = "";
                                                                    String lastDataDonatedFromDB = "";
                                                                    String phoneNoFromDb = "";

                                                                    for (DataSnapshot ds : snapshot.getChildren()) {
                                                                        bloodTypeFromDb = ds.child("bloodGroup").getValue(String.class);
                                                                        lastDataDonatedFromDB = ds.child("dateLastDonated").getValue(String.class);
                                                                        phoneNoFromDb = ds.child("mobile").getValue(String.class);

                                                                        Log.d("Blood Group from DB:", String.valueOf(bloodTypeFromDb));
                                                                        Log.d("Date from DB:", String.valueOf(lastDataDonatedFromDB));
                                                                        Log.d("Mobile from DB:", String.valueOf(phoneNoFromDb));

                                                                        //Filter donors calculating last donated date
                                                                        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
                                                                        Date date = new Date();
                                                                        String currentDate = dateFormat.format(date);
                                                                        Log.d("Current date", String.valueOf(currentDate));

                                                                        Date date1 = null;
                                                                        try {
                                                                            date1 = new SimpleDateFormat("yyyy/MM/dd").parse(currentDate);
                                                                        } catch (ParseException e) {
                                                                            e.printStackTrace();
                                                                        }
                                                                        Date date2 = null;
                                                                        try {
                                                                            date2 = new SimpleDateFormat("yyyy/MM/dd").parse(lastDataDonatedFromDB);
                                                                        } catch (ParseException e) {
                                                                            e.printStackTrace();
                                                                        }
                                                                        long diff = date1.getTime() - date2.getTime();

                                                                        TimeUnit time = TimeUnit.DAYS;
                                                                        long date_diffrence = time.convert(diff, TimeUnit.MILLISECONDS);
                                                                        System.out.println("The difference in days is : " + date_diffrence);

                                                                        /* Date of last blood donate day should be at least four months prior to current date*/
                                                                        if (bloodTypeFromDb.equals(bloodGroup_var) && date_diffrence >= 120) {
                                                                            //Build message
                                                                            String message = "URGENT! " + requesterName_var + " is requesting " + bloodGroup_var + " blood. For Patient: " + patientName_var + " Location: " + place_var;
                                                                            Log.d("Message", String.valueOf(message));

                                                                            SmsManager smsManager = SmsManager.getDefault();
                                                                            smsManager.sendTextMessage("+94" + phoneNoFromDb, null, message, null, null);

                                                                        }
                                                                    }

                                                                    Intent intent = new Intent(getApplicationContext(), LeftNavActivity.class);
                                                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                                    startActivity(intent);
                                                                    finish();
                                                                } else {
                                                                    Toast.makeText(RequestBloodActivity.this, "Database error.", Toast.LENGTH_SHORT).show();
                                                                }
                                                            }

                                                            @Override
                                                            public void onCancelled(@NonNull @NotNull DatabaseError error) {

                                                            }
                                                        });
                                                    }
                                                })
                                                .setNegativeButton(android.R.string.no, null).show();
                                    } else {
                                        Toast.makeText(getApplicationContext(), "Please select a location", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    txtPatientName.setError("Invalid patient's name");
                                }
                            } else {
                                txtPatientName.setError("Enter patient's name");
                            }
                        } else {
                            Toast.makeText(RequestBloodActivity.this, "User: +94" + phone + " is not exists.", Toast.LENGTH_SHORT).show();
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
        String activity = "requestBlood";
        Intent pickPointIntent = new Intent(RequestBloodActivity.this, ChooseLocationActivity.class);
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