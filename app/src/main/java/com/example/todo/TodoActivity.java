package com.example.todo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.google.android.material.textview.MaterialTextView;

public class TodoActivity extends AppCompatActivity {
    MaterialTextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo);
        textView=findViewById(R.id.usernamedisp);
        String username=getIntent().getExtras().getString("Username");
        textView.setText(username);

    }
}