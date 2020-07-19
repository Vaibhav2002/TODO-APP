package com.example.todo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class TodoActivity extends AppCompatActivity {
    TextView user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo);
        user=findViewById(R.id.usern);
        String username= getIntent().getExtras().getString("Username");
        user.setText(username);
    }
}