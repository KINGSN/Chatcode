package com.darwinbark.yahochat.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.darwinbark.yahochat.R;
import com.google.android.material.textfield.TextInputLayout;

public class LoginActivity extends AppCompatActivity {

    Button callSignUp, loginBtn,forgotbtn;
    ImageView image;
    TextView logoText, sloganText;
    TextInputLayout Phone, Password,emailid;
    String phone;
    String password,email;
    private ProgressDialog progressDialog;
    private Dialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        callSignUp = findViewById(R.id.signUp_screen);
        loginBtn = findViewById(R.id.login_btn);
        image = findViewById(R.id.logo_image);
        logoText = findViewById(R.id.logo_name);
        sloganText = findViewById(R.id.slogan_name);
        emailid = findViewById(R.id.emailid);
        Password = findViewById(R.id.password);
        forgotbtn=(Button)findViewById(R.id.forgotbtn);
        progressDialog = new ProgressDialog(LoginActivity.this);

        forgotbtn.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, HomeActivity.class));
            finish();
        });

    }

    public void OpenSignUp(View view){

    }
}