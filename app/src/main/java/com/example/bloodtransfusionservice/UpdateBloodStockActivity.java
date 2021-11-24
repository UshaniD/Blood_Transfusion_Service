package com.example.bloodtransfusionservice;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

public class UpdateBloodStockActivity extends AppCompatActivity {
    Button buttonVerify;
    TextInputLayout txtPhone;
    ImageView backImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_blood_stock);

        buttonVerify = findViewById(R.id.btn_user_next);
        txtPhone = findViewById(R.id.input_phone);
        backImg = findViewById(R.id.img_back);

        buttonVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone_var = txtPhone.getEditText().getText().toString();
                if (!phone_var.trim().isEmpty()) {
                    txtPhone.setError(null);
                    txtPhone.setErrorEnabled(false);
                    if (phone_var.trim().length() == 9) {
                        txtPhone.setError(null);
                        txtPhone.setErrorEnabled(false);

                        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                        DatabaseReference databaseReference = firebaseDatabase.getReference("donor");
                        Query checkmobile = databaseReference.orderByChild("mobile").equalTo(phone_var);

                        checkmobile.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                                if (snapshot.exists()) {
                                    Intent intent = new Intent(getApplicationContext(), VerifyBloodStockActivity.class);
                                    intent.putExtra("mobile", phone_var);
                                    startActivity(intent);
                                } else {
                                    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                                    DatabaseReference databaseReference = firebaseDatabase.getReference("defaultUsers");
                                    Query checkmobile = databaseReference.orderByChild("mobile").equalTo(phone_var);

                                    checkmobile.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                                            if (snapshot.exists()) {
                                                Intent intent = new Intent(getApplicationContext(), VerifyBloodStockActivity.class);
                                                intent.putExtra("mobile", phone_var);
                                                startActivity(intent);
                                            }else {
                                                txtPhone.setError("Invalid mobile number");
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull @NotNull DatabaseError error) {

                                        }
                                    });
                                }
                            }
                            @Override
                            public void onCancelled(@NonNull @NotNull DatabaseError error) {

                            }
                        });

                    } else {
                        txtPhone.setError("Please enter valid mobile number");
                    }
                } else {
                    txtPhone.setError("Enter mobile number");
                }
            }
        });

        backImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), FacilitatorLeftNavActivity.class);
                setResult(Activity.RESULT_CANCELED, intent);
                finish();

            }
        });
    }

    public void moveToPreviousScreen(View view) {
        Intent intent = new Intent(getApplicationContext(), FacilitatorLeftNavActivity.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {

    }
}