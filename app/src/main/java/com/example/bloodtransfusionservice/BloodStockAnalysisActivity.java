package com.example.bloodtransfusionservice;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class BloodStockAnalysisActivity extends AppCompatActivity {
    GridLayout gridLayout;
    DatePicker analysis_date;
    Button buttonSearch;
    ImageView backImg;
    EditText txt_A_positive_received, txt_A_positive_issued, txt_A_positive_requests, txt_A_positive_avg_issued;
    EditText txt_A_negative_received, txt_A_negative_issued, txt_A_negative_requests, txt_A_negative_avg_issued;
    EditText txt_B_positive_received, txt_B_positive_issued, txt_B_positive_requests, txt_B_positive_avg_issued;
    EditText txt_B_negative_received, txt_B_negative_issued, txt_B_negative_requests, txt_B_negative_avg_issued;
    EditText txt_AB_positive_received, txt_AB_positive_issued, txt_AB_positive_requests, txt_AB_positive_avg_issued;
    EditText txt_AB_negative_received, txt_AB_negative_issued, txt_AB_negative_requests, txt_AB_negative_avg_issued;
    EditText txt_O_positive_received, txt_O_positive_issued, txt_O_positive_requests, txt_O_positive_avg_issued;
    EditText txt_O_negative_received, txt_O_negative_issued, txt_O_negative_requests, txt_O_negative_avg_issued;
    String bloodIssuedDistrict;
    String bloodReceivedDistrict;
    String bloodRequestDistrict;

    String bloodIssuedDate;
    String bloodReceivedDate;
    String bloodRequestDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blood_stock_analysis);

        analysis_date = findViewById(R.id.input_stock_analysis_date);
        buttonSearch = findViewById(R.id.btn_search);
        backImg = findViewById(R.id.img_back);
        txt_A_positive_received = findViewById(R.id.A_positive_received_units);
        txt_A_positive_issued = findViewById(R.id.A_positive_issued_units);
        txt_A_positive_requests = findViewById(R.id.A_positive_requests_received);
        txt_A_positive_avg_issued = findViewById(R.id.A_positive_avg_blood_issued);
        txt_A_negative_received = findViewById(R.id.A_negative_received_units);
        txt_A_negative_issued = findViewById(R.id.A_negative_issued_units);
        txt_A_negative_requests = findViewById(R.id.A_negative_requests_received);
        txt_A_negative_avg_issued = findViewById(R.id.A_negative_avg_blood_issued);

        txt_AB_positive_received = findViewById(R.id.AB_positive_received_units);
        txt_AB_positive_issued = findViewById(R.id.AB_positive_issued_units);
        txt_AB_positive_requests = findViewById(R.id.AB_positive_requests_received);
        txt_AB_positive_avg_issued = findViewById(R.id.AB_positive_avg_blood_issued);
        txt_AB_negative_received = findViewById(R.id.AB_negative_received_units);
        txt_AB_negative_issued = findViewById(R.id.AB_negative_issued_units);
        txt_AB_negative_requests = findViewById(R.id.AB_negative_requests_received);
        txt_AB_negative_avg_issued = findViewById(R.id.AB_negative_avg_blood_issued);

        txt_B_positive_received = findViewById(R.id.B_positive_received_units);
        txt_B_positive_issued = findViewById(R.id.B_positive_issued_units);
        txt_B_positive_requests = findViewById(R.id.B_positive_requests_received);
        txt_B_positive_avg_issued = findViewById(R.id.B_positive_avg_blood_issued);
        txt_B_negative_received = findViewById(R.id.B_negative_received_units);
        txt_B_negative_issued = findViewById(R.id.B_negative_issued_units);
        txt_B_negative_requests = findViewById(R.id.B_negative_requests_received);
        txt_B_negative_avg_issued = findViewById(R.id.B_negative_avg_blood_issued);

        txt_O_positive_received = findViewById(R.id.O_positive_received_units);
        txt_O_positive_issued = findViewById(R.id.O_positive_issued_units);
        txt_O_positive_requests = findViewById(R.id.O_positive_requests_received);
        txt_O_positive_avg_issued = findViewById(R.id.O_positive_avg_blood_issued);
        txt_O_negative_received = findViewById(R.id.O_negative_received_units);
        txt_O_negative_issued = findViewById(R.id.O_negative_issued_units);
        txt_O_negative_requests = findViewById(R.id.O_negative_requests_received);
        txt_O_negative_avg_issued = findViewById(R.id.O_negative_avg_blood_issued);

        gridLayout = findViewById(R.id.blood_stock_analysis_grid);

        /* Get phone number from session */
        SessionManager sessionManager = new SessionManager(this, SessionManager.SESSION_USER_SESSION);
        HashMap<String, String> userDetails = sessionManager.getUserDetailFromSession();
        String phone = userDetails.get(SessionManager.KEY_PHONE_NO);
        Log.d("PHONE NUMBER : ", String.valueOf(phone));

        buttonSearch.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                txt_A_positive_received.setText("0.0");
                txt_A_positive_issued.setText("0.0");
                txt_A_positive_requests.setText("0");
                txt_A_positive_avg_issued.setText("0.0");
                txt_A_negative_received.setText("0.0");
                txt_A_negative_issued.setText("0.0");
                txt_A_negative_requests.setText("0");
                txt_A_negative_avg_issued.setText("0.0");

                txt_AB_positive_received.setText("0.0");
                txt_AB_positive_issued.setText("0.0");
                txt_AB_positive_requests.setText("0");
                txt_AB_positive_avg_issued.setText("0.0");
                txt_AB_negative_received.setText("0.0");
                txt_AB_negative_issued.setText("0.0");
                txt_AB_negative_requests.setText("0");
                txt_AB_negative_avg_issued.setText("0.0");

                txt_B_positive_received.setText("0.0");
                txt_B_positive_issued.setText("0.0");
                txt_B_positive_requests.setText("0");
                txt_B_positive_avg_issued.setText("0.0");
                txt_B_negative_received.setText("0.0");
                txt_B_negative_issued.setText("0.0");
                txt_B_negative_requests.setText("0");
                txt_B_negative_avg_issued.setText("0.0");

                txt_O_positive_received.setText("0.0");
                txt_O_positive_issued.setText("0.0");
                txt_O_positive_requests.setText("0");
                txt_O_positive_avg_issued.setText("0.0");
                txt_O_negative_received.setText("0.0");
                txt_O_negative_issued.setText("0.0");
                txt_O_negative_requests.setText("0");
                txt_O_negative_avg_issued.setText("0.0");

                int day = analysis_date.getDayOfMonth();
                int month = analysis_date.getMonth() + 1;
                int year = analysis_date.getYear();

                DateFormat outputFormat = new SimpleDateFormat("yyyy/MM/dd", Locale.US);
                DateFormat inputFormat = new SimpleDateFormat("yyyy/MM/dd", Locale.US);
                String analysisDate_ = "" + year + "/" + month + "/" + day;
                Log.d("analysisDate_ : ", String.valueOf(analysisDate_));

                Date date = null;
                try {
                    date = inputFormat.parse(analysisDate_);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                String formattedDate = outputFormat.format(date);

                FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                DatabaseReference databaseReference1 = firebaseDatabase.getReference("facilitator");
                Query checkFacilitatorDistrict = databaseReference1.orderByChild("mobile").equalTo(phone);

                checkFacilitatorDistrict.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            String facilitatorDistrict = snapshot.child(phone).child("district").getValue(String.class);
                            Log.d("facilitatorDistrict : ", String.valueOf(facilitatorDistrict));

                            /* Blood Issued */
                            DatabaseReference databaseReference1 = firebaseDatabase.getReference("bloodIssuedRegistry");
                            Query query1 = databaseReference1.orderByChild("district").equalTo(facilitatorDistrict);
                            query1.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if (snapshot.exists()) {
                                        bloodIssuedDistrict = "true";
                                        DatabaseReference databaseReference = firebaseDatabase.getReference("bloodIssuedRegistry");
                                        Query query = databaseReference.orderByChild("date").equalTo(formattedDate);
                                        query.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                if (snapshot.exists()) {
                                                    bloodIssuedDistrict = "true";
                                                    bloodIssuedDate = "true";
                                                    DatabaseReference databaseReference = firebaseDatabase.getReference("bloodIssuedRegistry");
                                                    Query query = databaseReference.orderByChild("reason").equalTo("Blood Issue");
                                                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                            if (snapshot.exists()) {
                                                                double sum_A_positive = 0;
                                                                double sum_A_negative = 0;
                                                                double sum_B_positive = 0;
                                                                double sum_B_negative = 0;
                                                                double sum_AB_positive = 0;
                                                                double sum_AB_negative = 0;
                                                                double sum_O_positive = 0;
                                                                double sum_O_negative = 0;

                                                                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                                                    Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();
                                                                    Object bloodUnit_var = map.get("bloodGroup");
                                                                    Object date_var = map.get("date");
                                                                    Object district_var = map.get("district");

                                                                    if (bloodUnit_var.equals("A+") && date_var.equals(formattedDate) && district_var.equals(facilitatorDistrict)) {
                                                                        Object units_A_positive = map.get("issuedUnit");
                                                                        double formattedUnits = Double.valueOf(String.valueOf(units_A_positive));
                                                                        sum_A_positive += formattedUnits;
                                                                        txt_A_positive_issued.setText(String.valueOf(sum_A_positive));
                                                                    } else if (bloodUnit_var.equals("A-") && date_var.equals(formattedDate) && district_var.equals(facilitatorDistrict)) {
                                                                        Object units_A_negative = map.get("issuedUnit");
                                                                        double formattedUnits = Double.valueOf(String.valueOf(units_A_negative));
                                                                        sum_A_negative += formattedUnits;
                                                                        txt_A_negative_issued.setText(String.valueOf(sum_A_negative));
                                                                    } else if (bloodUnit_var.equals("B+") && date_var.equals(formattedDate) && district_var.equals(facilitatorDistrict)) {
                                                                        Object units_B_positive = map.get("issuedUnit");
                                                                        double formattedUnits = Double.valueOf(String.valueOf(units_B_positive));
                                                                        sum_B_positive += formattedUnits;
                                                                        txt_B_positive_issued.setText(String.valueOf(sum_B_positive));
                                                                    } else if (bloodUnit_var.equals("B-") && date_var.equals(formattedDate) && district_var.equals(facilitatorDistrict)) {
                                                                        Object units_B_negative = map.get("issuedUnit");
                                                                        double formattedUnits = Double.valueOf(String.valueOf(units_B_negative));
                                                                        sum_B_negative += formattedUnits;
                                                                        txt_B_negative_issued.setText(String.valueOf(sum_B_negative));
                                                                    } else if (bloodUnit_var.equals("AB+") && date_var.equals(formattedDate) && district_var.equals(facilitatorDistrict)) {
                                                                        Object units_AB_positive = map.get("issuedUnit");
                                                                        double formattedUnits = Double.valueOf(String.valueOf(units_AB_positive));
                                                                        sum_AB_positive += formattedUnits;
                                                                        txt_AB_positive_issued.setText(String.valueOf(sum_AB_positive));
                                                                    } else if (bloodUnit_var.equals("AB-") && date_var.equals(formattedDate) && district_var.equals(facilitatorDistrict)) {
                                                                        Object units_AB_negative = map.get("issuedUnit");
                                                                        double formattedUnits = Double.valueOf(String.valueOf(units_AB_negative));
                                                                        sum_AB_negative += formattedUnits;
                                                                        txt_AB_negative_issued.setText(String.valueOf(sum_AB_negative));
                                                                    } else if (bloodUnit_var.equals("O+") && date_var.equals(formattedDate) && district_var.equals(facilitatorDistrict)) {
                                                                        Object units_O_positive = map.get("issuedUnit");
                                                                        double formattedUnits = Double.valueOf(String.valueOf(units_O_positive));
                                                                        sum_O_positive += formattedUnits;
                                                                        txt_O_positive_issued.setText(String.valueOf(sum_O_positive));
                                                                    } else if (bloodUnit_var.equals("O-") && date_var.equals(formattedDate) && district_var.equals(facilitatorDistrict)) {
                                                                        Object units_O_negative = map.get("issuedUnit");
                                                                        double formattedUnits = Double.valueOf(String.valueOf(units_O_negative));
                                                                        sum_O_negative += formattedUnits;
                                                                        txt_O_negative_issued.setText(String.valueOf(sum_O_negative));
                                                                    }
                                                                }
                                                            }
                                                        }

                                                        @Override
                                                        public void onCancelled(@NonNull DatabaseError error) {

                                                        }
                                                    });
                                                } else {
                                                    bloodIssuedDistrict = "true";
                                                    bloodIssuedDate = "false";
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull @NotNull DatabaseError error) {

                                            }
                                        });
                                    } else {
                                        bloodIssuedDistrict = "false";
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull @NotNull DatabaseError error) {

                                }
                            });

                            /* Blood Received */
                            DatabaseReference databaseReference2 = firebaseDatabase.getReference("bloodReceivedRegistry");
                            Query query2 = databaseReference2.orderByChild("donatedDistrict").equalTo(facilitatorDistrict);
                            query2.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if (snapshot.exists()) {
                                        bloodReceivedDistrict = "true";
                                        DatabaseReference databaseReference = firebaseDatabase.getReference("bloodReceivedRegistry");
                                        Query query = databaseReference.orderByChild("date").equalTo(formattedDate);
                                        query.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                if (snapshot.exists()) {
                                                    bloodReceivedDistrict = "true";
                                                    bloodReceivedDate = "true";
                                                    double sum_A_positive = 0;
                                                    double sum_A_negative = 0;
                                                    double sum_B_positive = 0;
                                                    double sum_B_negative = 0;
                                                    double sum_AB_positive = 0;
                                                    double sum_AB_negative = 0;
                                                    double sum_O_positive = 0;
                                                    double sum_O_negative = 0;

                                                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                                        Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();
                                                        Object bloodUnit_var = map.get("bloodGroup");
                                                        Object date_var = map.get("date");
                                                        Object district_var = map.get("donatedDistrict");

                                                        if (bloodUnit_var.equals("A+") && date_var.equals(formattedDate) && district_var.equals(facilitatorDistrict)) {
                                                            Object units_A_positive = map.get("receivedUnits");
                                                            double formattedUnits = Double.valueOf(String.valueOf(units_A_positive));
                                                            sum_A_positive += formattedUnits;
                                                            txt_A_positive_received.setText(String.valueOf(sum_A_positive));
                                                        } else if (bloodUnit_var.equals("A-") && date_var.equals(formattedDate) && district_var.equals(facilitatorDistrict)) {
                                                            Object units_A_negative = map.get("receivedUnits");
                                                            double formattedUnits = Double.valueOf(String.valueOf(units_A_negative));
                                                            sum_A_negative += formattedUnits;
                                                            txt_A_negative_received.setText(String.valueOf(sum_A_negative));
                                                        } else if (bloodUnit_var.equals("B+") && date_var.equals(formattedDate) && district_var.equals(facilitatorDistrict)) {
                                                            Object units_B_positive = map.get("receivedUnits");
                                                            double formattedUnits = Double.valueOf(String.valueOf(units_B_positive));
                                                            sum_B_positive += formattedUnits;
                                                            txt_B_positive_received.setText(String.valueOf(sum_B_positive));
                                                        } else if (bloodUnit_var.equals("B-") && date_var.equals(formattedDate) && district_var.equals(facilitatorDistrict)) {
                                                            Object units_B_negative = map.get("receivedUnits");
                                                            double formattedUnits = Double.valueOf(String.valueOf(units_B_negative));
                                                            sum_B_negative += formattedUnits;
                                                            txt_B_negative_received.setText(String.valueOf(sum_B_negative));
                                                        } else if (bloodUnit_var.equals("AB+") && date_var.equals(formattedDate) && district_var.equals(facilitatorDistrict)) {
                                                            Object units_AB_positive = map.get("receivedUnits");
                                                            double formattedUnits = Double.valueOf(String.valueOf(units_AB_positive));
                                                            sum_AB_positive += formattedUnits;
                                                            txt_AB_positive_received.setText(String.valueOf(sum_AB_positive));
                                                        } else if (bloodUnit_var.equals("AB-") && date_var.equals(formattedDate) && district_var.equals(facilitatorDistrict)) {
                                                            Object units_AB_negative = map.get("receivedUnits");
                                                            double formattedUnits = Double.valueOf(String.valueOf(units_AB_negative));
                                                            sum_AB_negative += formattedUnits;
                                                            txt_AB_negative_received.setText(String.valueOf(sum_AB_negative));
                                                        } else if (bloodUnit_var.equals("O+") && date_var.equals(formattedDate) && district_var.equals(facilitatorDistrict)) {
                                                            Object units_O_positive = map.get("receivedUnits");
                                                            double formattedUnits = Double.valueOf(String.valueOf(units_O_positive));
                                                            sum_O_positive += formattedUnits;
                                                            txt_O_positive_received.setText(String.valueOf(sum_O_positive));
                                                        } else if (bloodUnit_var.equals("O-") && date_var.equals(formattedDate) && district_var.equals(facilitatorDistrict)) {
                                                            Object units_O_negative = map.get("receivedUnits");
                                                            double formattedUnits = Double.valueOf(String.valueOf(units_O_negative));
                                                            sum_O_negative += formattedUnits;
                                                            txt_O_negative_received.setText(String.valueOf(sum_O_negative));
                                                        }
                                                    }
                                                } else {
                                                    bloodReceivedDistrict = "true";
                                                    bloodReceivedDate = "false";
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull @NotNull DatabaseError error) {

                                            }
                                        });
                                    } else {
                                        bloodReceivedDistrict = "false";
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull @NotNull DatabaseError error) {

                                }
                            });

                            /* Blood Requested */
                            DatabaseReference databaseReference3 = firebaseDatabase.getReference("request");
                            Query query3 = databaseReference3.orderByChild("district").equalTo(facilitatorDistrict);
                            query3.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if (snapshot.exists()) {
                                        bloodRequestDistrict = "true";
                                        DatabaseReference databaseReference = firebaseDatabase.getReference("request");
                                        Query query = databaseReference.orderByChild("date").equalTo(formattedDate);
                                        query.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                if (snapshot.exists()) {
                                                    bloodRequestDate = "true";
                                                    int sum_A_positive = 0;
                                                    int sum_A_negative = 0;
                                                    int sum_B_positive = 0;
                                                    int sum_B_negative = 0;
                                                    int sum_AB_positive = 0;
                                                    int sum_AB_negative = 0;
                                                    int sum_O_positive = 0;
                                                    int sum_O_negative = 0;

                                                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                                        Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();
                                                        Object bloodUnit_var = map.get("bloodType");
                                                        Object date_var = map.get("date");
                                                        Object district_var = map.get("district");

                                                        if (bloodUnit_var.equals("A+") && date_var.equals(formattedDate) && district_var.equals(facilitatorDistrict)) {
                                                            sum_A_positive += 1;
                                                            txt_A_positive_requests.setText(String.valueOf(sum_A_positive));
                                                        } else if (bloodUnit_var.equals("A-") && date_var.equals(formattedDate) && district_var.equals(facilitatorDistrict)) {
                                                            sum_A_negative += 1;
                                                            txt_A_negative_requests.setText(String.valueOf(sum_A_negative));
                                                        } else if (bloodUnit_var.equals("B+") && date_var.equals(formattedDate) && district_var.equals(facilitatorDistrict)) {
                                                            sum_B_positive += 1;
                                                            txt_B_positive_requests.setText(String.valueOf(sum_B_positive));
                                                        } else if (bloodUnit_var.equals("B-") && date_var.equals(formattedDate) && district_var.equals(facilitatorDistrict)) {
                                                            sum_B_negative += 1;
                                                            txt_B_negative_requests.setText(String.valueOf(sum_B_negative));
                                                        } else if (bloodUnit_var.equals("AB+") && date_var.equals(formattedDate) && district_var.equals(facilitatorDistrict)) {
                                                            sum_AB_positive += 1;
                                                            txt_AB_positive_requests.setText(String.valueOf(sum_AB_positive));
                                                        } else if (bloodUnit_var.equals("AB-") && date_var.equals(formattedDate) && district_var.equals(facilitatorDistrict)) {
                                                            sum_AB_negative += 1;
                                                            txt_AB_negative_requests.setText(String.valueOf(sum_AB_negative));
                                                        } else if (bloodUnit_var.equals("O+") && date_var.equals(formattedDate) && district_var.equals(facilitatorDistrict)) {
                                                            sum_O_positive += 1;
                                                            txt_O_positive_requests.setText(String.valueOf(sum_O_positive));
                                                        } else if (bloodUnit_var.equals("O-") && date_var.equals(formattedDate) && district_var.equals(facilitatorDistrict)) {
                                                            sum_O_negative += 1;
                                                            txt_O_negative_requests.setText(String.valueOf(sum_O_negative));
                                                        }
                                                    }
                                                } else {
                                                    bloodRequestDistrict = "true";
                                                    bloodRequestDate = "false";
                                                    errors();
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull @NotNull DatabaseError error) {

                                            }
                                        });
                                    } else {
                                        bloodRequestDistrict = "false";
                                        errors();
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull @NotNull DatabaseError error) {

                                }
                            });

                            /* Blood issued average per request */
                            DatabaseReference databaseReference4 = firebaseDatabase.getReference("request");
                            Query query4 = databaseReference4.orderByChild("district").equalTo(facilitatorDistrict);
                            query4.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if (snapshot.exists()) {
                                        DatabaseReference databaseReference = firebaseDatabase.getReference("request");
                                        Query query = databaseReference.orderByChild("date").equalTo(formattedDate);
                                        query.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                if (snapshot.exists()) {
                                                    int sum_A_positive_requests = 0;
                                                    int sum_A_negative_requests = 0;
                                                    int sum_B_positive_requests = 0;
                                                    int sum_B_negative_requests = 0;
                                                    int sum_AB_positive_requests = 0;
                                                    int sum_AB_negative_requests = 0;
                                                    int sum_O_positive_requests = 0;
                                                    int sum_O_negative_requests = 0;

                                                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                                        Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();
                                                        Object bloodUnit_var = map.get("bloodType");
                                                        Object date_var = map.get("date");
                                                        Object district_var = map.get("district");

                                                        if (bloodUnit_var.equals("A+") && date_var.equals(formattedDate) && district_var.equals(facilitatorDistrict)) {
                                                            sum_A_positive_requests += 1;
                                                        } else if (bloodUnit_var.equals("A-") && date_var.equals(formattedDate) && district_var.equals(facilitatorDistrict)) {
                                                            sum_A_negative_requests += 1;
                                                        } else if (bloodUnit_var.equals("B+") && date_var.equals(formattedDate) && district_var.equals(facilitatorDistrict)) {
                                                            sum_B_positive_requests += 1;
                                                        } else if (bloodUnit_var.equals("B-") && date_var.equals(formattedDate) && district_var.equals(facilitatorDistrict)) {
                                                            sum_B_negative_requests += 1;
                                                        } else if (bloodUnit_var.equals("AB+") && date_var.equals(formattedDate) && district_var.equals(facilitatorDistrict)) {
                                                            sum_AB_positive_requests += 1;
                                                        } else if (bloodUnit_var.equals("AB-") && date_var.equals(formattedDate) && district_var.equals(facilitatorDistrict)) {
                                                            sum_AB_negative_requests += 1;
                                                        } else if (bloodUnit_var.equals("O+") && date_var.equals(formattedDate) && district_var.equals(facilitatorDistrict)) {
                                                            sum_O_positive_requests += 1;
                                                        } else if (bloodUnit_var.equals("O-") && date_var.equals(formattedDate) && district_var.equals(facilitatorDistrict)) {
                                                            sum_O_negative_requests += 1;
                                                        }
                                                    }

                                                    /* Blood Issued */
                                                    DatabaseReference databaseReference1 = firebaseDatabase.getReference("bloodIssuedRegistry");
                                                    Query query1 = databaseReference1.orderByChild("district").equalTo(facilitatorDistrict);
                                                    int finalSum_A_positive_requests = sum_A_positive_requests;
                                                    int finalSum_A_negative_requests = sum_A_negative_requests;
                                                    int finalSum_AB_positive_requests = sum_AB_positive_requests;
                                                    int finalSum_AB_negative_requests = sum_AB_negative_requests;
                                                    int finalSum_B_positive_requests = sum_B_positive_requests;
                                                    int finalSum_B_negative_requests = sum_B_negative_requests;
                                                    int finalSum_O_positive_requests = sum_O_positive_requests;
                                                    int finalSum_O_negative_requests = sum_O_negative_requests;

                                                    query1.addListenerForSingleValueEvent(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                            if (snapshot.exists()) {
                                                                DatabaseReference databaseReference = firebaseDatabase.getReference("bloodIssuedRegistry");
                                                                Query query = databaseReference.orderByChild("date").equalTo(formattedDate);
                                                                query.addListenerForSingleValueEvent(new ValueEventListener() {
                                                                    @Override
                                                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                        if (snapshot.exists()) {
                                                                            DatabaseReference databaseReference = firebaseDatabase.getReference("bloodIssuedRegistry");
                                                                            Query query = databaseReference.orderByChild("reason").equalTo("Blood Issue");
                                                                            query.addListenerForSingleValueEvent(new ValueEventListener() {
                                                                                @Override
                                                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                                    if (snapshot.exists()) {
                                                                                        double sum_A_positive_avg = 0;
                                                                                        double sum_A_negative_avg = 0;
                                                                                        double sum_B_positive_avg = 0;
                                                                                        double sum_B_negative_avg = 0;
                                                                                        double sum_AB_positive_avg = 0;
                                                                                        double sum_AB_negative_avg = 0;
                                                                                        double sum_O_positive_avg = 0;
                                                                                        double sum_O_negative_avg = 0;

                                                                                        double sum_A_positive = 0;
                                                                                        double sum_A_negative = 0;
                                                                                        double sum_B_positive = 0;
                                                                                        double sum_B_negative = 0;
                                                                                        double sum_AB_positive = 0;
                                                                                        double sum_AB_negative = 0;
                                                                                        double sum_O_positive = 0;
                                                                                        double sum_O_negative = 0;

                                                                                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                                                                            Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();
                                                                                            Object bloodUnit_var = map.get("bloodGroup");
                                                                                            Object date_var = map.get("date");
                                                                                            Object district_var = map.get("district");

                                                                                            if (bloodUnit_var.equals("A+") && date_var.equals(formattedDate) && district_var.equals(facilitatorDistrict)) {
                                                                                                Object units_A_positive = map.get("issuedUnit");
                                                                                                double formattedUnits = Double.valueOf(String.valueOf(units_A_positive));
                                                                                                sum_A_positive += formattedUnits;

                                                                                                if (finalSum_A_positive_requests >= 1) {
                                                                                                    sum_A_positive_avg = sum_A_positive / finalSum_A_positive_requests;
                                                                                                    txt_A_positive_avg_issued.setText(String.valueOf(sum_A_positive_avg));
                                                                                                }
                                                                                            } else if (bloodUnit_var.equals("A-") && date_var.equals(formattedDate) && district_var.equals(facilitatorDistrict)) {
                                                                                                Object units_A_negative = map.get("issuedUnit");
                                                                                                double formattedUnits = Double.valueOf(String.valueOf(units_A_negative));
                                                                                                sum_A_negative += formattedUnits;

                                                                                                if (finalSum_A_negative_requests >= 1) {
                                                                                                    sum_A_negative_avg = sum_A_negative / finalSum_A_negative_requests;
                                                                                                    txt_A_negative_avg_issued.setText(String.valueOf(sum_A_negative_avg));
                                                                                                }
                                                                                            } else if (bloodUnit_var.equals("B+") && date_var.equals(formattedDate) && district_var.equals(facilitatorDistrict)) {
                                                                                                Object units_B_positive = map.get("issuedUnit");
                                                                                                double formattedUnits = Double.valueOf(String.valueOf(units_B_positive));
                                                                                                sum_B_positive += formattedUnits;

                                                                                                if (finalSum_B_positive_requests >= 1) {
                                                                                                    sum_B_positive_avg = sum_B_positive / finalSum_B_positive_requests;
                                                                                                    txt_B_positive_avg_issued.setText(String.valueOf(sum_B_positive_avg));
                                                                                                }
                                                                                            } else if (bloodUnit_var.equals("B-") && date_var.equals(formattedDate) && district_var.equals(facilitatorDistrict)) {
                                                                                                Object units_B_negative = map.get("issuedUnit");
                                                                                                double formattedUnits = Double.valueOf(String.valueOf(units_B_negative));
                                                                                                sum_B_negative += formattedUnits;

                                                                                                if (finalSum_B_negative_requests >= 1) {
                                                                                                    sum_B_negative_avg = sum_B_negative / finalSum_B_negative_requests;
                                                                                                    txt_B_negative_avg_issued.setText(String.valueOf(sum_B_negative_avg));
                                                                                                }
                                                                                            } else if (bloodUnit_var.equals("AB+") && date_var.equals(formattedDate) && district_var.equals(facilitatorDistrict)) {
                                                                                                Object units_AB_positive = map.get("issuedUnit");
                                                                                                double formattedUnits = Double.valueOf(String.valueOf(units_AB_positive));
                                                                                                sum_AB_positive += formattedUnits;

                                                                                                if (finalSum_AB_positive_requests >= 1) {
                                                                                                    sum_AB_positive_avg = sum_AB_positive / finalSum_AB_positive_requests;
                                                                                                    txt_AB_positive_avg_issued.setText(String.valueOf(sum_AB_positive_avg));
                                                                                                }
                                                                                            } else if (bloodUnit_var.equals("AB-") && date_var.equals(formattedDate) && district_var.equals(facilitatorDistrict)) {
                                                                                                Object units_AB_negative = map.get("issuedUnit");
                                                                                                double formattedUnits = Double.valueOf(String.valueOf(units_AB_negative));
                                                                                                sum_AB_negative += formattedUnits;

                                                                                                if (finalSum_AB_negative_requests >= 1) {
                                                                                                    sum_AB_negative_avg = sum_AB_negative / finalSum_AB_negative_requests;
                                                                                                    txt_AB_negative_avg_issued.setText(String.valueOf(sum_AB_negative_avg));
                                                                                                }
                                                                                            } else if (bloodUnit_var.equals("O+") && date_var.equals(formattedDate) && district_var.equals(facilitatorDistrict)) {
                                                                                                Object units_O_positive = map.get("issuedUnit");
                                                                                                double formattedUnits = Double.valueOf(String.valueOf(units_O_positive));
                                                                                                sum_O_positive += formattedUnits;

                                                                                                if (finalSum_O_positive_requests >= 1) {
                                                                                                    sum_O_positive_avg = sum_O_positive / finalSum_O_positive_requests;
                                                                                                    txt_O_positive_avg_issued.setText(String.valueOf(sum_O_positive_avg));
                                                                                                }
                                                                                            } else if (bloodUnit_var.equals("O-") && date_var.equals(formattedDate) && district_var.equals(facilitatorDistrict)) {
                                                                                                Object units_O_negative = map.get("issuedUnit");
                                                                                                double formattedUnits = Double.valueOf(String.valueOf(units_O_negative));
                                                                                                sum_O_negative += formattedUnits;

                                                                                                if (finalSum_O_negative_requests >= 1) {
                                                                                                    sum_O_negative_avg = sum_O_negative / finalSum_O_negative_requests;
                                                                                                    txt_O_negative_avg_issued.setText(String.valueOf(sum_O_negative_avg));
                                                                                                }
                                                                                            }
                                                                                        }
                                                                                    }
                                                                                }

                                                                                @Override
                                                                                public void onCancelled(@NonNull DatabaseError error) {

                                                                                }
                                                                            });
                                                                        }
                                                                    }

                                                                    @Override
                                                                    public void onCancelled(@NonNull @NotNull DatabaseError error) {

                                                                    }
                                                                });
                                                            }
                                                        }

                                                        @Override
                                                        public void onCancelled(@NonNull @NotNull DatabaseError error) {

                                                        }
                                                    });
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull @NotNull DatabaseError error) {

                                            }
                                        });
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull @NotNull DatabaseError error) {

                                }
                            });

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull @NotNull DatabaseError error) {

                    }
                });

            }
        });

        backImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), FacilitatorLeftNavActivity.class);
                setResult(Activity.RESULT_CANCELED, intent);
                finish();

            }
        });
    }

    private void errors() {
        /* Errors */
        if (bloodIssuedDistrict.equals("false") && bloodReceivedDistrict.equals("false") && bloodRequestDistrict.equals("false")) {
            Toast.makeText(getApplicationContext(), "No data available for your district!", Toast.LENGTH_SHORT).show();
        }
        if (bloodIssuedDate.equals("false") && bloodReceivedDate.equals("false") && bloodRequestDate.equals("false")) {
            Toast.makeText(getApplicationContext(), "No data available for provided date!", Toast.LENGTH_SHORT).show();
        }
    }


    public void moveToHomeScreen(View view) {
        Intent intent = new Intent(getApplicationContext(), LeftNavActivity.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {

    }

}