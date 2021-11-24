package com.example.bloodtransfusionservice;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;

public class UserSignup4Activity extends AppCompatActivity {
    TextInputLayout txtPhone, txtPassword, txtReenterPassword;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference reference;
    Button btnUserNext, buttonUserLogin, buttonUserHome;
    ProgressBar progressBar;
    static final int PICK_MAP_POINT_REQUEST = 1;
    ImageView backImg;
    String donorFlag, passwordResetVal, donorReg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_signup4);
        txtPhone = findViewById(R.id.input_phone);
        txtPassword = findViewById(R.id.input_password);
        txtReenterPassword = findViewById(R.id.input_reenter_password);
        btnUserNext = findViewById(R.id.btn_user_next);
        buttonUserLogin = findViewById(R.id.btn_user_login);
        buttonUserHome = findViewById(R.id.btn_user_home);
        backImg = findViewById(R.id.img_back);
        progressBar = findViewById(R.id.progress);

        donorFlag = getIntent().getStringExtra("Flag");
        passwordResetVal = getIntent().getStringExtra("passwordResetFlag");
        donorReg = getIntent().getStringExtra("donorRegistration");

        btnUserNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseDatabase = FirebaseDatabase.getInstance();
                reference = firebaseDatabase.getReference("donor");

                String phone_var = txtPhone.getEditText().getText().toString();
                String password_var = txtPassword.getEditText().getText().toString();
                String reenter_password_var = txtReenterPassword.getEditText().getText().toString();
                Log.d("phone no", String.valueOf(phone_var));

                if (!phone_var.trim().isEmpty()) {
                    txtPhone.setError(null);
                    txtPhone.setErrorEnabled(false);
                    if (phone_var.trim().length() == 9) {
                        txtPhone.setError(null);
                        txtPhone.setErrorEnabled(false);
                        if (!password_var.isEmpty()) {
                            txtPassword.setError(null);
                            txtPassword.setErrorEnabled(false);
                            if (password_var.matches("((?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{6,20})")) {
                                txtPassword.setError(null);
                                txtPassword.setErrorEnabled(false);
                                if (!reenter_password_var.isEmpty()) {
                                    txtReenterPassword.setError(null);
                                    txtReenterPassword.setErrorEnabled(false);
                                    if (reenter_password_var.equals(password_var)) {
                                        txtReenterPassword.setError(null);
                                        txtReenterPassword.setErrorEnabled(false);

                                        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                                        DatabaseReference databaseReference = firebaseDatabase.getReference("donor");
                                        Query checkmobile = databaseReference.orderByChild("mobile").equalTo(phone_var);

                                        checkmobile.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                if (!snapshot.exists()) {
                                                    txtPhone.setError(null);
                                                    txtPhone.setErrorEnabled(false);

                                                    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                                                    DatabaseReference databaseReference = firebaseDatabase.getReference("facilitator");
                                                    Query checkmobile = databaseReference.orderByChild("mobile").equalTo(phone_var);

                                                    checkmobile.addListenerForSingleValueEvent(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                                                            if (!snapshot.exists()) {
                                                                txtPhone.setError(null);
                                                                txtPhone.setErrorEnabled(false);

                                                                progressBar.setVisibility(View.VISIBLE);
                                                                btnUserNext.setVisibility(View.INVISIBLE);

                                                                PhoneAuthProvider.getInstance().verifyPhoneNumber("+94" + phone_var, 60, TimeUnit.SECONDS, UserSignup4Activity.this, new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                                                                    @Override
                                                                    public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                                                                        progressBar.setVisibility(View.VISIBLE);
                                                                        btnUserNext.setVisibility(View.INVISIBLE);
                                                                    }

                                                                    @Override
                                                                    public void onVerificationFailed(@NonNull FirebaseException e) {
                                                                        progressBar.setVisibility(View.VISIBLE);
                                                                        btnUserNext.setVisibility(View.INVISIBLE);
                                                                        Toast.makeText(UserSignup4Activity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                                                    }

                                                                    @Override
                                                                    public void onCodeSent(@NonNull String backendOtp, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                                                        String fullname_ = getIntent().getStringExtra("fullname");
                                                                        String email_ = getIntent().getStringExtra("email");
                                                                        String nic_ = getIntent().getStringExtra("nic");
                                                                        String bloodGroup_ = getIntent().getStringExtra("bloodGroup");
                                                                        String district_ = getIntent().getStringExtra("district");
                                                                        String date_ = getIntent().getStringExtra("lastDateDonated");
                                                                        String gender_ = getIntent().getStringExtra("gender");
                                                                        String dob_ = getIntent().getStringExtra("dob");
                                                                        String location_ = getIntent().getStringExtra("location");
                                                                        String address_ = getIntent().getStringExtra("address");
                                                                        String diseases_ = getIntent().getStringExtra("diseases");
                                                                        String txt_diseases_ = getIntent().getStringExtra("diseasesList");

                                                                        progressBar.setVisibility(View.VISIBLE);
                                                                        btnUserNext.setVisibility(View.INVISIBLE);
                                                                        Intent intent = new Intent(UserSignup4Activity.this, VerifyOTPActivity.class);
                                                                        intent.putExtra("mobile", phone_var);
                                                                        intent.putExtra("password", password_var);
                                                                        intent.putExtra("backendOtp", backendOtp);
                                                                        Log.d("====backendOtp: ", String.valueOf(backendOtp));
                                                                        intent.putExtra("fullname", fullname_);
                                                                        intent.putExtra("email", email_);
                                                                        intent.putExtra("nic", nic_);
                                                                        intent.putExtra("bloodGroup", bloodGroup_);
                                                                        intent.putExtra("district", district_);
                                                                        intent.putExtra("diseases", diseases_);
                                                                        intent.putExtra("diseasesList", txt_diseases_);
                                                                        intent.putExtra("lastDateDonated", date_);
                                                                        intent.putExtra("gender", gender_);
                                                                        intent.putExtra("dob", dob_);
                                                                        intent.putExtra("Flag", donorFlag);
                                                                        intent.putExtra("passwordResetFlag", passwordResetVal);
                                                                        intent.putExtra("donorRegistration", donorReg);
                                                                        intent.putExtra("location", location_);
                                                                        intent.putExtra("address", address_);

                                                                        startActivityForResult(intent, PICK_MAP_POINT_REQUEST);
                                                                    }
                                                                });
                                                            } else {
                                                                txtPhone.setError("Phone number already registered as a facilitator");
                                                            }
                                                        }

                                                        @Override
                                                        public void onCancelled(@NonNull @NotNull DatabaseError error) {

                                                        }
                                                    });

                                                } else {
                                                    txtPhone.setError("Phone number already registered as a donor");
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {
                                            }
                                        });

                                    } else {
                                        txtReenterPassword.setError("Passwords are not matching");
                                    }

                                } else {
                                    txtReenterPassword.setError("Please confirm password");
                                }

                            } else {
                                txtPassword.setError("Your password should contains lowercase,uppercase letters and must length within 6-20");
                            }

                        } else {
                            txtPassword.setError("Please enter password");
                        }

                    } else {
                        txtPhone.setError("Please enter valid mobile number");
                    }
                } else {
                    txtPhone.setError("Enter mobile number");
                }
            }
        });

        if (donorReg.equals("true")) {
            buttonUserHome.setVisibility(View.VISIBLE);
            buttonUserLogin.setVisibility(View.GONE);
            buttonUserHome.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), FacilitatorLeftNavActivity.class);
                    intent.putExtra("Flag", donorFlag);
                    intent.putExtra("passwordResetFlag", passwordResetVal);
                    intent.putExtra("donorRegistration", donorReg);
                    startActivity(intent);
                }
            });

            backImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), UserSignup3Activity.class);
                    intent.putExtra("Flag", donorFlag);
                    intent.putExtra("passwordResetFlag", passwordResetVal);
                    intent.putExtra("donorRegistration", donorReg);
                    setResult(Activity.RESULT_CANCELED, intent);
                    finish();
                }
            });

        } else {
            buttonUserHome.setVisibility(View.GONE);
            buttonUserLogin.setVisibility(View.VISIBLE);
            buttonUserLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), UserLoginActivity.class);
                    intent.putExtra("Flag", donorFlag);
                    intent.putExtra("passwordResetFlag", passwordResetVal);
                    intent.putExtra("donorRegistration", donorReg);
                    startActivity(intent);
                }
            });

            backImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), UserSignup3Activity.class);
                    intent.putExtra("Flag", donorFlag);
                    intent.putExtra("passwordResetFlag", passwordResetVal);
                    intent.putExtra("donorRegistration", donorReg);
                    setResult(Activity.RESULT_CANCELED, intent);
                    finish();
                }
            });

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