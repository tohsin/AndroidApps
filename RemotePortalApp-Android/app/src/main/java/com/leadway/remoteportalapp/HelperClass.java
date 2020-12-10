package com.leadway.remoteportalapp;

import android.app.Activity;
import android.content.Context;
import android.icu.text.SimpleDateFormat;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentActivity;

import com.google.android.material.navigation.NavigationView;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.leadway.remoteportalapp.Helpers.LoginDetails;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;

public class HelperClass {


    public static String success = "success";
    public static String errMsg = "errorMsg";
    public static String message = "message";
    private static Date date = new Date();

    public static String baseUrl = "http://mc.leadway.com:89/tenancyApplication/Service1.svc/";
    private static KProgressHUD sHud;

    public static Boolean isConnected(Context context){
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isAvailable() &&
                networkInfo.isConnected();
    }

//    public boolean isConnected() {
//        boolean connected = false;
//        try {
//            ConnectivityManager cm = (ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
//            NetworkInfo nInfo = cm.getActiveNetworkInfo();
//            connected = nInfo != null && nInfo.isAvailable() && nInfo.isConnected();
//            return connected;
//        } catch (Exception e) {
//            Log.e("Connectivity Exception", e.getMessage());
//        }
//        return connected;
//    }

    public static void showIndeterminateHud(Context context){
        sHud = KProgressHUD.create(context)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait")
                .setCancellable(true)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show();
    }

    public static void dismissHUD(){
        sHud.dismiss();
    }

    public static OkHttpClient myClient(){
        OkHttpClient client = new OkHttpClient();
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(500000, TimeUnit.SECONDS);
        builder.readTimeout(500000, TimeUnit.SECONDS);
        builder.writeTimeout(500000, TimeUnit.SECONDS);
        builder.retryOnConnectionFailure(true);
        client = builder.build();
        return client;

    }

    public static MediaType MEDIA_TYPE = MediaType.parse("application/json");


    public static void showToast(Activity activity, final Context context, final String message){
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(context,message,Toast.LENGTH_SHORT).show();
            }
        });
        return;
    }

    public static String getCurrentTime(){
        SimpleDateFormat formatter = new SimpleDateFormat("dd MMMM yyyy");
        String strDate = formatter.format(date);
        return  strDate;
    }

    public static String getCurrentDate(){
        SimpleDateFormat formatterTime = new SimpleDateFormat("HH:mm");
        String strTime =  formatterTime.format(date);
        return strTime;
    }

    public static void handleMenuSlide(FragmentActivity fragmentActivity){
        final DrawerLayout drawer  =  fragmentActivity.findViewById(R.id.drawer_layout);
        NavigationView navigationView = fragmentActivity.findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        TextView navUsername = headerView.findViewById(R.id.lblUsernameHeader);
        TextView navFullname = headerView.findViewById(R.id.lblfullNameHeader);
        navUsername.setText(LoginDetails.username);
        navFullname.setText(LoginDetails.fullName);

        if (drawer.isDrawerOpen(Gravity.LEFT)) {
            System.out.print("okay");
            //drawer.closeDrawer(Gravity.LEFT);
        }else{
            drawer.openDrawer(GravityCompat.START);
        }

    }
}

