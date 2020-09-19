package com.example.todo;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.util.Calendar;

public class newTodo extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    TextInputLayout title, description;
    ImageView add;
    ImageButton datepick;
    MaterialTextView dateshow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_todo);

        title = findViewById(R.id.titleinput);
        description = findViewById(R.id.descrinput);
        add = findViewById(R.id.addtodo);
        datepick=findViewById(R.id.datepick);
        dateshow=findViewById(R.id.dateshow);

        datepick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment frag=new dateFrag();
                frag.show(getSupportFragmentManager(),"date Picker");
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String TITLE = title.getEditText().getText().toString();
                final String DESC = description.getEditText().getText().toString();
                final String date=dateshow.getText().toString();
                final String username = getIntent().getExtras().getString("name");
                if (validate(TITLE, DESC,date)) {
                    DatabaseReference databaseReference;
                    if (getIntent().getExtras().getInt("todos") != 0)
                        databaseReference = FirebaseDatabase.getInstance().getReference("users").child(username).child("TODOS");
                    else
                        databaseReference = FirebaseDatabase.getInstance().getReference("users").child(username);
                    databaseReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (getIntent().getExtras().getInt("todos") != 0)
                                snapshot.getRef().child(TITLE).setValue(new newTodoHelper(TITLE, DESC,date));
                            else
                                snapshot.getRef().child("TODOS").child(TITLE).setValue(new newTodoHelper(TITLE,DESC,date));

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
    private boolean validate (String Title, String Desc,String date){
        boolean flag = true;
        if (Title.isEmpty()) {
            title.setError("Field cannot be empty");
            flag = false;
        } else title.setErrorEnabled(false);
        if (Desc.isEmpty()) {
            description.setError("Field cannot be empty");
            flag = false;
        } else description.setErrorEnabled(false);
        if(date.equals("Date")) {
            Toast.makeText(this, "Date cannot be empty", Toast.LENGTH_SHORT).show();
            flag=false;
        }
        return flag;
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar c=Calendar.getInstance();
        c.set(Calendar.YEAR,year);
        c.set(Calendar.MONTH,month);
        c.set(Calendar.DAY_OF_MONTH,dayOfMonth);
        String date= DateFormat.getDateInstance(DateFormat.SHORT).format(c.getTime());
        dateshow.setText(date);
    }
}