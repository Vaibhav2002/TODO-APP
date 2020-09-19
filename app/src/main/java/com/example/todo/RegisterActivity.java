package com.example.todo;

import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Pair;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ContextThemeWrapper;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.rilixtech.widget.countrycodepicker.CountryCodePicker;

public class RegisterActivity extends AppCompatActivity {
    TextInputLayout usernametext, passwordtext, fullnametext, phonetext;
    CountryCodePicker countryCodePicker;
    MaterialButton signup;
    MaterialTextView gotologin;
    LottieAnimationView progressBar;
    String usernameinput, passwordinput, fullnameinput, mobileinput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        usernametext = findViewById(R.id.usernameinput1);
        passwordtext = findViewById(R.id.passwordinput1);
        phonetext = findViewById(R.id.phoneinput1);
        fullnametext = findViewById(R.id.Fullnameinput1);
        progressBar = findViewById(R.id.progbar2);
        signup = findViewById(R.id.signupbtn);
        gotologin = findViewById(R.id.loingoto);
        countryCodePicker = findViewById(R.id.ccp);

        progressBar.setVisibility(View.GONE);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                usernameinput = usernametext.getEditText().getText().toString().trim();
                passwordinput = passwordtext.getEditText().getText().toString().trim();
                fullnameinput = fullnametext.getEditText().getText().toString().trim();
                mobileinput = countryCodePicker.getSelectedCountryCodeWithPlus() + phonetext.getEditText().getText().toString().trim();
                System.out.println(mobileinput);
                if (isInternetAvalable(RegisterActivity.this)) {
                    if (valid(usernameinput, passwordinput, fullnameinput, mobileinput)) {
                        progressBar.setVisibility(View.VISIBLE);
                        progressBar.playAnimation();
                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users").child(usernameinput);
                        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (!snapshot.exists()) {
                                    usernametext.setErrorEnabled(false);
                                    DatabaseReference databaseReference2 = FirebaseDatabase.getInstance().getReference("users");
                                    Query query = databaseReference2.orderByChild("mobile").equalTo(mobileinput);
                                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            if (!snapshot.exists()) {
                                                phonetext.setErrorEnabled(false);
                                                UserHelperClass userHelperClass = new UserHelperClass(usernameinput, passwordinput, mobileinput, fullnameinput);
                                                reguser(userHelperClass);
                                                progressBar.setVisibility(View.GONE);
                                                Toast.makeText(RegisterActivity.this, "Login in with your new account", Toast.LENGTH_SHORT).show();
                                                Pair pair[] = new Pair[2];
                                                pair[0] = new Pair<View, String>(usernametext, "usernametans");
                                                pair[1] = new Pair<View, String>(passwordtext, "passwordtans");
                                                ActivityOptions activityOptions = ActivityOptions.makeSceneTransitionAnimation(RegisterActivity.this, pair);
                                                startActivity(new Intent(RegisterActivity.this, LoginActivity.class), activityOptions.toBundle());
                                            } else
                                                phonetext.setError("Account with this number already exists");
                                            progressBar.setVisibility(View.GONE);
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {
                                        }
                                    });
                                } else usernametext.setError("Username already taken");
                                progressBar.setVisibility(View.GONE);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                            }
                        });
                    }
                } else
                    showdialog();
            }
        });

        gotologin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Pair pair[] = new Pair[2];
                pair[0] = new Pair<View, String>(usernametext, "usernametans");
                pair[1] = new Pair<View, String>(passwordtext, "passwordtans");
                ActivityOptions activityOptions = ActivityOptions.makeSceneTransitionAnimation(RegisterActivity.this, pair);
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class), activityOptions.toBundle());
            }
        });

    }

    private void showdialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(RegisterActivity.this, R.style.Theme_AppCompat_Light_Dialog_Alert));
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
                        startActivity(new Intent(RegisterActivity.this, WelcomeActivity.class));
                        finish();
                    }
                });
        builder.create();
        builder.show();
    }

    private boolean isInternetAvalable(RegisterActivity registerActivity) {
        ConnectivityManager connectivityManag = (ConnectivityManager) registerActivity.getSystemService(Context.CONNECTIVITY_SERVICE);
        assert connectivityManag != null;
        NetworkInfo wificonn = connectivityManag.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobileconn = connectivityManag.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        assert mobileconn != null;
        assert wificonn != null;
        return ((mobileconn != null && wificonn.isConnected()) || (wificonn != null && mobileconn.isConnected()));
    }

    void reguser(UserHelperClass userHelperClass) {
        progressBar.setVisibility(View.VISIBLE);
        progressBar.playAnimation();
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference("users");
        databaseReference.child(userHelperClass.username).setValue(userHelperClass);
        progressBar.setVisibility(View.GONE);
    }

    private boolean valid(String usernameinput, String passwordinput, String fullnameinput, String mobileinput) {
        boolean f1 = validfullname(fullnameinput);
        boolean f2 = validusername(usernameinput);
        boolean f3 = validephone(mobileinput);
        boolean f4 = validpassword(passwordinput);
        return f1 && f2 && f3 && f4;
    }

    private boolean validephone(String mobileinput) {
        boolean flag = true;
        if (mobileinput.isEmpty()) {
            phonetext.setError("Field cannot be empty");
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
                phonetext.setError("Invalid phone number");
        }
        if (flag) {
            phonetext.setErrorEnabled(false);
        }
        return flag;
    }

    private boolean validfullname(String fullnameinput) {
        boolean flag = true;
        if (fullnameinput.isEmpty()) {
            fullnametext.setError("Field cannot be empty");
            flag = false;
        } else {
            for (int i = 0; i < fullnameinput.length(); i++)
                if (!Character.isLetter(fullnameinput.charAt(i)) && fullnameinput.charAt(i) != ' ')
                    flag = false;
            if (!flag)
                fullnametext.setError("Special characters not allowed");
        }
        if (flag)
            fullnametext.setErrorEnabled(false);
        return flag;
    }

    private boolean validpassword(String passwordinput) {
        boolean flag = true;
        if (passwordinput.isEmpty()) {
            passwordtext.setError("Field cannot be empty");
            flag = false;
        } else if (passwordinput.length() <= 4) {
            passwordtext.setError("Password too short");
            flag = false;
        }
        if (flag)
            passwordtext.setErrorEnabled(false);
        return flag;
    }

    private boolean validusername(String usernameinput) {
        boolean flag = true;
        if (usernameinput.isEmpty()) {
            usernametext.setError("Field cannot be empty");
            flag = false;
        } else if (usernameinput.length() <= 4) {
            usernametext.setError("Username too short");
            flag = false;
        }
        if (flag)
            usernametext.setErrorEnabled(false);
        return flag;
    }
}