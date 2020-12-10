//package com.example.remoteportalapp.Helpers;
//
//import android.app.ProgressDialog;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.os.AsyncTask;
//
//import androidx.appcompat.app.AlertDialog;
//
//import com.example.remoteportalapp.R;
//
//import org.json.JSONException;
//import org.json.JSONObject;
//
//private class Login extends AsyncTask<String,Void,String> {
//
//    ProgressDialog dialog= new ProgressDialog(login.this);
//    JSONObject res=null;
//    JSONObject jreq= new JSONObject();
//    @Override
//    protected void onPreExecute() {
//        dialog.setTitle(UtilityClass.languageSelection(getResources().getString(R.string.dialog_login)));
//        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//        dialog.setMessage(UtilityClass.languageSelection(getResources().getString(R.string.dialog_wait_login)));
//        dialog.setIndeterminate(true);
//        dialog.setCancelable(false);
//        dialog.setCanceledOnTouchOutside(false);
//        dialog.show();
//
//    }
//    @Override
//    protected String doInBackground(String... strings) {
//
//
//        try {
//            res=new JSONObject(webServiceSync.methodCall("LeadLogin/",jreq));
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//        return "";
//    }
//
//    @Override
//    protected void onPostExecute(String s) {
//        dialog.hide();
//
//        try {
//            if(res.getBoolean("success")){
//                final JSONObject joversion=res.getJSONObject("vrsnRes");
//                if(!joversion.getBoolean("passVersion"))
//                {
//                    AlertDialog.Builder builder = new AlertDialog.Builder(login.this);
//                    builder.setTitle(UtilityClass.languageSelection("Update Available"));
//                    builder.setMessage(joversion.getString("message"));
//                    builder.setCancelable(false);
//                    builder.setPositiveButton(UtilityClass.languageSelection("Ok"), new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialogInterface, int i) {
//
//                            try {
//                                if(joversion.getBoolean("lockApp")){
//
//                                    finish();
//
//                                }
//                                else
//                                {
//                                    userpref.lang_id=res.getInt("lang_id");
//                                    userpref.sc_Login=res.getBoolean("sc_Login");
//                                    userpref.fullname = res.getString("userFullname");
//                                    userpref.username = res.getString("username");
//                                    userpref.language = res.getJSONArray("lang");
//                                    userpref.accountExi = res.getBoolean("bankAccount");
//                                    Intent si = new Intent(login.this, MainActivity.class);
//                                    startActivity(si);
//                                    //overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
//                                    finish();
//                                }
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }
//
//
//                        }
//                    });
//
//                    builder.show();
//
//
//                }
//                else {
//                    userpref.lang_id=res.getInt("lang_id");
//                    userpref.sc_Login=res.getBoolean("sc_Login");
//                    userpref.fullname = res.getString("userFullname");
//                    userpref.username = res.getString("username");
//                    // userpref.language = res.getJSONArray("lang");
//                    userpref.accountExi = res.getBoolean("bankAccount");
//                    Intent i = new Intent(login.this, MainActivity.class);
//                    startActivity(i);
//                    //overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
//                    finish();
//                }
//            }
//            else {
//
//                UtilityClass.displayMessage(login.this,UtilityClass.languageSelection("Login"),res.getString("errorMsg"));
//                //Toast.makeText(login.this,(res.getString("errorMsg")),Toast.LENGTH_LONG).show();
//
//            }
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//    }
//}