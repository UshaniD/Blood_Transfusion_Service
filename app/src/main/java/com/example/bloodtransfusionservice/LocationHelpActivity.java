package com.example.bloodtransfusionservice;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class LocationHelpActivity extends AppCompatActivity {
    String donorFlag, passwordResetVal, donorReg;
    ImageView backImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_help);

        donorFlag = getIntent().getStringExtra("Flag");
        passwordResetVal = getIntent().getStringExtra("passwordResetFlag");
        donorReg = getIntent().getStringExtra("donorRegistration");
        backImg = findViewById(R.id.img_back);

        backImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (donorFlag.equals("true")){
                    Intent intent = new Intent(getApplicationContext(), UserSignup1Activity.class);
                    intent.putExtra("Flag", donorFlag);
                    intent.putExtra("passwordResetFlag", passwordResetVal);
                    intent.putExtra("donorRegistration", donorReg);
                    setResult(Activity.RESULT_CANCELED, intent);
                    //startActivity(intent);
                    finish();
                }else{
                    Intent intent = new Intent(getApplicationContext(), FacilitatorSignup1Activity.class);
                    intent.putExtra("Flag", donorFlag);
                    intent.putExtra("passwordResetFlag", passwordResetVal);
                    intent.putExtra("donorRegistration", donorReg);
                    setResult(Activity.RESULT_CANCELED, intent);
                    //startActivity(intent);
                    finish();
                }
            }
        });
    }


    @Override
    public void onBackPressed() {

    }
}