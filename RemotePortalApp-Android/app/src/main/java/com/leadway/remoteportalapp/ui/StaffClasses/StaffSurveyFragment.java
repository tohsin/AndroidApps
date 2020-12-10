package com.leadway.remoteportalapp.ui.StaffClasses;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.leadway.remoteportalapp.HelperClass;
import com.leadway.remoteportalapp.Helpers.DataClasses;
import com.leadway.remoteportalapp.Helpers.LoginDetails;
import com.leadway.remoteportalapp.R;

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

public class StaffSurveyFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    private ArrayList<DataClasses.Branch> mListOfBranches;
    private Spinner mSpinnerBranches;
    private RadioGroup mGroupCleanliness;
    private RadioGroup mGroupPowerSupply;
    private RadioGroup mGroupSupport;
    private EditText mTxtComments;
    private Switch mSwitch_1;
    private Switch mSwitch_2;
    private Switch mSwitch_3;
    private Switch mSwitch_4;
    private Switch mSwitch_5;

    public StaffSurveyFragment() {
        // Required empty public constructor
    }

    public static StaffSurveyFragment newInstance(String param1, String param2) {
        StaffSurveyFragment fragment = new StaffSurveyFragment();
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
        return inflater.inflate(R.layout.fragment_staff_survey, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ImageView btnBAckRequest = getView().findViewById(R.id.btnBackRequestSuccess);
        btnBAckRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

        Button btnSubmitSurvey = getView().findViewById(R.id.btnSubmitSurvey);
        btnSubmitSurvey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitSurvey();

            }
        });
        //final DrawerLayout drawer  = (DrawerLayout) getActivity().findViewById(R.id.drawer_layout);

        ImageButton btnMenu = getView().findViewById(R.id.btnMenu);
        btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HelperClass.handleMenuSlide(getActivity());
            }
        });

        TextView textDate = getView().findViewById(R.id.textTodayDate);
        textDate.setText(HelperClass.getCurrentTime());

        TextView textTime = getView().findViewById(R.id.textTodayTime);
        textTime.setText(HelperClass.getCurrentDate());

        mSpinnerBranches =  getView().findViewById(R.id.spinnerLocation);
        mGroupCleanliness = getView().findViewById(R.id.radio_group_cleanliness);
        mGroupPowerSupply = getView().findViewById(R.id.radio_group_powersupply);
        mGroupSupport = getView().findViewById(R.id.radio_group_support);
        mTxtComments = getView().findViewById(R.id.txtComments);
        mSwitch_1 = getView().findViewById(R.id.switch_1);
        mSwitch_2 = getView().findViewById(R.id.switch_2);
        mSwitch_3 = getView().findViewById(R.id.switch_3);
        mSwitch_4 = getView().findViewById(R.id.switch_4);
        mSwitch_5 = getView().findViewById(R.id.switch_5);

        getListOfBranches();
    }

    private void getListOfBranches() {
        HelperClass.showIndeterminateHud(getActivity());
        OkHttpClient client = new OkHttpClient();
        String getBranchesEndpoint = "remoteWork/listOfficeBranches";
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
                        JSONArray branchListArray = myJsonObj.getJSONArray("branchList");
                        int branchNum;
                        if(branchListArray != null){
                            branchNum = branchListArray.length();
                        }else{
                            branchNum = 0;
                            HelperClass.showToast(getActivity(),getContext(),"No branches Available");
                        }
                        mListOfBranches = new ArrayList<DataClasses.Branch>();
                        for(int i = 0; i < branchNum; i++){
                            JSONObject singleBranchJSonObject = branchListArray.getJSONObject(i);
                            String address = singleBranchJSonObject.getString("address");
                            String branchName = singleBranchJSonObject.getString("branchName");
                            String branch_id = singleBranchJSonObject.getString("branch_id");
                            DataClasses.Branch newBranch = new DataClasses.Branch(address,branchName,branch_id);
                            mListOfBranches.add(newBranch);
                        }
                        final ArrayAdapter<DataClasses.Branch> adapterBranches = new ArrayAdapter<>(getContext(),android.R.layout.simple_spinner_item, mListOfBranches);
                        adapterBranches.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mSpinnerBranches.setAdapter(adapterBranches);
                            }
                        });
                        HelperClass.dismissHUD();
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

    private void submitSurvey(){
        if (! (mGroupCleanliness.getCheckedRadioButtonId() == -1 || mGroupPowerSupply.getCheckedRadioButtonId() == -1 ||
                mGroupSupport.getCheckedRadioButtonId() == -1 || mTxtComments.getText().toString().isEmpty())){
            HelperClass.showIndeterminateHud(getContext());
            String submitSurveyEndpoint = HelperClass.baseUrl + "remoteWork/submitSurveyQuestion";
            RadioButton cleanlinessButton = getView().findViewById(mGroupCleanliness.getCheckedRadioButtonId());
            RadioButton powerSupplyButton = getView().findViewById(mGroupPowerSupply.getCheckedRadioButtonId());
            RadioButton supportButton = getView().findViewById(mGroupSupport.getCheckedRadioButtonId());

            final String[] myAnswers = new String[9];
            myAnswers[0] = cleanlinessButton.getText().toString();
            myAnswers[1] = powerSupplyButton.getText().toString();
            myAnswers[2] = mSwitch_1.isChecked() ? "Yes":"No";
            myAnswers[3] = mSwitch_2.isChecked() ? "Yes":"No";
            myAnswers[4] = mSwitch_3.isChecked() ? "Yes":"No";
            myAnswers[5] = mSwitch_4.isChecked() ? "Yes":"No";
            myAnswers[6] = mSwitch_5.isChecked() ? "Yes":"No";
            myAnswers[7] = supportButton.getText().toString();
            myAnswers[8] = mTxtComments.getText().toString();

//            getActivity().runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    //mSwitch_1.che
//                    Toast.makeText(getContext(),mSwitch_1.isChecked() ? "Yes":"No",Toast.LENGTH_SHORT).show();
//                }
//            });


            JSONArray arrayObj = new JSONArray();
            for(int i = 0; i < myAnswers.length; i++){
                try {
                    JSONObject miniObj = new JSONObject();
                    miniObj.put("question_id",i+1);
                    miniObj.put("answer",myAnswers[i]);
                    arrayObj.put(miniObj);
                }catch (Exception ex){
                    ex.printStackTrace();
                }
            }

            JSONObject postData = new JSONObject();

            try{
                postData.put("username", LoginDetails.username);
                postData.put("answers",arrayObj);
            }catch (Exception ex){
                Log.e("json error",ex.toString());
            }

            RequestBody body = RequestBody.Companion.create(postData.toString(),HelperClass.MEDIA_TYPE);
            Request request = new Request.Builder()
                    .url(submitSurveyEndpoint)
                    .post(body)
                    //.header("Accept", "application/json")
                    .header("Content-Type", "application/json")
                    .build();

            HelperClass.myClient().newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    HelperClass.dismissHUD();
                    e.printStackTrace();
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    HelperClass.dismissHUD();
                    String myJsonString = response.body().string();
                    Log.d("json response",myJsonString);
                    try{
                        final JSONObject myJsonDict = new JSONObject(myJsonString);
                        final String message = myJsonDict.getString(HelperClass.message);
                        final String errMsg = myJsonDict.getString(HelperClass.errMsg);
                        if (myJsonDict.getBoolean(HelperClass.success)){
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    getActivity().onBackPressed();
                                    Toast.makeText(getContext(),message,Toast.LENGTH_SHORT).show();
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
                        ex.printStackTrace();
                    }
                }
            });



        }else {
            Toast.makeText(getContext(),"Please Fill All Fields", Toast.LENGTH_SHORT).show();
        }






    }


}
