package com.example.bloodtransfusionservice;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

import java.util.Calendar;

public class UserSignup2Activity extends AppCompatActivity {
    DatePicker datePicker_dob;
    RadioButton male, female, other;
    Button buttonNext, buttonUserLogin, buttonUserHome;
    String gender = "";
    static final int PICK_MAP_POINT_REQUEST = 1;
    ImageView backImg;
    String donorFlag, passwordResetVal, donorReg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_signup2);

        datePicker_dob = findViewById(R.id.input_dob);
        male = findViewById(R.id.input_male);
        female = findViewById(R.id.input_female);
        other = findViewById(R.id.input_other);
        buttonNext = findViewById(R.id.btn_user_next);
        buttonUserLogin = findViewById(R.id.btn_user_login);
        buttonUserHome = findViewById(R.id.btn_user_home);
        backImg = findViewById(R.id.img_back);

        donorFlag = getIntent().getStringExtra("Flag");
        passwordResetVal = getIntent().getStringExtra("passwordResetFlag");
        donorReg = getIntent().getStringExtra("donorRegistration");

        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePicker_dob = findViewById(R.id.input_dob);
                int day = datePicker_dob.getDayOfMonth();
                int month = datePicker_dob.getMonth() + 1;
                int year = datePicker_dob.getYear();
                String dob_ = "" + year + "/" + month + "/" + day;

                if (!(getAge(year, month, day) >= 18) || !(getAge(year, month, day) <= 60)) {
                    Toast.makeText(UserSignup2Activity.this, "You should be between 18 years and 60 years to be donate blood..!!", Toast.LENGTH_SHORT).show();
                } else {
                    if (!male.isChecked() && !female.isChecked() && !other.isChecked()) {
                        Toast.makeText(UserSignup2Activity.this, "Please select gender", Toast.LENGTH_SHORT).show();
                    } else if (male.isChecked()) {
                        gender = "Male";
                        String fullname_ = getIntent().getStringExtra("fullname");
                        String email_ = getIntent().getStringExtra("email");
                        String nic_ = getIntent().getStringExtra("nic");
                        String location_ = getIntent().getStringExtra("location");
                        String address_ = getIntent().getStringExtra("address");
                        String district_ = getIntent().getStringExtra("district");
                        String diseases_ = getIntent().getStringExtra("diseases");
                        String txt_diseases_ = getIntent().getStringExtra("diseasesList");

                        Intent intent = new Intent(UserSignup2Activity.this, UserSignup3Activity.class);

                        intent.putExtra("fullname", fullname_);
                        intent.putExtra("email", email_);
                        intent.putExtra("nic", nic_);
                        intent.putExtra("location", location_);
                        intent.putExtra("address", address_);
                        intent.putExtra("district", district_);
                        intent.putExtra("diseases", diseases_);
                        intent.putExtra("diseasesList", txt_diseases_);
                        intent.putExtra("gender", gender);
                        intent.putExtra("dob", dob_);
                        Log.d("Gender", String.valueOf(gender));
                        Log.d("Date of Birth", String.valueOf(dob_));
                        intent.putExtra("Flag", donorFlag);
                        intent.putExtra("passwordResetFlag", passwordResetVal);
                        intent.putExtra("donorRegistration", donorReg);

                        startActivityForResult(intent, PICK_MAP_POINT_REQUEST);
                    } else if (female.isChecked()) {
                        gender = "Female";
                        String fullname_ = getIntent().getStringExtra("fullname");
                        String email_ = getIntent().getStringExtra("email");
                        String nic_ = getIntent().getStringExtra("nic");
                        String location_ = getIntent().getStringExtra("location");
                        String address_ = getIntent().getStringExtra("address");
                        String district_ = getIntent().getStringExtra("district");
                        String diseases_ = getIntent().getStringExtra("diseases");
                        String txt_diseases_ = getIntent().getStringExtra("diseasesList");

                        Intent intent = new Intent(UserSignup2Activity.this, UserSignup3Activity.class);

                        intent.putExtra("fullname", fullname_);
                        intent.putExtra("email", email_);
                        intent.putExtra("nic", nic_);
                        intent.putExtra("location", location_);
                        intent.putExtra("address", address_);
                        intent.putExtra("district", district_);
                        intent.putExtra("diseases", diseases_);
                        intent.putExtra("diseasesList", txt_diseases_);
                        intent.putExtra("gender", gender);
                        intent.putExtra("dob", dob_);
                        Log.d("Gender", String.valueOf(gender));
                        Log.d("Date of Birth", String.valueOf(dob_));
                        intent.putExtra("Flag", donorFlag);
                        intent.putExtra("passwordResetFlag", passwordResetVal);
                        intent.putExtra("donorRegistration", donorReg);

                        startActivityForResult(intent, PICK_MAP_POINT_REQUEST);
                    } else {
                        gender = "Other";
                        String fullname_ = getIntent().getStringExtra("fullname");
                        String email_ = getIntent().getStringExtra("email");
                        String nic_ = getIntent().getStringExtra("nic");
                        String location_ = getIntent().getStringExtra("location");
                        String address_ = getIntent().getStringExtra("address");
                        String district_ = getIntent().getStringExtra("district");
                        String diseases_ = getIntent().getStringExtra("diseases");
                        String txt_diseases_ = getIntent().getStringExtra("diseasesList");

                        Intent intent = new Intent(UserSignup2Activity.this, UserSignup3Activity.class);

                        intent.putExtra("fullname", fullname_);
                        intent.putExtra("email", email_);
                        intent.putExtra("nic", nic_);
                        intent.putExtra("location", location_);
                        intent.putExtra("address", address_);
                        intent.putExtra("district", district_);
                        intent.putExtra("diseases", diseases_);
                        intent.putExtra("diseasesList", txt_diseases_);
                        intent.putExtra("gender", gender);
                        intent.putExtra("dob", dob_);
                        Log.d("Gender", String.valueOf(gender));
                        Log.d("Date of Birth", String.valueOf(dob_));
                        intent.putExtra("Flag", donorFlag);
                        intent.putExtra("passwordResetFlag", passwordResetVal);
                        intent.putExtra("donorRegistration", donorReg);

                        startActivityForResult(intent, PICK_MAP_POINT_REQUEST);
                    }
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
                    Intent intent = new Intent(getApplicationContext(), UserSignup1Activity.class);
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
                    Intent intent = new Intent(getApplicationContext(), UserSignup1Activity.class);
                    intent.putExtra("Flag", donorFlag);
                    intent.putExtra("passwordResetFlag", passwordResetVal);
                    intent.putExtra("donorRegistration", donorReg);
                    setResult(Activity.RESULT_CANCELED, intent);
                    finish();
                }
            });

        }
    }

    public int getAge(int year, int month, int day) {
        Calendar dob = Calendar.getInstance();
        Calendar today = Calendar.getInstance();
        dob.set(year, month, day);
        int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);

        if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)) {
            age--;
        }
        Integer ageInt = new Integer(age);
        String ageToday = ageInt.toString();
        Log.d("Age", String.valueOf(ageInt));

        return ageInt;
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