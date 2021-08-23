package com.darwinbark.yahochat.shubhcode;

import static com.google.firebase.auth.PhoneAuthProvider.getCredential;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.darwinbark.yahochat.R;
import com.darwinbark.yahochat.models.User;
import com.darwinbark.yahochat.utils.Constants;
import com.darwinbark.yahochat.utils.GlobalVariables;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.hbb20.CountryCodePicker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class RegisterActivity extends AppCompatActivity {


    Animation topAnim, bottomAnim,slide_in_left,slide_in_right;
    ImageView logo_image;

    private Button verifyButton,verifyotpBtn;
    private RelativeLayout verifyMobile,otpLayout,registerLayout;
    private com.chaos.view.PinView PinView;
    TextView verifyTitle,verifySubTitle;
    TextInputEditText verifyNumber, emailEt, passwordEt, emailForgotEt, nameRegisterEt, phoneRegisterEt,
            emailRegisterEt, passwordRegisterEt, referralRegisterEt, otpEt,resetpaswdtxt1,resetpaswdtxt2;
    ImageButton registerBtn ;
    String verificationID,oneSignalPlayerId,oneSignalPushToken,referedby,deviceid,name,mobileno,password, email,  phone,Password1,refferedby,firebaseOTP,countryCode;
    String codeBySystem;
    private FirebaseAuth auth,mAuth;
    PhoneAuthProvider.OnVerificationStateChangedCallbacks callbacks;
    FirebaseUser user;
    private Dialog loadingDialog;
    String verificationcodebysystem;
    com.chaos.view.PinView pinFromUser;
    CountryCodePicker country_code_picker;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private ImageButton BackBtn ;
    // private ViewDataBinding mBinding;


    @SuppressLint("HardwareIds")
    private String GetDeviceID(){
        TelephonyManager tm=(TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
        String deviceID = null;
        int readIMEI= ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_PHONE_STATE);
        if (readIMEI == PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.CUPCAKE) {
                deviceID = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);
            }
        }
        return deviceID;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        FirebaseApp.initializeApp(this);
        auth = FirebaseAuth.getInstance();
        mAuth=FirebaseAuth.getInstance();
        overridePendingTransition(R.anim.slide_in_right,R.anim.stay);
        preferences = getSharedPreferences(GlobalVariables.ADMIN_PREF, MODE_PRIVATE);
        editor = preferences.edit();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ECLAIR) {
            overridePendingTransition(R.anim.slide_in_right,R.anim.stay);
        }
        //String userOtp=PinView.getText().toString();

        pinFromUser = findViewById(R.id.pinView);

        loadingDialog = new Dialog(this);
        loadingDialog.setContentView(R.layout.lotiee_loading);
        loadingDialog.setCancelable(false);
        loadingDialog.getWindow().setBackgroundDrawableResource(
                R.color.transparent);

        PermissionListener permissionlistener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                Constants.DeviceID = getDeviceId(RegisterActivity.this);
                GetDeviceID();
                // Toast.makeText(ActivityRegisterBinding.this, "Permission Granted", Toast.LENGTH_SHORT).show();
                //Toast.makeText(ActivityRegisterBinding.this,  Constant.DeviceID, Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onPermissionDenied(List<String> deniedPermissions) {
                finish();
            }
        };

        TedPermission.with(this)
                .setPermissionListener(permissionlistener)
                .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                .setPermissions(Manifest.permission.READ_PHONE_STATE, Manifest.permission.ACCESS_NETWORK_STATE)
                .check();

        //Firebase init

        mAuth=FirebaseAuth.getInstance();

        init();
        clickListener();
        countryCode="+"+country_code_picker.getSelectedCountryCode();

    }

    private void init(){
        country_code_picker=findViewById(R.id.country_code_picker);
        deviceid = GetDeviceID();
        emailEt = findViewById(R.id.emailRegisterET);
        passwordEt = findViewById(R.id.passwordRegisterET);
//        nameRegisterEt = findViewById(R.id.nameRegisterET);
//        nameRegisterEt = findViewById(R.id.nameRegisterET);
//        nameRegisterEt = findViewById(R.id.nameRegisterET);
        phoneRegisterEt = findViewById(R.id.phoneRegisterET);
        emailRegisterEt = findViewById(R.id.emailRegisterET);
        passwordRegisterEt = findViewById(R.id.passwordRegisterET);
        referralRegisterEt = findViewById(R.id.referralRegisterET);
        logo_image = findViewById(R.id.logo_image);
        verifyTitle = findViewById(R.id.verifyTitle);
        verifySubTitle = findViewById(R.id.verifySubTitle);
        verifyotpBtn = findViewById(R.id.verifyotpBtn);
        pinFromUser = findViewById(R.id.pinView);
        deviceid = GetDeviceID();
        verifyNumber= findViewById(R.id.verifyNumber);
        verifyButton = findViewById(R.id.verifyBtn);
        registerBtn=findViewById(R.id.registerBtn);
        PinView=findViewById(R.id.pinView);
        verifyMobile=(RelativeLayout)findViewById(R.id.verifyMobile);
        otpLayout=(RelativeLayout)findViewById(R.id.otpLayout);
        registerLayout=(RelativeLayout)findViewById(R.id.registerLayout);
        BackBtn=(ImageButton) findViewById(R.id.BackBtn);

        verifyNumber= findViewById(R.id.verifyNumber);
        verifyButton = findViewById(R.id.verifyBtn);
        PinView=findViewById(R.id.pinView);
        verifyMobile=(RelativeLayout)findViewById(R.id.verifyMobile);
        otpLayout=(RelativeLayout)findViewById(R.id.otpLayout);
        registerLayout=(RelativeLayout)findViewById(R.id.registerLayout);


        ///////////////////// Animation///////////////////////////
        topAnim = AnimationUtils.loadAnimation(this, R.anim.top_animation);
        bottomAnim = AnimationUtils.loadAnimation(this, R.anim.bottom_animation);
        slide_in_left = AnimationUtils.loadAnimation(this, R.anim.slide_in_left);
        slide_in_right = AnimationUtils.loadAnimation(this, R.anim.slide_in_right);

        ///////////////////// Animation///////////////////////////
        logo_image = findViewById(R.id.logo_image);
        verifyTitle = findViewById(R.id.verifyTitle);
        verifySubTitle = findViewById(R.id.verifySubTitle);
        verifyotpBtn = findViewById(R.id.verifyotpBtn);

        ////////////////////Animation ////////////////////////
        logo_image.setAnimation(topAnim);
        verifyTitle.setAnimation(slide_in_left);
        verifySubTitle.setAnimation(slide_in_left);
        ////////////////////Animation ////////////////////////

    }

    private void clickListener(){

        country_code_picker.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
            @Override
            public void onCountrySelected() {
                countryCode="+"+country_code_picker.getSelectedCountryCode();

            }
        });

        verifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Objects.requireNonNull(verifyNumber.getText()).toString().isEmpty()){
                    verifyNumber.setError("Mobile No. can not be Empty");
                    // Toast.makeText(ActivityRegisterBinding.this, "Mobile No. can not be Empty", Toast.LENGTH_SHORT).show();
                    // return;
                }
                else if (verifyNumber.getText().toString().length()!=10)
                {
                    verifyNumber.setError("Insert 10 Digit Phone Number");
                    // Toast.makeText(ActivityRegisterBinding.this, "Input valid Mobile No.", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    String mobileNumber=countryCode+""+verifyNumber.getText().toString();
                    // Toast.makeText(ActivityRegisterBinding.this, ""+mobileNumber, Toast.LENGTH_SHORT).show();
                    loadingDialog.show();
                    sendVerificationCodeToUser(mobileNumber);

                }




            }
        });

        verifyotpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userOtp= Objects.requireNonNull(PinView.getText()).toString();


                if (userOtp.isEmpty() || userOtp.length() < 6) {
                    PinView.setError("Wrong OTP...");
                    PinView.requestFocus();
                    return;
                }else {
                    verifyCode(userOtp);
                    loadingDialog.show();
                }


                // Toast.makeText(ActivityRegisterBinding.this, userOtp+"success"+codeBySystem, Toast.LENGTH_SHORT).show();
                if (getCredential(codeBySystem,codeBySystem).equals(userOtp)){

                    otpLayout.setVisibility(View.GONE);
                    registerLayout.setVisibility(View.VISIBLE);
                }

                //signInWithPhoneAuthCredential(credential);

            }
        });


        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = Objects.requireNonNull(nameRegisterEt.getText()).toString();
                String email = Objects.requireNonNull(emailRegisterEt.getText()).toString();
                String phone = Objects.requireNonNull(phoneRegisterEt.getText()).toString();
                String password = Objects.requireNonNull(passwordRegisterEt.getText()).toString();
                String refferedby = Objects.requireNonNull(referralRegisterEt.getText()).toString();
                String deviceid = GetDeviceID();

                if (nameRegisterEt.getText().toString().isEmpty()){
                    nameRegisterEt.setError("Fields Cant be Empty");
                    //Toast.makeText(ActivityRegisterBinding.this, "Input valid email", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (phoneRegisterEt.getText().toString().isEmpty()|| phone.length() < 10){
                    phoneRegisterEt.setError("Insert A valid 10 Digit Phone Number");
                    // Toast.makeText(ActivityRegisterBinding.this, "Input valid name", Toast.LENGTH_SHORT).show();
                    return;
                }  if (emailRegisterEt.getText().toString().isEmpty()){
                    emailRegisterEt.setError("Insert A valid Email");
                    //Toast.makeText(ActivityRegisterBinding.this, "Input valid email", Toast.LENGTH_SHORT).show();
                    return;

                } if (TextUtils.isEmpty(password) || password.length() < 6) {
                    passwordRegisterEt.setError("Insert A Valid 6 digit password");
                    //Toast.makeText(ActivityRegisterBinding.this, "Input 6 digit valid password", Toast.LENGTH_SHORT).show();
                    return;
                }
                else {
                    loadingDialog.show();
                    User user = new User(deviceid, nameRegisterEt.getText().toString(), emailRegisterEt.getText().toString(), passwordRegisterEt.getText().toString(),
                            phoneRegisterEt.getText().toString(), referralRegisterEt.getText().toString());
                    //register(user);

                    register2(user);


                }
            }

        });


        BackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this,LoginActivity.class);
                startActivity(intent);
            }
        });

    }

    private void sendVerificationCodeToUser(String mobileNumber) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                mobileNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                TaskExecutors.MAIN_THREAD,// Activity (for callback binding)
                mCallbacks);        // OnVerificationStateChangedCallbacks

    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks =
            new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                @Override
                public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                    super.onCodeSent(s, forceResendingToken);
                    codeBySystem = s;

                    verificationcodebysystem= s;
                    loadingDialog.show();
                    verifyMobile.setVisibility(View.GONE);
                    otpLayout.setVisibility(View.VISIBLE);




                    Toast.makeText(RegisterActivity.this, "Mobile Verification Code sent, please check", Toast.LENGTH_SHORT).show();
                    loadingDialog.dismiss();
                }



                @Override
                public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                    String code = phoneAuthCredential.getSmsCode();
                    loadingDialog.show();
                    if (code != null) {
                        pinFromUser.setText(code);
                        verifyCode(code);
                        loadingDialog.dismiss();



                    }
                }

                @Override
                public void onCodeAutoRetrievalTimeOut(@NonNull String s) {
                    super.onCodeAutoRetrievalTimeOut(s);
                    // Toast.makeText(ActivityRegisterBinding.this, "codetimeout "+s, Toast.LENGTH_SHORT).show();

                    loadingDialog.dismiss();
                    Intent intent = new Intent(RegisterActivity.this,RegisterActivity.class);
                    startActivity(intent);
                }

                @Override
                public void onVerificationFailed(@NonNull FirebaseException e) {
                    loadingDialog.dismiss();
                    // Toast.makeText(ActivityRegisterBinding.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    Toast.makeText(RegisterActivity.this, "Verification Failed", Toast.LENGTH_SHORT).show();

                }
            };

    private void verifyCode(String code) {
        PhoneAuthCredential credential = getCredential(codeBySystem, code);
        signInWithPhoneAuthCredential(credential);

        loadingDialog.dismiss();


    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            //Verification completed successfully here Either
                            // store the data or do whatever desire
                            /*editor.putString(mobileno, String.valueOf(verifyNumber.getText()));*/
                            verifyMobile.setVisibility(View.GONE);
                            otpLayout.setVisibility(View.GONE);
                            phoneRegisterEt.setText(verifyNumber.getText());
                            phoneRegisterEt.setFocusable(false);
                            registerLayout.setVisibility(View.VISIBLE);

                            loadingDialog.dismiss();
                        } else {
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                loadingDialog.dismiss();
                                // Toast.makeText(ActivityRegisterBinding.this, "Verification Not Completed! Try again.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }

    private void register2(User user) {
        // final String name = nameRegisterEt.getText().toString();
        final String first_name = Objects.requireNonNull(mBinding.firstnameRegisterET.getText()).toString();
        final String last_name = Objects.requireNonNull(mBinding.lastnameRegisterET.getText()).toString();
        final String email = Objects.requireNonNull(emailRegisterEt.getText()).toString();
        final String password = Objects.requireNonNull(passwordRegisterEt.getText()).toString();
        final String reffered_by  = Objects.requireNonNull(referralRegisterEt.getText()).toString();
        final String deviceid = GetDeviceID();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, RestAPI.API_Registation,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Toast.makeText(LoginActivity.this, "RESPONSE: " + response, Toast.LENGTH_SHORT).show();
                        try {
                            loadingDialog.dismiss();
                            System.out.println(response);
                            JSONObject jsonObject = new JSONObject(response);

                            JSONArray jsonArray = jsonObject.getJSONArray(Constants.AppSid);

                            for (int i = 0; i < jsonArray.length(); i++) {

                                JSONObject object = jsonArray.getJSONObject(i);
                                String msg = object.getString("msg");
                                String success = object.getString("success");




                                if (success.equals("1")) {

                                    Intent intent=new Intent(RegisterActivity.this,LoginActivity.class);
                                    startActivity(intent);
                                    finishAffinity();
                                    Toast.makeText(RegisterActivity.this, "Registred Successfully", Toast.LENGTH_LONG).show();
                                    // progressDialog.dismiss();
                                    loadingDialog.dismiss();
                                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(RegisterActivity.this);
                                    alertDialogBuilder.setTitle(R.string.Congratulation);
                                    alertDialogBuilder.setMessage(msg);
                                    alertDialogBuilder.setIcon(R.mipmap.ic_launcher);
                                    alertDialogBuilder.setPositiveButton(RegisterActivity.this.getResources().getString(R.string.ok_message),
                                            new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface arg0, int arg1) {
                                                    startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                                                    //Log.d("Response",msg);
                                                    finishAffinity();
                                                }
                                            });

                                    AlertDialog alertDialog = alertDialogBuilder.create();
                                    alertDialog.show();

                                    // Toast.makeText(Register.this, "Congratulation! Registration Successfully", Toast.LENGTH_LONG).show();

                                } else {

                                    Intent intent=new Intent(RegisterActivity.this,LoginActivity.class);
                                    startActivity(intent);
                                    finishAffinity();


                                    //progressDialog.dismiss();
                                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(RegisterActivity.this);
                                    alertDialogBuilder.setTitle(R.string.app_name);
                                    alertDialogBuilder.setMessage(msg);
                                    alertDialogBuilder.setPositiveButton(RegisterActivity.this.getResources().getString(R.string.ok_message),
                                            new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface arg0, int arg1) {
                                                    startActivity(new Intent(RegisterActivity.this, RegisterActivity.class));
                                                    //Log.d("Response",msg);
                                                    finishAffinity();
                                                }
                                            });

                                    AlertDialog alertDialog = alertDialogBuilder.create();
                                    alertDialog.show();
                                    //  Toast.makeText(Register.this, msg, Toast.LENGTH_LONG).show();

                                }
                            }
                        } catch (JSONException e) {
                            // progressDialog.dismiss();
                            e.printStackTrace();
                        }
                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //  Toast.makeText(ActivityRegisterBinding.this, "RESPONSE: " + error, Toast.LENGTH_SHORT).show();
                Log.e("Error", "" + error.getMessage());
                //  Toast.makeText(ActivityRegisterBinding.this, "ErrorV: " + error.getMessage(),
                //Toast.LENGTH_SHORT).show();
                loadingDialog.dismiss();
                android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(RegisterActivity.this);
                alertDialogBuilder.setTitle("Something Went Wrong");
                alertDialogBuilder.setMessage("Please Try With Active Internet ");
                alertDialogBuilder.setIcon(R.mipmap.ic_launcher);
                alertDialogBuilder.setPositiveButton(RegisterActivity.this.getResources().getString(R.string.ok_message),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                finish();
                                loadingDialog.dismiss();
                                finishAffinity();
                            }
                        });

                android.app.AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();

            }
        }) { @Override
        protected Map<String, String> getParams() {

            Map<String, String> params = new HashMap<>();
            /* params.put("id", user.getUid());*/
            params.put("user_register", "");
            params.put("user_id",user.getPhone());
            params.put("username",email);
            params.put("first_name",first_name);
            params.put("last_name",last_name);
            params.put("email",email);
            params.put("device_id",deviceid);
            params.put("password",password);
            params.put("reffered_by",reffered_by);
            params.put("joining_bonus",preferences.getString(GlobalVariables.JOINING_BONUS,""));


            return params;
        }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(RegisterActivity.this);
        requestQueue.add(stringRequest);



    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

}