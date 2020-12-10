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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.leadway.remoteportalapp.HelperClass;
import com.leadway.remoteportalapp.Helpers.LoginDetails;
import com.leadway.remoteportalapp.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SupervisorDeclineREquestBottomSheet#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SupervisorDeclineREquestBottomSheet extends BottomSheetDialogFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    public static String mReceivedRequest_id;
    private Spinner mSpinnerDeclineReasons;
    private EditText mTxtReason;
    private ArrayList<String> mDeclineReasons;

    public SupervisorDeclineREquestBottomSheet() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SupervisorDeclineREquestBottomSheet.
     */
    // TODO: Rename and change types and number of parameters
    public static SupervisorDeclineREquestBottomSheet newInstance(String param1, String param2) {
        SupervisorDeclineREquestBottomSheet fragment = new SupervisorDeclineREquestBottomSheet();
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
        return inflater.inflate(R.layout.supervisor_decline_request_bottom_sheet, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button btnCancel = getView().findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

        mSpinnerDeclineReasons = getView().findViewById(R.id.spinnerReason);
        mTxtReason = getView().findViewById(R.id.txtReason);

        getListOfDeclineReasons();
        Button btnDeclineRequest = getView().findViewById(R.id.btnDecline);
        btnDeclineRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callDeclineRequest();
            }
        });
    }

    private void getListOfDeclineReasons() {
        //HelperClass.showIndeterminateHud(getActivity());
        OkHttpClient client = new OkHttpClient();
        String getBranchesEndpoint = "remoteWork/listrejectionReasons";
        Request request = new Request.Builder()
                .url(HelperClass.baseUrl + getBranchesEndpoint)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String jsonString = response.body().string();
                Log.d("response",jsonString);

                try {
                    JSONObject myJsonObj = new JSONObject(jsonString);
                    final String errMsg = myJsonObj.getString(HelperClass.errMsg);
                    if (myJsonObj.getBoolean(HelperClass.success)){
                        JSONArray rejectionList = myJsonObj.getJSONArray("rejectionCategory");
                        int branchNum;
                        if(rejectionList != null){
                            branchNum = rejectionList.length();
                        }else{
                            branchNum = 0;
                            HelperClass.showToast(getActivity(),getContext(),"No branches Available");
                        }
                        mDeclineReasons = new ArrayList<String>();
                        for(int i = 0; i < branchNum; i++){
                            String singleReason = rejectionList.getString(i);
                            mDeclineReasons.add(singleReason);
                        }
                        final ArrayAdapter<String> adapterBranches = new ArrayAdapter<>(getContext(),android.R.layout.simple_spinner_item, mDeclineReasons);
                        adapterBranches.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mSpinnerDeclineReasons.setAdapter(adapterBranches);
                            }
                        });
                        //HelperClass.dismissHUD();
                    }else {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getContext(),errMsg,Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                }catch (Exception ex){
                    ex.printStackTrace();
                }
            }
        });
    }

    private void callDeclineRequest() {
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
            postData.put("approve",false);
            postData.put("reason",mDeclineReasons.get(mSpinnerDeclineReasons.getSelectedItemPosition()));
            postData.put("reasonCategory",mTxtReason.getText().toString());
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


}
