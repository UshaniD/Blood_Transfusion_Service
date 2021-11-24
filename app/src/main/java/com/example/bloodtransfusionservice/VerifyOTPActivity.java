package com.example.bloodtransfusionservice;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class VerifyOTPActivity extends AppCompatActivity {
    EditText no1, no2, no3, no4, no5, no6;
    TextView txtMobileNumber;
    String txtOtp;
    Button buttonUserLogin, buttonUserHome;
    ImageView backImg;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference reference;
    static String passwordResetVal = "";
    static String donorReg = "";
    static String donorFlag = "";
    String encryptedPassword;
    String AES = "AES";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_otp);

        final Button buttonSubmit = findViewById(R.id.btn_submit);
        buttonUserLogin = findViewById(R.id.btn_user_login);
        buttonUserHome = findViewById(R.id.btn_user_home);
        backImg = findViewById(R.id.img_back);

        no1 = findViewById(R.id.input_no1);
        no2 = findViewById(R.id.input_no2);
        no3 = findViewById(R.id.input_no3);
        no4 = findViewById(R.id.input_no4);
        no5 = findViewById(R.id.input_no5);
        no6 = findViewById(R.id.input_no6);

        txtMobileNumber = findViewById(R.id.input_number_entered);
        txtMobileNumber.setText(String.format("+94%s", getIntent().getStringExtra("mobile")));
        txtOtp = getIntent().getStringExtra("backendOtp");
        Log.d("====backendOtp2: ", String.valueOf(txtOtp));
        donorFlag = getIntent().getStringExtra("Flag");
        passwordResetVal = getIntent().getStringExtra("passwordResetFlag");
        donorReg = getIntent().getStringExtra("donorRegistration");

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
                                    if (passwordResetVal.equals("false")) {
                                        if (donorFlag.equals("false")) {
                                            String mobile_ = getIntent().getStringExtra("mobile");
                                            String password_ = getIntent().getStringExtra("password");
                                            String fullname_ = getIntent().getStringExtra("fullname");
                                            String email_ = getIntent().getStringExtra("email");
                                            String nic_ = getIntent().getStringExtra("nic");
                                            String empid_ = getIntent().getStringExtra("empid");
                                            String district_ = getIntent().getStringExtra("district");
                                            String gender_ = getIntent().getStringExtra("gender");
                                            String dob_ = getIntent().getStringExtra("dob");
                                            String location_ = getIntent().getStringExtra("location");
                                            String address_ = getIntent().getStringExtra("address");

                                            /* Encrypt password */
                                            String key_ = "bts";

                                            try {
                                                encryptedPassword = encrypt(password_, key_);
                                                Log.d("encryptedPassword: ", String.valueOf(encryptedPassword));
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }

                                            firebaseDatabase = FirebaseDatabase.getInstance();
                                            reference = firebaseDatabase.getReference("facilitator");

                                            storeFacilitatorData storeFacilitatorData = new storeFacilitatorData(mobile_, fullname_, email_, nic_, encryptedPassword, empid_, district_, dob_, gender_, location_, address_);

                                            reference.child(mobile_).setValue(storeFacilitatorData);

                                            //Create session
                                            SessionManager sessionManager = new SessionManager(VerifyOTPActivity.this, SessionManager.SESSION_USER_SESSION);
                                            sessionManager.createLoginSession(mobile_);

                                            Toast.makeText(getApplicationContext(), "Registered successfully", Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(getApplicationContext(), FacilitatorLeftNavActivity.class);
                                            intent.putExtra("mobile", mobile_);
                                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            startActivity(intent);
                                            finish();

                                        } else {
                                            if (donorReg.equals("true")) {
                                                String mobile_ = getIntent().getStringExtra("mobile");
                                                String password_ = getIntent().getStringExtra("password");
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
                                                String donations_ = "0";
                                                String userStatus_ = "active";

                                                /* Encrypt password */
                                                String key_ = "bts";

                                                try {
                                                    encryptedPassword = encrypt(password_, key_);
                                                    Log.d("encryptedPassword: ", String.valueOf(encryptedPassword));
                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }

                                                firebaseDatabase = FirebaseDatabase.getInstance();
                                                reference = firebaseDatabase.getReference("donor");

                                                storeData storedata = new storeData(mobile_, fullname_, email_, nic_, encryptedPassword, bloodGroup_, district_, date_, dob_, gender_, location_, address_, donations_, userStatus_, diseases_, txt_diseases_);

                                                reference.child(mobile_).setValue(storedata);

                                                Toast.makeText(getApplicationContext(), "Registered successfully", Toast.LENGTH_SHORT).show();
                                                Intent intent = new Intent(getApplicationContext(), FacilitatorLeftNavActivity.class);
                                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                startActivity(intent);
                                                finish();
                                            }else{
                                                String mobile_ = getIntent().getStringExtra("mobile");
                                                String password_ = getIntent().getStringExtra("password");
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
                                                String donations_ = "0";
                                                String userStatus_ = "active";

                                                /* Encrypt password */
                                                String key_ = "bts";

                                                try {
                                                    encryptedPassword = encrypt(password_, key_);
                                                    Log.d("encryptedPassword: ", String.valueOf(encryptedPassword));
                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }

                                                firebaseDatabase = FirebaseDatabase.getInstance();
                                                reference = firebaseDatabase.getReference("donor");

                                                storeData storedata = new storeData(mobile_, fullname_, email_, nic_, encryptedPassword, bloodGroup_, district_, date_, dob_, gender_, location_, address_, donations_, userStatus_, diseases_, txt_diseases_);

                                                reference.child(mobile_).setValue(storedata);
                                                //Create session
                                                SessionManager sessionManager = new SessionManager(VerifyOTPActivity.this, SessionManager.SESSION_USER_SESSION);
                                                sessionManager.createLoginSession(mobile_);

                                                Toast.makeText(getApplicationContext(), "Registered successfully", Toast.LENGTH_SHORT).show();
                                                Intent intent = new Intent(getApplicationContext(), LeftNavActivity.class);
                                                intent.putExtra("mobile", mobile_);
                                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                startActivity(intent);
                                                finish();
                                            }

                                        }

                                    } else {
                                        if (donorFlag.equals("false")) {
                                            String mobile_ = getIntent().getStringExtra("mobile");
                                            String password_ = getIntent().getStringExtra("password");

                                            /* Encrypt password */
                                            String key_ = "bts";

                                            try {
                                                encryptedPassword = encrypt(password_, key_);
                                                Log.d("encryptedPassword: ", String.valueOf(encryptedPassword));
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }

                                            DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("facilitator").child(mobile_);
                                            Map<String, Object> updates = new HashMap<String, Object>();

                                            updates.put("password", encryptedPassword);

                                            ref.updateChildren(updates);

                                            //Create session
                                            SessionManager sessionManager = new SessionManager(VerifyOTPActivity.this, SessionManager.SESSION_USER_SESSION);
                                            sessionManager.createLoginSession(mobile_);

                                            Toast.makeText(getApplicationContext(), "Updated successfully", Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(getApplicationContext(), FacilitatorLeftNavActivity.class);
                                            intent.putExtra("mobile", mobile_);
                                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            startActivity(intent);
                                            finish();

                                        } else {
                                            String mobile_ = getIntent().getStringExtra("mobile");
                                            String password_ = getIntent().getStringExtra("password");

                                            /* Encrypt password */
                                            String key_ = "bts";

                                            try {
                                                encryptedPassword = encrypt(password_, key_);
                                                Log.d("encryptedPassword: ", String.valueOf(encryptedPassword));
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }

                                            DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("donor").child(mobile_);
                                            Map<String, Object> updates = new HashMap<String, Object>();

                                            updates.put("password", encryptedPassword);

                                            ref.updateChildren(updates);

                                            //Create session
                                            SessionManager sessionManager = new SessionManager(VerifyOTPActivity.this, SessionManager.SESSION_USER_SESSION);
                                            sessionManager.createLoginSession(mobile_);

                                            Toast.makeText(getApplicationContext(), "Updated successfully", Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(getApplicationContext(), LeftNavActivity.class);
                                            intent.putExtra("mobile", mobile_);
                                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            startActivity(intent);
                                            finish();

                                        }

                                    }

                                } else {
                                    Toast.makeText(VerifyOTPActivity.this, "Enter the correct OTP", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    } else {
                        Toast.makeText(VerifyOTPActivity.this, "Please check internet connection.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(VerifyOTPActivity.this, "Please enter all OTP numbers", Toast.LENGTH_SHORT).show();
                }
            }
        });

        numberotpmove();

        TextView txtResendOtp = findViewById(R.id.txt_resent_otp);
        txtResendOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PhoneAuthProvider.getInstance().verifyPhoneNumber("+94" + getIntent().getStringExtra("mobile"), 60, TimeUnit.SECONDS, VerifyOTPActivity.this, new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

                    }

                    @Override
                    public void onVerificationFailed(@NonNull FirebaseException e) {
                        Toast.makeText(VerifyOTPActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCodeSent(@NonNull String newbackendOtp, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                        txtOtp = newbackendOtp;
                        Toast.makeText(VerifyOTPActivity.this, "OTP code sent successfully..", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        if (donorReg.equals("true")) {
            buttonUserHome.setVisibility(View.VISIBLE);
            buttonUserLogin.setVisibility(View.GONE);
            buttonUserHome.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String donorFlag = getIntent().getStringExtra("Flag");
                    Intent intent = new Intent(getApplicationContext(), FacilitatorLeftNavActivity.class);
                    intent.putExtra("Flag", donorFlag);
                    intent.putExtra("passwordResetFlag", passwordResetVal);
                    intent.putExtra("donorRegistration", donorReg);
                    startActivity(intent);
                }
            });

            String donorFlag = getIntent().getStringExtra("Flag");
            Log.d("==============MYFLAG: ", String.valueOf(donorFlag));
            if (donorFlag.equals("false")) {
                backImg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getApplicationContext(), FacilitatorSignup3Activity.class);
                        intent.putExtra("Flag", donorFlag);
                        intent.putExtra("passwordResetFlag", passwordResetVal);
                        intent.putExtra("donorRegistration", donorReg);
                        setResult(Activity.RESULT_CANCELED, intent);
                        finish();
                    }
                });
            } else {
                backImg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getApplicationContext(), UserSignup4Activity.class);
                        intent.putExtra("Flag", donorFlag);
                        intent.putExtra("passwordResetFlag", passwordResetVal);
                        intent.putExtra("donorRegistration", donorReg);
                        setResult(Activity.RESULT_CANCELED, intent);
                        finish();
                    }
                });
            }

        } else {
            buttonUserHome.setVisibility(View.GONE);
            buttonUserLogin.setVisibility(View.VISIBLE);
            buttonUserLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String donorFlag = getIntent().getStringExtra("Flag");
                    Intent intent = new Intent(getApplicationContext(), UserLoginActivity.class);
                    intent.putExtra("Flag", donorFlag);
                    intent.putExtra("passwordResetFlag", passwordResetVal);
                    intent.putExtra("donorRegistration", donorReg);
                    startActivity(intent);
                }
            });

            String donorFlag = getIntent().getStringExtra("Flag");
            Log.d("==============MYFLAG: ", String.valueOf(donorFlag));
            if (donorFlag.equals("false")) {
                backImg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getApplicationContext(), FacilitatorSignup3Activity.class);
                        intent.putExtra("Flag", donorFlag);
                        intent.putExtra("passwordResetFlag", passwordResetVal);
                        intent.putExtra("donorRegistration", donorReg);
                        setResult(Activity.RESULT_CANCELED, intent);
                        finish();
                    }
                });
            } else {
                backImg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getApplicationContext(), UserSignup4Activity.class);
                        intent.putExtra("Flag", donorFlag);
                        intent.putExtra("passwordResetFlag", passwordResetVal);
                        intent.putExtra("donorRegistration", donorReg);
                        setResult(Activity.RESULT_CANCELED, intent);
                        finish();
                    }
                });
            }

        }
    }

    private String encrypt(String password, String keyVal) throws Exception{
        SecretKeySpec key = generateKey(keyVal);
        Cipher c = Cipher.getInstance(AES);
        c.init(Cipher.ENCRYPT_MODE, key);
        byte[] encVal = c.doFinal(password.getBytes());
        String encryptedVal = Base64.encodeToString(encVal, Base64.DEFAULT);
        return  encryptedVal;
    }

    private SecretKeySpec generateKey(String keyVal) throws Exception {
        final MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] bytes = keyVal.getBytes("UTF-8");
        digest.update(bytes, 0, bytes.length);
        byte[] key = digest.digest();
        SecretKeySpec secretKeySpec = new SecretKeySpec(key, "AES");
        return secretKeySpec;
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

    @Override
    public void onBackPressed() {

    }
}