package com.example.bloodtransfusionservice;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class VerifyOrganizeCampaignActivity extends AppCompatActivity {
    TextView txtCampId, txtOrganizerName, txtMobileNo, txtNic, txtLocation, txtAddress, txtStatus, txtDateCamp;
    DatePicker txtDateOfCamp;
    AutoCompleteTextView txtDistrict;
    Button buttonSubmit, buttonBack;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference reference;
    static final int PICK_MAP_POINT_REQUEST = 1;
    String isUpdateCampaign;
    ImageView backImg;
    String campaignId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_organize_campaign);

        String organizerName = getIntent().getStringExtra("organizerName");
        String nic = getIntent().getStringExtra("nic");
        String district = getIntent().getStringExtra("district");
        String place = getIntent().getStringExtra("place");
        String address = getIntent().getStringExtra("address");
        String mobile = getIntent().getStringExtra("mobile");
        String campaignDate = getIntent().getStringExtra("campaignDate");
        String latitude = getIntent().getStringExtra("latitude");
        String longitude = getIntent().getStringExtra("longitude");
        campaignId = getIntent().getStringExtra("campaignId");
        String status = getIntent().getStringExtra("status");
        isUpdateCampaign = getIntent().getStringExtra("isUpdateCampaign");
        backImg = findViewById(R.id.img_back);

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
        buttonSubmit = findViewById(R.id.btn_submit);
        buttonBack = findViewById(R.id.btn_back);
        final ProgressBar progressBar = findViewById(R.id.progress);

        //Set values
        txtCampId.setText(campaignId);
        txtOrganizerName.setText(organizerName);
        txtMobileNo.setText(mobile);
        txtNic.setText(nic);
        txtLocation.setText(place);
        txtAddress.setText(address);
        txtStatus.setText(status);
        txtDistrict.setText(district);
        txtDateCamp.setText(campaignDate);

        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isUpdateCampaign.equals("false")){
                    progressBar.setVisibility(View.VISIBLE);
                    buttonSubmit.setVisibility(View.INVISIBLE);

                    firebaseDatabase = FirebaseDatabase.getInstance();
                    reference = firebaseDatabase.getReference("campaign");
                    storeCampaignData storeCampaignData = new storeCampaignData(campaignId, organizerName, nic, campaignDate, mobile, district, place, address, status, latitude, longitude);
                    reference.child(campaignId).setValue(storeCampaignData);

                    Toast.makeText(getApplicationContext(), "Campaign successfully submitted!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), LeftNavActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivityForResult(intent, PICK_MAP_POINT_REQUEST);
                    finish();
                }else {
                    progressBar.setVisibility(View.VISIBLE);
                    buttonSubmit.setVisibility(View.INVISIBLE);

                    firebaseDatabase = FirebaseDatabase.getInstance();
                    reference = firebaseDatabase.getReference("campaign");
                    storeCampaignData storeCampaignData = new storeCampaignData(campaignId, organizerName, nic, campaignDate, mobile, district, place, address, status, latitude, longitude);
                    reference.child(campaignId).setValue(storeCampaignData);

                    Toast.makeText(getApplicationContext(), "Campaign updated submitted!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), LeftNavActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivityForResult(intent, PICK_MAP_POINT_REQUEST);
                    finish();
                }

            }
        });

        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isUpdateCampaign.equals("false")){
                    Intent intent = new Intent(getApplicationContext(), OrganizeCampaignActivity.class);
                    setResult(Activity.RESULT_CANCELED, intent);
                    //startActivity(intent);
                    finish();
                }else {
                    Intent intent = new Intent(getApplicationContext(), CampaignHistoryActivity.class);
                    setResult(Activity.RESULT_CANCELED, intent);
                    //startActivity(intent);
                    finish();
                }
            }
        });

        backImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isUpdateCampaign.equals("false")){
                    Intent intent = new Intent(getApplicationContext(), OrganizeCampaignActivity.class);
                    setResult(Activity.RESULT_CANCELED, intent);
                    finish();
                }else {
                    Intent intent = new Intent(getApplicationContext(), CampaignDetailsActivity.class);
                    setResult(Activity.RESULT_CANCELED, intent);
                    finish();
                }
            }
        });
    }

    public void moveToPreviousScreen(View view) {
        if (isUpdateCampaign.equals("false")){
            Intent intent = new Intent(getApplicationContext(), OrganizeCampaignActivity.class);
            setResult(Activity.RESULT_CANCELED, intent);
            intent.putExtra("campaignId", campaignId);
            startActivityForResult(intent, PICK_MAP_POINT_REQUEST);
            finish();
        }else {
            Intent intent = new Intent(getApplicationContext(), CampaignDetailsActivity.class);
            setResult(Activity.RESULT_CANCELED, intent);
            intent.putExtra("campaignId", campaignId);
            startActivityForResult(intent, PICK_MAP_POINT_REQUEST);
            finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_MAP_POINT_REQUEST) {
            if (resultCode == Activity.RESULT_CANCELED) {

            }

        }
    }

    @Override
    public void onBackPressed() {

    }
}