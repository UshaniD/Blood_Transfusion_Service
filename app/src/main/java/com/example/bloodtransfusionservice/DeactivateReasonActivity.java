package com.example.bloodtransfusionservice;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class DeactivateReasonActivity extends AppCompatActivity {
    TextInputLayout reason;
    Button btn_confirm, btn_back;
    ImageView backImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deactivate_reason);

        reason = findViewById(R.id.input_reason);
        btn_confirm = findViewById(R.id.btn_confirm);
        btn_back = findViewById(R.id.btn_back);
        backImg = findViewById(R.id.img_back);
        String donorID = getIntent().getStringExtra("deactivateID");

        ActivityCompat.requestPermissions(DeactivateReasonActivity.this, new String[]{android.Manifest.permission.SEND_SMS, Manifest.permission.READ_SMS}, PackageManager.PERMISSION_GRANTED);

        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String reason_var = reason.getEditText().getText().toString();

                if (!reason_var.isEmpty()) {
                    new AlertDialog.Builder(DeactivateReasonActivity.this)
                            .setTitle("Donor Deactivation")
                            .setMessage("Want to deactivate Donor ID: +94" + donorID)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    String status = "inactive";
                                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("donor").child(donorID);
                                    Map<String, Object> updates = new HashMap<String, Object>();
                                    updates.put("userStatus", status);
                                    ref.updateChildren(updates);

                                    //Send SMS
                                    //Build message
                                    String message = "You are deactivated due to: "+reason_var;
                                    Log.d("Message", String.valueOf(message));

                                    SmsManager smsManager = SmsManager.getDefault();
                                    smsManager.sendTextMessage("+94" + donorID, null, message, null, null);

                                    Intent intent = new Intent(getApplicationContext(), ViewDonorActivity.class);
                                    startActivity(intent);

                                }
                            })
                            .setNegativeButton(android.R.string.no, null).show();
                }else{
                    reason.setError("Please enter reason to deactivate");
                }
            }
        });

        backImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ViewDonorActivity.class);
                setResult(Activity.RESULT_CANCELED, intent);
                finish();

            }
        });

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), DonorDetailsActivity.class);
                setResult(Activity.RESULT_CANCELED, intent);
                finish();

            }
        });

    }
    @Override
    public void onBackPressed() {

    }
}