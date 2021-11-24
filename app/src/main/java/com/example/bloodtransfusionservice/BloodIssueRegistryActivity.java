package com.example.bloodtransfusionservice;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class BloodIssueRegistryActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    GridLayout gridLayout;
    BloodIssueHolder bloodIssueHolder;
    ArrayList<storeBloodIssueData> list;
    DatePicker issue_date;
    Button buttonSearch;
    ImageView backImg;
    TextView txt_A_positive, txt_A_negative, txt_B_positive, txt_B_negative, txt_AB_positive, txt_AB_negative, txt_O_positive, txt_O_negative;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blood_issue_registry);

        issue_date = findViewById(R.id.input_blood_issue_date);
        buttonSearch = findViewById(R.id.btn_search);
        backImg = findViewById(R.id.img_back);
        txt_A_positive = findViewById(R.id.txt_A_positive_units);
        txt_A_negative = findViewById(R.id.txt_A_negative_units);
        txt_B_positive = findViewById(R.id.txt_B_positive_units);
        txt_B_negative = findViewById(R.id.txt_B_negative_units);
        txt_AB_positive = findViewById(R.id.txt_AB_positive_units);
        txt_AB_negative = findViewById(R.id.txt_AB_negative_units);
        txt_O_positive = findViewById(R.id.txt_O_positive_units);
        txt_O_negative = findViewById(R.id.txt_O_negative_units);
        recyclerView = findViewById(R.id.blood_issue_registry);
        gridLayout = findViewById(R.id.blood_issue_grid);

        /* Get phone number from session */
        SessionManager sessionManager = new SessionManager(this, SessionManager.SESSION_USER_SESSION);
        HashMap<String, String> userDetails = sessionManager.getUserDetailFromSession();
        String phone = userDetails.get(SessionManager.KEY_PHONE_NO);
        Log.d("PHONE NUMBER : ", String.valueOf(phone));

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        list = new ArrayList<>();
        bloodIssueHolder = new BloodIssueHolder(this, list);
        recyclerView.setAdapter(bloodIssueHolder);

        buttonSearch.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                txt_A_positive.setText("0.0");
                txt_A_negative.setText("0.0");
                txt_B_positive.setText("0.0");
                txt_B_negative.setText("0.0");
                txt_AB_positive.setText("0.0");
                txt_AB_negative.setText("0.0");
                txt_O_positive.setText("0.0");
                txt_O_negative.setText("0.0");

                recyclerView.removeAllViewsInLayout();
                recyclerView.setVisibility(View.INVISIBLE);
                list.clear();

                int day = issue_date.getDayOfMonth();
                int month = issue_date.getMonth() + 1;
                int year = issue_date.getYear();

                DateFormat outputFormat = new SimpleDateFormat("yyyy/MM/dd", Locale.US);
                DateFormat inputFormat = new SimpleDateFormat("yyyy/MM/dd", Locale.US);
                String issueDate_ = "" + year + "/" + month + "/" + day;
                Log.d("issueDate_ : ", String.valueOf(issueDate_));

                Date date = null;
                try {
                    date = inputFormat.parse(issueDate_);
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

                            DatabaseReference databaseReference = firebaseDatabase.getReference("bloodIssuedRegistry");
                            Query query = databaseReference.orderByChild("district").equalTo(facilitatorDistrict);
                            query.addListenerForSingleValueEvent(new ValueEventListener() {
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
                                                                        txt_A_positive.setText(String.valueOf(sum_A_positive));
                                                                    } else if (bloodUnit_var.equals("A-") && date_var.equals(formattedDate) && district_var.equals(facilitatorDistrict)) {
                                                                        Object units_A_negative = map.get("issuedUnit");
                                                                        double formattedUnits = Double.valueOf(String.valueOf(units_A_negative));
                                                                        sum_A_negative += formattedUnits;
                                                                        txt_A_negative.setText(String.valueOf(sum_A_negative));
                                                                    } else if (bloodUnit_var.equals("B+") && date_var.equals(formattedDate) && district_var.equals(facilitatorDistrict)) {
                                                                        Object units_B_positive = map.get("issuedUnit");
                                                                        double formattedUnits = Double.valueOf(String.valueOf(units_B_positive));
                                                                        sum_B_positive += formattedUnits;
                                                                        txt_B_positive.setText(String.valueOf(sum_B_positive));
                                                                    } else if (bloodUnit_var.equals("B-") && date_var.equals(formattedDate) && district_var.equals(facilitatorDistrict)) {
                                                                        Object units_B_negative = map.get("issuedUnit");
                                                                        double formattedUnits = Double.valueOf(String.valueOf(units_B_negative));
                                                                        sum_B_negative += formattedUnits;
                                                                        txt_B_negative.setText(String.valueOf(sum_B_negative));
                                                                    } else if (bloodUnit_var.equals("AB+") && date_var.equals(formattedDate) && district_var.equals(facilitatorDistrict)) {
                                                                        Object units_AB_positive = map.get("issuedUnit");
                                                                        double formattedUnits = Double.valueOf(String.valueOf(units_AB_positive));
                                                                        sum_AB_positive += formattedUnits;
                                                                        txt_AB_positive.setText(String.valueOf(sum_AB_positive));
                                                                    } else if (bloodUnit_var.equals("AB-") && date_var.equals(formattedDate) && district_var.equals(facilitatorDistrict)) {
                                                                        Object units_AB_negative = map.get("issuedUnit");
                                                                        double formattedUnits = Double.valueOf(String.valueOf(units_AB_negative));
                                                                        sum_AB_negative += formattedUnits;
                                                                        txt_AB_negative.setText(String.valueOf(sum_AB_negative));
                                                                    } else if (bloodUnit_var.equals("O+") && date_var.equals(formattedDate) && district_var.equals(facilitatorDistrict)) {
                                                                        Object units_O_positive = map.get("issuedUnit");
                                                                        double formattedUnits = Double.valueOf(String.valueOf(units_O_positive));
                                                                        sum_O_positive += formattedUnits;
                                                                        txt_O_positive.setText(String.valueOf(sum_O_positive));
                                                                    } else if (bloodUnit_var.equals("O-") && date_var.equals(formattedDate) && district_var.equals(facilitatorDistrict)) {
                                                                        Object units_O_negative = map.get("issuedUnit");
                                                                        double formattedUnits = Double.valueOf(String.valueOf(units_O_negative));
                                                                        sum_O_negative += formattedUnits;
                                                                        txt_O_negative.setText(String.valueOf(sum_O_negative));
                                                                    }
                                                                }
                                                            } else {
                                                                Toast.makeText(getApplicationContext(), "No data available for blood issue", Toast.LENGTH_SHORT).show();
                                                            }
                                                        }

                                                        @Override
                                                        public void onCancelled(@NonNull DatabaseError error) {

                                                        }
                                                    });
                                                } else {
                                                    Toast.makeText(getApplicationContext(), "No data available for provided date!", Toast.LENGTH_SHORT).show();
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull @NotNull DatabaseError error) {

                                            }
                                        });
                                    } else {
                                        Toast.makeText(getApplicationContext(), "No data available for your district!", Toast.LENGTH_SHORT).show();
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

    public void moveToHomeScreen(View view) {
        Intent intent = new Intent(getApplicationContext(), LeftNavActivity.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {

    }

}