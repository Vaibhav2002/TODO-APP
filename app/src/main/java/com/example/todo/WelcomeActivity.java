package com.example.todo;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;

import com.google.android.material.button.MaterialButton;

public class WelcomeActivity extends AppCompatActivity {
    MaterialButton login,register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        login=findViewById(R.id.loginbtn);
        register=findViewById(R.id.registerbtn);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Pair pair =new Pair<View,String>(login,"loginbtntrans");
                ActivityOptions activityOptions=ActivityOptions.makeSceneTransitionAnimation(WelcomeActivity.this,pair);
                Intent intent=new Intent(WelcomeActivity.this,LoginActivity.class);
                startActivity(intent,activityOptions.toBundle());
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Pair pair =new Pair<View,String>(register,"loginbtntrans");
                ActivityOptions activityOptions=ActivityOptions.makeSceneTransitionAnimation(WelcomeActivity.this,pair);
                Intent intent=new Intent(WelcomeActivity.this,RegisterActivity.class);
                startActivity(intent,activityOptions.toBundle());
            }
        });

    }
}