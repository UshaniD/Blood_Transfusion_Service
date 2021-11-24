package com.example.bloodtransfusionservice;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class VerifyBloodStockActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    VerifyUserInfoHolder verifyUserInfoHolder;
    ArrayList<storeData> list;
    TextInputLayout txtBloodStockUnits;
    Button buttonUpdate, buttonCancel;
    ImageView backImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_blood_stock);

        //Entered donor mobile number
        String phone = getIntent().getStringExtra("mobile");
        backImg = findViewById(R.id.img_back);

        //Get facilitator phone number from session
        SessionManager sessionManager = new SessionManager(this, SessionManager.SESSION_USER_SESSION);
        HashMap<String, String> userDetails = sessionManager.getUserDetailFromSession();
        String phoneUser = userDetails.get(SessionManager.KEY_PHONE_NO);
        Log.d("PHONE SESSION", String.valueOf(phoneUser));

        recyclerView = findViewById(R.id.verify_user_recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        list = new ArrayList<>();
        verifyUserInfoHolder = new VerifyUserInfoHolder(this, list);
        recyclerView.setAdapter(verifyUserInfoHolder);
        txtBloodStockUnits = findViewById(R.id.input_count);
        buttonUpdate = findViewById(R.id.btn_update);
        buttonCancel = findViewById(R.id.btn_cancel);

        updateUserInfo(phone);

        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String bloodUnits = txtBloodStockUnits.getEditText().getText().toString();
                if (!bloodUnits.isEmpty()) {
                    txtBloodStockUnits.setError(null);
                    txtBloodStockUnits.setErrorEnabled(false);
                    Double unit_var = Double.parseDouble(bloodUnits);
                    Log.d("UNIT_VAR", String.valueOf(unit_var));

                    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                    DatabaseReference databaseReference = firebaseDatabase.getReference("facilitator");
                    Query checkDistrict = databaseReference.orderByChild("mobile").equalTo(phoneUser);

                    checkDistrict.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                String district_var = snapshot.child(phoneUser).child("district").getValue(String.class);
                                Log.d("DISTRICT_VAR", String.valueOf(district_var));

                                FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                                DatabaseReference databaseReference = firebaseDatabase.getReference("donor");
                                Query checkBloodGroup = databaseReference.orderByChild("mobile").equalTo(phone);

                                checkBloodGroup.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                                        if (snapshot.exists()) {
                                            String bloodGroup_var = snapshot.child(phone).child("bloodGroup").getValue(String.class);
                                            String noOfDonations_ = snapshot.child(phone).child("noOfDonations").getValue(String.class);
                                            int noOfDonations_var = Integer.parseInt(noOfDonations_);
                                            Log.d("BLOOD_GROUP_VAR", String.valueOf(bloodGroup_var));

                                            FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                                            DatabaseReference databaseReference = firebaseDatabase.getReference("stock");
                                            Query query = databaseReference.child(district_var).child(bloodGroup_var);

                                            query.addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                    if (snapshot.exists()) {
                                                        String unit = snapshot.child("unit").getValue().toString();
                                                        Log.d("UNIT", String.valueOf(unit));
                                                        Double ext_unit_var = Double.parseDouble(unit);
                                                        Double sum = ext_unit_var + unit_var;
                                                        String sumString = String.valueOf(sum);
                                                        new AlertDialog.Builder(VerifyBloodStockActivity.this)
                                                                .setTitle("Verify Blood Stock Update")
                                                                .setMessage("New blood stock is: " + sumString + " Want to update blood stock?")
                                                                .setIcon(android.R.drawable.ic_dialog_alert)
                                                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                                                    public void onClick(DialogInterface dialog, int whichButton) {
                                                                        databaseReference.child(district_var).child(bloodGroup_var).child("unit").setValue(sumString);
                                                                        Toast.makeText(getApplicationContext(), "Blood stock updated successfully!", Toast.LENGTH_SHORT).show();

                                                                        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
                                                                        Date date = new Date();
                                                                        String currentDate = dateFormat.format(date);
                                                                        Log.d("Current date", String.valueOf(currentDate));

                                                                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("donor").child(phone);
                                                                        Map<String, Object> updates = new HashMap<String, Object>();
                                                                        updates.put("dateLastDonated", currentDate);
                                                                        int donation = noOfDonations_var + 1;
                                                                        String donationConverted = Integer.toString(donation);
                                                                        updates.put("noOfDonations", String.valueOf(donationConverted));
                                                                        ref.updateChildren(updates);

                                                                        String id = UUID.randomUUID().toString();

                                                                        DatabaseReference databaseReference1 = firebaseDatabase.getReference("bloodReceivedRegistry");
                                                                        storeBloodReceivedData storeBloodReceivedData = new storeBloodReceivedData(id, phone, bloodGroup_var, bloodUnits, currentDate, district_var, phoneUser);
                                                                        databaseReference1.child(id).setValue(storeBloodReceivedData);

                                                                        Intent intent = new Intent(getApplicationContext(), UpdateBloodStockActivity.class);
                                                                        startActivity(intent);
                                                                    }
                                                                })
                                                                .setNegativeButton(android.R.string.no, null).show();
                                                    } else {
                                                        new AlertDialog.Builder(VerifyBloodStockActivity.this)
                                                                .setTitle("Verify Blood Stock Update")
                                                                .setMessage("New blood stock is: " + bloodUnits + " Want to update blood stock?")
                                                                .setIcon(android.R.drawable.ic_dialog_alert)
                                                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                                                    public void onClick(DialogInterface dialog, int whichButton) {
                                                                        storeStockData storeStockData = new storeStockData(bloodGroup_var, district_var, bloodUnits);
                                                                        databaseReference.child(district_var).child(bloodGroup_var).setValue(storeStockData);

                                                                        Toast.makeText(getApplicationContext(), "Updated new blood stock record successfully!", Toast.LENGTH_SHORT).show();

                                                                        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
                                                                        Date date = new Date();
                                                                        String currentDate = dateFormat.format(date);
                                                                        Log.d("Current date", String.valueOf(currentDate));

                                                                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("donor").child(phone);
                                                                        Map<String, Object> updates = new HashMap<String, Object>();
                                                                        int donation = noOfDonations_var + 1;
                                                                        String donationConverted = Integer.toString(donation);
                                                                        updates.put("noOfDonations", String.valueOf(donationConverted));
                                                                        updates.put("dateLastDonated", currentDate);
                                                                        ref.updateChildren(updates);

                                                                        String id = UUID.randomUUID().toString();

                                                                        DatabaseReference databaseReference1 = firebaseDatabase.getReference("bloodReceivedRegistry");
                                                                        storeBloodReceivedData storeBloodReceivedData = new storeBloodReceivedData(id, phone, bloodGroup_var, bloodUnits, currentDate, district_var, phoneUser);
                                                                        databaseReference1.child(id).setValue(storeBloodReceivedData);

                                                                        Intent intent = new Intent(getApplicationContext(), UpdateBloodStockActivity.class);
                                                                        startActivity(intent);
                                                                    }
                                                                })
                                                                .setNegativeButton(android.R.string.no, null).show();
                                                    }
                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError error) {

                                                }
                                            });
                                        } else {
                                            FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                                            DatabaseReference databaseReference = firebaseDatabase.getReference("defaultUsers");
                                            Query checkBloodGroup = databaseReference.orderByChild("mobile").equalTo(phone);

                                            checkBloodGroup.addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                                                    if (snapshot.exists()) {
                                                        String bloodGroup_var = snapshot.child(phone).child("bloodGroup").getValue(String.class);
                                                        Log.d("BLOOD_GROUP_VAR", String.valueOf(bloodGroup_var));

                                                        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                                                        DatabaseReference databaseReference = firebaseDatabase.getReference("stock");
                                                        Query query = databaseReference.child(district_var).child(bloodGroup_var);

                                                        query.addListenerForSingleValueEvent(new ValueEventListener() {
                                                            @Override
                                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                if (snapshot.exists()) {
                                                                    String unit = snapshot.child("unit").getValue().toString();
                                                                    Log.d("UNIT", String.valueOf(unit));
                                                                    Double ext_unit_var = Double.parseDouble(unit);
                                                                    Double sum = ext_unit_var + unit_var;
                                                                    String sumString = String.valueOf(sum);

                                                                    new AlertDialog.Builder(VerifyBloodStockActivity.this)
                                                                            .setTitle("Verify Blood Stock Update")
                                                                            .setMessage("New blood stock is: " + sumString + " Want to update blood stock?")
                                                                            .setIcon(android.R.drawable.ic_dialog_alert)
                                                                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                                                                public void onClick(DialogInterface dialog, int whichButton) {
                                                                                    databaseReference.child(district_var).child(bloodGroup_var).child("unit").setValue(sumString);

                                                                                    Toast.makeText(getApplicationContext(), "Blood stock updated successfully!", Toast.LENGTH_SHORT).show();

                                                                                    DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
                                                                                    Date date = new Date();
                                                                                    String currentDate = dateFormat.format(date);
                                                                                    Log.d("Current date", String.valueOf(currentDate));
                                                                                    String id = UUID.randomUUID().toString();

                                                                                    DatabaseReference databaseReference1 = firebaseDatabase.getReference("bloodReceivedRegistry");
                                                                                    storeBloodReceivedData storeBloodReceivedData = new storeBloodReceivedData(id, phone, bloodGroup_var, bloodUnits, currentDate, district_var, phoneUser);
                                                                                    databaseReference1.child(id).setValue(storeBloodReceivedData);

                                                                                    Intent intent = new Intent(getApplicationContext(), UpdateBloodStockActivity.class);
                                                                                    startActivity(intent);
                                                                                }
                                                                            })
                                                                            .setNegativeButton(android.R.string.no, null).show();

                                                                } else {
                                                                    new AlertDialog.Builder(VerifyBloodStockActivity.this)
                                                                            .setTitle("Verify Blood Stock Update")
                                                                            .setMessage("New blood stock is: " + bloodUnits + " Want to update blood stock?")
                                                                            .setIcon(android.R.drawable.ic_dialog_alert)
                                                                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                                                                public void onClick(DialogInterface dialog, int whichButton) {
                                                                                    storeStockData storeStockData = new storeStockData(bloodGroup_var, district_var, bloodUnits);
                                                                                    databaseReference.child(district_var).child(bloodGroup_var).setValue(storeStockData);

                                                                                    Toast.makeText(getApplicationContext(), "Updated new blood stock record successfully!", Toast.LENGTH_SHORT).show();

                                                                                    DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
                                                                                    Date date = new Date();
                                                                                    String currentDate = dateFormat.format(date);
                                                                                    Log.d("Current date", String.valueOf(currentDate));
                                                                                    String id = UUID.randomUUID().toString();

                                                                                    DatabaseReference databaseReference1 = firebaseDatabase.getReference("bloodReceivedRegistry");
                                                                                    storeBloodReceivedData storeBloodReceivedData = new storeBloodReceivedData(id, phone, bloodGroup_var, bloodUnits, currentDate, district_var, phoneUser);
                                                                                    databaseReference1.child(id).setValue(storeBloodReceivedData);

                                                                                    Intent intent = new Intent(getApplicationContext(), UpdateBloodStockActivity.class);
                                                                                    startActivity(intent);
                                                                                }
                                                                            })
                                                                            .setNegativeButton(android.R.string.no, null).show();
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

                } else {
                    txtBloodStockUnits.setError("Please enter blood unit count");
                }
            }
        });
    }

    private void updateUserInfo(String phone) {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference("donor");
        Query checkmobile = databaseReference.orderByChild("mobile").equalTo(phone);

        checkmobile.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        storeData data = dataSnapshot.getValue(storeData.class);
                        list.add(data);
                    }
                    verifyUserInfoHolder.notifyDataSetChanged();
                } else {
                    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                    DatabaseReference databaseReference = firebaseDatabase.getReference("defaultUsers");
                    Query checkmobile = databaseReference.orderByChild("mobile").equalTo(phone);

                    checkmobile.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                    storeData data = dataSnapshot.getValue(storeData.class);
                                    list.add(data);
                                }
                                verifyUserInfoHolder.notifyDataSetChanged();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull @NotNull DatabaseError error) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), FacilitatorLeftNavActivity.class);
                setResult(Activity.RESULT_CANCELED, intent);
                finish();

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
        Intent intent = new Intent(getApplicationContext(), UpdateBloodStockActivity.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {

    }
}