package com.example.bloodtransfusionservice;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class ListOfDiseasesActivity extends AppCompatActivity {
    String donorReg;
    TextView linkTextView;
    ScrollView scrollView;
    FloatingActionButton buttonScrollTop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_diseases);

        scrollView = findViewById(R.id.scrollView);
        buttonScrollTop = findViewById(R.id.buttonScrollTop);

        //Get phone number from session
        SessionManager sessionManager = new SessionManager(this, SessionManager.SESSION_USER_SESSION);
        HashMap<String, String> userDetails = sessionManager.getUserDetailFromSession();
        String phone = userDetails.get(SessionManager.KEY_PHONE_NO);
        Log.d("PHONE NUMBER : ", String.valueOf(phone));

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference("donor");
        Query checkMobile = databaseReference.orderByChild("mobile").equalTo(phone);

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
        setupHyperlink();

        scrollView.getViewTreeObserver()
                .addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
                    @Override
                    public void onScrollChanged() {
                        if (scrollView.getChildAt(0).getBottom()
                                <= (scrollView.getHeight() + scrollView.getScrollY())) {
                            buttonScrollTop.setVisibility(View.VISIBLE);
                            buttonScrollTop.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    scrollView.setScrollY(0);
                                }
                            });
                        } else {
                            buttonScrollTop.setVisibility(View.INVISIBLE);
                        }
                    }
                });
    }

    private void setupHyperlink() {
        linkTextView = findViewById(R.id.donor_diseases);
        linkTextView.setMovementMethod(LinkMovementMethod.getInstance());
    }

    public void moveToPreviousScreen(View view) {
        if (donorReg.equals("true")){
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