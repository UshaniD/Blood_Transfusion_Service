package com.example.bloodtransfusionservice;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.security.MessageDigest;
import java.util.Calendar;
import java.util.HashMap;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class UserProfileActivity extends AppCompatActivity {
    TextInputLayout txtFullname, txtEmail, txtNic, txtPassword, txtDiseases;
    TextView txtPhone;
    AutoCompleteTextView districts, bloodGroups, diseases;
    TextView txtPlace, txtAddress;
    Button buttonChooseLocation, buttonUpdate, buttonCancel;
    DatePicker datePicker_dob;
    RadioButton male, female, other;
    static final int PICK_MAP_POINT_REQUEST = 1;
    DatabaseReference reference;
    String gender = "";
    FirebaseDatabase firebaseDatabase;
    String longAddress;
    String noOfDonationsFromDb;
    String dateLastDonated_var;
    String userStatusFromDb;
    String encryptedPassword;
    String decyptedPassword;
    String AES = "AES";
    String district_var_db;
    String diseases_var_db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        reference = FirebaseDatabase.getInstance().getReference("donor");

        //Get phone number from session
        SessionManager sessionManager = new SessionManager(this, SessionManager.SESSION_USER_SESSION);
        HashMap<String, String> userDetails = sessionManager.getUserDetailFromSession();
        String phone = userDetails.get(SessionManager.KEY_PHONE_NO);
        Log.d("PHONE: ", String.valueOf(phone));

        txtFullname = findViewById(R.id.input_fullname);
        txtEmail = findViewById(R.id.input_email);
        txtNic = findViewById(R.id.input_nic);
        districts = findViewById(R.id.districts);
        bloodGroups = findViewById(R.id.blood_group);
        txtPlace = findViewById(R.id.txt_place);
        txtAddress = findViewById(R.id.txt_address);
        buttonChooseLocation = findViewById(R.id.btn_choose_location);
        datePicker_dob = findViewById(R.id.input_dob);
        male = findViewById(R.id.input_male);
        female = findViewById(R.id.input_female);
        other = findViewById(R.id.input_other);
        txtPhone = findViewById(R.id.txt_phone_no);
        txtPassword = findViewById(R.id.input_password);
        buttonUpdate = findViewById(R.id.btn_user_update);
        buttonCancel = findViewById(R.id.btn_user_cancel);
        diseases = findViewById(R.id.diseases_dropdown);
        txtDiseases = findViewById(R.id.input_diseases);

        //Display user profile data by getting from table
        showAllUserData(phone);

        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePicker_dob = findViewById(R.id.input_dob);
                int day = datePicker_dob.getDayOfMonth();
                int month = datePicker_dob.getMonth() + 1;
                int year = datePicker_dob.getYear();
                String dob_ = "" + year + "/" + month + "/" + day;

                String fullname_var = txtFullname.getEditText().getText().toString();
                String email_var = txtEmail.getEditText().getText().toString();
                String nic_var = txtNic.getEditText().getText().toString();
                String districts_var = districts.getText().toString();
                String place_var = txtPlace.getText().toString();
                String address_var = txtAddress.getText().toString();
                String password_var = txtPassword.getEditText().getText().toString();
                String blood_groups_var = bloodGroups.getText().toString();
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
                                        if (!password_var.isEmpty()) {
                                            txtPassword.setError(null);
                                            txtPassword.setErrorEnabled(false);
                                            if (password_var.matches("((?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{6,20})")) {
                                                txtPassword.setError(null);
                                                txtPassword.setErrorEnabled(false);

                                                /* Encrypt password */
                                                String key_ = "bts";

                                                try {
                                                    encryptedPassword = encrypt(password_var, key_);
                                                    Log.d("encryptedPassword: ", String.valueOf(encryptedPassword));
                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }

                                                if (!place_var.isEmpty()) {
                                                    if (diseases_var.equals("Yes")) {
                                                        if (!txt_diseases_var.isEmpty()) {
                                                            txtDiseases.setError(null);
                                                            txtDiseases.setErrorEnabled(false);

                                                            if (getAge(year, month, day) >= 18 || getAge(year, month, day) < 60) {
                                                                if (!male.isChecked() && !female.isChecked() && !other.isChecked()) {
                                                                    Toast.makeText(UserProfileActivity.this, "Please select gender", Toast.LENGTH_SHORT).show();
                                                                } else if (male.isChecked()) {
                                                                    gender = "Male";

                                                                    if (!districts_var.equals(district_var_db)) {
                                                                        new AlertDialog.Builder(UserProfileActivity.this)
                                                                                .setTitle("Profile Update")
                                                                                .setMessage("Want to change your district? You will no longer getting information related to " + district_var_db + " district.")
                                                                                .setIcon(android.R.drawable.ic_dialog_alert)
                                                                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                                                                    public void onClick(DialogInterface dialog, int whichButton) {
                                                                                        firebaseDatabase = FirebaseDatabase.getInstance();
                                                                                        reference = firebaseDatabase.getReference("donor");
                                                                                        storeData storedata = new storeData(phone, fullname_var, email_var, nic_var, encryptedPassword, blood_groups_var, districts_var, dateLastDonated_var, dob_, gender, place_var, address_var, noOfDonationsFromDb, userStatusFromDb, diseases_var, txt_diseases_var);
                                                                                        reference.child(phone).setValue(storedata);

                                                                                        Toast.makeText(getApplicationContext(), "Updated successfully", Toast.LENGTH_SHORT).show();
                                                                                        Intent intent = new Intent(UserProfileActivity.this, LeftNavActivity.class);
                                                                                        intent.putExtra("mobile", phone);
                                                                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                                                        startActivityForResult(intent, PICK_MAP_POINT_REQUEST);
                                                                                        finish();
                                                                                    }
                                                                                })
                                                                                .setNegativeButton(android.R.string.no, null).show();
                                                                    } else {
                                                                        firebaseDatabase = FirebaseDatabase.getInstance();
                                                                        reference = firebaseDatabase.getReference("donor");
                                                                        storeData storedata = new storeData(phone, fullname_var, email_var, nic_var, encryptedPassword, blood_groups_var, districts_var, dateLastDonated_var, dob_, gender, place_var, address_var, noOfDonationsFromDb, userStatusFromDb, diseases_var, txt_diseases_var);
                                                                        reference.child(phone).setValue(storedata);

                                                                        Toast.makeText(getApplicationContext(), "Updated successfully", Toast.LENGTH_SHORT).show();
                                                                        Intent intent = new Intent(UserProfileActivity.this, LeftNavActivity.class);
                                                                        intent.putExtra("mobile", phone);
                                                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                                        startActivityForResult(intent, PICK_MAP_POINT_REQUEST);
                                                                        finish();
                                                                    }

                                                                } else if (female.isChecked()) {
                                                                    gender = "Female";
                                                                    if (!districts_var.equals(district_var_db)) {
                                                                        new AlertDialog.Builder(UserProfileActivity.this)
                                                                                .setTitle("Profile Update")
                                                                                .setMessage("Want to change your district? You will no longer getting information related to " + district_var_db + " district.")
                                                                                .setIcon(android.R.drawable.ic_dialog_alert)
                                                                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                                                                    public void onClick(DialogInterface dialog, int whichButton) {
                                                                                        firebaseDatabase = FirebaseDatabase.getInstance();
                                                                                        reference = firebaseDatabase.getReference("donor");
                                                                                        storeData storedata = new storeData(phone, fullname_var, email_var, nic_var, encryptedPassword, blood_groups_var, districts_var, dateLastDonated_var, dob_, gender, place_var, address_var, noOfDonationsFromDb, userStatusFromDb, diseases_var, txt_diseases_var);
                                                                                        reference.child(phone).setValue(storedata);

                                                                                        Toast.makeText(getApplicationContext(), "Updated successfully", Toast.LENGTH_SHORT).show();
                                                                                        Intent intent = new Intent(UserProfileActivity.this, LeftNavActivity.class);
                                                                                        intent.putExtra("mobile", phone);
                                                                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                                                        startActivityForResult(intent, PICK_MAP_POINT_REQUEST);
                                                                                        finish();
                                                                                    }
                                                                                })
                                                                                .setNegativeButton(android.R.string.no, null).show();
                                                                    } else {
                                                                        firebaseDatabase = FirebaseDatabase.getInstance();
                                                                        reference = firebaseDatabase.getReference("donor");
                                                                        storeData storedata = new storeData(phone, fullname_var, email_var, nic_var, encryptedPassword, blood_groups_var, districts_var, dateLastDonated_var, dob_, gender, place_var, address_var, noOfDonationsFromDb, userStatusFromDb, diseases_var, txt_diseases_var);
                                                                        reference.child(phone).setValue(storedata);

                                                                        Toast.makeText(getApplicationContext(), "Updated successfully", Toast.LENGTH_SHORT).show();
                                                                        Intent intent = new Intent(UserProfileActivity.this, LeftNavActivity.class);
                                                                        intent.putExtra("mobile", phone);
                                                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                                        startActivityForResult(intent, PICK_MAP_POINT_REQUEST);
                                                                        finish();
                                                                    }

                                                                } else {
                                                                    gender = "Other";
                                                                    if (!districts_var.equals(district_var_db)) {
                                                                        new AlertDialog.Builder(UserProfileActivity.this)
                                                                                .setTitle("Profile Update")
                                                                                .setMessage("Want to change your district? You will no longer getting information related to " + district_var_db + " district.")
                                                                                .setIcon(android.R.drawable.ic_dialog_alert)
                                                                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                                                                    public void onClick(DialogInterface dialog, int whichButton) {
                                                                                        firebaseDatabase = FirebaseDatabase.getInstance();
                                                                                        reference = firebaseDatabase.getReference("donor");
                                                                                        storeData storedata = new storeData(phone, fullname_var, email_var, nic_var, encryptedPassword, blood_groups_var, districts_var, dateLastDonated_var, dob_, gender, place_var, address_var, noOfDonationsFromDb, userStatusFromDb, diseases_var, txt_diseases_var);
                                                                                        reference.child(phone).setValue(storedata);

                                                                                        Toast.makeText(getApplicationContext(), "Updated successfully", Toast.LENGTH_SHORT).show();
                                                                                        Intent intent = new Intent(UserProfileActivity.this, LeftNavActivity.class);
                                                                                        intent.putExtra("mobile", phone);
                                                                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                                                        startActivityForResult(intent, PICK_MAP_POINT_REQUEST);
                                                                                        finish();
                                                                                    }
                                                                                })
                                                                                .setNegativeButton(android.R.string.no, null).show();
                                                                    } else {
                                                                        firebaseDatabase = FirebaseDatabase.getInstance();
                                                                        reference = firebaseDatabase.getReference("donor");
                                                                        storeData storedata = new storeData(phone, fullname_var, email_var, nic_var, encryptedPassword, blood_groups_var, districts_var, dateLastDonated_var, dob_, gender, place_var, address_var, noOfDonationsFromDb, userStatusFromDb, diseases_var, txt_diseases_var);
                                                                        reference.child(phone).setValue(storedata);

                                                                        Toast.makeText(getApplicationContext(), "Updated successfully", Toast.LENGTH_SHORT).show();
                                                                        Intent intent = new Intent(UserProfileActivity.this, LeftNavActivity.class);
                                                                        intent.putExtra("mobile", phone);
                                                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                                        startActivityForResult(intent, PICK_MAP_POINT_REQUEST);
                                                                        finish();
                                                                    }
                                                                }
                                                            } else {
                                                                Toast.makeText(UserProfileActivity.this, "You should be between 18 years and 60 years to be eligible to donate blood..!!", Toast.LENGTH_SHORT).show();
                                                            }
                                                        } else {
                                                            txtDiseases.setError("List down diseases is mandatory");
                                                        }
                                                    } else {
                                                        if (getAge(year, month, day) >= 18 || getAge(year, month, day) < 60) {
                                                            if (!male.isChecked() && !female.isChecked() && !other.isChecked()) {
                                                                Toast.makeText(UserProfileActivity.this, "Please select gender", Toast.LENGTH_SHORT).show();
                                                            } else if (male.isChecked()) {
                                                                gender = "Male";

                                                                if (!districts_var.equals(district_var_db)) {
                                                                    new AlertDialog.Builder(UserProfileActivity.this)
                                                                            .setTitle("Profile Update")
                                                                            .setMessage("Want to change your district? You will no longer getting information related to " + district_var_db + " district.")
                                                                            .setIcon(android.R.drawable.ic_dialog_alert)
                                                                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                                                                public void onClick(DialogInterface dialog, int whichButton) {
                                                                                    firebaseDatabase = FirebaseDatabase.getInstance();
                                                                                    reference = firebaseDatabase.getReference("donor");
                                                                                    storeData storedata = new storeData(phone, fullname_var, email_var, nic_var, encryptedPassword, blood_groups_var, districts_var, dateLastDonated_var, dob_, gender, place_var, address_var, noOfDonationsFromDb, userStatusFromDb, diseases_var, txt_diseases_var);
                                                                                    reference.child(phone).setValue(storedata);

                                                                                    Toast.makeText(getApplicationContext(), "Updated successfully", Toast.LENGTH_SHORT).show();
                                                                                    Intent intent = new Intent(UserProfileActivity.this, LeftNavActivity.class);
                                                                                    intent.putExtra("mobile", phone);
                                                                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                                                    startActivityForResult(intent, PICK_MAP_POINT_REQUEST);
                                                                                    finish();
                                                                                }
                                                                            })
                                                                            .setNegativeButton(android.R.string.no, null).show();
                                                                } else {
                                                                    firebaseDatabase = FirebaseDatabase.getInstance();
                                                                    reference = firebaseDatabase.getReference("donor");
                                                                    storeData storedata = new storeData(phone, fullname_var, email_var, nic_var, encryptedPassword, blood_groups_var, districts_var, dateLastDonated_var, dob_, gender, place_var, address_var, noOfDonationsFromDb, userStatusFromDb, diseases_var, txt_diseases_var);
                                                                    reference.child(phone).setValue(storedata);

                                                                    Toast.makeText(getApplicationContext(), "Updated successfully", Toast.LENGTH_SHORT).show();
                                                                    Intent intent = new Intent(UserProfileActivity.this, LeftNavActivity.class);
                                                                    intent.putExtra("mobile", phone);
                                                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                                    startActivityForResult(intent, PICK_MAP_POINT_REQUEST);
                                                                    finish();
                                                                }

                                                            } else if (female.isChecked()) {
                                                                gender = "Female";
                                                                if (!districts_var.equals(district_var_db)) {
                                                                    new AlertDialog.Builder(UserProfileActivity.this)
                                                                            .setTitle("Profile Update")
                                                                            .setMessage("Want to change your district? You will no longer getting information related to " + district_var_db + " district.")
                                                                            .setIcon(android.R.drawable.ic_dialog_alert)
                                                                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                                                                public void onClick(DialogInterface dialog, int whichButton) {
                                                                                    firebaseDatabase = FirebaseDatabase.getInstance();
                                                                                    reference = firebaseDatabase.getReference("donor");
                                                                                    storeData storedata = new storeData(phone, fullname_var, email_var, nic_var, encryptedPassword, blood_groups_var, districts_var, dateLastDonated_var, dob_, gender, place_var, address_var, noOfDonationsFromDb, userStatusFromDb, diseases_var, txt_diseases_var);
                                                                                    reference.child(phone).setValue(storedata);

                                                                                    Toast.makeText(getApplicationContext(), "Updated successfully", Toast.LENGTH_SHORT).show();
                                                                                    Intent intent = new Intent(UserProfileActivity.this, LeftNavActivity.class);
                                                                                    intent.putExtra("mobile", phone);
                                                                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                                                    startActivityForResult(intent, PICK_MAP_POINT_REQUEST);
                                                                                    finish();
                                                                                }
                                                                            })
                                                                            .setNegativeButton(android.R.string.no, null).show();
                                                                } else {
                                                                    firebaseDatabase = FirebaseDatabase.getInstance();
                                                                    reference = firebaseDatabase.getReference("donor");
                                                                    storeData storedata = new storeData(phone, fullname_var, email_var, nic_var, encryptedPassword, blood_groups_var, districts_var, dateLastDonated_var, dob_, gender, place_var, address_var, noOfDonationsFromDb, userStatusFromDb, diseases_var, txt_diseases_var);
                                                                    reference.child(phone).setValue(storedata);

                                                                    Toast.makeText(getApplicationContext(), "Updated successfully", Toast.LENGTH_SHORT).show();
                                                                    Intent intent = new Intent(UserProfileActivity.this, LeftNavActivity.class);
                                                                    intent.putExtra("mobile", phone);
                                                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                                    startActivityForResult(intent, PICK_MAP_POINT_REQUEST);
                                                                    finish();
                                                                }

                                                            } else {
                                                                gender = "Other";
                                                                if (!districts_var.equals(district_var_db)) {
                                                                    new AlertDialog.Builder(UserProfileActivity.this)
                                                                            .setTitle("Profile Update")
                                                                            .setMessage("Want to change your district? You will no longer getting information related to " + district_var_db + " district.")
                                                                            .setIcon(android.R.drawable.ic_dialog_alert)
                                                                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                                                                public void onClick(DialogInterface dialog, int whichButton) {
                                                                                    firebaseDatabase = FirebaseDatabase.getInstance();
                                                                                    reference = firebaseDatabase.getReference("donor");
                                                                                    storeData storedata = new storeData(phone, fullname_var, email_var, nic_var, encryptedPassword, blood_groups_var, districts_var, dateLastDonated_var, dob_, gender, place_var, address_var, noOfDonationsFromDb, userStatusFromDb, diseases_var, txt_diseases_var);
                                                                                    reference.child(phone).setValue(storedata);

                                                                                    Toast.makeText(getApplicationContext(), "Updated successfully", Toast.LENGTH_SHORT).show();
                                                                                    Intent intent = new Intent(UserProfileActivity.this, LeftNavActivity.class);
                                                                                    intent.putExtra("mobile", phone);
                                                                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                                                    startActivityForResult(intent, PICK_MAP_POINT_REQUEST);
                                                                                    finish();
                                                                                }
                                                                            })
                                                                            .setNegativeButton(android.R.string.no, null).show();
                                                                } else {
                                                                    firebaseDatabase = FirebaseDatabase.getInstance();
                                                                    reference = firebaseDatabase.getReference("donor");
                                                                    storeData storedata = new storeData(phone, fullname_var, email_var, nic_var, encryptedPassword, blood_groups_var, districts_var, dateLastDonated_var, dob_, gender, place_var, address_var, noOfDonationsFromDb, userStatusFromDb, diseases_var, txt_diseases_var);
                                                                    reference.child(phone).setValue(storedata);

                                                                    Toast.makeText(getApplicationContext(), "Updated successfully", Toast.LENGTH_SHORT).show();
                                                                    Intent intent = new Intent(UserProfileActivity.this, LeftNavActivity.class);
                                                                    intent.putExtra("mobile", phone);
                                                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                                    startActivityForResult(intent, PICK_MAP_POINT_REQUEST);
                                                                    finish();
                                                                }
                                                            }
                                                        } else {
                                                            Toast.makeText(UserProfileActivity.this, "You should be between 18 years and 60 years to be eligible to donate blood..!!", Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                } else {
                                                    Toast.makeText(getApplicationContext(), "Please select a location", Toast.LENGTH_SHORT).show();
                                                }
                                            } else {
                                                txtPassword.setError("Your password should contains lowercase,uppercase letters and must length within 6-20");
                                            }

                                        } else {
                                            txtPassword.setError("Please enter password");
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

    }

    private void pickPointOnMap() {
        String activity = "userProfileUpdate";
        Intent pickPointIntent = new Intent(UserProfileActivity.this, ChooseLocationActivity.class);
        pickPointIntent.putExtra("locationActivity", activity);
        startActivityForResult(pickPointIntent, PICK_MAP_POINT_REQUEST);

    }

    private void showAllUserData(String phone_var) {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference("donor");
        Query checkmobile = databaseReference.orderByChild("mobile").equalTo(phone_var);

        checkmobile.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    //Get all user data from donor table
                    String fullname_var = snapshot.child(phone_var).child("fullname").getValue(String.class);
                    String email_var = snapshot.child(phone_var).child("email").getValue(String.class);
                    String nic_var = snapshot.child(phone_var).child("nic").getValue(String.class);
                    district_var_db = snapshot.child(phone_var).child("district").getValue(String.class);
                    diseases_var_db = snapshot.child(phone_var).child("diseases").getValue(String.class);
                    String diseases_list_var_db = snapshot.child(phone_var).child("diseasesList").getValue(String.class);
                    String location_var = snapshot.child(phone_var).child("location").getValue(String.class);
                    String address_var = snapshot.child(phone_var).child("address").getValue(String.class);
                    String dob_var = snapshot.child(phone_var).child("dob").getValue(String.class);
                    String gender_var = snapshot.child(phone_var).child("gender").getValue(String.class);
                    String bloodGroup_var = snapshot.child(phone_var).child("bloodGroup").getValue(String.class);
                    Log.d("BLOOD GROUP: ", String.valueOf(bloodGroup_var));
                    dateLastDonated_var = snapshot.child(phone_var).child("dateLastDonated").getValue(String.class);
                    String password_var = snapshot.child(phone_var).child("password").getValue(String.class);
                    noOfDonationsFromDb = snapshot.child(phone_var).child("noOfDonations").getValue(String.class);
                    userStatusFromDb = snapshot.child(phone_var).child("userStatus").getValue(String.class);

                    /* Decrypt password */
                    String key_ = "bts"; //key to decrypt
                    try {
                        decyptedPassword = decrypt(password_var, key_);
                        Log.d("decyptedPassword: ", String.valueOf(decyptedPassword));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    //Set values
                    txtPhone.setText(phone_var);
                    txtFullname.getEditText().setText(fullname_var);
                    txtEmail.getEditText().setText(email_var);
                    txtNic.getEditText().setText(nic_var);
                    txtPassword.getEditText().setText(decyptedPassword);
                    txtPlace.setText(location_var);
                    txtAddress.setText(address_var);
                    txtDiseases.getEditText().setText(diseases_list_var_db);

                    //Set district
                    String[] district_options = {"Kandy", "Kalutara", "Gampaha", "Matale", "Colombo", "Nuwara Eliya", "Matara", "Galle", "Hambantota",
                            "Badulla", "Monaragala", "Kegalle", "Ratnapura", "Auradhapura", "Polonnaruwa", "Trincomalee", "Batticaloa", "Ampara", "Puttalam", "Kurunegala",
                            "Jaffna", "Kilinochchi", "Mannar", "Mulaitivu", "Vavuniya"};
                    ArrayAdapter arrayAdapter1 = new ArrayAdapter(UserProfileActivity.this, R.layout.format_dropdown, district_options);
                    for (int i = 0; i < district_options.length; i++) {
                        if (arrayAdapter1.getItem(i).toString().equals(district_var_db)) {
                            districts.setText(arrayAdapter1.getItem(i).toString(), false);
                            districts.setAdapter(arrayAdapter1);
                            break;
                        }
                    }

                    //Set diseases
                    String[] diseases_options = {"No", "Yes"};
                    ArrayAdapter arrayAdapter3 = new ArrayAdapter(UserProfileActivity.this, R.layout.format_dropdown, diseases_options);
                    for (int i = 0; i < diseases_options.length; i++) {
                        if (arrayAdapter3.getItem(i).toString().equals(diseases_var_db)) {
                            diseases.setText(arrayAdapter3.getItem(i).toString(), false);
                            diseases.setAdapter(arrayAdapter3);
                            break;
                        }
                    }

                    //Set blood group
                    String[] blood_groups_option = {"A-", "A+", "B+", "B-", "AB+", "AB-", "O+", "O-"};
                    ArrayAdapter arrayAdapter2 = new ArrayAdapter(UserProfileActivity.this, R.layout.format_dropdown, blood_groups_option);
                    for (int i = 0; i < blood_groups_option.length; i++) {
                        Log.d("ITEM: ", arrayAdapter2.getItem(i).toString());
                        if (arrayAdapter2.getItem(i).toString().equals(bloodGroup_var)) {
                            bloodGroups.setText(arrayAdapter2.getItem(i).toString(), false);
                            bloodGroups.setAdapter(arrayAdapter2);
                            break;
                        }
                    }

                    //Set date of birth
                    String[] split = dob_var.split("/");
                    int year = Integer.valueOf(split[0]);
                    Log.d("YEAR: ", String.valueOf(year));
                    int month = Integer.valueOf(split[1]);
                    Log.d("MONTH: ", String.valueOf(month));
                    int day = Integer.valueOf(split[2]);
                    Log.d("DAY: ", String.valueOf(day));
                    datePicker_dob.updateDate(year, month - 1, day);

                    //Set gender
                    if (gender_var.equals("Female")) {
                        female.setChecked(true);
                    } else if (gender_var.equals("Male")) {
                        male.setChecked(true);
                    } else {
                        other.setChecked(true);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }

    /* Decrypt password */
    private String decrypt(String password, String keyVal) throws Exception {
        SecretKeySpec key = generateKey(keyVal);
        Cipher c = Cipher.getInstance(AES);
        c.init(Cipher.DECRYPT_MODE, key);
        byte[] decodedVal = Base64.decode(password, Base64.DEFAULT);
        byte[] decVal = c.doFinal(decodedVal);
        String decryptedVal = new String(decVal);
        return decryptedVal;
    }

    /* Encrypt password */
    private String encrypt(String password, String keyVal) throws Exception {
        SecretKeySpec key = generateKey(keyVal);
        Cipher c = Cipher.getInstance(AES);
        c.init(Cipher.ENCRYPT_MODE, key);
        byte[] encVal = c.doFinal(password.getBytes());
        String encryptedVal = Base64.encodeToString(encVal, Base64.DEFAULT);
        return encryptedVal;
    }

    private SecretKeySpec generateKey(String keyVal) throws Exception {
        final MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] bytes = keyVal.getBytes("UTF-8");
        digest.update(bytes, 0, bytes.length);
        byte[] key = digest.digest();
        SecretKeySpec secretKeySpec = new SecretKeySpec(key, "AES");
        return secretKeySpec;
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
            if (resultCode == Activity.RESULT_OK) {
                String locationAddressLong = data.getStringExtra("myLocation");
                longAddress = data.getStringExtra("longAddress");
                txtPlace.setText(String.format("%s", locationAddressLong));
                txtAddress.setText(String.format("%s", longAddress));
            }

        }
    }

    public void moveToPreviousScreen(View view) {
        Intent intent = new Intent(getApplicationContext(), LeftNavActivity.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {

    }

}