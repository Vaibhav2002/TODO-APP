package com.example.todo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class AccdetailEdit extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accdetail_edit);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent=new Intent(AccdetailEdit.this,TodoActivity.class);
                intent.putExtra("Username",getIntent().getExtras().getString("username"));
                startActivity(intent);
                finish();
            }
        },2500);
    }
}