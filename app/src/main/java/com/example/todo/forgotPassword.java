package com.example.todo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class forgotPassword extends AppCompatActivity {
    MaterialTextView subtitle;
    String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        subtitle=findViewById(R.id.subtitle);
        String username=getIntent().getExtras().getString("username");
        System.out.println(username);
        assert username != null;
        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("users").child(username).child("email");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                email= snapshot.getValue().toString();
                System.out.println(email);
                String sub= "Please type the 6 digit verification code sent to\n"+email;
                System.out.println(sub);
                subtitle.setText(sub);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
}