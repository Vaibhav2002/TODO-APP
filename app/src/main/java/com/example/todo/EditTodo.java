package com.example.todo;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.util.Calendar;

public class EditTodo extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    TextInputLayout editTitle, editDesc;
    ImageView save, delete;
    ImageButton datepick;
    MaterialTextView dateshow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_todo);
        editTitle = findViewById(R.id.titleedit);
        editDesc = findViewById(R.id.descedit);
        save = findViewById(R.id.savetodo);
        delete = findViewById(R.id.deletetodo);
        datepick = findViewById(R.id.datepick2);
        dateshow = findViewById(R.id.dateshow2);
        final String title = getIntent().getExtras().getString("Title");
        final String descript = getIntent().getExtras().getString("Desc");
        final String date = getIntent().getExtras().getString("Date");
        editTitle.getEditText().setText(title);
        editDesc.getEditText().setText(descript);
        dateshow.setText(date);
        final String username = getIntent().getExtras().getString("Username");
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String Title = editTitle.getEditText().getText().toString();
                final String Descript = editDesc.getEditText().getText().toString();
                final String Date = dateshow.getText().toString();
                if (validate(Title, Descript, Date)) {
                    final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users").child(username).child("TODOS").child(title);
                    databaseReference.removeValue();
                    DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference("users").child(username).child("TODOS");
                    databaseReference1.child(Title).setValue(new newTodoHelper(Title, Descript, Date));
                    finish();
                }
            }
        });
        datepick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment frag = new dateFrag();
                frag.show(getSupportFragmentManager(), "Date show");
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
                AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(EditTodo.this, R.style.Theme_AppCompat_Dialog_Alert));
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


    private boolean validate(String title, String descript, String date) {
        boolean flag = true;
        if (title.isEmpty()) {
            editTitle.setError("Field cannot be empty");
            flag = false;
        } else editTitle.setErrorEnabled(false);
        if (descript.isEmpty()) {
            editDesc.setError("Field cannot be empty");
            flag = false;
        } else editDesc.setErrorEnabled(false);
        if (date.isEmpty()) {
            Toast.makeText(this, "Date cannot be empty", Toast.LENGTH_SHORT).show();
            flag = false;
        }
        return flag;
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        String date = DateFormat.getDateInstance(DateFormat.SHORT).format(c.getTime());
        dateshow.setText(date);
    }
}