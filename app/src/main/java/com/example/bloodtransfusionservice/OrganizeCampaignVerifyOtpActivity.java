package com.example.bloodtransfusionservice;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class OrganizeCampaignVerifyOtpActivity extends AppCompatActivity {
    EditText no1, no2, no3, no4, no5, no6;
    TextView txtMobileNumber;
    String txtOtp;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_organize_campaign_verify_otp);

        final Button buttonSubmit = findViewById(R.id.btn_submit);

        no1 = findViewById(R.id.input_no1);
        no2 = findViewById(R.id.input_no2);
        no3 = findViewById(R.id.input_no3);
        no4 = findViewById(R.id.input_no4);
        no5 = findViewById(R.id.input_no5);
        no6 = findViewById(R.id.input_no6);

        txtMobileNumber = findViewById(R.id.input_number_entered);
        txtMobileNumber.setText(String.format("+94%s", getIntent().getStringExtra("mobile")));
        txtOtp = getIntent().getStringExtra("backendOtp");

        final ProgressBar progressBar = findViewById(R.id.progress_verifyOTP);

        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!no1.getText().toString().trim().isEmpty() && !no2.getText().toString().trim().isEmpty() && !no3.getText().toString().trim().isEmpty() && !no4.getText().toString().trim().isEmpty() && !no5.getText().toString().trim().isEmpty() && !no6.getText().toString().trim().isEmpty()) {
                    String enterCodeOtp = no1.getText().toString() + no2.getText().toString() + no3.getText().toString() + no4.getText().toString() + no5.getText().toString() + no6.getText().toString();

                    if (txtOtp != null) {
                        progressBar.setVisibility(View.VISIBLE);
                        buttonSubmit.setVisibility(View.INVISIBLE);

                        PhoneAuthCredential phoneAuthCredential = PhoneAuthProvider.getCredential(txtOtp, enterCodeOtp);

                        FirebaseAuth.getInstance().signInWithCredential(phoneAuthCredential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                progressBar.setVisibility(View.GONE);
                                buttonSubmit.setVisibility(View.VISIBLE);
                                if (task.isSuccessful()) {
                                    String organizerName_ = getIntent().getStringExtra("organizerName");
                                    String nic_ = getIntent().getStringExtra("nic");
                                    String district_ = getIntent().getStringExtra("district");
                                    String place_ = getIntent().getStringExtra("place");
                                    String address_ = getIntent().getStringExtra("address");
                                    String mobile_ = getIntent().getStringExtra("mobile");
                                    String campaignDate_ = getIntent().getStringExtra("campaignDate");
                                    String latitude_ = getIntent().getStringExtra("latitude");
                                    String longitude_ = getIntent().getStringExtra("longitude");
                                    String campaignId = UUID.randomUUID().toString();
                                    String status = "Pending";

                                    firebaseDatabase = FirebaseDatabase.getInstance();
                                    reference = firebaseDatabase.getReference("campaign");
                                    storeCampaignData storeCampaignData = new storeCampaignData(campaignId, organizerName_, nic_, campaignDate_, mobile_, district_, place_, address_, status, latitude_, longitude_);
                                    reference.child(campaignId).setValue(storeCampaignData);

                                    Toast.makeText(getApplicationContext(), "Campaign created successfully", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(getApplicationContext(), LeftNavActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                    finish();

                                } else {
                                    Toast.makeText(OrganizeCampaignVerifyOtpActivity.this, "Enter the correct OTP", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    } else {
                        Toast.makeText(OrganizeCampaignVerifyOtpActivity.this, "Please check internet connection.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(OrganizeCampaignVerifyOtpActivity.this, "Please enter all OTP numbers", Toast.LENGTH_SHORT).show();
                }
            }
        });

        numberotpmove();

        TextView txtResendOtp = findViewById(R.id.txt_resent_otp);
        txtResendOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PhoneAuthProvider.getInstance().verifyPhoneNumber("+94" + getIntent().getStringExtra("mobile"), 60, TimeUnit.SECONDS, OrganizeCampaignVerifyOtpActivity.this, new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

                    }

                    @Override
                    public void onVerificationFailed(@NonNull FirebaseException e) {
                        Toast.makeText(OrganizeCampaignVerifyOtpActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCodeSent(@NonNull String newbackendOtp, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                        txtOtp = newbackendOtp;
                        Toast.makeText(OrganizeCampaignVerifyOtpActivity.this, "OTP code sent successfully..", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void numberotpmove() {
        no1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().trim().isEmpty()) {
                    no2.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        no2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().trim().isEmpty()) {
                    no3.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        no3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().trim().isEmpty()) {
                    no4.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        no4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().trim().isEmpty()) {
                    no5.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        no5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().trim().isEmpty()) {
                    no6.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


    }

    public void moveToHomeScreen(View view) {
        Intent intent = new Intent(getApplicationContext(), LeftNavActivity.class);
        startActivity(intent);
    }

    public void moveToPreviousScreen(View view) {
            Intent intent = new Intent(getApplicationContext(), OrganizeCampaignActivity.class);
            startActivity(intent);
    }

    @Override
    public void onBackPressed() {

    }
}