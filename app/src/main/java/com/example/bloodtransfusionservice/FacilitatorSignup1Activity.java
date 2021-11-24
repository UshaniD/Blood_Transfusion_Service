package com.example.bloodtransfusionservice;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

public class FacilitatorSignup1Activity extends AppCompatActivity {
    AutoCompleteTextView districts;
    TextInputLayout txtFullname, txtEmail, txtNic;
    Button buttonUserNext, buttonChooseLocation;
    TextView txtPlace, txtAddress, txtLocationHelp;
    static final int PICK_MAP_POINT_REQUEST = 1;
    String donorFlag, donorReg;
    String passwordResetVal;
    String longAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facilitator_signup1);

        donorFlag = getIntent().getStringExtra("Flag");
        passwordResetVal = getIntent().getStringExtra("passwordResetFlag");
        donorReg = getIntent().getStringExtra("donorRegistration");

        txtLocationHelp = findViewById(R.id.locationHelp);
        buttonUserNext = findViewById(R.id.btn_user_next);
        buttonChooseLocation = findViewById(R.id.btn_choose_location);
        txtPlace = findViewById(R.id.txt_place);
        txtAddress = findViewById(R.id.txt_address);
        districts = findViewById(R.id.districts);
        txtFullname = findViewById(R.id.input_fullname);
        txtEmail = findViewById(R.id.input_email);
        txtNic = findViewById(R.id.input_nic);

        String[] district_options = {"Colombo", "Kalutara", "Gampaha", "Matale", "Kandy", "Nuwara Eliya", "Matara", "Galle", "Hambantota",
                "Badulla", "Monaragala", "Kegalle", "Ratnapura", "Auradhapura", "Polonnaruwa", "Trincomalee", "Batticaloa", "Ampara", "Puttalam", "Kurunegala",
                "Jaffna", "Kilinochchi", "Mannar", "Mulaitivu", "Vavuniya"};
        ArrayAdapter arrayAdapter1 = new ArrayAdapter(this, R.layout.format_dropdown, district_options);
        districts.setText(arrayAdapter1.getItem(0).toString(), false);
        districts.setAdapter(arrayAdapter1);

        buttonUserNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public final void onClick(View v) {
                String fullname_var = txtFullname.getEditText().getText().toString();
                String email_var = txtEmail.getEditText().getText().toString();
                String nic_var = txtNic.getEditText().getText().toString();
                String districts_var = districts.getText().toString();
                String place_var = txtPlace.getText().toString();
                String address_var = txtAddress.getText().toString();

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
                                        if (!place_var.isEmpty()) {
                                            Intent intent = new Intent(getApplicationContext(), FacilitatorSignup2Activity.class);
                                            intent.putExtra("fullname", fullname_var);
                                            intent.putExtra("email", email_var);
                                            intent.putExtra("nic", nic_var);
                                            intent.putExtra("district", districts_var);
                                            intent.putExtra("Flag", donorFlag);
                                            intent.putExtra("passwordResetFlag", passwordResetVal);
                                            intent.putExtra("donorRegistration", donorReg);
                                            intent.putExtra("address", address_var);
                                            intent.putExtra("location", place_var);
                                            Log.d("========MY-LOCATION: ", String.valueOf(place_var));
                                            startActivity(intent);

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

        txtLocationHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LocationHelpActivity.class);
                intent.putExtra("Flag", donorFlag);
                intent.putExtra("passwordResetFlag", passwordResetVal);
                intent.putExtra("donorRegistration", donorReg);
                startActivity(intent);
            }
        });

    }

    private void pickPointOnMap() {
        String activity = "facilitatorSignup";
        String donorReg = "false";
        Intent pickPointIntent = new Intent(FacilitatorSignup1Activity.this, ChooseLocationActivity.class);
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

    public void moveToLoginScreen(View view) {
        String donorFlag = getIntent().getStringExtra("Flag");
        Intent intent = new Intent(getApplicationContext(), UserLoginActivity.class);
        intent.putExtra("Flag", donorFlag);
        intent.putExtra("passwordResetFlag", passwordResetVal);
        intent.putExtra("donorRegistration", donorReg);
        startActivity(intent);
    }

    public void moveToPreviousScreen(View view) {
        String donorFlag = getIntent().getStringExtra("Flag");
        Intent intent = new Intent(getApplicationContext(), LoginDecisionActivity.class);
        intent.putExtra("Flag", donorFlag);
        intent.putExtra("passwordResetFlag", passwordResetVal);
        intent.putExtra("donorRegistration", donorReg);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {

    }
}