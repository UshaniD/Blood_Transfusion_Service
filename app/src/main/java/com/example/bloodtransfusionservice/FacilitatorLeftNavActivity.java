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

public class FacilitatorLeftNavActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    TextView buttonConfirmCampaign, buttonRegisterNewDonor, buttonUpdateBloodStock, buttonRemoveDonor, buttonViewBloodStock;
    static String donorFlag = "false";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facilitator_left_nav);

        buttonConfirmCampaign = findViewById(R.id.btn_confirm_campaign);
        buttonRegisterNewDonor = findViewById(R.id.btn_register_new_donor);
        buttonUpdateBloodStock = findViewById(R.id.btn_add_new_donor);
        buttonRemoveDonor = findViewById(R.id.btn_remove_donor);
        buttonViewBloodStock = findViewById(R.id.btn_blood_store);

        drawerLayout = findViewById(R.id.drawerlayout);
        navigationView = findViewById(R.id.navigation_view);
        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_open, R.string.navigation_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        navigationView.setCheckedItem(R.id.home_menu);

        buttonConfirmCampaign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ConfirmCampaignActivity.class);
                startActivity(intent);
            }
        });

        buttonUpdateBloodStock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), UpdateBloodStockActivity.class);
                startActivity(intent);
            }
        });

        buttonViewBloodStock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ViewBloodStockActivity.class);
                startActivity(intent);
            }
        });

        buttonRemoveDonor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ViewDonorActivity.class);
                startActivity(intent);
            }
        });

        buttonRegisterNewDonor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String donorFlag = "true";
                String passwordResetVal = "false";
                String donorReg = "true";
                Intent intent = new Intent(getApplicationContext(), UserSignup1Activity.class);
                intent.putExtra("Flag", donorFlag);
                intent.putExtra("passwordResetFlag", passwordResetVal);
                intent.putExtra("donorRegistration", donorReg);
                Log.d("Flag", String.valueOf(donorFlag));
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
                Intent intent = new Intent(FacilitatorLeftNavActivity.this, AboutusActivity.class);
                startActivity(intent);
                break;
            case R.id.info:
                Intent intent6 = new Intent(FacilitatorLeftNavActivity.this, FacilitatorInformationActivity.class);
                startActivity(intent6);
                break;
            case R.id.facilitator_info_menu:
                Intent intent1 = new Intent(FacilitatorLeftNavActivity.this, FacilitatorInfoActivity.class);
                startActivity(intent1);
                break;
            case R.id.blood_issue_registry_menu:
                Intent intent2 = new Intent(FacilitatorLeftNavActivity.this, BloodIssueRegistryActivity.class);
                startActivity(intent2);
                break;
            case R.id.blood_received_registry_menu:
                Intent intent7 = new Intent(FacilitatorLeftNavActivity.this, BloodReceivedRegistryActivity.class);
                startActivity(intent7);
                break;
            case R.id.blood_request_menu:
                Intent intent3 = new Intent(FacilitatorLeftNavActivity.this, BloodRequestsActivity.class);
                startActivity(intent3);
                break;
            case R.id.request_blood_menu:
                Intent intent8 = new Intent(FacilitatorLeftNavActivity.this, ViewRequestBloodActivity.class);
                startActivity(intent8);
                break;
            case R.id.analysis_menu:
                Intent intent9 = new Intent(FacilitatorLeftNavActivity.this, BloodStockAnalysisActivity.class);
                startActivity(intent9);
                break;
            case R.id.campaigns_menu:
                Intent intent4 = new Intent(FacilitatorLeftNavActivity.this, UpComingCampaignActivity.class);
                startActivity(intent4);
                break;
            case R.id.profile_menu:
                Intent intent5 = new Intent(FacilitatorLeftNavActivity.this, FacilitatorProfileActivity.class);
                startActivity(intent5);
                break;
            case R.id.logout_menu:
                new AlertDialog.Builder(FacilitatorLeftNavActivity.this)
                        .setTitle("Logout")
                        .setMessage("Are you sure. Do you want to logout?")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                SessionManager sessionManager = new SessionManager(FacilitatorLeftNavActivity.this, SessionManager.SESSION_REMEMBER_ME_SESSION);
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