package com.example.bloodtransfusionservice;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class ChooseLocationActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMapLongClickListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {
    GoogleMap mGoogleMap;
    FloatingActionButton fab;
    SupportMapFragment mapFragment;
    SearchView searchView;
    boolean isPermissionGranted;
    private FusedLocationProviderClient mLocationClient;
    ImageView backImg;
    String activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_location);

        backImg = findViewById(R.id.img_back);
        fab = findViewById(R.id.btn_fab);

        String donorFlag = getIntent().getStringExtra("Flag");
        String passwordResetVal = getIntent().getStringExtra("passwordResetFlag");
        String donorReg = getIntent().getStringExtra("donorRegistration");
        activity = getIntent().getStringExtra("locationActivity");

        checkMyPermission();
        initMap();

        mLocationClient = new FusedLocationProviderClient(this);
        getCurrentLocation(activity);

        //Click on current location icon
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getCurrentLocation(activity);
            }
        });

        //Location search via search bar
        searchView = findViewById(R.id.search_loc);
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map_view);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                mGoogleMap.clear();
                String location = searchView.getQuery().toString();
                List<Address> addressList = null;
                if (location != null || !location.equals("")) {
                    Geocoder geocoder = new Geocoder(ChooseLocationActivity.this);
                    try {
                        addressList = geocoder.getFromLocationName(location, 1);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    if(addressList != null && addressList.size() > 0) {
                        Address address = addressList.get(0);
                        LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
                        String latitudeStr = String.valueOf(latLng.latitude);
                        String longitudeStr = String.valueOf(latLng.longitude);
                        double latitude = latLng.latitude;
                        double longitude = latLng.longitude;

                        mGoogleMap.addMarker(new MarkerOptions().position(latLng).title(location));
                        mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 18));
                        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                        mGoogleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                            @Override
                            public boolean onMarkerClick(Marker marker) {
                                if (activity.equals("facilitatorSignup")) {
                                    Intent intent = new Intent(getApplicationContext(), FacilitatorSignup1Activity.class);
                                    String locationLatLang = latitude + " : " + longitude;

                                    Geocoder geocoder;
                                    List<Address> addresses = null;
                                    geocoder = new Geocoder(ChooseLocationActivity.this, Locale.getDefault());

                                    try {
                                        addresses = geocoder.getFromLocation(latitude, longitude, 1);
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }

                                    String address = addresses.get(0).getAddressLine(0);
                                    String city = addresses.get(0).getLocality();
                                    String state = addresses.get(0).getAdminArea();
                                    String country = addresses.get(0).getCountryName();
                                    String postalCode = addresses.get(0).getPostalCode();
                                    String knownName = addresses.get(0).getFeatureName();

                                    intent.putExtra("latitude", latitudeStr);
                                    intent.putExtra("longitude", longitudeStr);
                                    intent.putExtra("latitudeLongitude", locationLatLang);
                                    intent.putExtra("address", address);
                                    intent.putExtra("city", city);
                                    intent.putExtra("state", state);
                                    intent.putExtra("country", country);
                                    intent.putExtra("postalcode", postalCode);
                                    intent.putExtra("knownname", knownName);

                                    String myLocation = "https://maps.google.com/?q=" + latitude + "," + longitude;
                                    String longAddress = address + ", " + state;
                                    intent.putExtra("longAddress", longAddress);
                                    Log.d("ADDRESS: ", String.valueOf(myLocation));
                                    intent.putExtra("myLocation", myLocation);
                                    Log.d("My Location: ", String.valueOf(myLocation));
                                    setResult(Activity.RESULT_OK, intent);
                                    finish();
                                } else if (activity.equals("userSignup")) {
                                    Intent intent = new Intent(getApplicationContext(), UserSignup1Activity.class);
                                    String locationLatLang = latitude + " : " + longitude;

                                    Geocoder geocoder;
                                    List<Address> addresses = null;
                                    geocoder = new Geocoder(ChooseLocationActivity.this, Locale.getDefault());

                                    try {
                                        addresses = geocoder.getFromLocation(latitude, longitude, 1);
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }

                                    String address = addresses.get(0).getAddressLine(0);
                                    String city = addresses.get(0).getLocality();
                                    String state = addresses.get(0).getAdminArea();
                                    String country = addresses.get(0).getCountryName();
                                    String postalCode = addresses.get(0).getPostalCode();
                                    String knownName = addresses.get(0).getFeatureName();

                                    intent.putExtra("latitude", latitudeStr);
                                    intent.putExtra("longitude", longitudeStr);
                                    intent.putExtra("latitudeLongitude", locationLatLang);
                                    intent.putExtra("address", address);
                                    intent.putExtra("city", city);
                                    intent.putExtra("state", state);
                                    intent.putExtra("country", country);
                                    intent.putExtra("postalcode", postalCode);
                                    intent.putExtra("knownname", knownName);

                                    String myLocation = "https://maps.google.com/?q=" + latitude + "," + longitude;
                                    String longAddress = address + ", " + state;
                                    intent.putExtra("longAddress", longAddress);
                                    Log.d("ADDRESS: ", String.valueOf(longAddress));

                                    intent.putExtra("myLocation", myLocation);
                                    Log.d("My Location: ", String.valueOf(myLocation));
                                    setResult(Activity.RESULT_OK, intent);
                                    finish();
                                } else if (activity.equals("createCampaign")) {
                                    Intent intent = new Intent(getApplicationContext(), OrganizeCampaignActivity.class);
                                    String locationLatLang = latitude + " : " + longitude;

                                    Geocoder geocoder;
                                    List<Address> addresses = null;
                                    geocoder = new Geocoder(ChooseLocationActivity.this, Locale.getDefault());

                                    try {
                                        addresses = geocoder.getFromLocation(latitude, longitude, 1);
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }

                                    String address = addresses.get(0).getAddressLine(0);
                                    String city = addresses.get(0).getLocality();
                                    String state = addresses.get(0).getAdminArea();
                                    String country = addresses.get(0).getCountryName();
                                    String postalCode = addresses.get(0).getPostalCode();
                                    String knownName = addresses.get(0).getFeatureName();

                                    intent.putExtra("latitude", latitudeStr);
                                    Log.d("==LAT STR: ", String.valueOf(latitudeStr));
                                    intent.putExtra("longitude", longitudeStr);
                                    Log.d("==LONG STR: ", String.valueOf(longitudeStr));
                                    intent.putExtra("latitudeLongitude", locationLatLang);
                                    intent.putExtra("address", address);
                                    intent.putExtra("city", city);
                                    intent.putExtra("state", state);
                                    intent.putExtra("country", country);
                                    intent.putExtra("postalcode", postalCode);
                                    intent.putExtra("knownname", knownName);

                                    String myLocation = "https://maps.google.com/?q=" + latitude + "," + longitude;
                                    String longAddress = address + ", " + state;
                                    intent.putExtra("longAddress", longAddress);
                                    Log.d("ADDRESS: ", String.valueOf(longAddress));

                                    intent.putExtra("myLocation", myLocation);
                                    Log.d("My Location: ", String.valueOf(myLocation));
                                    setResult(Activity.RESULT_OK, intent);
                                    finish();
                                } else if (activity.equals("userProfileUpdate")) {
                                    Intent intent = new Intent(getApplicationContext(), UserProfileActivity.class);
                                    String locationLatLang = latitude + " : " + longitude;

                                    Geocoder geocoder;
                                    List<Address> addresses = null;
                                    geocoder = new Geocoder(ChooseLocationActivity.this, Locale.getDefault());

                                    try {
                                        addresses = geocoder.getFromLocation(latitude, longitude, 1);
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }

                                    String address = addresses.get(0).getAddressLine(0);
                                    String city = addresses.get(0).getLocality();
                                    String state = addresses.get(0).getAdminArea();
                                    String country = addresses.get(0).getCountryName();
                                    String postalCode = addresses.get(0).getPostalCode();
                                    String knownName = addresses.get(0).getFeatureName();

                                    intent.putExtra("latitude", latitudeStr);
                                    intent.putExtra("longitude", longitudeStr);
                                    intent.putExtra("latitudeLongitude", locationLatLang);
                                    intent.putExtra("address", address);
                                    intent.putExtra("city", city);
                                    intent.putExtra("state", state);
                                    intent.putExtra("country", country);
                                    intent.putExtra("postalcode", postalCode);
                                    intent.putExtra("knownname", knownName);

                                    String myLocation = "https://maps.google.com/?q=" + latitude + "," + longitude;
                                    String longAddress = address + ", " + state;
                                    intent.putExtra("longAddress", longAddress);
                                    Log.d("ADDRESS: ", String.valueOf(longAddress));

                                    intent.putExtra("myLocation", myLocation);
                                    Log.d("My Location: ", String.valueOf(myLocation));
                                    setResult(Activity.RESULT_OK, intent);
                                    finish();
                                } else if (activity.equals("facilitatorProfileUpdate")) {
                                    Intent intent = new Intent(getApplicationContext(), FacilitatorProfileActivity.class);
                                    String locationLatLang = latitude + " : " + longitude;

                                    Geocoder geocoder;
                                    List<Address> addresses = null;
                                    geocoder = new Geocoder(ChooseLocationActivity.this, Locale.getDefault());

                                    try {
                                        addresses = geocoder.getFromLocation(latitude, longitude, 1);
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }

                                    String address = addresses.get(0).getAddressLine(0);
                                    String city = addresses.get(0).getLocality();
                                    String state = addresses.get(0).getAdminArea();
                                    String country = addresses.get(0).getCountryName();
                                    String postalCode = addresses.get(0).getPostalCode();
                                    String knownName = addresses.get(0).getFeatureName();

                                    intent.putExtra("latitude", latitudeStr);
                                    intent.putExtra("longitude", longitudeStr);
                                    intent.putExtra("latitudeLongitude", locationLatLang);
                                    intent.putExtra("address", address);
                                    intent.putExtra("city", city);
                                    intent.putExtra("state", state);
                                    intent.putExtra("country", country);
                                    intent.putExtra("postalcode", postalCode);
                                    intent.putExtra("knownname", knownName);

                                    String myLocation = "https://maps.google.com/?q=" + latitude + "," + longitude;
                                    String longAddress = address + ", " + state;
                                    intent.putExtra("longAddress", longAddress);
                                    Log.d("ADDRESS: ", String.valueOf(longAddress));

                                    intent.putExtra("myLocation", myLocation);
                                    Log.d("My Location: ", String.valueOf(myLocation));
                                    setResult(Activity.RESULT_OK, intent);
                                    finish();
                                } else {
                                    Intent intent = new Intent(getApplicationContext(), RequestBloodActivity.class);
                                    String locationLatLang = latitude + " : " + longitude;

                                    Geocoder geocoder;
                                    List<Address> addresses = null;
                                    geocoder = new Geocoder(ChooseLocationActivity.this, Locale.getDefault());

                                    try {
                                        addresses = geocoder.getFromLocation(latitude, longitude, 1);
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }

                                    String address = addresses.get(0).getAddressLine(0);
                                    String city = addresses.get(0).getLocality();
                                    String state = addresses.get(0).getAdminArea();
                                    String country = addresses.get(0).getCountryName();
                                    String postalCode = addresses.get(0).getPostalCode();
                                    String knownName = addresses.get(0).getFeatureName();

                                    intent.putExtra("latitude", latitudeStr);
                                    intent.putExtra("longitude", longitudeStr);
                                    intent.putExtra("latitudeLongitude", locationLatLang);
                                    intent.putExtra("address", address);
                                    intent.putExtra("city", city);
                                    intent.putExtra("state", state);
                                    intent.putExtra("country", country);
                                    intent.putExtra("postalcode", postalCode);
                                    intent.putExtra("knownname", knownName);

                                    String myLocation = "https://maps.google.com/?q=" + latitude + "," + longitude;
                                    String longAddress = address + ", " + state;
                                    intent.putExtra("longAddress", longAddress);
                                    Log.d("ADDRESS: ", String.valueOf(longAddress));

                                    intent.putExtra("myLocation", myLocation);
                                    Log.d("My Location: ", String.valueOf(myLocation));
                                    setResult(Activity.RESULT_OK, intent);
                                    finish();
                                }
                                return false;
                            }
                        });

                    } else {
                        Toast.makeText(getApplicationContext(), "Location does not exists. Please search exact location.", Toast.LENGTH_SHORT).show();
                    }

                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        mapFragment.getMapAsync(this);

        //Click on back icon on the map
        if (activity.equals("userSignup")) {
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

        } else if (activity.equals("facilitatorSignup")) {
            backImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), FacilitatorSignup1Activity.class);
                    intent.putExtra("Flag", donorFlag);
                    intent.putExtra("passwordResetFlag", passwordResetVal);
                    intent.putExtra("donorRegistration", donorReg);
                    setResult(Activity.RESULT_CANCELED, intent);
                    finish();
                }
            });
        } else if (activity.equals("createCampaign")) {
            backImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), OrganizeCampaignActivity.class);
                    intent.putExtra("Flag", donorFlag);
                    intent.putExtra("passwordResetFlag", passwordResetVal);
                    intent.putExtra("donorRegistration", donorReg);
                    setResult(Activity.RESULT_CANCELED, intent);
                    finish();
                }
            });

        } else if (activity.equals("userProfileUpdate")) {
            backImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), UserProfileActivity.class);
                    intent.putExtra("Flag", donorFlag);
                    intent.putExtra("passwordResetFlag", passwordResetVal);
                    intent.putExtra("donorRegistration", donorReg);
                    setResult(Activity.RESULT_CANCELED, intent);
                    finish();
                }
            });

        } else if (activity.equals("facilitatorProfileUpdate")) {
            backImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), FacilitatorProfileActivity.class);
                    intent.putExtra("Flag", donorFlag);
                    intent.putExtra("passwordResetFlag", passwordResetVal);
                    intent.putExtra("donorRegistration", donorReg);
                    setResult(Activity.RESULT_CANCELED, intent);
                    finish();
                }
            });

        } else {
            backImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), RequestBloodActivity.class);
                    intent.putExtra("Flag", donorFlag);
                    intent.putExtra("passwordResetFlag", passwordResetVal);
                    intent.putExtra("donorRegistration", donorReg);
                    setResult(Activity.RESULT_CANCELED, intent);
                    finish();
                }
            });
        }
    }

    @SuppressLint("MissingPermission")
    private void getCurrentLocation(String activity) {
        mLocationClient.getLastLocation().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Location location = task.getResult();
                gotoLocation(location.getLongitude(), location.getLatitude());
                if (location != null || !location.equals("")) {
                    LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                    String latitudeStr = String.valueOf(latLng.latitude);
                    String longitudeStr = String.valueOf(latLng.longitude);
                    double latitude = latLng.latitude;
                    double longitude = latLng.longitude;

                    mGoogleMap.addMarker(new MarkerOptions().position(latLng).title(String.valueOf(location)));
                    mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 18));
                    mGoogleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                        @Override
                        public boolean onMarkerClick(Marker marker) {
                            if (activity.equals("facilitatorSignup")) {
                                Intent intent = new Intent(getApplicationContext(), FacilitatorSignup1Activity.class);
                                String locationLatLang = latitude + " : " + longitude;

                                Geocoder geocoder;
                                List<Address> addresses = null;
                                geocoder = new Geocoder(ChooseLocationActivity.this, Locale.getDefault());

                                try {
                                    addresses = geocoder.getFromLocation(latitude, longitude, 1);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                                String address = addresses.get(0).getAddressLine(0);
                                String city = addresses.get(0).getLocality();
                                String state = addresses.get(0).getAdminArea();
                                String country = addresses.get(0).getCountryName();
                                String postalCode = addresses.get(0).getPostalCode();
                                String knownName = addresses.get(0).getFeatureName();

                                intent.putExtra("latitude", latitudeStr);
                                intent.putExtra("longitude", longitudeStr);
                                intent.putExtra("latitudeLongitude", locationLatLang);
                                intent.putExtra("address", address);
                                intent.putExtra("city", city);
                                intent.putExtra("state", state);
                                intent.putExtra("country", country);
                                intent.putExtra("postalcode", postalCode);
                                intent.putExtra("knownname", knownName);

                                String myLocation = "https://maps.google.com/?q=" + latitude + "," + longitude;
                                String longAddress = address + ", " + state;
                                intent.putExtra("longAddress", longAddress);
                                Log.d("ADDRESS: ", String.valueOf(longAddress));

                                intent.putExtra("myLocation", myLocation);
                                Log.d("My Location: ", String.valueOf(myLocation));
                                setResult(Activity.RESULT_OK, intent);
                                finish();
                            } else if (activity.equals("userSignup")) {
                                Intent intent = new Intent(getApplicationContext(), UserSignup1Activity.class);
                                String locationLatLang = latitude + " : " + longitude;

                                Geocoder geocoder;
                                List<Address> addresses = null;
                                geocoder = new Geocoder(ChooseLocationActivity.this, Locale.getDefault());

                                try {
                                    addresses = geocoder.getFromLocation(latitude, longitude, 1);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                                String address = addresses.get(0).getAddressLine(0);
                                String city = addresses.get(0).getLocality();
                                String state = addresses.get(0).getAdminArea();
                                String country = addresses.get(0).getCountryName();
                                String postalCode = addresses.get(0).getPostalCode();
                                String knownName = addresses.get(0).getFeatureName();

                                intent.putExtra("latitude", latitudeStr);
                                intent.putExtra("longitude", longitudeStr);
                                intent.putExtra("latitudeLongitude", locationLatLang);
                                intent.putExtra("address", address);
                                intent.putExtra("city", city);
                                intent.putExtra("state", state);
                                intent.putExtra("country", country);
                                intent.putExtra("postalcode", postalCode);
                                intent.putExtra("knownname", knownName);

                                String myLocation = "https://maps.google.com/?q=" + latitude + "," + longitude;
                                String longAddress = address + ", " + state;
                                intent.putExtra("longAddress", longAddress);
                                Log.d("ADDRESS: ", String.valueOf(longAddress));

                                intent.putExtra("myLocation", myLocation);
                                Log.d("My Location: ", String.valueOf(myLocation));
                                setResult(Activity.RESULT_OK, intent);
                                finish();
                            } else if (activity.equals("createCampaign")) {
                                Intent intent = new Intent(getApplicationContext(), OrganizeCampaignActivity.class);
                                String locationLatLang = latitude + " : " + longitude;

                                Geocoder geocoder;
                                List<Address> addresses = null;
                                geocoder = new Geocoder(ChooseLocationActivity.this, Locale.getDefault());

                                try {
                                    addresses = geocoder.getFromLocation(latitude, longitude, 1);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                                String address = addresses.get(0).getAddressLine(0);
                                String city = addresses.get(0).getLocality();
                                String state = addresses.get(0).getAdminArea();
                                String country = addresses.get(0).getCountryName();
                                String postalCode = addresses.get(0).getPostalCode();
                                String knownName = addresses.get(0).getFeatureName();

                                intent.putExtra("latitude", latitudeStr);
                                intent.putExtra("longitude", longitudeStr);
                                intent.putExtra("latitudeLongitude", locationLatLang);
                                intent.putExtra("address", address);
                                intent.putExtra("city", city);
                                intent.putExtra("state", state);
                                intent.putExtra("country", country);
                                intent.putExtra("postalcode", postalCode);
                                intent.putExtra("knownname", knownName);

                                String myLocation = "https://maps.google.com/?q=" + latitude + "," + longitude;
                                String longAddress = address + ", " + state;
                                intent.putExtra("longAddress", longAddress);
                                Log.d("ADDRESS: ", String.valueOf(longAddress));

                                intent.putExtra("myLocation", myLocation);
                                Log.d("My Location: ", String.valueOf(myLocation));
                                setResult(Activity.RESULT_OK, intent);
                                finish();
                            } else if (activity.equals("userProfileUpdate")) {
                                Intent intent = new Intent(getApplicationContext(), UserProfileActivity.class);
                                String locationLatLang = latitude + " : " + longitude;

                                Geocoder geocoder;
                                List<Address> addresses = null;
                                geocoder = new Geocoder(ChooseLocationActivity.this, Locale.getDefault());

                                try {
                                    addresses = geocoder.getFromLocation(latitude, longitude, 1);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                                String address = addresses.get(0).getAddressLine(0);
                                String city = addresses.get(0).getLocality();
                                String state = addresses.get(0).getAdminArea();
                                String country = addresses.get(0).getCountryName();
                                String postalCode = addresses.get(0).getPostalCode();
                                String knownName = addresses.get(0).getFeatureName();

                                intent.putExtra("latitude", latitudeStr);
                                intent.putExtra("longitude", longitudeStr);
                                intent.putExtra("latitudeLongitude", locationLatLang);
                                intent.putExtra("address", address);
                                intent.putExtra("city", city);
                                intent.putExtra("state", state);
                                intent.putExtra("country", country);
                                intent.putExtra("postalcode", postalCode);
                                intent.putExtra("knownname", knownName);

                                String myLocation = "https://maps.google.com/?q=" + latitude + "," + longitude;
                                String longAddress = address + ", " + state;
                                intent.putExtra("longAddress", longAddress);
                                Log.d("ADDRESS: ", String.valueOf(longAddress));

                                intent.putExtra("myLocation", myLocation);
                                Log.d("My Location: ", String.valueOf(myLocation));
                                setResult(Activity.RESULT_OK, intent);
                                finish();
                            } else if (activity.equals("facilitatorProfileUpdate")) {
                                Intent intent = new Intent(getApplicationContext(), FacilitatorProfileActivity.class);
                                String locationLatLang = latitude + " : " + longitude;

                                Geocoder geocoder;
                                List<Address> addresses = null;
                                geocoder = new Geocoder(ChooseLocationActivity.this, Locale.getDefault());

                                try {
                                    addresses = geocoder.getFromLocation(latitude, longitude, 1);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                                String address = addresses.get(0).getAddressLine(0);
                                String city = addresses.get(0).getLocality();
                                String state = addresses.get(0).getAdminArea();
                                String country = addresses.get(0).getCountryName();
                                String postalCode = addresses.get(0).getPostalCode();
                                String knownName = addresses.get(0).getFeatureName();

                                intent.putExtra("latitude", latitudeStr);
                                intent.putExtra("longitude", longitudeStr);
                                intent.putExtra("latitudeLongitude", locationLatLang);
                                intent.putExtra("address", address);
                                intent.putExtra("city", city);
                                intent.putExtra("state", state);
                                intent.putExtra("country", country);
                                intent.putExtra("postalcode", postalCode);
                                intent.putExtra("knownname", knownName);

                                String myLocation = "https://maps.google.com/?q=" + latitude + "," + longitude;
                                String longAddress = address + ", " + state;
                                intent.putExtra("longAddress", longAddress);
                                Log.d("ADDRESS: ", String.valueOf(longAddress));

                                intent.putExtra("myLocation", myLocation);
                                Log.d("My Location: ", String.valueOf(myLocation));
                                setResult(Activity.RESULT_OK, intent);
                                finish();
                            } else {
                                Intent intent = new Intent(getApplicationContext(), RequestBloodActivity.class);
                                String locationLatLang = latitude + " : " + longitude;

                                Geocoder geocoder;
                                List<Address> addresses = null;
                                geocoder = new Geocoder(ChooseLocationActivity.this, Locale.getDefault());

                                try {
                                    addresses = geocoder.getFromLocation(latitude, longitude, 1);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                                String address = addresses.get(0).getAddressLine(0);
                                String city = addresses.get(0).getLocality();
                                String state = addresses.get(0).getAdminArea();
                                String country = addresses.get(0).getCountryName();
                                String postalCode = addresses.get(0).getPostalCode();
                                String knownName = addresses.get(0).getFeatureName();

                                intent.putExtra("latitude", latitudeStr);
                                intent.putExtra("longitude", longitudeStr);
                                intent.putExtra("latitudeLongitude", locationLatLang);
                                intent.putExtra("address", address);
                                intent.putExtra("city", city);
                                intent.putExtra("state", state);
                                intent.putExtra("country", country);
                                intent.putExtra("postalcode", postalCode);
                                intent.putExtra("knownname", knownName);

                                String myLocation = "https://maps.google.com/?q=" + latitude + "," + longitude;
                                String longAddress = address + ", " + state;
                                intent.putExtra("longAddress", longAddress);
                                Log.d("ADDRESS: ", String.valueOf(longAddress));

                                intent.putExtra("myLocation", myLocation);
                                Log.d("My Location: ", String.valueOf(myLocation));
                                setResult(Activity.RESULT_OK, intent);
                                finish();
                            }
                            return false;
                        }
                    });
                }
            }
        });
    }

    private void initMap() {
        if (isPermissionGranted) {
            SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map_view);
            supportMapFragment.getMapAsync(this);
        }
    }

    private void gotoLocation(double longitude, double latitude) {
        LatLng latLng = new LatLng(latitude, longitude);

        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 18);
        mGoogleMap.moveCamera(cameraUpdate);
        mGoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
    }

    private void checkMyPermission() {
        Dexter.withContext(this).withPermission(Manifest.permission.ACCESS_FINE_LOCATION).withListener(new PermissionListener() {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                Toast.makeText(ChooseLocationActivity.this, "Permission granted..", Toast.LENGTH_SHORT).show();
                isPermissionGranted = true;
            }

            @Override
            public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", getPackageName(), "");
                intent.setData(uri);
                startActivity(intent);
            }

            @Override
            public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                permissionToken.continuePermissionRequest();

            }
        }).check();
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mGoogleMap = googleMap;
        mGoogleMap.setOnMapLongClickListener(this);

    }

    @SuppressLint("MissingPermission")
    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {


    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onMapLongClick(LatLng latLng) {
        Log.d("=========Long press: ", String.valueOf(latLng));
        mGoogleMap.clear();
        //mGoogleMap.addMarker(new MarkerOptions().position(latLng));

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mLocationClient.getLastLocation().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Location location = task.getResult();
                gotoLocation(location.getLongitude(), location.getLatitude());
                if (location != null || !location.equals("")) {
                    LatLng latLng1 = new LatLng(location.getLatitude(), location.getLongitude());
                    String latitudeStr = String.valueOf(latLng.latitude);
                    String longitudeStr = String.valueOf(latLng.longitude);
                    double latitude = latLng.latitude;
                    double longitude = latLng.longitude;

                    mGoogleMap.addMarker(new MarkerOptions().position(latLng).title(String.valueOf(location)));
                    mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 18));
                    mGoogleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                        @Override
                        public boolean onMarkerClick(Marker marker) {
                            if (activity.equals("facilitatorSignup")) {
                                Intent intent = new Intent(getApplicationContext(), FacilitatorSignup1Activity.class);
                                String locationLatLang = latitude + " : " + longitude;

                                Geocoder geocoder;
                                List<Address> addresses = null;
                                geocoder = new Geocoder(ChooseLocationActivity.this, Locale.getDefault());

                                try {
                                    addresses = geocoder.getFromLocation(latitude, longitude, 1);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                                String address = addresses.get(0).getAddressLine(0);
                                String city = addresses.get(0).getLocality();
                                String state = addresses.get(0).getAdminArea();
                                String country = addresses.get(0).getCountryName();
                                String postalCode = addresses.get(0).getPostalCode();
                                String knownName = addresses.get(0).getFeatureName();

                                intent.putExtra("latitude", latitudeStr);
                                intent.putExtra("longitude", longitudeStr);
                                intent.putExtra("latitudeLongitude", locationLatLang);
                                intent.putExtra("address", address);
                                intent.putExtra("city", city);
                                intent.putExtra("state", state);
                                intent.putExtra("country", country);
                                intent.putExtra("postalcode", postalCode);
                                intent.putExtra("knownname", knownName);

                                String myLocation = "https://maps.google.com/?q=" + latitude + "," + longitude;
                                String longAddress = address + ", " + state;
                                intent.putExtra("longAddress", longAddress);
                                Log.d("ADDRESS: ", String.valueOf(longAddress));

                                intent.putExtra("myLocation", myLocation);
                                Log.d("My Location: ", String.valueOf(myLocation));
                                setResult(Activity.RESULT_OK, intent);
                                finish();
                            } else if (activity.equals("userSignup")) {
                                Intent intent = new Intent(getApplicationContext(), UserSignup1Activity.class);
                                String locationLatLang = latitude + " : " + longitude;

                                Geocoder geocoder;
                                List<Address> addresses = null;
                                geocoder = new Geocoder(ChooseLocationActivity.this, Locale.getDefault());

                                try {
                                    addresses = geocoder.getFromLocation(latitude, longitude, 1);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                                String address = addresses.get(0).getAddressLine(0);
                                String city = addresses.get(0).getLocality();
                                String state = addresses.get(0).getAdminArea();
                                String country = addresses.get(0).getCountryName();
                                String postalCode = addresses.get(0).getPostalCode();
                                String knownName = addresses.get(0).getFeatureName();

                                intent.putExtra("latitude", latitudeStr);
                                intent.putExtra("longitude", longitudeStr);
                                intent.putExtra("latitudeLongitude", locationLatLang);
                                intent.putExtra("address", address);
                                intent.putExtra("city", city);
                                intent.putExtra("state", state);
                                intent.putExtra("country", country);
                                intent.putExtra("postalcode", postalCode);
                                intent.putExtra("knownname", knownName);

                                String myLocation = "https://maps.google.com/?q=" + latitude + "," + longitude;
                                String longAddress = address + ", " + state;
                                intent.putExtra("longAddress", longAddress);
                                Log.d("ADDRESS: ", String.valueOf(longAddress));

                                intent.putExtra("myLocation", myLocation);
                                Log.d("My Location: ", String.valueOf(myLocation));
                                setResult(Activity.RESULT_OK, intent);
                                finish();
                            } else if (activity.equals("createCampaign")) {
                                Intent intent = new Intent(getApplicationContext(), OrganizeCampaignActivity.class);
                                String locationLatLang = latitude + " : " + longitude;

                                Geocoder geocoder;
                                List<Address> addresses = null;
                                geocoder = new Geocoder(ChooseLocationActivity.this, Locale.getDefault());

                                try {
                                    addresses = geocoder.getFromLocation(latitude, longitude, 1);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                                String address = addresses.get(0).getAddressLine(0);
                                String city = addresses.get(0).getLocality();
                                String state = addresses.get(0).getAdminArea();
                                String country = addresses.get(0).getCountryName();
                                String postalCode = addresses.get(0).getPostalCode();
                                String knownName = addresses.get(0).getFeatureName();

                                intent.putExtra("latitude", latitudeStr);
                                intent.putExtra("longitude", longitudeStr);
                                intent.putExtra("latitudeLongitude", locationLatLang);
                                intent.putExtra("address", address);
                                intent.putExtra("city", city);
                                intent.putExtra("state", state);
                                intent.putExtra("country", country);
                                intent.putExtra("postalcode", postalCode);
                                intent.putExtra("knownname", knownName);

                                String myLocation = "https://maps.google.com/?q=" + latitude + "," + longitude;
                                String longAddress = address + ", " + state;
                                intent.putExtra("longAddress", longAddress);
                                Log.d("ADDRESS: ", String.valueOf(longAddress));

                                intent.putExtra("myLocation", myLocation);
                                Log.d("My Location: ", String.valueOf(myLocation));
                                setResult(Activity.RESULT_OK, intent);
                                finish();
                            } else if (activity.equals("userProfileUpdate")) {
                                Intent intent = new Intent(getApplicationContext(), UserProfileActivity.class);
                                String locationLatLang = latitude + " : " + longitude;

                                Geocoder geocoder;
                                List<Address> addresses = null;
                                geocoder = new Geocoder(ChooseLocationActivity.this, Locale.getDefault());

                                try {
                                    addresses = geocoder.getFromLocation(latitude, longitude, 1);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                                String address = addresses.get(0).getAddressLine(0);
                                String city = addresses.get(0).getLocality();
                                String state = addresses.get(0).getAdminArea();
                                String country = addresses.get(0).getCountryName();
                                String postalCode = addresses.get(0).getPostalCode();
                                String knownName = addresses.get(0).getFeatureName();

                                intent.putExtra("latitude", latitudeStr);
                                intent.putExtra("longitude", longitudeStr);
                                intent.putExtra("latitudeLongitude", locationLatLang);
                                intent.putExtra("address", address);
                                intent.putExtra("city", city);
                                intent.putExtra("state", state);
                                intent.putExtra("country", country);
                                intent.putExtra("postalcode", postalCode);
                                intent.putExtra("knownname", knownName);

                                String myLocation = "https://maps.google.com/?q=" + latitude + "," + longitude;
                                String longAddress = address + ", " + state;
                                intent.putExtra("longAddress", longAddress);
                                Log.d("ADDRESS: ", String.valueOf(longAddress));

                                intent.putExtra("myLocation", myLocation);
                                Log.d("My Location: ", String.valueOf(myLocation));
                                setResult(Activity.RESULT_OK, intent);
                                finish();
                            } else if (activity.equals("facilitatorProfileUpdate")) {
                                Intent intent = new Intent(getApplicationContext(), FacilitatorProfileActivity.class);
                                String locationLatLang = latitude + " : " + longitude;

                                Geocoder geocoder;
                                List<Address> addresses = null;
                                geocoder = new Geocoder(ChooseLocationActivity.this, Locale.getDefault());

                                try {
                                    addresses = geocoder.getFromLocation(latitude, longitude, 1);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                                String address = addresses.get(0).getAddressLine(0);
                                String city = addresses.get(0).getLocality();
                                String state = addresses.get(0).getAdminArea();
                                String country = addresses.get(0).getCountryName();
                                String postalCode = addresses.get(0).getPostalCode();
                                String knownName = addresses.get(0).getFeatureName();

                                intent.putExtra("latitude", latitudeStr);
                                intent.putExtra("longitude", longitudeStr);
                                intent.putExtra("latitudeLongitude", locationLatLang);
                                intent.putExtra("address", address);
                                intent.putExtra("city", city);
                                intent.putExtra("state", state);
                                intent.putExtra("country", country);
                                intent.putExtra("postalcode", postalCode);
                                intent.putExtra("knownname", knownName);

                                String myLocation = "https://maps.google.com/?q=" + latitude + "," + longitude;
                                String longAddress = address + ", " + state;
                                intent.putExtra("longAddress", longAddress);
                                Log.d("ADDRESS: ", String.valueOf(longAddress));

                                intent.putExtra("myLocation", myLocation);
                                Log.d("My Location: ", String.valueOf(myLocation));
                                setResult(Activity.RESULT_OK, intent);
                                finish();
                            } else {
                                Intent intent = new Intent(getApplicationContext(), RequestBloodActivity.class);
                                String locationLatLang = latitude + " : " + longitude;

                                Geocoder geocoder;
                                List<Address> addresses = null;
                                geocoder = new Geocoder(ChooseLocationActivity.this, Locale.getDefault());

                                try {
                                    addresses = geocoder.getFromLocation(latitude, longitude, 1);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                                String address = addresses.get(0).getAddressLine(0);
                                String city = addresses.get(0).getLocality();
                                String state = addresses.get(0).getAdminArea();
                                String country = addresses.get(0).getCountryName();
                                String postalCode = addresses.get(0).getPostalCode();
                                String knownName = addresses.get(0).getFeatureName();

                                intent.putExtra("latitude", latitudeStr);
                                intent.putExtra("longitude", longitudeStr);
                                intent.putExtra("latitudeLongitude", locationLatLang);
                                intent.putExtra("address", address);
                                intent.putExtra("city", city);
                                intent.putExtra("state", state);
                                intent.putExtra("country", country);
                                intent.putExtra("postalcode", postalCode);
                                intent.putExtra("knownname", knownName);

                                String myLocation = "https://maps.google.com/?q=" + latitude + "," + longitude;
                                String longAddress = address + ", " + state;
                                intent.putExtra("longAddress", longAddress);
                                Log.d("ADDRESS: ", String.valueOf(longAddress));

                                intent.putExtra("myLocation", myLocation);
                                Log.d("My Location: ", String.valueOf(myLocation));
                                setResult(Activity.RESULT_OK, intent);
                                finish();
                            }
                            return false;
                        }
                    });
                }
            }
        });

    }

    @Override
    public void onBackPressed() {

    }
}