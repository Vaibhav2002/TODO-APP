package com.example.todo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ContextThemeWrapper;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ResetPassword extends AppCompatActivity {
    TextInputLayout pass, conpass;
    MaterialButton reset;
    LottieAnimationView progbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        pass = findViewById(R.id.passedit);
        conpass = findViewById(R.id.confirmpass);
        reset = findViewById(R.id.resetbtn);
        progbar=findViewById(R.id.progbarreset);
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isInternetAvalable(ResetPassword.this)) {
                    String passed = pass.getEditText().getText().toString();
                    String Confirmpass = conpass.getEditText().getText().toString();
                    if (verify(passed, Confirmpass)) {
                        progbar.setVisibility(View.VISIBLE);
                        progbar.playAnimation();
                        String username=getIntent().getExtras().getString("username");
                        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("users").child(username);
                        databaseReference.child("password").setValue(passed);
                        progbar.setVisibility(View.GONE);
                        Toast.makeText(ResetPassword.this, "Password reset succesfull", Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent(ResetPassword.this,ResetComplete.class);
                        startActivity(intent);
                    }
                }
                else showdialog();
            }
        });

    }

    private boolean verify(String passed, String confirmpass) {
        boolean flag = true;
        if (passed.isEmpty()) {
            pass.setError("Field cannot be empty");
            flag = false;
        }
        if (confirmpass.isEmpty()) {
            flag = false;
            conpass.setError("Field cannot be empty");
        }
        if (!flag)
            return flag;
        if (!passed.equals(confirmpass)) {
            conpass.setError("Passwords do not match");
            flag = false;
        }
        if (flag) {
            pass.setErrorEnabled(false);
            conpass.setErrorEnabled(false);
        }
        return flag;
    }
    private void showdialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(ResetPassword.this,R.style.Theme_AppCompat_Dialog_Alert));
        builder.setMessage("Connect to the internet to continue")
                .setTitle("Connect to internet")
                .setCancelable(false)
                .setPositiveButton("Connect", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(ResetPassword.this, WelcomeActivity.class));
                        finish();
                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private boolean isInternetAvalable(ResetPassword resetactivity) {
        ConnectivityManager connectivityManag = (ConnectivityManager) resetactivity.getSystemService(Context.CONNECTIVITY_SERVICE);
        assert connectivityManag != null;
        NetworkInfo wificonn = connectivityManag.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobileconn = connectivityManag.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        assert mobileconn != null;
        assert wificonn != null;
        return ((mobileconn != null && wificonn.isConnected()) || (wificonn != null && mobileconn.isConnected()));
    }
}