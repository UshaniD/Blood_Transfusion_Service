package com.example.bloodtransfusionservice;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class AboutusActivity extends AppCompatActivity {
    String donorReg;
    TextView linkTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aboutus);

        /* Get phone number from session */
        SessionManager sessionManager = new SessionManager(this, SessionManager.SESSION_USER_SESSION);
        HashMap<String, String> userDetails = sessionManager.getUserDetailFromSession();
        String phone = userDetails.get(SessionManager.KEY_PHONE_NO);
        Log.d("PHONE NUMBER : ", String.valueOf(phone));

        /* Create reference to table */
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference("donor");
        Query checkMobile = databaseReference.orderByChild("mobile").equalTo(phone);

        /* Verify whether mobile number exists on donor table */
        checkMobile.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    donorReg = "true";
                } else {
                    donorReg = "false";
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        /* Call hyperlink method */
        setupHyperlink();
    }

    private void setupHyperlink() {
        linkTextView = findViewById(R.id.aboutUs);
        linkTextView.setMovementMethod(LinkMovementMethod.getInstance());
    }

    /* Move back to previous activity */
    public void moveToPreviousScreen(View view) {
        if (donorReg.equals("true")) {
            Intent intent = new Intent(getApplicationContext(), LeftNavActivity.class);
            startActivity(intent);
        } else {
            Intent intent = new Intent(getApplicationContext(), FacilitatorLeftNavActivity.class);
            startActivity(intent);
        }

    }

    @Override
    public void onBackPressed() {

    }
}