package com.example.bloodtransfusionservice;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;

import android.text.method.LinkMovementMethod;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

public class DonorDiseasesActivity extends AppCompatActivity {
    Button buttonUserAccept, buttonUserBack;
    ScrollView scrollView;
    FloatingActionButton buttonScrollTop;
    TextView linkTextView;
    ImageView backImg;
    String donorFlag, passwordResetVal, donorReg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donor_diseases);

        donorFlag = getIntent().getStringExtra("Flag");
        passwordResetVal = getIntent().getStringExtra("passwordResetFlag");
        donorReg = getIntent().getStringExtra("donorRegistration");
        scrollView = findViewById(R.id.scrollView);
        buttonScrollTop = findViewById(R.id.buttonScrollTop);

        //buttonUserAccept = findViewById(R.id.btn_user_accept);
        //buttonUserBack = findViewById(R.id.btn_user_reject);
        backImg = findViewById(R.id.img_back);

        /*buttonUserAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DonorDiseasesActivity.this, UserSignup1Activity.class);
                intent.putExtra("Flag", donorFlag);
                intent.putExtra("passwordResetFlag", passwordResetVal);
                intent.putExtra("donorRegistration", donorReg);
                startActivity(intent);
            }
        });

        buttonUserBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DonorDiseasesActivity.this, UserLoginActivity.class);
                intent.putExtra("Flag", donorFlag);
                intent.putExtra("passwordResetFlag", passwordResetVal);
                intent.putExtra("donorRegistration", donorReg);
                startActivity(intent);
            }
        });*/

        scrollView.getViewTreeObserver()
                .addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
                    @Override
                    public void onScrollChanged() {
                        if (scrollView.getChildAt(0).getBottom()
                                <= (scrollView.getHeight() + scrollView.getScrollY())) {
                            buttonScrollTop.setVisibility(View.VISIBLE);
                            buttonScrollTop.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    scrollView.setScrollY(0);
                                }
                            });
                        } else {
                            buttonScrollTop.setVisibility(View.INVISIBLE);
                        }
                    }
                });

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

        /* Call hyperlink method */
        setupHyperlink();
    }

    private void setupHyperlink() {
        linkTextView = findViewById(R.id.donor_diseases);
        linkTextView.setMovementMethod(LinkMovementMethod.getInstance());
    }

    @Override
    public void onBackPressed() {

    }
}