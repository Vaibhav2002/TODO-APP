package com.example.todo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class newTodo extends AppCompatActivity {
    TextInputLayout title, description;
    ImageView add;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_todo);

        title = findViewById(R.id.titleinput);
        description = findViewById(R.id.descrinput);
        add = findViewById(R.id.addtodo);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String TITLE = title.getEditText().getText().toString();
                String DESC = description.getEditText().getText().toString();
                String username = getIntent().getExtras().getString("name");
                DatabaseReference databaseReference;
                if (getIntent().getExtras().getInt("todos") != 0)
                    databaseReference = FirebaseDatabase.getInstance().getReference("users").child(username).child("TODOS");
                else
                    databaseReference = FirebaseDatabase.getInstance().getReference("users").child(username);

                if (getIntent().getExtras().getInt("todos") != 0)
                    databaseReference.child(TITLE).setValue(new newTodoHelper(TITLE, DESC));
                else
                    databaseReference.child("TODOS").child(TITLE).setValue(new newTodoHelper(TITLE, DESC));
                Toast.makeText(newTodo.this, "Todo added", Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(newTodo.this,TodoActivity.class);
                intent.putExtra("Username",username);
                startActivity(intent);
                finish();
            }
        });
    }
}