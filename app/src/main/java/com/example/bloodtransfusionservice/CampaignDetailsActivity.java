package com.example.bloodtransfusionservice;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

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

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class CampaignDetailsActivity extends AppCompatActivity {
    TextView txtCampId, txtOrganizerName, txtMobileNo, txtNic, txtLocation, txtAddress, txtStatus, txtDateCamp;
    DatePicker txtDateOfCamp;
    AutoCompleteTextView txtDistrict;
    Button buttonUpdate, buttonDelete, buttonShare, buttonBack, buttonLocation;
    ShareDialog shareDialog;
    String phone;
    static final int PICK_MAP_POINT_REQUEST = 1;
    String latitude;
    String longitude;
    String longAddress;
    String campId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_campaign_details);

        /* Get phone number from session */
        SessionManager sessionManager = new SessionManager(this, SessionManager.SESSION_USER_SESSION);
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
        buttonUpdate = findViewById(R.id.btn_update);
        buttonDelete = findViewById(R.id.btn_delete);
        buttonShare = findViewById(R.id.btn_share);
        buttonBack = findViewById(R.id.btn_back);
        buttonLocation = findViewById(R.id.btn_choose_location);

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

                    if (status_var.equals("Pending")) {
                        txtDateCamp.setVisibility(View.GONE);
                        txtDateOfCamp.setVisibility(View.VISIBLE);
                        buttonShare.setVisibility(View.GONE);
                        buttonLocation.setVisibility(View.VISIBLE);
                        buttonUpdate.setVisibility(View.VISIBLE);
                        buttonDelete.setVisibility(View.VISIBLE);
                        buttonBack.setVisibility(View.VISIBLE);

                        //Set values
                        txtCampId.setText(campId_var);
                        txtOrganizerName.setText(organizerName_var);
                        txtMobileNo.setText(phone_var);
                        txtNic.setText(nic_var);
                        txtLocation.setText(location_var);
                        txtAddress.setText(address_var);
                        txtStatus.setText(status_var);

                        //Set district
                        String[] district_options = {"Kandy", "Kalutara", "Gampaha", "Matale", "Colombo", "Nuwara Eliya", "Matara", "Galle", "Hambantota",
                                "Badulla", "Monaragala", "Kegalle", "Ratnapura", "Auradhapura", "Polonnaruwa", "Trincomalee", "Batticaloa", "Ampara", "Puttalam", "Kurunegala",
                                "Jaffna", "Kilinochchi", "Mannar", "Mulaitivu", "Vavuniya"};
                        ArrayAdapter arrayAdapter1 = new ArrayAdapter(CampaignDetailsActivity.this, R.layout.format_dropdown, district_options);
                        for (int i = 0; i < district_options.length; i++) {
                            if (arrayAdapter1.getItem(i).toString().equals(district_var)) {
                                txtDistrict.setText(arrayAdapter1.getItem(i).toString(), false);
                                txtDistrict.setAdapter(arrayAdapter1);
                                break;
                            }
                        }

                        //Set date of birth
                        String[] split = dateOfCamp_var.split("/");
                        int year = Integer.valueOf(split[0]);
                        Log.d("YEAR: ", String.valueOf(year));
                        int month = Integer.valueOf(split[1]);
                        Log.d("MONTH: ", String.valueOf(month));
                        int day = Integer.valueOf(split[2]);
                        Log.d("DAY: ", String.valueOf(day));
                        txtDateOfCamp.updateDate(year, month - 1, day);

                        //Update button click
                        buttonUpdate.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String isUpdateCampaign = "true";
                                String districts_var = txtDistrict.getText().toString();
                                String place_var = txtLocation.getText().toString();
                                String address_var = txtAddress.getText().toString();

                                int day = txtDateOfCamp.getDayOfMonth();
                                int month = txtDateOfCamp.getMonth() + 1;
                                int year = txtDateOfCamp.getYear();
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
                                            if (date_diffrence >= 30) {
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
                                                                            Toast.makeText(CampaignDetailsActivity.this, "You already have created a campaign on same date.", Toast.LENGTH_SHORT).show();
                                                                        } else {
                                                                            Intent intent = new Intent(CampaignDetailsActivity.this, VerifyOrganizeCampaignActivity.class);
                                                                            intent.putExtra("organizerName", organizerName_var);
                                                                            intent.putExtra("nic", nic_var);
                                                                            intent.putExtra("district", districts_var);
                                                                            intent.putExtra("place", place_var);
                                                                            intent.putExtra("address", address_var);
                                                                            intent.putExtra("mobile", phone);
                                                                            intent.putExtra("campaignDate", date_);
                                                                            intent.putExtra("latitude", latitude);
                                                                            intent.putExtra("longitude", longitude);
                                                                            intent.putExtra("campaignId", campId);
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
                                                                Intent intent = new Intent(CampaignDetailsActivity.this, VerifyOrganizeCampaignActivity.class);
                                                                intent.putExtra("organizerName", organizerName_var);
                                                                intent.putExtra("nic", nic_var);
                                                                intent.putExtra("district", districts_var);
                                                                intent.putExtra("place", place_var);
                                                                intent.putExtra("address", address_var);
                                                                intent.putExtra("mobile", phone);
                                                                intent.putExtra("campaignDate", date_);
                                                                intent.putExtra("latitude", latitude);
                                                                intent.putExtra("longitude", longitude);
                                                                intent.putExtra("campaignId", campId);
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
                                                Toast.makeText(CampaignDetailsActivity.this, "Request should submit 30 days prior to the campaign date.", Toast.LENGTH_SHORT).show();
                                            }
                                        } else {
                                            Toast.makeText(CampaignDetailsActivity.this, "User: +94" + phone + " is not exists.", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
                                    }
                                });

                            }
                        });

                        buttonLocation.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                pickPointOnMap();
                            }
                        });

                        buttonDelete.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                                DatabaseReference databaseReference = firebaseDatabase.getReference("campaign").child(campId);

                                new AlertDialog.Builder(CampaignDetailsActivity.this)
                                        .setTitle("Campaign Deletion")
                                        .setMessage("Want to delete campaign ID: " + campId)
                                        .setIcon(android.R.drawable.ic_dialog_alert)
                                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int whichButton) {
                                                databaseReference.removeValue();
                                                Toast.makeText(getApplicationContext(), "Campaign successfully deleted.", Toast.LENGTH_SHORT).show();
                                                Intent intent = new Intent(getApplicationContext(), CampaignHistoryActivity.class);
                                                startActivity(intent);
                                            }
                                        })
                                        .setNegativeButton(android.R.string.no, null).show();

                            }
                        });

                    } else if (status_var.equals("Approved")) {
                        //Set values
                        txtDateCamp.setVisibility(View.VISIBLE);
                        txtDateOfCamp.setVisibility(View.GONE);
                        buttonLocation.setVisibility(View.GONE);
                        buttonUpdate.setVisibility(View.GONE);
                        buttonDelete.setVisibility(View.GONE);
                        buttonBack.setVisibility(View.VISIBLE);
                        buttonShare.setVisibility(View.VISIBLE);
                        txtCampId.setText(campId_var);
                        txtOrganizerName.setText(organizerName_var);
                        txtMobileNo.setText(phone_var);
                        txtNic.setText(nic_var);
                        txtLocation.setText(location_var);
                        txtAddress.setText(address_var);
                        txtStatus.setText(status_var);
                        txtDistrict.setText(district_var);
                        txtDateCamp.setText(dateOfCamp_var);
                        shareDialog = new ShareDialog(CampaignDetailsActivity.this);

                        buttonShare.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                /*ShareLinkContent linkContent = new ShareLinkContent.Builder().setQuote("BLOOD DONATION CAMPAIGN").setContentDescription("Date of campaign: "+dateOfCamp_var).setContentUrl(Uri.parse(location_var)).build();
                                if (ShareDialog.canShow(ShareLinkContent.class)){
                                    shareDialog.show(linkContent);
                                }*/

                                final ShareLinkContent content = new ShareLinkContent.Builder()
                                        .setQuote("Date of campaign: " + dateOfCamp_var + " \nDistrict: " + district_var + "\nAddress: " + address_var + "\nOrganizer: " + organizerName_var)
                                        .setContentUrl(Uri.parse(location_var))
                                        .setShareHashtag(new ShareHashtag.Builder()
                                                .setHashtag("#BTS #DonateBlood #SaveLife")
                                                .build())
                                        .build();
                                ShareDialog shareDialog = new ShareDialog(CampaignDetailsActivity.this);
                                shareDialog.show(content);

                            }
                        });


                    } else {
                        //Set values
                        txtDateCamp.setVisibility(View.VISIBLE);
                        txtDateOfCamp.setVisibility(View.GONE);
                        buttonLocation.setVisibility(View.GONE);
                        buttonUpdate.setVisibility(View.GONE);
                        buttonDelete.setVisibility(View.VISIBLE);
                        buttonShare.setVisibility(View.GONE);
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

                        buttonDelete.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                                DatabaseReference databaseReference = firebaseDatabase.getReference("campaign").child(campId);

                                new AlertDialog.Builder(CampaignDetailsActivity.this)
                                        .setTitle("Campaign Deletion")
                                        .setMessage("Want to delete campaign ID: " + campId)
                                        .setIcon(android.R.drawable.ic_dialog_alert)
                                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int whichButton) {
                                                databaseReference.removeValue();
                                                Toast.makeText(getApplicationContext(), "Campaign successfully deleted.", Toast.LENGTH_SHORT).show();
                                                Intent intent = new Intent(getApplicationContext(), CampaignHistoryActivity.class);
                                                startActivity(intent);
                                            }
                                        })
                                        .setNegativeButton(android.R.string.no, null).show();

                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }

    private void pickPointOnMap() {
        String activity = "createCampaign";
        Intent pickPointIntent = new Intent(CampaignDetailsActivity.this, ChooseLocationActivity.class);
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
                txtLocation.setText(String.format("%s", locationAddressLong));
                txtAddress.setText(String.format("%s", longAddress));
            }

        }
    }

    public void moveToPreviousScreen(View view) {
        Intent intent = new Intent(getApplicationContext(), CampaignHistoryActivity.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {

    }
}