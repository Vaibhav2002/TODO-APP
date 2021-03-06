package com.example.todo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

public class ResetComplete extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_complete);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(ResetComplete.this, "Login with your new password", Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(ResetComplete.this,LoginActivity.class);
                startActivity(intent);
                finish();
            }
        },5000);
    }
}