package com.example.todo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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
                final String TITLE = title.getEditText().getText().toString();
                final String DESC = description.getEditText().getText().toString();
                final String username = getIntent().getExtras().getString("name");
                if (validate(TITLE, DESC)) {
                    DatabaseReference databaseReference;
                    if (getIntent().getExtras().getInt("todos") != 0)
                        databaseReference = FirebaseDatabase.getInstance().getReference("users").child(username).child("TODOS");
                    else
                        databaseReference = FirebaseDatabase.getInstance().getReference("users").child(username);
                    databaseReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (getIntent().getExtras().getInt("todos") != 0)
                                snapshot.getRef().child(TITLE).setValue(new newTodoHelper(TITLE, DESC));
                            else
                                snapshot.getRef().child("TODOS").child(TITLE).setValue(new newTodoHelper(TITLE,DESC));
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });
                    Toast.makeText(newTodo.this, "Todo added", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(newTodo.this, TodoActivity.class);
                    intent.putExtra("Username", username);
                    startActivity(intent);
                    finish();
                }

            }
        });
    }
    private boolean validate (String Title, String Desc){
        boolean flag = true;
        if (Title.isEmpty()) {
            title.setError("Field cannot be empty");
            flag = false;
        } else title.setErrorEnabled(false);
        if (Desc.isEmpty()) {
            description.setError("Field cannot be empty");
            flag = false;
        } else description.setErrorEnabled(false);
        return flag;
    }

}