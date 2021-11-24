package com.example.bloodtransfusionservice;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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

public class FacilitatorSignup3Activity extends AppCompatActivity {
    TextInputLayout txtPhone, txtPassword, txtReenterPassword, txtEmpId;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference reference;
    Button btnUserNext;
    ProgressBar progressBar;
    static final int PICK_MAP_POINT_REQUEST = 1;
    String donorFlag, donorReg, passwordResetVal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facilitator_signup3);

        txtEmpId = findViewById(R.id.input_emp_id);
        txtPhone = findViewById(R.id.input_phone);
        txtPassword = findViewById(R.id.input_password);
        txtReenterPassword = findViewById(R.id.input_reenter_password);
        btnUserNext = findViewById(R.id.btn_user_next);
        progressBar = findViewById(R.id.progress);

        donorFlag = getIntent().getStringExtra("Flag");
        passwordResetVal = getIntent().getStringExtra("passwordResetFlag");
        donorReg = getIntent().getStringExtra("donorRegistration");

        btnUserNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String empid_var = txtEmpId.getEditText().getText().toString();
                String phone_var = txtPhone.getEditText().getText().toString();
                String password_var = txtPassword.getEditText().getText().toString();
                String reenter_password_var = txtReenterPassword.getEditText().getText().toString();
                Log.d("phone no", String.valueOf(phone_var));

                if (!empid_var.trim().isEmpty()) {
                    txtEmpId.setError(null);
                    txtEmpId.setErrorEnabled(false);

                    firebaseDatabase = FirebaseDatabase.getInstance();
                    reference = firebaseDatabase.getReference("employee");

                    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                    DatabaseReference databaseReference = firebaseDatabase.getReference("employee");
                    Query checkempid = databaseReference.orderByChild("empid").equalTo(empid_var);

                    checkempid.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                txtEmpId.setError(null);
                                txtEmpId.setErrorEnabled(false);

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
                                                        DatabaseReference databaseReference = firebaseDatabase.getReference("facilitator");
                                                        Query checkmobile = databaseReference.orderByChild("mobile").equalTo(phone_var);

                                                        checkmobile.addListenerForSingleValueEvent(new ValueEventListener() {
                                                            @Override
                                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                if (!snapshot.exists()) {
                                                                    txtPhone.setError(null);
                                                                    txtPhone.setErrorEnabled(false);

                                                                    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                                                                    DatabaseReference databaseReference = firebaseDatabase.getReference("donor");
                                                                    Query checkmobile = databaseReference.orderByChild("mobile").equalTo(phone_var);

                                                                    checkmobile.addListenerForSingleValueEvent(new ValueEventListener() {
                                                                        @Override
                                                                        public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                                                                            if (!snapshot.exists()) {
                                                                                txtPhone.setError(null);
                                                                                txtPhone.setErrorEnabled(false);

                                                                                progressBar.setVisibility(View.VISIBLE);
                                                                                btnUserNext.setVisibility(View.INVISIBLE);

                                                                                PhoneAuthProvider.getInstance().verifyPhoneNumber("+94" + phone_var, 60, TimeUnit.SECONDS, FacilitatorSignup3Activity.this, new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                                                                                    @Override
                                                                                    public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                                                                                        progressBar.setVisibility(View.VISIBLE);
                                                                                        btnUserNext.setVisibility(View.INVISIBLE);
                                                                                    }

                                                                                    @Override
                                                                                    public void onVerificationFailed(@NonNull FirebaseException e) {
                                                                                        progressBar.setVisibility(View.VISIBLE);
                                                                                        btnUserNext.setVisibility(View.INVISIBLE);
                                                                                        Toast.makeText(FacilitatorSignup3Activity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                                                                    }

                                                                                    @Override
                                                                                    public void onCodeSent(@NonNull String backendOtp, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                                                                        String fullname_ = getIntent().getStringExtra("fullname");
                                                                                        String email_ = getIntent().getStringExtra("email");
                                                                                        String nic_ = getIntent().getStringExtra("nic");
                                                                                        String district_ = getIntent().getStringExtra("district");
                                                                                        String gender_ = getIntent().getStringExtra("gender");
                                                                                        String dob_ = getIntent().getStringExtra("dob");
                                                                                        String location_ = getIntent().getStringExtra("location");
                                                                                        String address_ = getIntent().getStringExtra("address");

                                                                                        progressBar.setVisibility(View.VISIBLE);
                                                                                        btnUserNext.setVisibility(View.INVISIBLE);

                                                                                        Intent intent = new Intent(FacilitatorSignup3Activity.this, VerifyOTPActivity.class);

                                                                                        intent.putExtra("mobile", phone_var);
                                                                                        intent.putExtra("password", password_var);
                                                                                        intent.putExtra("backendOtp", backendOtp);
                                                                                        intent.putExtra("fullname", fullname_);
                                                                                        intent.putExtra("email", email_);
                                                                                        intent.putExtra("nic", nic_);
                                                                                        intent.putExtra("empid", empid_var);
                                                                                        intent.putExtra("district", district_);
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
                                                                                txtPhone.setError("Phone number already registered as a donor");
                                                                            }
                                                                        }

                                                                        @Override
                                                                        public void onCancelled(@NonNull @NotNull DatabaseError error) {

                                                                        }
                                                                    });

                                                                } else {
                                                                    txtPhone.setError("Phone number already registered as a facilitator");
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
                                                txtPassword.setError("Your password should contain at least one uppercase, lowercase, number and it must have length within 6-20");
                                            }

                                        } else {
                                            txtPassword.setError("Please enter password");
                                        }

                                    } else {
                                        txtPhone.setError("Please enter valid mobile number");
                                        //Toast.makeText(UserSignup4Activity.this, "Please enter valid mobile number", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    txtPhone.setError("Enter mobile number");
                                    //Toast.makeText(UserSignup4Activity.this, "Enter mobile number", Toast.LENGTH_SHORT).show();

                                }

                            } else {
                                txtEmpId.setError("Invalid Employee number. Please contact administrator");
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });
                } else {
                    txtEmpId.setError("Enter employee number");
                }
            }
        });
    }

    public void moveToLoginScreen(View view) {
        String donorFlag = getIntent().getStringExtra("Flag");
        Intent intent = new Intent(getApplicationContext(), UserLoginActivity.class);
        intent.putExtra("Flag", donorFlag);
        intent.putExtra("passwordResetFlag", passwordResetVal);
        intent.putExtra("donorRegistration", donorReg);
        startActivity(intent);
    }

    public void moveToPreviousScreen(View view) {
        String donorFlag = getIntent().getStringExtra("Flag");
        Log.d("==============MYFLAG: ", String.valueOf(donorFlag));
        Intent intent = new Intent(getApplicationContext(), FacilitatorSignup2Activity.class);
        intent.putExtra("Flag", donorFlag);
        intent.putExtra("passwordResetFlag", passwordResetVal);
        intent.putExtra("donorRegistration", donorReg);
        setResult(Activity.RESULT_CANCELED, intent);
        finish();
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