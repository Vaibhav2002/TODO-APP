package com.example.todo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class TodoActivity extends AppCompatActivity {
    MaterialTextView textView;
    RecyclerView recyclerView;
    ArrayList<myDoes> list;
    DoesAdapter doesAdapter;
    ImageView logout,newtodo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo);
        textView=findViewById(R.id.usernamedisp);
        logout=findViewById(R.id.logouticon);
        newtodo=findViewById(R.id.newtodobtn);
        final String username=getIntent().getExtras().getString("Username");
        textView.setText(username);
        recyclerView=findViewById(R.id.recycleview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        list=new ArrayList<>();

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TodoActivity.this,LoginActivity.class));
                finish();
            }
        });

        newtodo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(TodoActivity.this,newTodo.class);
                intent.putExtra("todos",list.size());
                intent.putExtra("name",username);
                startActivity(intent);
            }
        });
        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("users").child(username).child("TODOS");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot:snapshot.getChildren())
                {
                    myDoes p=dataSnapshot.getValue(myDoes.class);
                    list.add(p);
                }
                System.out.println(list.size());
                doesAdapter =new DoesAdapter(TodoActivity.this,list);
                recyclerView.setAdapter(doesAdapter);
                doesAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(TodoActivity.this, "NO data found", Toast.LENGTH_SHORT).show();
            }
        });
    }
}