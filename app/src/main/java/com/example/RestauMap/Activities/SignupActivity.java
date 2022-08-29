package com.example.RestauMap.Activities;

import android.app.ProgressDialog;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.RestauMap.Model.register.RegisterPOST;
import com.example.RestauMap.R;
import com.example.RestauMap.Retrofit.ApiClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignupActivity extends AppCompatActivity {

    //edittext+button
    private EditText editEmail = null;
    private EditText editPassword = null;
    private EditText editConfirmPassword = null;
    private EditText editName = null;
    private Button btnSignUp = null;

    //toast
    private Toast mToast = null;

    //user info
    private String udid = "12345";
    private String appid = "6";

    /**
     * OnCreate
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); //lock Portrait view

        //buttons
        btnSignUp = (Button) findViewById((R.id.btn_signup));

        //EditText fields
        editName = (EditText) findViewById(R.id.s_name);
        editPassword = (EditText) findViewById(R.id.s_password);
        editConfirmPassword = (EditText) findViewById(R.id.s_confirm_password);
        editEmail = (EditText) findViewById(R.id.s_email);


        //signup button
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //dialog on loading
                final ProgressDialog mDialog = new ProgressDialog(SignupActivity.this);

                mDialog.setMessage("Please wait...");
                mDialog.show();

                if (TextUtils.isEmpty(editEmail.getText().toString()) || TextUtils.isEmpty(editPassword.getText().toString()) || TextUtils.isEmpty(editName.getText().toString())) {
                    if (mToast != null) mToast.cancel();
                    mToast = Toast.makeText(SignupActivity.this, "Email/Password/Name Required", Toast.LENGTH_SHORT);
                    mToast.show();
                    mDialog.dismiss();
                } else {
                    if((editPassword.getText().toString()).equals(editConfirmPassword.getText().toString())) {
                        //proceed to register
                        register(mDialog);
                    }else{
                        if (mToast != null) mToast.cancel();
                        mToast = Toast.makeText(SignupActivity.this, "Password missmatch!", Toast.LENGTH_SHORT);
                        mToast.show();
                        mDialog.dismiss();
                    }
                }
            }
        });
    }

    /**
     * login attempt
     */
    public void register(final ProgressDialog mDialog){
        Call<RegisterPOST> userRegister = ApiClient.getRegisterService().userRegister(udid, appid, editEmail.getText().toString(), editPassword.getText().toString(), editName.getText().toString());
        userRegister.enqueue(new Callback<RegisterPOST>() {
            @Override
            public void onResponse(Call<RegisterPOST> call, Response<RegisterPOST> response) {
                if (response.body() == null){
                    if (mToast != null) mToast.cancel();
                    mToast = Toast.makeText(SignupActivity.this, "Empty Body!", Toast.LENGTH_SHORT);
                    mToast.show();
                }
                else {

                    if (response.isSuccessful()) {
                        mDialog.dismiss();
                        switch (response.body().getRegisterResponse().getSuccess()) {
                            case "0":
                                if (mToast != null) mToast.cancel();
                                mToast = Toast.makeText(SignupActivity.this, "Error Code 0!", Toast.LENGTH_SHORT);
                                mToast.show();
                                break;
                            case "1":
                                if (mToast != null) mToast.cancel();
                                mToast = Toast.makeText(SignupActivity.this, "Error Code 1!", Toast.LENGTH_SHORT);
                                mToast.show();
                                break;
                            case "2":

                                if (mToast != null) mToast.cancel();
                                mToast = Toast.makeText(SignupActivity.this, "Registered Succesfully!", Toast.LENGTH_SHORT);
                                mToast.show();
                                finish();
                                break;
                        }
                    } else {
                        if (mToast != null) mToast.cancel();
                        mToast = Toast.makeText(SignupActivity.this, "No response!", Toast.LENGTH_SHORT);
                        mToast.show();
                    }
                }

            }

            @Override
            public void onFailure(Call<RegisterPOST> call, Throwable t) {
                if (mToast != null) mToast.cancel();
                mToast = Toast.makeText(SignupActivity.this,"Throwable: "+t.getLocalizedMessage(),Toast.LENGTH_SHORT);
                mToast.show();
            }

        });
    }
}