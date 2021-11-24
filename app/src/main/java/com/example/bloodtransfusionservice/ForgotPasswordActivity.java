package com.example.bloodtransfusionservice;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
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

public class ForgotPasswordActivity extends AppCompatActivity {
    Button buttonReset;
    TextInputLayout inputPhone, inputNewPassword, inputConfirmPassword;
    FirebaseAuth mAuth;
    ProgressDialog pd;
    ProgressBar progressBar;
    static String passwordResetVal = "";
    static String donorFlag = "";
    static String donorReg = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        mAuth = FirebaseAuth.getInstance();
        pd = new ProgressDialog(this);
        pd.setMessage("Loading...");
        pd.setCancelable(true);
        pd.setCanceledOnTouchOutside(false);
        passwordResetVal = getIntent().getStringExtra("passwordResetFlag");
        donorFlag = getIntent().getStringExtra("Flag");

        inputNewPassword = findViewById(R.id.input_password);
        inputConfirmPassword = findViewById(R.id.input_reenter_password);
        inputPhone = findViewById(R.id.input_phone);
        progressBar = findViewById(R.id.progress);
        buttonReset = findViewById(R.id.btn_reset);

        buttonReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone_var = inputPhone.getEditText().getText().toString();
                String password_var = inputNewPassword.getEditText().getText().toString();
                String confirmPassword_var = inputConfirmPassword.getEditText().getText().toString();
                Log.d("phone no", String.valueOf(phone_var));

                if (!phone_var.trim().isEmpty()) {
                    inputPhone.setError(null);
                    inputPhone.setErrorEnabled(false);
                    if (phone_var.trim().length() == 9) {
                        inputPhone.setError(null);
                        inputPhone.setErrorEnabled(false);
                        if (!password_var.isEmpty()) {
                            inputNewPassword.setError(null);
                            inputNewPassword.setErrorEnabled(false);
                            if (password_var.matches("((?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{6,20})")) {
                                inputNewPassword.setError(null);
                                inputNewPassword.setErrorEnabled(false);
                                if (!confirmPassword_var.isEmpty()) {
                                    inputConfirmPassword.setError(null);
                                    inputConfirmPassword.setErrorEnabled(false);
                                    if (confirmPassword_var.equals(password_var)) {
                                        inputConfirmPassword.setError(null);
                                        inputConfirmPassword.setErrorEnabled(false);

                                        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                                        DatabaseReference databaseReference = firebaseDatabase.getReference("donor");
                                        Query checkmobile = databaseReference.orderByChild("mobile").equalTo(phone_var);

                                        checkmobile.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                if (snapshot.exists()) {
                                                    inputPhone.setError(null);
                                                    inputPhone.setErrorEnabled(false);
                                                    progressBar.setVisibility(View.VISIBLE);
                                                    buttonReset.setVisibility(View.INVISIBLE);

                                                    PhoneAuthProvider.getInstance().verifyPhoneNumber("+94" + phone_var, 60, TimeUnit.SECONDS, ForgotPasswordActivity.this, new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                                                        @Override
                                                        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                                                            progressBar.setVisibility(View.VISIBLE);
                                                            buttonReset.setVisibility(View.INVISIBLE);
                                                        }

                                                        @Override
                                                        public void onVerificationFailed(@NonNull FirebaseException e) {
                                                            progressBar.setVisibility(View.VISIBLE);
                                                            buttonReset.setVisibility(View.INVISIBLE);
                                                            Toast.makeText(ForgotPasswordActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                                        }

                                                        @Override
                                                        public void onCodeSent(@NonNull String backendOtp, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                                            progressBar.setVisibility(View.VISIBLE);
                                                            buttonReset.setVisibility(View.INVISIBLE);
                                                            donorFlag = "true";
                                                            donorReg = "false";
                                                            Intent intent = new Intent(getApplicationContext(), VerifyOTPActivity.class);
                                                            intent.putExtra("mobile", phone_var);
                                                            intent.putExtra("password", password_var);
                                                            intent.putExtra("Flag", donorFlag);
                                                            intent.putExtra("backendOtp", backendOtp);
                                                            intent.putExtra("passwordResetFlag", passwordResetVal);
                                                            intent.putExtra("donorRegistration", donorReg);
                                                            startActivity(intent);
                                                        }
                                                    });

                                                } else {
                                                    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                                                    DatabaseReference databaseReference = firebaseDatabase.getReference("facilitator");
                                                    Query checkmobile = databaseReference.orderByChild("mobile").equalTo(phone_var);

                                                    checkmobile.addListenerForSingleValueEvent(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                                                            if (snapshot.exists()) {
                                                                inputPhone.setError(null);
                                                                inputPhone.setErrorEnabled(false);
                                                                progressBar.setVisibility(View.VISIBLE);
                                                                buttonReset.setVisibility(View.INVISIBLE);

                                                                PhoneAuthProvider.getInstance().verifyPhoneNumber("+94" + phone_var, 60, TimeUnit.SECONDS, ForgotPasswordActivity.this, new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                                                                    @Override
                                                                    public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                                                                        progressBar.setVisibility(View.VISIBLE);
                                                                        buttonReset.setVisibility(View.INVISIBLE);
                                                                    }

                                                                    @Override
                                                                    public void onVerificationFailed(@NonNull FirebaseException e) {
                                                                        progressBar.setVisibility(View.VISIBLE);
                                                                        buttonReset.setVisibility(View.INVISIBLE);
                                                                        Toast.makeText(ForgotPasswordActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                                                    }

                                                                    @Override
                                                                    public void onCodeSent(@NonNull String backendOtp, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                                                        progressBar.setVisibility(View.VISIBLE);
                                                                        buttonReset.setVisibility(View.INVISIBLE);
                                                                        donorFlag = "false";
                                                                        donorReg = "false";
                                                                        Intent intent = new Intent(getApplicationContext(), VerifyOTPActivity.class);
                                                                        intent.putExtra("mobile", phone_var);
                                                                        intent.putExtra("password", password_var);
                                                                        intent.putExtra("Flag", donorFlag);
                                                                        intent.putExtra("backendOtp", backendOtp);
                                                                        intent.putExtra("passwordResetFlag", passwordResetVal);
                                                                        intent.putExtra("donorRegistration", donorReg);
                                                                        startActivity(intent);
                                                                    }
                                                                });
                                                            } else {
                                                                inputPhone.setError("Phone number not exists");
                                                            }
                                                        }

                                                        @Override
                                                        public void onCancelled(@NonNull @NotNull DatabaseError error) {

                                                        }
                                                    });
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {
                                            }
                                        });

                                    } else {
                                        inputConfirmPassword.setError("Passwords are not matching");
                                    }

                                } else {
                                    inputConfirmPassword.setError("Please confirm password");
                                }

                            } else {
                                inputNewPassword.setError("Your password should contains lowercase,uppercase letters and must length within 6-20");
                            }

                        } else {
                            inputNewPassword.setError("Please enter password");
                        }

                    } else {
                        inputPhone.setError("Please enter valid mobile number");
                    }
                } else {
                    inputPhone.setError("Enter mobile number");
                }
            }
        });
    }

    public void moveToPreviousScreen(View view) {
        Intent intent = new Intent(getApplicationContext(), UserLoginActivity.class);
        startActivity(intent);
    }

    public void moveToLoginScreen(View view) {
        Intent intent = new Intent(getApplicationContext(), UserLoginActivity.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {

    }
}