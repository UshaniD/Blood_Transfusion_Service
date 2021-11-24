package com.example.bloodtransfusionservice;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;

import java.util.HashMap;

public class LeftNavActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    TextView button_campaigns_near_me, button_request_blood, button_organize_campaign;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_left_nav);

        drawerLayout = findViewById(R.id.drawerlayout);
        navigationView = findViewById(R.id.navigation_view);
        toolbar = findViewById(R.id.toolbar);
        String mobile = getIntent().getStringExtra("mobile");
        Log.d("==========MOBILE: ", String.valueOf(mobile));

        //Get phone number from session
        SessionManager sessionManager = new SessionManager(this,SessionManager.SESSION_USER_SESSION);
        HashMap<String, String> userDetails = sessionManager.getUserDetailFromSession();
        String phone = userDetails.get(SessionManager.KEY_PHONE_NO);
        Log.d("==========PHONE: ", String.valueOf(phone));

        setSupportActionBar(toolbar);
        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_open, R.string.navigation_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        navigationView.setCheckedItem(R.id.home_menu);
        button_request_blood = findViewById(R.id.btn_request_blood);
        button_campaigns_near_me = findViewById(R.id.btn_campaigns_near_me);
        button_organize_campaign = findViewById(R.id.btn_organize_campaign);

        button_campaigns_near_me.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CampaignsNearMeActivity.class);
                startActivity(intent);
            }
        });

        button_request_blood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), RequestBloodActivity.class);
                startActivity(intent);
            }
        });

        button_organize_campaign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), OrganizeCampaignActivity.class);
                intent.putExtra("mobile", mobile);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {

        }

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.home_menu:
                break;
            case R.id.aboutus_menu:
                Intent intent = new Intent(LeftNavActivity.this, AboutusActivity.class);
                startActivity(intent);
                break;
            case R.id.info:
                Intent intent2 = new Intent(LeftNavActivity.this, UserInformationActivity.class);
                startActivity(intent2);
                break;
            case R.id.diseases:
                Intent intent8 = new Intent(LeftNavActivity.this, ListOfDiseasesActivity.class);
                startActivity(intent8);
                break;
            case R.id.campaign_history_menu:
                Intent intent3 = new Intent(LeftNavActivity.this, CampaignHistoryActivity.class);
                startActivity(intent3);
                break;
            case R.id.request_blood_menu:
                Intent intent6 = new Intent(LeftNavActivity.this, RequestBloodHistoryActivity.class);
                startActivity(intent6);
                break;
            case R.id.achievements_menu:
                Intent intent7 = new Intent(LeftNavActivity.this, MyAchievementsActivity.class);
                startActivity(intent7);
                break;
            case R.id.profile_menu:
                Intent intent5 = new Intent(LeftNavActivity.this, UserProfileActivity.class);
                startActivity(intent5);
                break;
            case R.id.logout_menu:
                new AlertDialog.Builder(LeftNavActivity.this)
                        .setTitle("Logout")
                        .setMessage("Are you sure. Do you want to logout?")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                SessionManager sessionManager = new SessionManager(LeftNavActivity.this, SessionManager.SESSION_REMEMBER_ME_SESSION);
                                sessionManager.logoutUserFromSession();
                                startActivity(new Intent(getApplicationContext(),UserLoginActivity.class));
                                finish();
                            }
                        })
                        .setNegativeButton(android.R.string.no, null).show();
                break;

        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}