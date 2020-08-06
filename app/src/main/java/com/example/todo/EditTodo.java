package com.example.todo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ContextThemeWrapper;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EditTodo extends AppCompatActivity {
    TextInputLayout editTitle, editDesc;
    ImageView save, delete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_todo);
        editTitle = findViewById(R.id.titleedit);
        editDesc = findViewById(R.id.descedit);
        save = findViewById(R.id.savetodo);
        delete = findViewById(R.id.deletetodo);
        final String title = getIntent().getExtras().getString("Title");
        final String descript = getIntent().getExtras().getString("Desc");
        editTitle.getEditText().setText(title);
        editDesc.getEditText().setText(descript);
        final String username = getIntent().getExtras().getString("Username");
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String Title = editTitle.getEditText().getText().toString();
                final String Descript = editDesc.getEditText().getText().toString();
                if (validate(Title, Descript)) {
                    final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users").child(username).child("TODOS").child(title);
                    databaseReference.removeValue();
                    DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference("users").child(username).child("TODOS");
                    databaseReference1.child(Title).setValue(new newTodoHelper(Title, Descript));
                    finish();
                }
            }
        });
        save.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(EditTodo.this, "Save Todo", Toast.LENGTH_SHORT).show();
                return true;
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(EditTodo.this,R.style.Theme_AppCompat_Dialog_Alert));
                builder.setMessage("Are you sure you want to delete this todo?")
                        .setCancelable(true)
                        .setTitle("Delete")
                        .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users").child(username).child("TODOS").child(title);
                                databaseReference.removeValue();
                                Toast.makeText(EditTodo.this, "Todo deleted", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
            }

        });
        delete.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(EditTodo.this, "Delete Todo", Toast.LENGTH_SHORT).show();
                return true;
            }
        });
    }


    private boolean validate(String title, String descript) {
        boolean flag = true;
        if (title.isEmpty()) {
            editTitle.setError("Field cannot be empty");
            flag = false;
        } else editTitle.setErrorEnabled(false);
        if (descript.isEmpty()) {
            editDesc.setError("Field cannot be empty");
            flag = false;
        } else editDesc.setErrorEnabled(false);
        return flag;
    }
}