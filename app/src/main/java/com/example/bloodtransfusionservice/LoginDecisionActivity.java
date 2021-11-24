package com.example.bloodtransfusionservice;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class LoginDecisionActivity extends AppCompatActivity {
    Button btn_user, btn_facilitator;
    ProgressDialog pd;
    static String donorFlag = "true";
    static String donorReg = "true";
    static String passwordResetVal = "false";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_decision);
        pd = new ProgressDialog(this);
        pd.setMessage("Loading...");
        pd.setCancelable(true);
        pd.setCanceledOnTouchOutside(false);
        passwordResetVal = getIntent().getStringExtra("passwordResetFlag");

        btn_user = findViewById(R.id.btn_user);
        btn_facilitator = findViewById(R.id.btn_facilitator);

        btn_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                donorFlag = "true";
                donorReg = "false"; // Not coming through facilitator registration
                Intent intent = new Intent(getApplicationContext(), UserSignup1Activity.class);
                intent.putExtra("Flag", donorFlag);
                intent.putExtra("passwordResetFlag", passwordResetVal);
                intent.putExtra("donorRegistration", donorReg);
                Log.d("Flag", String.valueOf(donorFlag));
                startActivity(intent);
            }
        });

        btn_facilitator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String donorFlag = "false";
                String donorReg = "false"; // Not coming through facilitator registration
                Intent intent = new Intent(getApplicationContext(), FacilitatorSignup1Activity.class);
                intent.putExtra("Flag", donorFlag);
                intent.putExtra("passwordResetFlag", passwordResetVal);
                intent.putExtra("donorRegistration", donorReg);
                Log.d("Flag", String.valueOf(donorFlag));
                startActivity(intent);
            }
        });
    }

    public void moveToPreviousScreen(View view) {
        Intent intent = new Intent(getApplicationContext(), UserLoginActivity.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {

    }
}