package com.example.bloodtransfusionservice;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
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
import java.util.concurrent.TimeUnit;

public class ViewRequestBloodActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    GridLayout gridLayout;
    BloodStockHolder bloodStockHolder;
    ArrayList<storeStockData> list;
    ImageView backImg;
    EditText txt_A_positive, txt_A_negative, txt_B_positive, txt_B_negative, txt_AB_positive, txt_AB_negative, txt_O_positive, txt_O_negative;
    TextView txt_A_positive_request, txt_A_negative_request, txt_B_positive_request, txt_B_negative_request, txt_AB_positive_request, txt_AB_negative_request, txt_O_positive_request, txt_O_negative_request;
    String facilitatorDistrict;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_request_blood);

        backImg = findViewById(R.id.img_back);
        txt_A_positive = findViewById(R.id.txt_A_positive_units);
        txt_A_negative = findViewById(R.id.txt_A_negative_units);
        txt_B_positive = findViewById(R.id.txt_B_positive_units);
        txt_B_negative = findViewById(R.id.txt_B_negative_units);
        txt_AB_positive = findViewById(R.id.txt_AB_positive_units);
        txt_AB_negative = findViewById(R.id.txt_AB_negative_units);
        txt_O_positive = findViewById(R.id.txt_O_positive_units);
        txt_O_negative = findViewById(R.id.txt_O_negative_units);
        recyclerView = findViewById(R.id.view_request_blood);
        gridLayout = findViewById(R.id.view_request_blood_grid);
        txt_A_positive_request = findViewById(R.id.txt_A_positive_request);
        txt_A_negative_request = findViewById(R.id.txt_A_negative_request);
        txt_B_positive_request = findViewById(R.id.txt_B_positive_request);
        txt_B_negative_request = findViewById(R.id.txt_B_negative_request);
        txt_AB_positive_request = findViewById(R.id.txt_AB_positive_request);
        txt_AB_negative_request = findViewById(R.id.txt_AB_negative_request);
        txt_O_positive_request = findViewById(R.id.txt_O_positive_request);
        txt_O_negative_request = findViewById(R.id.txt_O_negative_request);

        /* Get phone number from session */
        SessionManager sessionManager = new SessionManager(this, SessionManager.SESSION_USER_SESSION);
        HashMap<String, String> userDetails = sessionManager.getUserDetailFromSession();
        String phone = userDetails.get(SessionManager.KEY_PHONE_NO);
        Log.d("PHONE NUMBER : ", String.valueOf(phone));

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        list = new ArrayList<>();
        bloodStockHolder = new BloodStockHolder(this, list);
        recyclerView.setAdapter(bloodStockHolder);

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference1 = firebaseDatabase.getReference("facilitator");
        Query checkFacilitatorDistrict = databaseReference1.orderByChild("mobile").equalTo(phone);

        checkFacilitatorDistrict.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    facilitatorDistrict = snapshot.child(phone).child("district").getValue(String.class);
                    Log.d("facilitatorDistrict : ", String.valueOf(facilitatorDistrict));

                    DatabaseReference databaseReference = firebaseDatabase.getReference("stock");
                    Query query = databaseReference.child(facilitatorDistrict);
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

                                    if (bloodUnit_var.equals("A+")) {
                                        Object units_A_positive = map.get("unit");
                                        double formattedUnits = Double.valueOf(String.valueOf(units_A_positive));
                                        sum_A_positive += formattedUnits;
                                        txt_A_positive.setText(String.valueOf(sum_A_positive));
                                    } else if (bloodUnit_var.equals("A-")) {
                                        Object units_A_negative = map.get("unit");
                                        double formattedUnits = Double.valueOf(String.valueOf(units_A_negative));
                                        sum_A_negative += formattedUnits;
                                        txt_A_negative.setText(String.valueOf(sum_A_negative));
                                    } else if (bloodUnit_var.equals("B+")) {
                                        Object units_B_positive = map.get("unit");
                                        double formattedUnits = Double.valueOf(String.valueOf(units_B_positive));
                                        sum_B_positive += formattedUnits;
                                        txt_B_positive.setText(String.valueOf(sum_B_positive));
                                    } else if (bloodUnit_var.equals("B-") ) {
                                        Object units_B_negative = map.get("unit");
                                        double formattedUnits = Double.valueOf(String.valueOf(units_B_negative));
                                        sum_B_negative += formattedUnits;
                                        txt_B_negative.setText(String.valueOf(sum_B_negative));
                                    } else if (bloodUnit_var.equals("AB+") ) {
                                        Object units_AB_positive = map.get("unit");
                                        double formattedUnits = Double.valueOf(String.valueOf(units_AB_positive));
                                        sum_AB_positive += formattedUnits;
                                        txt_AB_positive.setText(String.valueOf(sum_AB_positive));
                                    } else if (bloodUnit_var.equals("AB-") ) {
                                        Object units_AB_negative = map.get("unit");
                                        double formattedUnits = Double.valueOf(String.valueOf(units_AB_negative));
                                        sum_AB_negative += formattedUnits;
                                        txt_AB_negative.setText(String.valueOf(sum_AB_negative));
                                    } else if (bloodUnit_var.equals("O+")) {
                                        Object units_O_positive = map.get("unit");
                                        double formattedUnits = Double.valueOf(String.valueOf(units_O_positive));
                                        sum_O_positive += formattedUnits;
                                        txt_O_positive.setText(String.valueOf(sum_O_positive));
                                    } else if (bloodUnit_var.equals("O-")) {
                                        Object units_O_negative = map.get("unit");
                                        double formattedUnits = Double.valueOf(String.valueOf(units_O_negative));
                                        sum_O_negative += formattedUnits;
                                        txt_O_negative.setText(String.valueOf(sum_O_negative));
                                    }
                                }
                            } else {
                                txt_A_positive.setText("0.0");
                                txt_A_negative.setText("0.0");
                                txt_B_positive.setText("0.0");
                                txt_B_negative.setText("0.0");
                                txt_AB_positive.setText("0.0");
                                txt_AB_negative.setText("0.0");
                                txt_O_positive.setText("0.0");
                                txt_O_negative.setText("0.0");
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

        txt_A_positive_request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(ViewRequestBloodActivity.this)
                        .setTitle("Request Blood Confirmation")
                        .setMessage("This will send SMS to all the available donors which match your requirement. Do you want to proceed?")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                FirebaseDatabase firebaseDatabase1 = FirebaseDatabase.getInstance();
                                DatabaseReference reference1 = firebaseDatabase1.getReference("donor");
                                Query checkBloodGroup = reference1.orderByChild("bloodGroup").equalTo("A+");
                                checkBloodGroup.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        if (snapshot.exists()){
                                            String lastDataDonatedFromDB = "";
                                            String phoneNoFromDb = "";

                                            for (DataSnapshot ds : snapshot.getChildren()) {
                                                lastDataDonatedFromDB = ds.child("dateLastDonated").getValue(String.class);
                                                phoneNoFromDb = ds.child("mobile").getValue(String.class);

                                                Log.d("Date from DB:", String.valueOf(lastDataDonatedFromDB));
                                                Log.d("Mobile from DB:", String.valueOf(phoneNoFromDb));

                                                //Filter donors calculating last donated date
                                                DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
                                                Date date = new Date();
                                                String currentDate = dateFormat.format(date);
                                                Log.d("Current date", String.valueOf(currentDate));

                                                Date date1 = null;
                                                try {
                                                    date1 = new SimpleDateFormat("yyyy/MM/dd").parse(currentDate);
                                                } catch (ParseException e) {
                                                    e.printStackTrace();
                                                }
                                                Date date2 = null;
                                                try {
                                                    date2 = new SimpleDateFormat("yyyy/MM/dd").parse(lastDataDonatedFromDB);
                                                } catch (ParseException e) {
                                                    e.printStackTrace();
                                                }
                                                long diff = date1.getTime() - date2.getTime();

                                                TimeUnit time = TimeUnit.DAYS;
                                                long date_diffrence = time.convert(diff, TimeUnit.MILLISECONDS);
                                                System.out.println("The difference in days is : " + date_diffrence);

                                                /* Date of last blood donate day should be at least four months prior to current date*/
                                                if (date_diffrence >= 120) {
                                                    //Build message
                                                    String message = "URGENT! A+ blood need for "+facilitatorDistrict+" district. Please donate blood to your nearest hospital";
                                                    Log.d("Message", String.valueOf(message));

                                                    SmsManager smsManager = SmsManager.getDefault();
                                                    smsManager.sendTextMessage("+94" + phoneNoFromDb, null, message, null, null);

                                                }
                                            }

                                            Intent intent = new Intent(getApplicationContext(), ViewRequestBloodActivity.class);
                                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            startActivity(intent);
                                            finish();
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                            }
                        })
                        .setNegativeButton(android.R.string.no, null).show();
            }
        });

        txt_A_negative_request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(ViewRequestBloodActivity.this)
                        .setTitle("Request Blood Confirmation")
                        .setMessage("This will send SMS to all the available donors which match your requirement. Do you want to proceed?")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                FirebaseDatabase firebaseDatabase1 = FirebaseDatabase.getInstance();
                                DatabaseReference reference1 = firebaseDatabase1.getReference("donor");
                                Query checkBloodGroup = reference1.orderByChild("bloodGroup").equalTo("A-");
                                checkBloodGroup.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        if (snapshot.exists()){
                                            String lastDataDonatedFromDB = "";
                                            String phoneNoFromDb = "";

                                            for (DataSnapshot ds : snapshot.getChildren()) {
                                                lastDataDonatedFromDB = ds.child("dateLastDonated").getValue(String.class);
                                                phoneNoFromDb = ds.child("mobile").getValue(String.class);

                                                Log.d("Date from DB:", String.valueOf(lastDataDonatedFromDB));
                                                Log.d("Mobile from DB:", String.valueOf(phoneNoFromDb));

                                                //Filter donors calculating last donated date
                                                DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
                                                Date date = new Date();
                                                String currentDate = dateFormat.format(date);
                                                Log.d("Current date", String.valueOf(currentDate));

                                                Date date1 = null;
                                                try {
                                                    date1 = new SimpleDateFormat("yyyy/MM/dd").parse(currentDate);
                                                } catch (ParseException e) {
                                                    e.printStackTrace();
                                                }
                                                Date date2 = null;
                                                try {
                                                    date2 = new SimpleDateFormat("yyyy/MM/dd").parse(lastDataDonatedFromDB);
                                                } catch (ParseException e) {
                                                    e.printStackTrace();
                                                }
                                                long diff = date1.getTime() - date2.getTime();

                                                TimeUnit time = TimeUnit.DAYS;
                                                long date_diffrence = time.convert(diff, TimeUnit.MILLISECONDS);
                                                System.out.println("The difference in days is : " + date_diffrence);

                                                /* Date of last blood donate day should be at least four months prior to current date*/
                                                if (date_diffrence >= 120) {
                                                    //Build message
                                                    String message = "URGENT! A- blood need for "+facilitatorDistrict+" district. Please donate blood to your nearest hospital";
                                                    Log.d("Message", String.valueOf(message));

                                                    SmsManager smsManager = SmsManager.getDefault();
                                                    smsManager.sendTextMessage("+94" + phoneNoFromDb, null, message, null, null);

                                                }
                                            }

                                            Intent intent = new Intent(getApplicationContext(), ViewRequestBloodActivity.class);
                                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            startActivity(intent);
                                            finish();
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                            }
                        })
                        .setNegativeButton(android.R.string.no, null).show();
            }
        });

        txt_B_positive_request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(ViewRequestBloodActivity.this)
                        .setTitle("Request Blood Confirmation")
                        .setMessage("This will send SMS to all the available donors which match your requirement. Do you want to proceed?")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                FirebaseDatabase firebaseDatabase1 = FirebaseDatabase.getInstance();
                                DatabaseReference reference1 = firebaseDatabase1.getReference("donor");
                                Query checkBloodGroup = reference1.orderByChild("bloodGroup").equalTo("B+");
                                checkBloodGroup.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        if (snapshot.exists()){
                                            String lastDataDonatedFromDB = "";
                                            String phoneNoFromDb = "";

                                            for (DataSnapshot ds : snapshot.getChildren()) {
                                                lastDataDonatedFromDB = ds.child("dateLastDonated").getValue(String.class);
                                                phoneNoFromDb = ds.child("mobile").getValue(String.class);

                                                Log.d("Date from DB:", String.valueOf(lastDataDonatedFromDB));
                                                Log.d("Mobile from DB:", String.valueOf(phoneNoFromDb));

                                                //Filter donors calculating last donated date
                                                DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
                                                Date date = new Date();
                                                String currentDate = dateFormat.format(date);
                                                Log.d("Current date", String.valueOf(currentDate));

                                                Date date1 = null;
                                                try {
                                                    date1 = new SimpleDateFormat("yyyy/MM/dd").parse(currentDate);
                                                } catch (ParseException e) {
                                                    e.printStackTrace();
                                                }
                                                Date date2 = null;
                                                try {
                                                    date2 = new SimpleDateFormat("yyyy/MM/dd").parse(lastDataDonatedFromDB);
                                                } catch (ParseException e) {
                                                    e.printStackTrace();
                                                }
                                                long diff = date1.getTime() - date2.getTime();

                                                TimeUnit time = TimeUnit.DAYS;
                                                long date_diffrence = time.convert(diff, TimeUnit.MILLISECONDS);
                                                System.out.println("The difference in days is : " + date_diffrence);

                                                /* Date of last blood donate day should be at least four months prior to current date*/
                                                if (date_diffrence >= 120) {
                                                    //Build message
                                                    String message = "URGENT! B+ blood need for "+facilitatorDistrict+" district. Please donate blood to your nearest hospital";
                                                    Log.d("Message", String.valueOf(message));

                                                    SmsManager smsManager = SmsManager.getDefault();
                                                    smsManager.sendTextMessage("+94" + phoneNoFromDb, null, message, null, null);

                                                }
                                            }

                                            Intent intent = new Intent(getApplicationContext(), ViewRequestBloodActivity.class);
                                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            startActivity(intent);
                                            finish();
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                            }
                        })
                        .setNegativeButton(android.R.string.no, null).show();
            }
        });

        txt_B_negative_request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(ViewRequestBloodActivity.this)
                        .setTitle("Request Blood Confirmation")
                        .setMessage("This will send SMS to all the available donors which match your requirement. Do you want to proceed?")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                FirebaseDatabase firebaseDatabase1 = FirebaseDatabase.getInstance();
                                DatabaseReference reference1 = firebaseDatabase1.getReference("donor");
                                Query checkBloodGroup = reference1.orderByChild("bloodGroup").equalTo("B-");
                                checkBloodGroup.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        if (snapshot.exists()){
                                            String lastDataDonatedFromDB = "";
                                            String phoneNoFromDb = "";

                                            for (DataSnapshot ds : snapshot.getChildren()) {
                                                lastDataDonatedFromDB = ds.child("dateLastDonated").getValue(String.class);
                                                phoneNoFromDb = ds.child("mobile").getValue(String.class);

                                                Log.d("Date from DB:", String.valueOf(lastDataDonatedFromDB));
                                                Log.d("Mobile from DB:", String.valueOf(phoneNoFromDb));

                                                //Filter donors calculating last donated date
                                                DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
                                                Date date = new Date();
                                                String currentDate = dateFormat.format(date);
                                                Log.d("Current date", String.valueOf(currentDate));

                                                Date date1 = null;
                                                try {
                                                    date1 = new SimpleDateFormat("yyyy/MM/dd").parse(currentDate);
                                                } catch (ParseException e) {
                                                    e.printStackTrace();
                                                }
                                                Date date2 = null;
                                                try {
                                                    date2 = new SimpleDateFormat("yyyy/MM/dd").parse(lastDataDonatedFromDB);
                                                } catch (ParseException e) {
                                                    e.printStackTrace();
                                                }
                                                long diff = date1.getTime() - date2.getTime();

                                                TimeUnit time = TimeUnit.DAYS;
                                                long date_diffrence = time.convert(diff, TimeUnit.MILLISECONDS);
                                                System.out.println("The difference in days is : " + date_diffrence);

                                                /* Date of last blood donate day should be at least four months prior to current date*/
                                                if (date_diffrence >= 120) {
                                                    //Build message
                                                    String message = "URGENT! B- blood need for "+facilitatorDistrict+" district. Please donate blood to your nearest hospital";
                                                    Log.d("Message", String.valueOf(message));

                                                    SmsManager smsManager = SmsManager.getDefault();
                                                    smsManager.sendTextMessage("+94" + phoneNoFromDb, null, message, null, null);

                                                }
                                            }

                                            Intent intent = new Intent(getApplicationContext(), ViewRequestBloodActivity.class);
                                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            startActivity(intent);
                                            finish();
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                            }
                        })
                        .setNegativeButton(android.R.string.no, null).show();
            }
        });

        txt_AB_positive_request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(ViewRequestBloodActivity.this)
                        .setTitle("Request Blood Confirmation")
                        .setMessage("This will send SMS to all the available donors which match your requirement. Do you want to proceed?")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                FirebaseDatabase firebaseDatabase1 = FirebaseDatabase.getInstance();
                                DatabaseReference reference1 = firebaseDatabase1.getReference("donor");
                                Query checkBloodGroup = reference1.orderByChild("bloodGroup").equalTo("AB+");
                                checkBloodGroup.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        if (snapshot.exists()){
                                            String lastDataDonatedFromDB = "";
                                            String phoneNoFromDb = "";

                                            for (DataSnapshot ds : snapshot.getChildren()) {
                                                lastDataDonatedFromDB = ds.child("dateLastDonated").getValue(String.class);
                                                phoneNoFromDb = ds.child("mobile").getValue(String.class);

                                                Log.d("Date from DB:", String.valueOf(lastDataDonatedFromDB));
                                                Log.d("Mobile from DB:", String.valueOf(phoneNoFromDb));

                                                //Filter donors calculating last donated date
                                                DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
                                                Date date = new Date();
                                                String currentDate = dateFormat.format(date);
                                                Log.d("Current date", String.valueOf(currentDate));

                                                Date date1 = null;
                                                try {
                                                    date1 = new SimpleDateFormat("yyyy/MM/dd").parse(currentDate);
                                                } catch (ParseException e) {
                                                    e.printStackTrace();
                                                }
                                                Date date2 = null;
                                                try {
                                                    date2 = new SimpleDateFormat("yyyy/MM/dd").parse(lastDataDonatedFromDB);
                                                } catch (ParseException e) {
                                                    e.printStackTrace();
                                                }
                                                long diff = date1.getTime() - date2.getTime();

                                                TimeUnit time = TimeUnit.DAYS;
                                                long date_diffrence = time.convert(diff, TimeUnit.MILLISECONDS);
                                                System.out.println("The difference in days is : " + date_diffrence);

                                                /* Date of last blood donate day should be at least four months prior to current date*/
                                                if (date_diffrence >= 120) {
                                                    //Build message
                                                    String message = "URGENT! AB+ blood need for "+facilitatorDistrict+" district. Please donate blood to your nearest hospital";
                                                    Log.d("Message", String.valueOf(message));

                                                    SmsManager smsManager = SmsManager.getDefault();
                                                    smsManager.sendTextMessage("+94" + phoneNoFromDb, null, message, null, null);

                                                }
                                            }

                                            Intent intent = new Intent(getApplicationContext(), ViewRequestBloodActivity.class);
                                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            startActivity(intent);
                                            finish();
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                            }
                        })
                        .setNegativeButton(android.R.string.no, null).show();
            }
        });

        txt_AB_negative_request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(ViewRequestBloodActivity.this)
                        .setTitle("Request Blood Confirmation")
                        .setMessage("This will send SMS to all the available donors which match your requirement. Do you want to proceed?")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                FirebaseDatabase firebaseDatabase1 = FirebaseDatabase.getInstance();
                                DatabaseReference reference1 = firebaseDatabase1.getReference("donor");
                                Query checkBloodGroup = reference1.orderByChild("bloodGroup").equalTo("AB-");
                                checkBloodGroup.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        if (snapshot.exists()){
                                            String lastDataDonatedFromDB = "";
                                            String phoneNoFromDb = "";

                                            for (DataSnapshot ds : snapshot.getChildren()) {
                                                lastDataDonatedFromDB = ds.child("dateLastDonated").getValue(String.class);
                                                phoneNoFromDb = ds.child("mobile").getValue(String.class);

                                                Log.d("Date from DB:", String.valueOf(lastDataDonatedFromDB));
                                                Log.d("Mobile from DB:", String.valueOf(phoneNoFromDb));

                                                //Filter donors calculating last donated date
                                                DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
                                                Date date = new Date();
                                                String currentDate = dateFormat.format(date);
                                                Log.d("Current date", String.valueOf(currentDate));

                                                Date date1 = null;
                                                try {
                                                    date1 = new SimpleDateFormat("yyyy/MM/dd").parse(currentDate);
                                                } catch (ParseException e) {
                                                    e.printStackTrace();
                                                }
                                                Date date2 = null;
                                                try {
                                                    date2 = new SimpleDateFormat("yyyy/MM/dd").parse(lastDataDonatedFromDB);
                                                } catch (ParseException e) {
                                                    e.printStackTrace();
                                                }
                                                long diff = date1.getTime() - date2.getTime();

                                                TimeUnit time = TimeUnit.DAYS;
                                                long date_diffrence = time.convert(diff, TimeUnit.MILLISECONDS);
                                                System.out.println("The difference in days is : " + date_diffrence);

                                                /* Date of last blood donate day should be at least four months prior to current date*/
                                                if (date_diffrence >= 120) {
                                                    //Build message
                                                    String message = "URGENT! AB- blood need for "+facilitatorDistrict+" district. Please donate blood to your nearest hospital";
                                                    Log.d("Message", String.valueOf(message));

                                                    SmsManager smsManager = SmsManager.getDefault();
                                                    smsManager.sendTextMessage("+94" + phoneNoFromDb, null, message, null, null);

                                                }
                                            }

                                            Intent intent = new Intent(getApplicationContext(), ViewRequestBloodActivity.class);
                                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            startActivity(intent);
                                            finish();
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                            }
                        })
                        .setNegativeButton(android.R.string.no, null).show();
            }
        });

        txt_O_positive_request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(ViewRequestBloodActivity.this)
                        .setTitle("Request Blood Confirmation")
                        .setMessage("This will send SMS to all the available donors which match your requirement. Do you want to proceed?")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                FirebaseDatabase firebaseDatabase1 = FirebaseDatabase.getInstance();
                                DatabaseReference reference1 = firebaseDatabase1.getReference("donor");
                                Query checkBloodGroup = reference1.orderByChild("bloodGroup").equalTo("O+");
                                checkBloodGroup.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        if (snapshot.exists()){
                                            String lastDataDonatedFromDB = "";
                                            String phoneNoFromDb = "";

                                            for (DataSnapshot ds : snapshot.getChildren()) {
                                                lastDataDonatedFromDB = ds.child("dateLastDonated").getValue(String.class);
                                                phoneNoFromDb = ds.child("mobile").getValue(String.class);

                                                Log.d("Date from DB:", String.valueOf(lastDataDonatedFromDB));
                                                Log.d("Mobile from DB:", String.valueOf(phoneNoFromDb));

                                                //Filter donors calculating last donated date
                                                DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
                                                Date date = new Date();
                                                String currentDate = dateFormat.format(date);
                                                Log.d("Current date", String.valueOf(currentDate));

                                                Date date1 = null;
                                                try {
                                                    date1 = new SimpleDateFormat("yyyy/MM/dd").parse(currentDate);
                                                } catch (ParseException e) {
                                                    e.printStackTrace();
                                                }
                                                Date date2 = null;
                                                try {
                                                    date2 = new SimpleDateFormat("yyyy/MM/dd").parse(lastDataDonatedFromDB);
                                                } catch (ParseException e) {
                                                    e.printStackTrace();
                                                }
                                                long diff = date1.getTime() - date2.getTime();

                                                TimeUnit time = TimeUnit.DAYS;
                                                long date_diffrence = time.convert(diff, TimeUnit.MILLISECONDS);
                                                System.out.println("The difference in days is : " + date_diffrence);

                                                /* Date of last blood donate day should be at least four months prior to current date*/
                                                if (date_diffrence >= 120) {
                                                    //Build message
                                                    String message = "URGENT! O+ blood need for "+facilitatorDistrict+" district. Please donate blood to your nearest hospital";
                                                    Log.d("Message", String.valueOf(message));

                                                    SmsManager smsManager = SmsManager.getDefault();
                                                    smsManager.sendTextMessage("+94" + phoneNoFromDb, null, message, null, null);

                                                }
                                            }

                                            Intent intent = new Intent(getApplicationContext(), ViewRequestBloodActivity.class);
                                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            startActivity(intent);
                                            finish();
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                            }
                        })
                        .setNegativeButton(android.R.string.no, null).show();
            }
        });

        txt_O_negative_request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(ViewRequestBloodActivity.this)
                        .setTitle("Request Blood Confirmation")
                        .setMessage("This will send SMS to all the available donors which match your requirement. Do you want to proceed?")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                FirebaseDatabase firebaseDatabase1 = FirebaseDatabase.getInstance();
                                DatabaseReference reference1 = firebaseDatabase1.getReference("donor");
                                Query checkBloodGroup = reference1.orderByChild("bloodGroup").equalTo("O-");
                                checkBloodGroup.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        if (snapshot.exists()){
                                            String lastDataDonatedFromDB = "";
                                            String phoneNoFromDb = "";

                                            for (DataSnapshot ds : snapshot.getChildren()) {
                                                lastDataDonatedFromDB = ds.child("dateLastDonated").getValue(String.class);
                                                phoneNoFromDb = ds.child("mobile").getValue(String.class);

                                                Log.d("Date from DB:", String.valueOf(lastDataDonatedFromDB));
                                                Log.d("Mobile from DB:", String.valueOf(phoneNoFromDb));

                                                //Filter donors calculating last donated date
                                                DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
                                                Date date = new Date();
                                                String currentDate = dateFormat.format(date);
                                                Log.d("Current date", String.valueOf(currentDate));

                                                Date date1 = null;
                                                try {
                                                    date1 = new SimpleDateFormat("yyyy/MM/dd").parse(currentDate);
                                                } catch (ParseException e) {
                                                    e.printStackTrace();
                                                }
                                                Date date2 = null;
                                                try {
                                                    date2 = new SimpleDateFormat("yyyy/MM/dd").parse(lastDataDonatedFromDB);
                                                } catch (ParseException e) {
                                                    e.printStackTrace();
                                                }
                                                long diff = date1.getTime() - date2.getTime();

                                                TimeUnit time = TimeUnit.DAYS;
                                                long date_diffrence = time.convert(diff, TimeUnit.MILLISECONDS);
                                                System.out.println("The difference in days is : " + date_diffrence);

                                                /* Date of last blood donate day should be at least four months prior to current date*/
                                                if (date_diffrence >= 120) {
                                                    //Build message
                                                    String message = "URGENT! O- blood need for "+facilitatorDistrict+" district. Please donate blood to your nearest hospital";
                                                    Log.d("Message", String.valueOf(message));

                                                    SmsManager smsManager = SmsManager.getDefault();
                                                    smsManager.sendTextMessage("+94" + phoneNoFromDb, null, message, null, null);

                                                }
                                            }

                                            Intent intent = new Intent(getApplicationContext(), ViewRequestBloodActivity.class);
                                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            startActivity(intent);
                                            finish();
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                            }
                        })
                        .setNegativeButton(android.R.string.no, null).show();
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