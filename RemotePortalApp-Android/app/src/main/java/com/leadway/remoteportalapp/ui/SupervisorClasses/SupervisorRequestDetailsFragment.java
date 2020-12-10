package com.leadway.remoteportalapp.ui.SupervisorClasses;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.fragment.NavHostFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.leadway.remoteportalapp.HelperClass;
import com.leadway.remoteportalapp.Helpers.LoginDetails;
import com.leadway.remoteportalapp.R;

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
 * Use the {@link SupervisorRequestDetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SupervisorRequestDetailsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private String mReceivedRequest_id;
    private TextView mLblRequestNo;
    private TextView mLblLocation;
    private TextView mLblFullName;
    private TextView mLblDuration;
    private TextView mLblReason;
    private TextView mLblLocationStatus;
    private TextView mLblCurrentStatus;

    public SupervisorRequestDetailsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SupervisorRequestDetailsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SupervisorRequestDetailsFragment newInstance(String param1, String param2) {
        SupervisorRequestDetailsFragment fragment = new SupervisorRequestDetailsFragment();
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
        return inflater.inflate(R.layout.fragment_supervisor_request_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mReceivedRequest_id = SupervisorRequestDetailsFragmentArgs.fromBundle(getArguments()).getRequestId();

        ImageButton btnMenu = getView().findViewById(R.id.btnMenu);
        btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HelperClass.handleMenuSlide(getActivity());
            }
        });

        ImageView btnBack = getView().findViewById(R.id.btnBack);
        btnBack.setClickable(true);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

        mLblRequestNo = getView().findViewById(R.id.lblRequestNo);
        mLblFullName = getView().findViewById(R.id.lblFullName);
        mLblLocation = getView().findViewById(R.id.lblLocation);
        mLblDuration = getView().findViewById(R.id.lblDuration);
        mLblReason = getView().findViewById(R.id.lblReason);
        mLblLocationStatus = getView().findViewById(R.id.lblLocationStatus);
        mLblCurrentStatus = getView().findViewById(R.id.lblUserlocation);


        TextView textDate = getView().findViewById(R.id.textTodayDate);
        textDate.setText(HelperClass.getCurrentTime());

        TextView textTime = getView().findViewById(R.id.textTodayTime);
        textTime.setText(HelperClass.getCurrentDate());

        Button btnDecline = getView().findViewById(R.id.btnDecline);
        btnDecline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SupervisorDeclineREquestBottomSheet bottomSheetFragmentSuspend = new SupervisorDeclineREquestBottomSheet();
                SupervisorDeclineREquestBottomSheet.mReceivedRequest_id = mReceivedRequest_id;
                bottomSheetFragmentSuspend.show(getChildFragmentManager(), bottomSheetFragmentSuspend.getTag());
            }
        });

        Button btnApprove = getView().findViewById(R.id.btnApprove);
        btnApprove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                callApproveRequest();


            }
        });

        getRequestDetails();
    }

    private void callApproveRequest() {
        if(!(HelperClass.isConnected(getContext()))){
            Toast.makeText(getContext(),"No Internet Available",Toast.LENGTH_SHORT).show();
            return;
        }
        HelperClass.showIndeterminateHud(getContext());
        String approveRequestURL = HelperClass.baseUrl + "remoteWork/approveRemoteWork";
        JSONObject postData = new JSONObject();
        try{
            postData.put("username", LoginDetails.username);
//            postData.put("username","o-faseyitan");
            postData.put("request_id",mReceivedRequest_id);
            postData.put("approve",true);
            postData.put("reason","");
            postData.put("reasonCategory","");
        }catch (Exception ex){
            ex.printStackTrace();
        }
        RequestBody body = RequestBody.Companion.create(postData.toString(),HelperClass.MEDIA_TYPE);
        Request request = new Request.Builder().url(approveRequestURL).post(body).build();
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
                Log.d("jsonString",jsonString);
                HelperClass.dismissHUD();
                try {
                    JSONObject myJsonDict = new JSONObject(jsonString);
                    final String message = myJsonDict.getString("message");
                    final String errMSg = myJsonDict.getString(HelperClass.errMsg);

                    if(myJsonDict.getBoolean(HelperClass.success)){
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getContext(),message,Toast.LENGTH_SHORT).show();
                                NavDirections action = SupervisorRequestDetailsFragmentDirections.actionSupervisorRequestDetailsFragmentToSupervisoreRequestApproved();
                                NavHostFragment.findNavController(getParentFragment()).navigate(action);
                            }
                        });
                    }else{
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getContext(),errMSg,Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                }catch (Exception ex){
                    Log.e("Json parsing",ex.toString());
                }

            }
        });


    }

    private void getRequestDetails() {
        if(!(HelperClass.isConnected(getContext()))){
            Toast.makeText(getContext(),"No Internet Available",Toast.LENGTH_SHORT).show();
            return;
        }
        String getRequestDetailsUrl = HelperClass.baseUrl + "remoteWork/getPendingRemoteWorkDetails?request_id=" + mReceivedRequest_id;
        Request request = new Request.Builder().url(getRequestDetailsUrl).build();
        HelperClass.showIndeterminateHud(getContext());
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
                Log.d("json Response",jsonString);
                try {


                    JSONObject myJsonDict = new JSONObject(jsonString);
                    mLblRequestNo.setText(myJsonDict.getString("requestNo"));
                    mLblFullName.setText(myJsonDict.getString("fullName"));
                    mLblDuration.setText(myJsonDict.getString("duration"));
                    mLblReason.setText(myJsonDict.getString("reason"));
                    mLblLocation.setText(myJsonDict.getString("location"));
                    mLblLocationStatus.setText(myJsonDict.getString("locationStatus").isEmpty() ? "N/A":myJsonDict.getString("locationStatus"));
                    mLblCurrentStatus.setText(myJsonDict.getString("userlocation").isEmpty() ? "N/A":myJsonDict.getString("userlocation"));
//                    final String locationsStatus = myJsonDict.getString("locationStatus");
//                    //Log.d("json Response",locationsStatus);
//
//                    final String userStatus = myJsonDict.getString("userlocation");
//                    //Log.d("json Response",userStatus);
//
//                    getActivity().runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//
//
//                        }
//                    });

                }catch (Exception ex){
                    Log.e("error parsing json", ex.toString());
                }
                HelperClass.dismissHUD();
            }
        });

    }


}
