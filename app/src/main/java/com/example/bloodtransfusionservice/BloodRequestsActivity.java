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

public class BloodRequestsActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    GridLayout gridLayout;
    BloodRequestHolder bloodRequestHolder;
    ArrayList<storeBloodRequestData> list;
    DatePicker request_date;
    Button buttonSearch;
    ImageView backImg;
    TextView txt_A_positive, txt_A_negative, txt_B_positive, txt_B_negative, txt_AB_positive, txt_AB_negative, txt_O_positive, txt_O_negative;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blood_requests);
        request_date = findViewById(R.id.input_blood_request_date);
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
        recyclerView = findViewById(R.id.blood_request_registry);
        gridLayout = findViewById(R.id.blood_request_grid);

        /* Get phone number from session */
        SessionManager sessionManager = new SessionManager(this, SessionManager.SESSION_USER_SESSION);
        HashMap<String, String> userDetails = sessionManager.getUserDetailFromSession();
        String phone = userDetails.get(SessionManager.KEY_PHONE_NO);
        Log.d("PHONE NUMBER : ", String.valueOf(phone));

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        list = new ArrayList<>();
        bloodRequestHolder = new BloodRequestHolder(this, list);
        recyclerView.setAdapter(bloodRequestHolder);

        buttonSearch.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                txt_A_positive.setText("0");
                txt_A_negative.setText("0");
                txt_B_positive.setText("0");
                txt_B_negative.setText("0");
                txt_AB_positive.setText("0");
                txt_AB_negative.setText("0");
                txt_O_positive.setText("0");
                txt_O_negative.setText("0");

                recyclerView.removeAllViewsInLayout();
                recyclerView.setVisibility(View.INVISIBLE);
                list.clear();
                int day = request_date.getDayOfMonth();
                int month = request_date.getMonth() + 1;
                int year = request_date.getYear();

                DateFormat outputFormat = new SimpleDateFormat("yyyy/MM/dd", Locale.US);
                DateFormat inputFormat = new SimpleDateFormat("yyyy/MM/dd", Locale.US);
                String requestDate_ = "" + year + "/" + month + "/" + day;
                Log.d("requestDate_ : ", String.valueOf(requestDate_));

                Date date = null;
                try {
                    date = inputFormat.parse(requestDate_);
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

                            DatabaseReference databaseReference = firebaseDatabase.getReference("request");
                            Query query = databaseReference.orderByChild("district").equalTo(facilitatorDistrict);
                            query.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if (snapshot.exists()) {
                                        DatabaseReference databaseReference = firebaseDatabase.getReference("request");
                                        Query query = databaseReference.orderByChild("date").equalTo(formattedDate);
                                        query.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                if (snapshot.exists()) {
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
                                                            txt_A_positive.setText(String.valueOf(sum_A_positive));
                                                        } else if (bloodUnit_var.equals("A-") && date_var.equals(formattedDate) && district_var.equals(facilitatorDistrict)) {
                                                            sum_A_negative += 1;
                                                            txt_A_negative.setText(String.valueOf(sum_A_negative));
                                                        } else if (bloodUnit_var.equals("B+") && date_var.equals(formattedDate) && district_var.equals(facilitatorDistrict)) {
                                                            sum_B_positive += 1;
                                                            txt_B_positive.setText(String.valueOf(sum_B_positive));
                                                        } else if (bloodUnit_var.equals("B-") && date_var.equals(formattedDate) && district_var.equals(facilitatorDistrict)) {
                                                            sum_B_negative += 1;
                                                            txt_B_negative.setText(String.valueOf(sum_B_negative));
                                                        } else if (bloodUnit_var.equals("AB+") && date_var.equals(formattedDate) && district_var.equals(facilitatorDistrict)) {
                                                            sum_AB_positive += 1;
                                                            txt_AB_positive.setText(String.valueOf(sum_AB_positive));
                                                        } else if (bloodUnit_var.equals("AB-") && date_var.equals(formattedDate) && district_var.equals(facilitatorDistrict)) {
                                                            sum_AB_negative += 1;
                                                            txt_AB_negative.setText(String.valueOf(sum_AB_negative));
                                                        } else if (bloodUnit_var.equals("O+") && date_var.equals(formattedDate) && district_var.equals(facilitatorDistrict)) {
                                                            sum_O_positive += 1;
                                                            txt_O_positive.setText(String.valueOf(sum_O_positive));
                                                        } else if (bloodUnit_var.equals("O-") && date_var.equals(formattedDate) && district_var.equals(facilitatorDistrict)) {
                                                            sum_O_negative += 1;
                                                            txt_O_negative.setText(String.valueOf(sum_O_negative));
                                                        }
                                                    }
                                                } else {
                                                    Toast.makeText(getApplicationContext(), "No data available for selected date!", Toast.LENGTH_SHORT).show();
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