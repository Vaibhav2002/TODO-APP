package com.example.todo;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.mukesh.OtpView;

import java.util.concurrent.TimeUnit;

public class forgotPassword extends AppCompatActivity {
    MaterialTextView subtitle;
    String mobileNo;
    OtpView otp;
    String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        subtitle = findViewById(R.id.subtitle);
        otp = findViewById(R.id.otp);
        username = getIntent().getExtras().getString("username");
        mobileNo = getIntent().getExtras().getString("mobile");
        System.out.println(mobileNo);
        String s="Please enter the 6-digit code sent to\n"+mobileNo;
        subtitle.setText(s);
        sendVerifictaionCode();
    }

    private void sendVerifictaionCode() {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(mobileNo, 15, TimeUnit.SECONDS, this, mCallbacks);
    }

    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new
            PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                @Override
                public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                    System.out.println(phoneAuthCredential.getSmsCode());
                    otp.setText(phoneAuthCredential.getSmsCode());
                    Toast.makeText(getApplicationContext(), "verification Complete", Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(forgotPassword.this,ResetPassword.class);
                    intent.putExtra("username",username);
                    startActivity(intent);
                    finish();
                }

                @Override
                public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                    super.onCodeSent(s, forceResendingToken);

                }

                @Override
                public void onVerificationFailed(@NonNull FirebaseException e) {
                    Toast.makeText(forgotPassword.this, "Verification Failed", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onCodeAutoRetrievalTimeOut(@NonNull String s) {
                    super.onCodeAutoRetrievalTimeOut(s);
                    Toast.makeText(forgotPassword.this, "Verifictaion failed", Toast.LENGTH_SHORT).show();
                    finish();
                }
            };
}