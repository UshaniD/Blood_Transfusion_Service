package com.example.bloodtransfusionservice;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;
import java.util.HashMap;

public class MyAchievementsActivity extends AppCompatActivity {
    TextView txtNoOfTimesDonated, txtLastDonateDate, txtNextDate;
    Button buttonBack, buttonHistory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_achievements);

        //Get phone number from session
        SessionManager sessionManager = new SessionManager(this, SessionManager.SESSION_USER_SESSION);
        HashMap<String, String> userDetails = sessionManager.getUserDetailFromSession();
        String phone = userDetails.get(SessionManager.KEY_PHONE_NO);
        Log.d("PHONE: ", String.valueOf(phone));

        txtNoOfTimesDonated = findViewById(R.id.txt_times_blood_donated);
        txtLastDonateDate = findViewById(R.id.txt_last_blood_donated_date);
        txtNextDate = findViewById(R.id.txt_next_blood_donation_date);
        buttonBack = findViewById(R.id.btn_back);
        buttonHistory = findViewById(R.id.btn_histroy);

        //Display user profile data by getting from table
        showAllUserData(phone);

        buttonHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), BloodDonationHistoryActivity.class);
                startActivity(intent);
            }
        });

    }

    private void showAllUserData(String phone_var) {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference("donor");
        Query checkmobile = databaseReference.orderByChild("mobile").equalTo(phone_var);

        checkmobile.addListenerForSingleValueEvent(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    //Get all user data from donor table
                    String noOfDonations_var = snapshot.child(phone_var).child("noOfDonations").getValue(String.class);
                    String dateLastDonated_var = snapshot.child(phone_var).child("dateLastDonated").getValue(String.class);

                    /* Adding four months to last donated date */
                    String dateConvert = dateLastDonated_var.replace('/', '-');
                    String dateAfterAddMonths = LocalDate.parse(dateConvert).plusMonths(4).toString();
                    String nextDate = dateAfterAddMonths.replace('-', '/');
                    Log.d("nextDate: ", String.valueOf(nextDate));

                    //Set values
                    txtNoOfTimesDonated.setText(noOfDonations_var);
                    txtLastDonateDate.setText(dateLastDonated_var);
                    txtNextDate.setText(nextDate);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void moveToPreviousScreen(View view) {
        Intent intent = new Intent(getApplicationContext(), LeftNavActivity.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {

    }


}