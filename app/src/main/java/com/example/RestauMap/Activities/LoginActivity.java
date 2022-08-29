package com.example.RestauMap.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.RestauMap.Model.login.LoginPOST;
import com.example.RestauMap.Model.login.User;
import com.example.RestauMap.R;
import com.example.RestauMap.Retrofit.ApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity  {

    //activity tag
    private static final String TAG = "LoginActivity";

    //layout
    private RelativeLayout rellay1 = null;
    private RelativeLayout rellay2 = null;

    //buttons
    private Button btnSignIn = null;
    private Button button_signup = null;

    //toast
    private Toast mToast = null;

    //edittext
    private EditText editEmail = null;
    private EditText editPassword = null;

    //user info
    private String udid = "12345";
    private String appid = "6";
    private String token = null;

    /**
     * hide on start
     */
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            rellay1.setVisibility(View.VISIBLE);
            rellay2.setVisibility(View.VISIBLE);
        }
    };

    /**
     * on create
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); //lock Portrait view

        //rellays to hide for splash
        rellay1 = findViewById(R.id.rellay1);
        rellay2 = findViewById(R.id.rellay2);

        //buttons
        btnSignIn = findViewById(R.id.login);
        button_signup = findViewById(R.id.Signup);

        //editText fields
        editPassword = findViewById(R.id.l_password);
        editEmail = findViewById(R.id.l_email);

        //Sign in button clicked
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //dialog on loading
                final ProgressDialog mDialog = new ProgressDialog(LoginActivity.this);

                mDialog.setMessage("Please wait...");
                mDialog.show();

                if (TextUtils.isEmpty(editEmail.getText().toString()) || TextUtils.isEmpty(editPassword.getText().toString())) {
                    if (mToast != null) mToast.cancel();
                    mToast = Toast.makeText(LoginActivity.this, "Username / Password Required", Toast.LENGTH_SHORT);
                    mToast.show();
                    mDialog.dismiss();
                } else {
                    //proceed to login
                    login(mDialog);
                }

            }

        });

        //Signup java
        button_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openSignup();
            }
        });

        //splash timeout
        new Handler(Looper.myLooper()).postDelayed(runnable, 2000);
    }

    /**
     * login attempt
     */
    public void login(final ProgressDialog mDialog){

        //token
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "getInstanceId failed", task.getException());
                            return;
                        }

                        // Get new Instance ID token
                        token = task.getResult().getToken();

                        //call server
                        Call<LoginPOST> loginResponseCall = ApiClient.getUserService().userLogin(udid, appid, editEmail.getText().toString(), editPassword.getText().toString(), token);
                        loginResponseCall.enqueue(new Callback<LoginPOST>() {
                            @Override
                            public void onResponse(Call<LoginPOST> call, Response<LoginPOST> response) {
                                if (response.body() == null){
                                    if (mToast != null) mToast.cancel();
                                    mToast = Toast.makeText(LoginActivity.this, "Empty Body!", Toast.LENGTH_SHORT);
                                    mToast.show();
                                }
                                else {
                                    if (response.isSuccessful()) {
                                        mDialog.dismiss();
                                        final User u = response.body().getLoginResponse().getUser();
                                        switch (response.body().getLoginResponse().getSuccess()) {
                                            case "0":
                                                if (mToast != null) mToast.cancel();
                                                mToast = Toast.makeText(LoginActivity.this, "Error Code 0!", Toast.LENGTH_SHORT);
                                                mToast.show();
                                                break;
                                            case "1":
                                                if (mToast != null) mToast.cancel();
                                                mToast = Toast.makeText(LoginActivity.this, "Error Code 1!", Toast.LENGTH_SHORT);
                                                mToast.show();
                                                break;
                                            case "2":
                                                if (mToast != null) mToast.cancel();
                                                mToast = Toast.makeText(LoginActivity.this, "Login Succesful!", Toast.LENGTH_SHORT);
                                                mToast.show();
                                                openMap(u);
                                                break;
                                        }
                                    } else {
                                        if (mToast != null) mToast.cancel();
                                        mToast = Toast.makeText(LoginActivity.this, "No response!", Toast.LENGTH_SHORT);
                                        mToast.show();
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<LoginPOST> call, Throwable t) {
                                if (mToast != null) mToast.cancel();
                                mToast = Toast.makeText(LoginActivity.this,"Throwable: "+t.getLocalizedMessage(),Toast.LENGTH_SHORT);
                                mToast.show();
                            }

                        });
                    }
                });
    }

    /**
     * Open map
     */
    public void openMap(User u) {
        Intent intent = new Intent(this, MapsActivity.class);
        intent.putExtra("user_detail", u);
        startActivity(intent);
        finish();
    }

    /**
     * open Signup
     */
    public void openSignup() {
        Intent intent = new Intent(this, SignupActivity.class);
        startActivity(intent);
    }
}