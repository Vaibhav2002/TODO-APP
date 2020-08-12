package com.example.todo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.ScrollView;
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
    ImageView newtodo,moreitem;
    ScrollView scrollView;
    String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo);
        textView=findViewById(R.id.usernamedisp);
        newtodo=findViewById(R.id.newtodobtn);
        scrollView=findViewById(R.id.scroll);
        moreitem=findViewById(R.id.more);
        username=getIntent().getExtras().getString("Username");
        textView.setText(username);
        recyclerView=findViewById(R.id.recycleview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        list=new ArrayList<>();

        moreitem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu=new PopupMenu(TodoActivity.this,v);
                popupMenu.inflate(R.menu.todomenu);
                popupMenu.show();
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId())
                        {
                            case R.id.editacc:
                                Intent intent=new Intent(TodoActivity.this,editAccDetails.class);
                                System.out.println(username);
                                intent.putExtra("username",username);
                                startActivity(intent);
                                return true;
                            case R.id.logout:
                                startActivity(new Intent(TodoActivity.this,LoginActivity.class));
                                finish();
                                return true;
                        }
                        return false;
                    }
                });
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
        scrollView.setSmoothScrollingEnabled(true);
        scrollView.fling(20);
        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("users").child(username).child("TODOS");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for(DataSnapshot dataSnapshot:snapshot.getChildren())
                {
                    myDoes p=dataSnapshot.getValue(myDoes.class);
                    list.add(p);
                }
                System.out.println(list.size());
                doesAdapter =new DoesAdapter(TodoActivity.this,list,username);
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