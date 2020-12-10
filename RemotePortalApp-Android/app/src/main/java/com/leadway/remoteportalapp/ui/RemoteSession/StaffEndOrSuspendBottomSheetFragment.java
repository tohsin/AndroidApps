package com.leadway.remoteportalapp.ui.RemoteSession;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.leadway.remoteportalapp.HelperClass;
import com.leadway.remoteportalapp.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link StaffEndOrSuspendBottomSheetFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StaffEndOrSuspendBottomSheetFragment extends BottomSheetDialogFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    public static String fragmentTitle = "";
    public static String sessionID = "";
    private EditText mTxtReason;


    public StaffEndOrSuspendBottomSheetFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment StaffEndOrSuspendBottomSheetFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static StaffEndOrSuspendBottomSheetFragment newInstance(String param1, String param2) {
        StaffEndOrSuspendBottomSheetFragment fragment = new StaffEndOrSuspendBottomSheetFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.supervisor_end_or_suspend_bottom_layout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView textDate = getView().findViewById(R.id.textTodayDate);
        textDate.setText(HelperClass.getCurrentTime());

        TextView textTime = getView().findViewById(R.id.textTodayTime);
        textTime.setText(HelperClass.getCurrentDate());

        TextView lblFragmentTitle = getView().findViewById(R.id.lblTitle);
        lblFragmentTitle.setText(fragmentTitle);

        mTxtReason = getView().findViewById(R.id.txtReason);

        Button btnEndSuspend = getView().findViewById(R.id.btnEndSuspend);
        btnEndSuspend.setText(fragmentTitle);
        btnEndSuspend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleEndorSuspendSession();
            }
        });

        TextView lblSubtitle = getView().findViewById(R.id.lblSubtitle);
        if (fragmentTitle == "End Remote Session"){
            lblSubtitle.setText("Reason For Ending Remote Session");
        }else {
            lblSubtitle.setText("Reason For Suspending Remote Session");
        }


        Button btnCancel = getView().findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });


    }

    private void handleEndorSuspendSession() {
        if (mTxtReason.getText().toString().isEmpty()){
            Toast.makeText(getContext(),"Reason Cannot Be Empty",Toast.LENGTH_SHORT).show();
            return;
        }
        String url;
        if (fragmentTitle == "End Remote Session"){
            url = HelperClass.baseUrl + "remoteWork/checkOutSession";
        }else {
            url = HelperClass.baseUrl + "remoteWork/suspendSession";
        }
        JSONObject postData = new JSONObject();
        try {
            postData.put("session_id",sessionID);
            postData.put("remark",mTxtReason.getText().toString());
        }catch (Exception ex){
            ex.printStackTrace();
        }

        if(!(HelperClass.isConnected(getContext()))){
            Toast.makeText(getContext(),"No Internet Available",Toast.LENGTH_SHORT).show();
            return;
        }
        HelperClass.showIndeterminateHud(getContext());
        RequestBody body = RequestBody.Companion.create(postData.toString(),HelperClass.MEDIA_TYPE);
        final Request request = new Request.Builder().url(url).post(body).build();
        HelperClass.myClient().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        HelperClass.dismissHUD();
                    }
                });
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String jsonString = response.body().string();
                Log.e("json",jsonString);

                try {
                    JSONObject myJsonDict = new JSONObject(jsonString);
                    Boolean success = myJsonDict.getBoolean(HelperClass.success);
                    final String errMsg = myJsonDict.getString(HelperClass.errMsg);
                    if (success){
                      final String message = myJsonDict.getString("message");
                      getActivity().runOnUiThread(new Runnable() {
                          @Override
                          public void run() {
                              Toast.makeText(getContext(),message,Toast.LENGTH_SHORT).show();
                              getActivity().getSupportFragmentManager().popBackStack();
                          }
                      });
                    }else {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getContext(),errMsg,Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                }catch (Exception ex){
                    Log.e("error parsing json",ex.toString());
                }
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        HelperClass.dismissHUD();
                    }
                });

            }
        });
    }
}
