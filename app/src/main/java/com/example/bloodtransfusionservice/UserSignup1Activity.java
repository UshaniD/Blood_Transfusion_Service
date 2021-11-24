package com.example.bloodtransfusionservice;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

public class UserSignup1Activity extends AppCompatActivity {
    AutoCompleteTextView districts, diseases;
    TextInputLayout txtFullname, txtEmail, txtNic, txtDiseases;
    Button buttonUserNext, buttonUserLogin, buttonUserHome;
    Button buttonChooseLocation;
    TextView txtPlace, txtAddress, txtLocationHelp, txtDiseasesHelp;
    static final int PICK_MAP_POINT_REQUEST = 1;
    ImageView backImg;
    String donorFlag, passwordResetVal, donorReg;
    String longAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_signup1);

        donorFlag = getIntent().getStringExtra("Flag");
        passwordResetVal = getIntent().getStringExtra("passwordResetFlag");
        donorReg = getIntent().getStringExtra("donorRegistration");

        txtLocationHelp = findViewById(R.id.locationHelp);
        txtDiseasesHelp = findViewById(R.id.diseasesHelp);
        buttonUserNext = findViewById(R.id.btn_user_next);
        buttonUserLogin = findViewById(R.id.btn_user_login);
        buttonUserHome = findViewById(R.id.btn_user_home);
        backImg = findViewById(R.id.img_back);
        districts = findViewById(R.id.districts);
        diseases = findViewById(R.id.diseases_dropdown);
        txtFullname = findViewById(R.id.input_fullname);
        txtEmail = findViewById(R.id.input_email);
        txtNic = findViewById(R.id.input_nic);
        txtDiseases = findViewById(R.id.input_diseases);
        txtPlace = findViewById(R.id.txt_place);
        txtAddress = findViewById(R.id.txt_address);
        buttonChooseLocation = findViewById(R.id.btn_choose_location);

        String[] district_options = {"Colombo", "Kalutara", "Gampaha", "Matale", "Kandy", "Nuwara Eliya", "Matara", "Galle", "Hambantota",
                "Badulla", "Monaragala", "Kegalle", "Ratnapura", "Auradhapura", "Polonnaruwa", "Trincomalee", "Batticaloa", "Ampara", "Puttalam", "Kurunegala",
                "Jaffna", "Kilinochchi", "Mannar", "Mulaitivu", "Vavuniya"};
        ArrayAdapter arrayAdapter1 = new ArrayAdapter(this, R.layout.format_dropdown, district_options);
        districts.setText(arrayAdapter1.getItem(0).toString(), false);
        districts.setAdapter(arrayAdapter1);

        String[] diseases_options = {"No", "Yes"};
        ArrayAdapter arrayAdapter2 = new ArrayAdapter(this, R.layout.format_dropdown, diseases_options);
        diseases.setText(arrayAdapter2.getItem(0).toString(), false);
        diseases.setAdapter(arrayAdapter2);

        buttonUserNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fullname_var = txtFullname.getEditText().getText().toString();
                String email_var = txtEmail.getEditText().getText().toString();
                String nic_var = txtNic.getEditText().getText().toString();
                String districts_var = districts.getText().toString();
                String place_var = txtPlace.getText().toString();
                String address_var = txtAddress.getText().toString();
                String diseases_var = diseases.getText().toString();
                String txt_diseases_var = txtDiseases.getEditText().getText().toString();

                if (!fullname_var.isEmpty()) {
                    txtFullname.setError(null);
                    txtFullname.setErrorEnabled(false);
                    if (fullname_var.matches("^[\\p{L} .'-]+$")) {
                        txtFullname.setError(null);
                        txtFullname.setErrorEnabled(false);
                        if (!email_var.isEmpty()) {
                            txtEmail.setError(null);
                            txtEmail.setErrorEnabled(false);
                            if (!nic_var.isEmpty()) {
                                txtNic.setError(null);
                                txtNic.setErrorEnabled(false);
                                if (nic_var.matches("^([0-9]{9}[x|X|v|V]|[0-9]{12})$")) {
                                    txtNic.setError(null);
                                    txtNic.setErrorEnabled(false);
                                    if (email_var.matches("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+")) {
                                        txtEmail.setError(null);
                                        txtEmail.setErrorEnabled(false);
                                        if (!place_var.isEmpty()) {
                                            if (diseases_var.equals("Yes")) {
                                                if (!txt_diseases_var.isEmpty()) {
                                                    txtDiseases.setError(null);
                                                    txtDiseases.setErrorEnabled(false);
                                                    Intent intent = new Intent(UserSignup1Activity.this, UserSignup2Activity.class);

                                                    intent.putExtra("fullname", fullname_var);
                                                    intent.putExtra("email", email_var);
                                                    intent.putExtra("nic", nic_var);
                                                    intent.putExtra("district", districts_var);
                                                    intent.putExtra("diseases", diseases_var);
                                                    intent.putExtra("diseasesList", txt_diseases_var);
                                                    intent.putExtra("location", place_var);
                                                    intent.putExtra("address", address_var);
                                                    intent.putExtra("Flag", donorFlag);
                                                    intent.putExtra("passwordResetFlag", passwordResetVal);
                                                    intent.putExtra("donorRegistration", donorReg);

                                                    startActivityForResult(intent, PICK_MAP_POINT_REQUEST);
                                                } else {
                                                    txtDiseases.setError("List down diseases is mandatory");
                                                }
                                            } else {
                                                Intent intent = new Intent(UserSignup1Activity.this, UserSignup2Activity.class);

                                                intent.putExtra("fullname", fullname_var);
                                                intent.putExtra("email", email_var);
                                                intent.putExtra("nic", nic_var);
                                                intent.putExtra("district", districts_var);
                                                intent.putExtra("diseases", diseases_var);
                                                intent.putExtra("diseasesList", txt_diseases_var);
                                                intent.putExtra("location", place_var);
                                                intent.putExtra("address", address_var);
                                                intent.putExtra("Flag", donorFlag);
                                                intent.putExtra("passwordResetFlag", passwordResetVal);
                                                intent.putExtra("donorRegistration", donorReg);

                                                startActivityForResult(intent, PICK_MAP_POINT_REQUEST);
                                            }
                                        } else {
                                            Toast.makeText(getApplicationContext(), "Please select a location", Toast.LENGTH_SHORT).show();
                                        }
                                    } else {
                                        txtEmail.setError("Email format is incorrect");
                                    }

                                } else {
                                    txtNic.setError("Please enter valid NIC");
                                }
                            } else {
                                txtNic.setError("Please enter NIC");
                            }
                        } else {
                            txtEmail.setError("Please enter email");
                        }

                    } else {
                        txtFullname.setError("Invalid characters in full name");
                    }
                } else {
                    txtFullname.setError("Please enter full name");
                }

            }
        });

        buttonChooseLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickPointOnMap();
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
                    Intent intent = new Intent(getApplicationContext(), FacilitatorLeftNavActivity.class);
                    intent.putExtra("Flag", donorFlag);
                    intent.putExtra("passwordResetFlag", passwordResetVal);
                    intent.putExtra("donorRegistration", donorReg);
                    startActivity(intent);
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
                    Intent intent = new Intent(getApplicationContext(), LoginDecisionActivity.class);
                    intent.putExtra("Flag", donorFlag);
                    intent.putExtra("passwordResetFlag", passwordResetVal);
                    intent.putExtra("donorRegistration", donorReg);
                    startActivity(intent);
                }
            });

        }

        txtLocationHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LocationHelpActivity.class);
                intent.putExtra("Flag", donorFlag);
                intent.putExtra("passwordResetFlag", passwordResetVal);
                intent.putExtra("donorRegistration", donorReg);
                startActivityForResult(intent, PICK_MAP_POINT_REQUEST);
            }
        });

        txtDiseasesHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), DonorDiseasesActivity.class);
                intent.putExtra("Flag", donorFlag);
                intent.putExtra("passwordResetFlag", passwordResetVal);
                intent.putExtra("donorRegistration", donorReg);
                startActivityForResult(intent, PICK_MAP_POINT_REQUEST);
            }
        });

    }

    private void pickPointOnMap() {
        String activity = "userSignup";
        Intent pickPointIntent = new Intent(UserSignup1Activity.this, ChooseLocationActivity.class);
        pickPointIntent.putExtra("locationActivity", activity);
        pickPointIntent.putExtra("Flag", donorFlag);
        pickPointIntent.putExtra("passwordResetFlag", passwordResetVal);
        pickPointIntent.putExtra("donorRegistration", donorReg);
        startActivityForResult(pickPointIntent, PICK_MAP_POINT_REQUEST);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_MAP_POINT_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {
                String locationAddressLong = data.getStringExtra("myLocation");
                longAddress = data.getStringExtra("longAddress");
                txtPlace.setText(String.format("%s", locationAddressLong));
                txtAddress.setText(String.format("%s", longAddress));
            }

        }
    }

    @Override
    public void onBackPressed() {

    }
}