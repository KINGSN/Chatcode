package com.darwinbark.yahochat.shubhcode;

import static android.Manifest.permission.READ_PHONE_STATE;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.darwinbark.yahochat.R;
import com.darwinbark.yahochat.utils.Constants;
import com.google.android.material.textfield.TextInputLayout;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.util.List;

public class LoginActivity extends AppCompatActivity {
    Button callSignUp, loginBtn,forgotbtn;
    ImageView image;
    TextView logoText, sloganText;
    TextInputLayout Phone, Password,emailid;
    String phone;
    String password,email;
    private ProgressDialog progressDialog;
    private Dialog loadingDialog;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private String deviceid;

    @SuppressLint("HardwareIds")
    private String GetDeviceID() {
        TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        String deviceID = null;
        int readIMEI = ContextCompat.checkSelfPermission(this,
                READ_PHONE_STATE);
        if (readIMEI == PackageManager.PERMISSION_GRANTED) {
            deviceID = android.provider.Settings.Secure.getString(this.getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
        }
        return deviceID;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);
        init();

        PermissionListener permissionlistener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                Constants.DeviceID = getDeviceId(LoginActivity.this);
                GetDeviceID();
                // Toast.makeText(ActivityRegisterBinding.this, "Permission Granted", Toast.LENGTH_SHORT).show();
                // Toast.makeText(LoginActivity.this,  Constant.DeviceID, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPermissionDenied(List<String> deniedPermissions) {
                finish();
            }
        };

        TedPermission.with(this)
                .setPermissionListener(permissionlistener)
                .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                .setPermissions(READ_PHONE_STATE, Manifest.permission.ACCESS_NETWORK_STATE)
                .check();

    }


    public void init(){
        callSignUp = findViewById(R.id.signUp_screen);
        loginBtn = findViewById(R.id.login_btn);
        image = findViewById(R.id.logo_image);
        logoText = findViewById(R.id.logo_name);
        sloganText = findViewById(R.id.slogan_name);
        emailid = findViewById(R.id.emailid);
        Password = findViewById(R.id.password);
        forgotbtn=(Button)findViewById(R.id.forgotbtn);
        progressDialog = new ProgressDialog(LoginActivity.this);
    }

    public void OpenSignUp(View view)
    {
        Intent loginIntent = new Intent(LoginActivity.this,LoginActivity.class);
        startActivity(loginIntent);
        finish();
    }

    @SuppressLint({"MissingPermission", "HardwareIds"})
    private String getDeviceId(Context context) {

        String deviceId;

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            deviceId = android.provider.Settings.Secure.getString(context.getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
            // Toast.makeText(LoginActivity.this,  deviceId, Toast.LENGTH_SHORT).show();

        }else {

            final TelephonyManager mTelephony = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);



            if (mTelephony.getDeviceId() != null) {
                deviceId = mTelephony.getDeviceId();
            } else {
                deviceId = android.provider.Settings.Secure.getString(context.getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
                //  Toast.makeText(LoginActivity.this,  deviceId, Toast.LENGTH_SHORT).show();
            }
        }

        return deviceId;

    }

}