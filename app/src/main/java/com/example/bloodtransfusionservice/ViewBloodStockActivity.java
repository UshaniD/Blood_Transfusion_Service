package com.example.bloodtransfusionservice;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;

public class ViewBloodStockActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    BloodStockHolder bloodStockHolder;
    ArrayList<storeStockData> list;
    AutoCompleteTextView districts;
    Button buttonSearch;
    public static String district_var;
    static String releaseFlag = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_blood_stock);

        //Get phone number from session
        SessionManager sessionManager = new SessionManager(this,SessionManager.SESSION_USER_SESSION);
        HashMap<String, String> userDetails = sessionManager.getUserDetailFromSession();
        String phone = userDetails.get(SessionManager.KEY_PHONE_NO);
        Log.d("PHONE NUMBER : ", String.valueOf(phone));

        recyclerView = findViewById(R.id.view_blood_stock_recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        list = new ArrayList<>();
        bloodStockHolder = new BloodStockHolder(this, list);
        recyclerView.setAdapter(bloodStockHolder);
        districts = findViewById(R.id.districts);
        buttonSearch = findViewById(R.id.btn_search);

        String[] district_options = {"Colombo", "Kalutara", "Gampaha", "Matale", "Kandy", "Nuwara Eliya", "Matara", "Galle", "Hambantota",
                "Badulla", "Monaragala", "Kegalle", "Ratnapura", "Auradhapura", "Polonnaruwa", "Trincomalee", "Batticaloa", "Ampara", "Puttalam", "Kurunegala",
                "Jaffna", "Kilinochchi", "Mannar", "Mulaitivu", "Vavuniya"};
        ArrayAdapter arrayAdapter1 = new ArrayAdapter(this, R.layout.format_dropdown, district_options);
        districts.setText(arrayAdapter1.getItem(0).toString(), false);
        districts.setAdapter(arrayAdapter1);

        buttonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                releaseFlag = "";
                recyclerView.removeAllViewsInLayout();
                recyclerView.setVisibility(View.INVISIBLE);
                list.clear();
                district_var = districts.getText().toString();
                Log.d("district_var : ", String.valueOf(district_var));
                FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();

                DatabaseReference databaseReference1 = firebaseDatabase.getReference("facilitator");
                Query checkFacilitatorDistrict = databaseReference1.orderByChild("mobile").equalTo(phone);
                checkFacilitatorDistrict.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            String facilitatorDistrict = snapshot.child(phone).child("district").getValue(String.class);
                            Log.d("facilitatorDistrict : ", String.valueOf(facilitatorDistrict));
                            if (district_var.equals(facilitatorDistrict)) {
                                releaseFlag = "true";
                            } else {
                                releaseFlag = "false";
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull @NotNull DatabaseError error) {

                    }
                });

                DatabaseReference databaseReference = firebaseDatabase.getReference("stock");
                Query query = databaseReference.child(district_var);

                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            recyclerView.setVisibility(View.VISIBLE);
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                storeStockData data = dataSnapshot.getValue(storeStockData.class);
                                list.add(data);
                            }
                            bloodStockHolder.notifyDataSetChanged();
                        } else {
                            Toast.makeText(getApplicationContext(), "No data available for selected district!", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull @NotNull DatabaseError error) {

                    }
                });
            }
        });

        bloodStockHolder.OnRecyclerViewClickListner(new BloodStockHolder.OnRecyclerViewClickListner() {
            @Override
            public void onReleaseClick(int position, String id, String unit) {
                Toast.makeText(getApplicationContext(), "SELECTED POSITION: " + position + " BLOOD TYPE: " + id + " BLOOD UNITS: " + unit, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), VerifyStockReleaseActivity.class);
                intent.putExtra("bloodType", id);
                intent.putExtra("bloodUnit", unit);
                intent.putExtra("district", district_var);
                intent.putExtra("position", position);
                startActivity(intent);
            }
        });

    }

    public void moveToHomeScreen(View view) {
        Intent intent = new Intent(getApplicationContext(), FacilitatorLeftNavActivity.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {

    }
}