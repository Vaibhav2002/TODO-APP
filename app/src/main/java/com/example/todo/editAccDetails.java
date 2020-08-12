package com.example.todo;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ContextThemeWrapper;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.rilixtech.widget.countrycodepicker.CountryCodePicker;

public class editAccDetails extends AppCompatActivity {
    TextInputLayout fullname, phone, pass;
    CountryCodePicker cpp;
    MaterialButton edit;
    LottieAnimationView prog;
    String user, mob;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_acc_details);
        pass = findViewById(R.id.passwordedit);
        fullname = findViewById(R.id.Fullnameedit);
        phone = findViewById(R.id.phoneedit);
        cpp = findViewById(R.id.ccp2);
        edit = findViewById(R.id.acceditbtn);
        prog = findViewById(R.id.progbar3);

        user = getIntent().getExtras().getString("username");


        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users").child(user);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    fullname.getEditText().setText(snapshot.child("fullname").getValue().toString());
                    cpp.setCountryForPhoneCode(Integer.parseInt(snapshot.child("mobile").getValue().toString().substring(1, 3)));
                    phone.getEditText().setText(snapshot.child("mobile").getValue().toString().substring(3));
                    mob = cpp.getSelectedCountryCodeWithPlus() + phone.getEditText().getText().toString();
                    pass.getEditText().setText(snapshot.child("password").getValue().toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prog.setVisibility(View.VISIBLE);
                prog.playAnimation();
                final String passwordinput = pass.getEditText().getText().toString().trim();
                final String fullnameinput = fullname.getEditText().getText().toString().trim();
                final String mobileinput = cpp.getSelectedCountryCodeWithPlus() + phone.getEditText().getText().toString().trim();

                DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference("users").child(user);
                if (isInternetAvalable(editAccDetails.this)) {
                    if (valid(passwordinput, fullnameinput, mobileinput)) {
                        if (mob.equals(mobileinput)) {
                            phone.setErrorEnabled(false);
                            databaseReference1.child("fullname").setValue(fullnameinput);
                            databaseReference1.child("password").setValue(passwordinput);
                            prog.setVisibility(View.GONE);
                            Intent intent = new Intent(editAccDetails.this, AccdetailEdit.class);
                            intent.putExtra("username", user);
                            startActivity(intent);
                            finish();
                        } else {
                            DatabaseReference databaseReference2 = FirebaseDatabase.getInstance().getReference("users");
                            Query query = databaseReference2.orderByChild("mobile").equalTo(mobileinput);
                            query.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if (!snapshot.exists()) {
                                        phone.setErrorEnabled(false);
                                        DatabaseReference databaseReference3 = FirebaseDatabase.getInstance().getReference("users").child(user);
                                        databaseReference3.child("fullname").setValue(fullnameinput);
                                        databaseReference3.child("mobile").setValue(mobileinput);
                                        databaseReference3.child("password").setValue(passwordinput);
                                        prog.setVisibility(View.GONE);
                                        Intent intent = new Intent(editAccDetails.this, AccdetailEdit.class);
                                        intent.putExtra("username", user);
                                        startActivity(intent);
                                        finish();
                                    } else
                                        phone.setError("Account with this phone number already exists");
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                        }

                    }
                } else {
                    prog.setVisibility(View.GONE);
                    showdialog();
                }
            }
        });


    }

    private boolean isInternetAvalable(editAccDetails editAccDetails) {
        ConnectivityManager connectivityManag = (ConnectivityManager) editAccDetails.getSystemService(Context.CONNECTIVITY_SERVICE);
        assert connectivityManag != null;
        NetworkInfo wificonn = connectivityManag.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobileconn = connectivityManag.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        assert mobileconn != null;
        assert wificonn != null;
        return ((mobileconn != null && wificonn.isConnected()) || (wificonn != null && mobileconn.isConnected()));
    }

    private void showdialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(editAccDetails.this, R.style.Theme_AppCompat_Light_Dialog_Alert));
        builder.setMessage("Connect to the internet to continue")
                .setCancelable(false)
                .setTitle("Connect to internet")
                .setPositiveButton("Connect", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(editAccDetails.this, TodoActivity.class));
                        finish();
                    }
                });
        builder.create();
        builder.show();
    }

    private boolean valid(String passwordinput, String fullnameinput, String mobileinput) {
        boolean f1 = validfullname(fullnameinput);
        boolean f3 = validephone(mobileinput);
        boolean f4 = validpassword(passwordinput);
        return f1 && f3 && f4;
    }

    private boolean validephone(String mobileinput) {
        boolean flag = true;
        if (mobileinput.isEmpty()) {
            phone.setError("Field cannot be empty");
            flag = false;
        } else {
            if (mobileinput.length() != 13)
                flag = false;
            else {
                for (int i = 1; i < mobileinput.length(); i++) {
                    if (!Character.isDigit(mobileinput.charAt(i))) {
                        flag = false;
                        break;
                    }
                }
            }
            if (!flag)
                phone.setError("Invalid phone number");
        }
        if (flag) {
            phone.setErrorEnabled(false);
        }
        return flag;
    }

    private boolean validfullname(String fullnameinput) {
        boolean flag = true;
        if (fullnameinput.isEmpty()) {
            fullname.setError("Field cannot be empty");
            flag = false;
        } else {
            for (int i = 0; i < fullnameinput.length(); i++)
                if (!Character.isLetter(fullnameinput.charAt(i)) && fullnameinput.charAt(i) != ' ')
                    flag = false;
            if (!flag)
                fullname.setError("Special characters not allowed");
        }
        if (flag)
            fullname.setErrorEnabled(false);
        return flag;
    }

    private boolean validpassword(String passwordinput) {
        boolean flag = true;
        if (passwordinput.isEmpty()) {
            pass.setError("Field cannot be empty");
            flag = false;
        } else if (passwordinput.length() <= 4) {
            pass.setError("Password too short");
            flag = false;
        }
        if (flag)
            pass.setErrorEnabled(false);
        return flag;
    }


    void reguser(UserHelperClass userHelperClass) {
        prog.setVisibility(View.VISIBLE);
        prog.playAnimation();
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference("users");
        databaseReference.child(userHelperClass.username).setValue(userHelperClass);
        prog.setVisibility(View.GONE);
    }
}
