package com.example.todo;

import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Pair;
import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {
    TextInputLayout username, password;
    MaterialTextView signupdirect;
    MaterialButton loginbutton;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        username = findViewById(R.id.usernameinput);
        password = findViewById(R.id.passwordinput);
        progressBar=findViewById(R.id.progbar);
        loginbutton = findViewById(R.id.loginbtn);
        signupdirect=findViewById(R.id.signupgoto);
        loginbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isInternetAvalable(LoginActivity.this)) {
                    String usernametext = username.getEditText().getText().toString().trim();
                    String userpassword = password.getEditText().getText().toString().trim();
                    if (validate(usernametext, userpassword))
                        usercheck(usernametext, userpassword);
                }
                else showdialog();
            }
        });

        signupdirect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Pair[] pair =new Pair[2];
                pair[0]=new Pair<View,String>(username,"usernametrans");
                pair[1]=new Pair<View,String>(password,"passwordtrans");
                ActivityOptions activityOptions=ActivityOptions.makeSceneTransitionAnimation(LoginActivity.this,pair);
                Intent intent=new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent,activityOptions.toBundle());
            }
        });

    }

    private void showdialog() {
        AlertDialog.Builder builder=new AlertDialog.Builder(LoginActivity.this);
        builder.setMessage("Connect to the internet to continue")
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
                        startActivity(new Intent(LoginActivity.this,WelcomeActivity.class));
                        finish();
                    }
                });
    }

    private boolean isInternetAvalable(LoginActivity loginActivity) {
        ConnectivityManager connectivityManag= (ConnectivityManager) loginActivity.getSystemService(Context.CONNECTIVITY_SERVICE);
        assert connectivityManag != null;
        NetworkInfo wificonn=connectivityManag.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobileconn=connectivityManag.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        assert mobileconn != null;
        assert wificonn != null;
        return((mobileconn!=null&&wificonn.isConnected())||(wificonn!=null&&mobileconn.isConnected()));
    }

    private boolean validate(String usernametext, String userpassword) {
        boolean flag = true;
        if (usernametext.isEmpty()) {
            username.setError("Field cannot be empty");
            flag = false;
        }
        if (userpassword.isEmpty()) {
            password.setError("Field cannot be empty");
            flag = false;
        }
        if (flag) {
            username.setErrorEnabled(false);
            password.setErrorEnabled(false);
        }
        return flag;
    }

    private void usercheck(final String usernametext, final String userpassword) {
        progressBar.setVisibility(View.VISIBLE);
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference("users");
        Query check = databaseReference.orderByChild("username").equalTo(usernametext);
        final boolean[] userfound = {true};
        final boolean[] wrongpass = {false};
        final boolean[] passfound = {false};
        check.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String passwordin = dataSnapshot.child(usernametext).child("password").getValue(String.class);
                    progressBar.setVisibility(View.GONE);
                    if (passwordin.equals(userpassword)) {
                        System.out.println("pass correct");
                        Intent intent=new Intent(LoginActivity.this,TodoActivity.class);
                        intent.putExtra("Username",usernametext);
                        startActivity(intent);
                        passfound[0] = true;
                    } else {
                        System.out.println("incorrect pass");
                        wrongpass[0] = true;
                    }
                } else {
                    System.out.println("user not found");
                    userfound[0] = false;
                }
                if (!userfound[0])
                    username.setError("User not found");
                else {
                    username.setErrorEnabled(false);
                    if (wrongpass[0])
                        password.setError("Incorrect password");
                    else if(passfound[0])
                        password.setErrorEnabled(false);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });


    }
}