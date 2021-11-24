package com.example.bloodtransfusionservice;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class LeftNavHeader extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.left_nav_header);

        //Get phone number from session
        SessionManager sessionManager = new SessionManager(this, SessionManager.SESSION_USER_SESSION);
        HashMap<String,String> userDetails =  sessionManager.getUserDetailFromSession();
        String phone = userDetails.get(SessionManager.KEY_PHONE_NO);
        Log.d("==========PHONE: 94+", String.valueOf(phone));
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull @NotNull MenuItem item) {
        return false;
    }
}