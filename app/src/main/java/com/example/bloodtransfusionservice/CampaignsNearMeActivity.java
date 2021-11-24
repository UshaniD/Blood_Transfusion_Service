package com.example.bloodtransfusionservice;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.ChildEventListener;
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
import java.util.concurrent.TimeUnit;

public class CampaignsNearMeActivity extends FragmentActivity implements OnMapReadyCallback {
    FusedLocationProviderClient fusedLocationProviderClient;
    private static final int REQUEST_CODE = 101;
    String phone;
    ArrayList<LatLng> arrayList = new ArrayList<>();
    String dateFromDb = "";
    String statusFromDB = "";
    ArrayList<String> list = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_campaigns_near_me);

        /* Get phone number from session */
        SessionManager sessionManager = new SessionManager(this, SessionManager.SESSION_USER_SESSION);
        HashMap<String, String> userDetails = sessionManager.getUserDetailFromSession();
        phone = userDetails.get(SessionManager.KEY_PHONE_NO);
        Log.d("PHONE NUMBER : ", String.valueOf(phone));

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        fetchCampaings();

    }

    private void fetchCampaings() {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference reference = firebaseDatabase.getReference("donor");
        Query checkmobileNo = reference.orderByChild("mobile").equalTo(phone);

        checkmobileNo.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String donor_district = snapshot.child(phone).child("district").getValue(String.class);

                    /*FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                    DatabaseReference databaseReference = firebaseDatabase.getReference("campaign");
                    databaseReference.addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                            storeCampaignData data = snapshot.getValue(storeCampaignData.class);
                            String campDate = data.getDateOfCamp();
                            Log.d("Camp date", String.valueOf(campDate));

                            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
                            Date date = new Date();
                            String currentDate = dateFormat.format(date);
                            Log.d("Current date", String.valueOf(currentDate));

                            Date date1 = null;
                            try {
                                date1 = new SimpleDateFormat("yyyy/MM/dd").parse(campDate);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            Date date2 = null;
                            try {
                                date2 = new SimpleDateFormat("yyyy/MM/dd").parse(currentDate);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            long diff = date1.getTime() - date2.getTime();

                            TimeUnit time = TimeUnit.DAYS;
                            long diffrence = time.convert(diff, TimeUnit.MILLISECONDS);
                            System.out.println("The difference in days is : " + diffrence);

                            if (data.getDistrict().equals(donor_district) && data.getStatus().equals("Approved") && diffrence > 0) {
                                String latitude_var = data.getLatitude();
                                double lat = Double.parseDouble(latitude_var);
                                Log.d("========LATITUDE:", String.valueOf(lat));

                                String longitude_var = data.getLongitude();
                                double lon = Double.parseDouble(longitude_var);
                                Log.d("========LONGITUDE:", String.valueOf(lon));

                                LatLng latLng = new LatLng(lat, lon);
                                Log.d("========LATLANG:", String.valueOf(latLng));

                                SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map_view);
                                supportMapFragment.getMapAsync(CampaignsNearMeActivity.this);
                                arrayList.add(latLng);
                            }

                        }

                        @Override
                        public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                        }

                        @Override
                        public void onChildRemoved(@NonNull DataSnapshot snapshot) {

                        }

                        @Override
                        public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(getApplicationContext(), "No near by campaigns", Toast.LENGTH_SHORT).show();
                        }
                    });*/

                    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                    DatabaseReference databaseReference = firebaseDatabase.getReference("campaign");
                    Query check_district = databaseReference.orderByChild("district").equalTo(donor_district);

                    check_district.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                //Get current date
                                DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
                                Date date = new Date();
                                String currentDate = dateFormat.format(date);
                                Log.d("Current date", String.valueOf(currentDate));

                                //String dateFromDb = "";
                                //String statusFromDB = "";
                                for (DataSnapshot ds : snapshot.getChildren()) {
                                    //Get status and the date of the camp which match for donors district
                                    dateFromDb = ds.child("dateOfCamp").getValue(String.class);
                                    Log.d("========date from DB:", String.valueOf(dateFromDb));

                                    statusFromDB = ds.child("status").getValue(String.class);
                                    Log.d("========status from DB:", String.valueOf(statusFromDB));

                                    //Convert date format of the date from DB
                                    Date date1 = null;
                                    try {
                                        date1 = new SimpleDateFormat("yyyy/MM/dd").parse(dateFromDb);
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                    //Convert date format of current date
                                    Date date2 = null;
                                    try {
                                        date2 = new SimpleDateFormat("yyyy/MM/dd").parse(currentDate);
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                    //Get difference
                                    long diff = date1.getTime() - date2.getTime();

                                    //Convert format of different of the date
                                    TimeUnit time = TimeUnit.DAYS;
                                    long date_diffrence = time.convert(diff, TimeUnit.MILLISECONDS);
                                    System.out.println("The difference in days is : " + date_diffrence);

                                    //Condition check status approved and future date of camp
                                    if (date_diffrence >= 0 && statusFromDB.equals("Approved")) {
                                        String latitude_var = ds.child("latitude").getValue(String.class);
                                        double lat = Double.parseDouble(latitude_var);
                                        Log.d("========LATITUDE:", String.valueOf(lat));

                                        String longitude_var = ds.child("longitude").getValue(String.class);
                                        double lon = Double.parseDouble(longitude_var);
                                        Log.d("========LONGITUDE:", String.valueOf(lon));

                                        SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map_view);
                                        supportMapFragment.getMapAsync(CampaignsNearMeActivity.this);
                                        LatLng latLng = new LatLng(lat,lon);
                                        Log.d("========LATLANG:", String.valueOf(latLng));
                                        arrayList.add(latLng);
                                        list.add(dateFromDb);

                                    }
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull @NotNull DatabaseError error) {
                            Toast.makeText(CampaignsNearMeActivity.this, "No campaigns near.", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        for (int i = 0; i < arrayList.size(); i++) {
            googleMap.addMarker(new MarkerOptions().position(arrayList.get(i)).title("Camp Date: "+list.get(i)));
            googleMap.animateCamera(CameraUpdateFactory.newLatLng(arrayList.get(i)));
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(arrayList.get(i), 8));
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull @NotNull String[] permissions, @NonNull @NotNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    fetchCampaings();
                }
                break;
        }
    }

    public void moveToHomeScreen(View view) {
        Intent intent = new Intent(getApplicationContext(), LeftNavActivity.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {

    }


}