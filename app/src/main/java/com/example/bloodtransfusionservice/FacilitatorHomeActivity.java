package com.example.bloodtransfusionservice;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class FacilitatorHomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facilitator_home);
    }

    public void moveToRegisterDonor(View view) {
        String donorFlag = "false";
        Intent intent = new Intent(getApplicationContext(), UserSignup1Activity.class);
        intent.putExtra("Flag", donorFlag);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {

    }
}