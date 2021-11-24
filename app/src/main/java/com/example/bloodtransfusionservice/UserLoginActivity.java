package com.example.bloodtransfusionservice;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.security.MessageDigest;
import java.util.HashMap;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class UserLoginActivity extends AppCompatActivity {
    Button signup, login, button_forgotPassword;
    TextInputLayout txtPhone, txtPassword;
    EditText txt_phone, txt_password;
    static String passwordResetVal = "";
    CheckBox rememberMe;
    String decyptedPassword;
    String AES = "AES";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_user_login);

        /*Variable hooks*/
        signup = findViewById(R.id.btn_user_signup);
        login = findViewById(R.id.btn_user_login);
        txtPhone = findViewById(R.id.input_phone);
        txtPassword = findViewById(R.id.input_password);
        txt_phone = findViewById(R.id.txt_phoneNo); //For Remember me session EditText
        txt_password = findViewById(R.id.txt_password); //For Remember me session EditText
        button_forgotPassword = findViewById(R.id.btn_forgot_password);
        rememberMe = findViewById(R.id.checkbox_remember_me);
        final ProgressBar progressBar = findViewById(R.id.progress);

        /*Check whether remember me already saved in shared preferences*/
        SessionManager sessionManager1 = new SessionManager(UserLoginActivity.this, SessionManager.SESSION_REMEMBER_ME_SESSION);
        if (sessionManager1.checkRememberMe()){
            HashMap<String,String> rememberMeDetails = sessionManager1.getRememberMeDetailFromSession();
            txt_phone.setText(rememberMeDetails.get(SessionManager.KEY_SESSION_PHONE_NO));
            txt_password.setText(rememberMeDetails.get(SessionManager.KEY_SESSION_PASSWORD));
        }

        /*Click on Login button*/
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /* Get entered phone number and password */
                String phone_val = txtPhone.getEditText().getText().toString();
                String password_val = txtPassword.getEditText().getText().toString();

                if (!phone_val.isEmpty()) {
                    txtPhone.setError(null);
                    txtPhone.setErrorEnabled(false);
                    if (!password_val.isEmpty()) {
                        txtPassword.setError(null);
                        txtPassword.setErrorEnabled(false);

                        final String phone_ = txtPhone.getEditText().getText().toString();
                        final String password_ = txtPassword.getEditText().getText().toString();

                        /* Check on donor table using phone number */
                        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                        DatabaseReference databaseReference = firebaseDatabase.getReference("donor");
                        Query checkmobile = databaseReference.orderByChild("mobile").equalTo(phone_val);

                        checkmobile.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.exists()) {
                                    txtPhone.setError(null);
                                    txtPhone.setErrorEnabled(false);

                                    /* Get password for the entered phone number*/
                                    String checkpassword = snapshot.child(phone_).child("password").getValue(String.class);

                                    /* Decrypt password */
                                    String key_ = "bts"; //key to decrypt
                                    try {
                                        decyptedPassword = decrypt(checkpassword, key_);
                                        Log.d("decyptedPassword: ", String.valueOf(decyptedPassword));
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }

                                    /* Verify password */
                                    if (decyptedPassword.equals(password_)) {
                                        /* Get user status for the entered phone number*/
                                        String checkUserStatus = snapshot.child(phone_).child("userStatus").getValue(String.class);

                                        if (checkUserStatus.equals("inactive")){
                                            Toast.makeText(getApplicationContext(), "Your mobile number currently inactivated.", Toast.LENGTH_SHORT).show();
                                        }else {
                                            txtPassword.setError(null);
                                            txtPassword.setErrorEnabled(false);
                                            progressBar.setVisibility(View.VISIBLE);
                                            login.setVisibility(View.INVISIBLE);

                                            /* Create session if credentials exist */
                                            SessionManager sessionManager = new SessionManager(UserLoginActivity.this, SessionManager.SESSION_USER_SESSION);
                                            sessionManager.createLoginSession(phone_);

                                            /* Check remember me clicked */
                                            if (rememberMe.isChecked()){
                                                SessionManager sessionManager1 = new SessionManager(UserLoginActivity.this, SessionManager.SESSION_REMEMBER_ME_SESSION);
                                                sessionManager1.createRememberMeSession(phone_,password_);
                                            }

                                            Toast.makeText(getApplicationContext(), "Login successful!", Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(getApplicationContext(), LeftNavActivity.class);
                                            intent.putExtra("mobile", phone_);
                                            startActivity(intent);
                                            finish();
                                        }

                                    } else {
                                        txtPassword.setError("Wrong password");
                                    }
                                } else if(!snapshot.exists()){
                                    txtPhone.setError(null);
                                    txtPhone.setErrorEnabled(false);
                                    final String phone_ = txtPhone.getEditText().getText().toString();
                                    final String password_ = txtPassword.getEditText().getText().toString();

                                    /* Check on facilitator table using phone number if not in donor table */
                                    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                                    DatabaseReference databaseReference = firebaseDatabase.getReference("facilitator");
                                    Query checkmobile = databaseReference.orderByChild("mobile").equalTo(phone_val);
                                    Log.d("==========phone no", String.valueOf(phone_));

                                    checkmobile.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                                            if (snapshot.exists()) {
                                                txtPhone.setError(null);
                                                txtPhone.setErrorEnabled(false);

                                                String checkpassword = snapshot.child(phone_).child("password").getValue(String.class);

                                                /* Decrypt password */
                                                String key_ = "bts"; //key to decrypt
                                                try {
                                                    decyptedPassword = decrypt(checkpassword, key_);
                                                    Log.d("decyptedPassword: ", String.valueOf(decyptedPassword));
                                                    if (decyptedPassword.equals(password_)) {
                                                        txtPassword.setError(null);
                                                        txtPassword.setErrorEnabled(false);
                                                        progressBar.setVisibility(View.VISIBLE);
                                                        login.setVisibility(View.INVISIBLE);

                                                        //Create session
                                                        SessionManager sessionManager = new SessionManager(UserLoginActivity.this, SessionManager.SESSION_USER_SESSION);
                                                        sessionManager.createLoginSession(phone_);

                                                        if (rememberMe.isChecked()){
                                                            SessionManager sessionManager1 = new SessionManager(UserLoginActivity.this, SessionManager.SESSION_REMEMBER_ME_SESSION);
                                                            sessionManager1.createRememberMeSession(phone_,password_);
                                                        }

                                                        Toast.makeText(getApplicationContext(), "Login successful!", Toast.LENGTH_SHORT).show();
                                                        Intent intent = new Intent(getApplicationContext(), FacilitatorLeftNavActivity.class);
                                                        intent.putExtra("mobile", phone_);
                                                        startActivity(intent);
                                                        finish();
                                                    } else {
                                                        txtPassword.setError("Wrong password");
                                                    }
                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                    txtPassword.setError("Wrong password");
                                                }


                                            }else {
                                                txtPhone.setError("Phone number not existing. Please signup.");
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
                        txtPassword.setError("Please enter password");
                    }
                } else {
                    txtPhone.setError("Please enter phone number");
                }

            }
        });

        /*Click on new user registration button*/
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                passwordResetVal = "false";
                Intent intent = new Intent(getApplicationContext(), LoginDecisionActivity.class);
                intent.putExtra("passwordResetFlag", passwordResetVal);
                startActivity(intent);
            }
        });

        /*Click on forgot password button*/
        button_forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                passwordResetVal = "true";
                Intent intent = new Intent(getApplicationContext(), ForgotPasswordActivity.class);
                intent.putExtra("passwordResetFlag", passwordResetVal);
                startActivity(intent);
            }
        });

    }

    /* Decrypt password */
    private String decrypt(String password, String keyVal) throws Exception {
        SecretKeySpec key = generateKey(keyVal);
        Cipher c = Cipher.getInstance(AES);
        c.init(Cipher.DECRYPT_MODE, key);
        byte[] decodedVal = Base64.decode(password, Base64.DEFAULT);
        byte[] decVal = c.doFinal(decodedVal);
        String decryptedVal = new String(decVal);
        return decryptedVal;
    }

    private SecretKeySpec generateKey(String keyVal) throws Exception {
        final MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] bytes = keyVal.getBytes("UTF-8");
        digest.update(bytes, 0, bytes.length);
        byte[] key = digest.digest();
        SecretKeySpec secretKeySpec = new SecretKeySpec(key, "AES");
        return secretKeySpec;
    }

    /*Disabled mobile back button*/
    @Override
    public void onBackPressed() {

    }
}