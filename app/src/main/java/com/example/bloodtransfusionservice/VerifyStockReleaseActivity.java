package com.example.bloodtransfusionservice;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

public class VerifyStockReleaseActivity extends AppCompatActivity {
    Button buttonUpdate, buttonCancel;
    TextView txtDistrict, txtTypes, txtUnit;
    TextInputLayout txtBloodStockUnits, txtComment;
    ImageView backImg;
    AutoCompleteTextView reason;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_stock_release);

        //Entered donor mobile number
        String bloodType_var = getIntent().getStringExtra("bloodType");
        String bloodUnit_var = getIntent().getStringExtra("bloodUnit");
        String district_var = getIntent().getStringExtra("district");
        backImg = findViewById(R.id.img_back);

        //Get phone number from session
        SessionManager sessionManager = new SessionManager(this, SessionManager.SESSION_USER_SESSION);
        HashMap<String, String> userDetails = sessionManager.getUserDetailFromSession();
        String phone = userDetails.get(SessionManager.KEY_PHONE_NO);
        Log.d("PHONE NUMBER : ", String.valueOf(phone));

        txtDistrict = findViewById(R.id.txt_district);
        txtTypes = findViewById(R.id.txt_blood_type);
        txtUnit = findViewById(R.id.txt_blood_unit);
        txtBloodStockUnits = findViewById(R.id.input_count);
        reason = findViewById(R.id.reason);
        txtComment = findViewById(R.id.input_comment);
        buttonUpdate = findViewById(R.id.btn_update);
        buttonCancel = findViewById(R.id.btn_cancel);

        String[] reason_options = {"Blood Issue", "Blood Reduce"};
        ArrayAdapter arrayAdapter1 = new ArrayAdapter(this, R.layout.format_dropdown, reason_options);
        reason.setText(arrayAdapter1.getItem(0).toString(), false);
        reason.setAdapter(arrayAdapter1);

        updateStockData(district_var, bloodType_var, bloodUnit_var);

        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String bloodUnits = txtBloodStockUnits.getEditText().getText().toString();
                if (!bloodUnits.isEmpty()) {
                    txtBloodStockUnits.setError(null);
                    txtBloodStockUnits.setErrorEnabled(false);
                    Double unit_var = Double.parseDouble(bloodUnits);
                    Log.d("UNIT_VAR", String.valueOf(unit_var));
                    Double ext_unit_var = Double.parseDouble(bloodUnit_var);
                    String reason_var = reason.getText().toString();
                    if (reason_var.equals("Blood Reduce")) {
                        txtComment.setVisibility(View.VISIBLE);
                        String comment_var = txtComment.getEditText().getText().toString();
                        if (!comment_var.isEmpty()) {
                            txtComment.setError(null);
                            txtComment.setErrorEnabled(false);
                            if (ext_unit_var >= unit_var) {
                                Double finalUnit = ext_unit_var - unit_var;
                                String finalUnitString = String.valueOf(finalUnit);
                                new AlertDialog.Builder(VerifyStockReleaseActivity.this)
                                        .setTitle("Verify Blood Issue")
                                        .setMessage("New blood stock is: " + finalUnitString + " Want to issue blood?")
                                        .setIcon(android.R.drawable.ic_dialog_alert)
                                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int whichButton) {
                                                FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                                                DatabaseReference databaseReference = firebaseDatabase.getReference("stock");
                                                storeStockData storeStockData = new storeStockData(bloodType_var, district_var, finalUnitString);
                                                databaseReference.child(district_var).child(bloodType_var).setValue(storeStockData);

                                                String finalUnitVar = String.valueOf(unit_var);
                                                DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
                                                Date date = new Date();
                                                String currentDate = dateFormat.format(date);
                                                Log.d("Current date", String.valueOf(currentDate));
                                                String issueId = UUID.randomUUID().toString();

                                                DatabaseReference databaseReference1 = firebaseDatabase.getReference("bloodIssuedRegistry");
                                                storeBloodIssueData storeBloodIssueData = new storeBloodIssueData(issueId, phone, finalUnitVar, bloodType_var, reason_var, comment_var, currentDate, district_var);
                                                databaseReference1.child(issueId).setValue(storeBloodIssueData);
                                                Toast.makeText(getApplicationContext(), "Updated new blood stock record successfully!", Toast.LENGTH_SHORT).show();
                                                Intent intent = new Intent(getApplicationContext(), FacilitatorLeftNavActivity.class);
                                                startActivity(intent);
                                            }
                                        })
                                        .setNegativeButton(android.R.string.no, null).show();
                            } else {
                                Toast.makeText(getApplicationContext(), "Existing blood stock less than requirement.", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            txtComment.setError("Please enter a comment");
                        }
                    } else {
                        txtComment.setVisibility(View.VISIBLE);
                        String comment_var = txtComment.getEditText().getText().toString();
                        if (ext_unit_var >= unit_var) {
                            Double finalUnit = ext_unit_var - unit_var;
                            String finalUnitString = String.valueOf(finalUnit);
                            new AlertDialog.Builder(VerifyStockReleaseActivity.this)
                                    .setTitle("Verify Blood Issue")
                                    .setMessage("New blood stock is: " + finalUnitString + " Want to issue blood?")
                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int whichButton) {
                                            FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                                            DatabaseReference databaseReference = firebaseDatabase.getReference("stock");
                                            storeStockData storeStockData = new storeStockData(bloodType_var, district_var, finalUnitString);
                                            databaseReference.child(district_var).child(bloodType_var).setValue(storeStockData);

                                            String finalUnitVar = String.valueOf(unit_var);
                                            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
                                            Date date = new Date();
                                            String currentDate = dateFormat.format(date);
                                            Log.d("Current date", String.valueOf(currentDate));
                                            String issueId = UUID.randomUUID().toString();

                                            DatabaseReference databaseReference1 = firebaseDatabase.getReference("bloodIssuedRegistry");
                                            storeBloodIssueData storeBloodIssueData = new storeBloodIssueData(issueId, phone, finalUnitVar, bloodType_var, reason_var, comment_var, currentDate, district_var);
                                            databaseReference1.child(issueId).setValue(storeBloodIssueData);
                                            Toast.makeText(getApplicationContext(), "Updated new blood stock record successfully!", Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(getApplicationContext(), FacilitatorLeftNavActivity.class);
                                            startActivity(intent);
                                        }
                                    })
                                    .setNegativeButton(android.R.string.no, null).show();
                        } else {
                            Toast.makeText(getApplicationContext(), "Existing blood stock less than requirement.", Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    txtBloodStockUnits.setError("Please enter blood units to issue.");
                }
            }
        });

        backImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ViewBloodStockActivity.class);
                setResult(Activity.RESULT_CANCELED, intent);
                finish();

            }
        });
    }

    private void updateStockData(String district, String type, String unit) {
        txtDistrict.setText(district);
        txtTypes.setText(type);
        txtUnit.setText(unit);
    }

    public void moveToHomeScreen(View view) {
        Intent intent = new Intent(getApplicationContext(), FacilitatorLeftNavActivity.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {

    }
}