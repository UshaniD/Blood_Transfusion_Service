package com.example.bloodtransfusionservice;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class UserSignup3Activity extends AppCompatActivity {
    DatePicker datePicker_lastDateOfDonated;
    Button buttonUserNext, buttonUserLogin, buttonUserHome;
    AutoCompleteTextView bloodGroups;
    static final int PICK_MAP_POINT_REQUEST = 1;
    ImageView backImg;
    String donorFlag, passwordResetVal, donorReg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_signup3);
        buttonUserNext = findViewById(R.id.btn_user_next);
        buttonUserLogin = findViewById(R.id.btn_user_login);
        buttonUserHome = findViewById(R.id.btn_user_home);
        backImg = findViewById(R.id.img_back);
        bloodGroups = findViewById(R.id.blood_group);

        donorFlag = getIntent().getStringExtra("Flag");
        passwordResetVal = getIntent().getStringExtra("passwordResetFlag");
        donorReg = getIntent().getStringExtra("donorRegistration");

        String[] blood_groups_option = {"A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-"};
        ArrayAdapter arrayAdapter1 = new ArrayAdapter(this, R.layout.format_dropdown, blood_groups_option);
        bloodGroups.setText(arrayAdapter1.getItem(0).toString(), false);
        bloodGroups.setAdapter(arrayAdapter1);

        buttonUserNext.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                datePicker_lastDateOfDonated = findViewById(R.id.last_date_of_donated);
                int day = datePicker_lastDateOfDonated.getDayOfMonth();
                int month = datePicker_lastDateOfDonated.getMonth() + 1;
                int year = datePicker_lastDateOfDonated.getYear();
                String blood_groups_var = bloodGroups.getText().toString();
                String dateEntered = "" + year + "/" + month + "/" + day;

                DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
                Date date = new Date();
                String currentDate = dateFormat.format(date);
                Log.d("Current date", String.valueOf(currentDate));

                Date dateNew = null;
                try {
                    dateNew = (Date) dateFormat.parse(dateEntered);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                SimpleDateFormat newFormat = new SimpleDateFormat("yyyy/MM/dd");
                String date_ = newFormat.format(dateNew);
                Log.d("date_", String.valueOf(date_));

                Date date1 = null;
                try {
                    date1 = new SimpleDateFormat("yyyy/MM/dd").parse(date_);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Date date2 = null;
                try {
                    date2 = new SimpleDateFormat("yyyy/MM/dd").parse(currentDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                long diff = date2.getTime() - date1.getTime();

                TimeUnit time = TimeUnit.DAYS;
                long diffrence = time.convert(diff, TimeUnit.MILLISECONDS);
                System.out.println("The difference in days is : " + diffrence);

                if (diffrence < 0) {
                    Toast.makeText(UserSignup3Activity.this, "Future dates can not been entered", Toast.LENGTH_SHORT).show();
                } else {
                    String fullname_ = getIntent().getStringExtra("fullname");
                    String email_ = getIntent().getStringExtra("email");
                    String nic_ = getIntent().getStringExtra("nic");
                    String location_ = getIntent().getStringExtra("location");
                    String address_ = getIntent().getStringExtra("address");
                    String district_ = getIntent().getStringExtra("district");
                    String gender_ = getIntent().getStringExtra("gender");
                    String dob_ = getIntent().getStringExtra("dob");
                    String diseases_ = getIntent().getStringExtra("diseases");
                    String txt_diseases_ = getIntent().getStringExtra("diseasesList");

                    Intent intent = new Intent(UserSignup3Activity.this, UserSignup4Activity.class);

                    intent.putExtra("fullname", fullname_);
                    intent.putExtra("email", email_);
                    intent.putExtra("nic", nic_);
                    intent.putExtra("location", location_);
                    intent.putExtra("address", address_);
                    intent.putExtra("district", district_);
                    intent.putExtra("lastDateDonated", date_);
                    intent.putExtra("gender", gender_);
                    intent.putExtra("dob", dob_);
                    intent.putExtra("diseases", diseases_);
                    intent.putExtra("diseasesList", txt_diseases_);
                    intent.putExtra("bloodGroup", blood_groups_var);
                    Log.d("Last Date donated", String.valueOf(date_));
                    Log.d("Blood group", String.valueOf(blood_groups_var));
                    Log.d("District", String.valueOf(district_));

                    intent.putExtra("Flag", donorFlag);
                    intent.putExtra("passwordResetFlag", passwordResetVal);
                    intent.putExtra("donorRegistration", donorReg);

                    startActivityForResult(intent, PICK_MAP_POINT_REQUEST);
                }

            }
        });

        if (donorReg.equals("true")) {
            buttonUserHome.setVisibility(View.VISIBLE);
            buttonUserLogin.setVisibility(View.GONE);
            buttonUserHome.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), FacilitatorLeftNavActivity.class);
                    intent.putExtra("Flag", donorFlag);
                    intent.putExtra("passwordResetFlag", passwordResetVal);
                    intent.putExtra("donorRegistration", donorReg);
                    startActivity(intent);
                }
            });

            backImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), UserSignup2Activity.class);
                    intent.putExtra("Flag", donorFlag);
                    intent.putExtra("passwordResetFlag", passwordResetVal);
                    intent.putExtra("donorRegistration", donorReg);
                    setResult(Activity.RESULT_CANCELED, intent);
                    finish();
                }
            });

        } else {
            buttonUserHome.setVisibility(View.GONE);
            buttonUserLogin.setVisibility(View.VISIBLE);
            buttonUserLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), UserLoginActivity.class);
                    intent.putExtra("Flag", donorFlag);
                    intent.putExtra("passwordResetFlag", passwordResetVal);
                    intent.putExtra("donorRegistration", donorReg);
                    startActivity(intent);
                }
            });

            backImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), UserSignup2Activity.class);
                    intent.putExtra("Flag", donorFlag);
                    intent.putExtra("passwordResetFlag", passwordResetVal);
                    intent.putExtra("donorRegistration", donorReg);
                    setResult(Activity.RESULT_CANCELED, intent);
                    finish();
                }
            });

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_MAP_POINT_REQUEST) {
            if (resultCode == Activity.RESULT_CANCELED) {

            }

        }
    }

    @Override
    public void onBackPressed() {

    }
}