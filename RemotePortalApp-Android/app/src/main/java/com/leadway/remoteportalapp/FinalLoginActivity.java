package com.leadway.remoteportalapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.leadway.remoteportalapp.Helpers.LoginDetails;
import com.google.android.material.textfield.TextInputLayout;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.leadway.remoteportalapp.ui.SupervisorClasses.SupervisorRequestListFragment;
import com.leadway.remoteportalapp.ui.home.HomeFragment;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.Executor;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class FinalLoginActivity extends AppCompatActivity {

    public static String activityName = "";
    private EditText mTxtUsername;
    private EditText mTxtPassword;
    public String android_id;
    private Executor executor;
    private BiometricPrompt biometricPrompt;
    private BiometricPrompt.PromptInfo promptInfo;

//    HomeFragment mHomeFragment;
//    SupervisorRequestListFragment mSupervisorRequestListFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_staff);

        Button btnLogin = findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mTxtUsername.getText().toString().isEmpty() || mTxtPassword.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(),"Please Fill All Fields",Toast.LENGTH_SHORT).show();
                }else{
                    testLoginApiCall(false);
                }

            }
        });

        mTxtUsername = findViewById(R.id.txtUsername);
        mTxtPassword = findViewById(R.id.txtPassword);
        mTxtUsername.setText("i-soyebo@leadway.com");
        mTxtPassword.setText("S4s4w1A*");
        android_id = Settings.Secure.getString(getApplicationContext().getContentResolver(),
                Settings.Secure.ANDROID_ID);


        ImageView backButton = findViewById(R.id.btnBack);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        TextView viewTitle = findViewById(R.id.lblLoginTitle);
        viewTitle.setText(activityName);


        executor = ContextCompat.getMainExecutor(this);
        biometricPrompt = new BiometricPrompt(FinalLoginActivity.this,
                executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode,
                                              @NotNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                Toast.makeText(getApplicationContext(),
                        "Authentication error: " + errString, Toast.LENGTH_SHORT)
                        .show();
            }

            @Override
            public void onAuthenticationSucceeded(
                    @NotNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                Toast.makeText(getApplicationContext(),
                        "Authentication succeeded!", Toast.LENGTH_SHORT).show();
                testLoginApiCall(true);
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                Toast.makeText(getApplicationContext(), "Authentication failed",
                        Toast.LENGTH_SHORT)
                        .show();
            }
        });

        promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Biometric login for my app")
                .setSubtitle("Log in using your biometric credential")
                .setNegativeButtonText("Use account password")
                .build();

        // Prompt appears when user clicks "Log in".
        // Consider integrating with the keystore to unlock cryptographic operations,
        // if needed by your app.

        ImageButton btnFingerPrint = findViewById(R.id.btnFingerPrint);
        btnFingerPrint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                biometricPrompt.authenticate(promptInfo);
            }
        });
    }


    void testLoginApiCall(Boolean usesBiometric){
        if(!(HelperClass.isConnected(getApplicationContext()))){
            Toast.makeText(this,"No Internet Available",Toast.LENGTH_SHORT).show();
            return;
        }
//        final KProgressHUD hud = KProgressHUD.create(FinalLoginActivity.this)
//                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
//                .setLabel("Please wait")
//                .setCancellable(true)
//                .setAnimationSpeed(2)
//                .setDimAmount(0.5f)
//                .show();

        String url = HelperClass.baseUrl + "remoteWork/remoteAppLogin";
        JSONObject postdata = new JSONObject();

        if(usesBiometric){
            try {
                postdata.put("username", "");
                postdata.put("password", "");
                postdata.put("supervisor",activityName == "Supervisor Login");
                postdata.put("device_id",android_id);
                postdata.put("biometric",true);
            } catch(JSONException e){
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }else {
            String exj = Base64.encodeToString(mTxtPassword.getText().toString().getBytes(),Base64.NO_WRAP);
            try {
                postdata.put("username", mTxtUsername.getText().toString());
                postdata.put("password", exj);
                postdata.put("supervisor",activityName == "Supervisor Login");
                postdata.put("device_id",android_id);
                postdata.put("biometric",false);
            } catch(JSONException e){
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }





        RequestBody body = RequestBody.Companion.create(postdata.toString(), HelperClass.MEDIA_TYPE);

        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .build();


//        OkHttpClient client = new OkHttpClient();
//        OkHttpClient.Builder builder = new OkHttpClient.Builder();
//        builder.connectTimeout(500000, TimeUnit.SECONDS);
//        builder.readTimeout(500000, TimeUnit.SECONDS);
//        builder.writeTimeout(500000, TimeUnit.SECONDS);
//        builder.retryOnConnectionFailure(true);
//        client = builder.build();
        HelperClass.showIndeterminateHud(this);
        HelperClass.myClient().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        HelperClass.dismissHUD();

                    }
                });

                final String mMessage = e.getMessage();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),mMessage,Toast.LENGTH_SHORT).show();

                    }
                });
                Log.w("failure Response", mMessage);
            }


            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //HelperClass.dismissHUD();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        HelperClass.dismissHUD();

                    }
                });
                try {
                    String mMessage = response.body().string();

                    Log.e("success response", mMessage);

                    final JSONObject jsonLoginDetails = new JSONObject(mMessage);
                    final String errMSg = jsonLoginDetails.getString(HelperClass.errMsg);

                    if (jsonLoginDetails.getBoolean(HelperClass.success)){
                        LoginDetails.username = jsonLoginDetails.getString("username").replace("@leadway.com","");
                        LoginDetails.fullName = jsonLoginDetails.getString("fullName");
                        Intent goToHomeView = new Intent(getApplicationContext(), NewStaffActivity.class);
                        startActivity(goToHomeView);

                    }else{
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(),errMSg,Toast.LENGTH_SHORT).show();

                            }
                        });
                    }

                }catch (final Exception ex){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),ex.toString(),Toast.LENGTH_SHORT).show();

                        }
                    });
                }
                //String mMessage = response.body().string();
                //Log.e("success response", mMessage);
            }
        });
    }

//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//        if (mHomeFragment.isVisible()){
//            mHomeFragment.onBackPressed();
//        }else if(mSupervisorRequestListFragment.isVisible()){
//            mSupervisorRequestListFragment.onBackPressed();
//        }else {
//            super.onBackPressed();
//        }
//    }
}
