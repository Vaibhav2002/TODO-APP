package com.example.todo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {
    TextInputLayout username, password;
    MaterialButton loginbutton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        username = findViewById(R.id.usernameinput);
        password = findViewById(R.id.passwordinput);
        loginbutton = findViewById(R.id.loginbtn);
        loginbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String usernametext = username.getEditText().getText().toString().trim();
                String userpassword = password.getEditText().getText().toString().trim();
                if (validate(usernametext, userpassword))
                    usercheck(usernametext, userpassword);
            }
        });

    }

    private boolean validate(String usernametext, String userpassword) {
        boolean flag = true;
        if (usernametext.length() <= 4) {
            username.setError("Username length too small");
            flag = false;
        } else if (usernametext.length() >= 12) {
            username.setError("Username length too long");
            flag = false;
        }
        if (userpassword.length() <= 4) {
            password.setError("Password length too small");
            flag = false;
        }
        if (flag) {
            username.setErrorEnabled(false);
            password.setErrorEnabled(false);
        }
        return flag;
    }

    private void usercheck(final String usernametext, final String userpassword) {
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