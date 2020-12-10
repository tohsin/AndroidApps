package com.leadway.remoteportalapp.ui.StaffClasses;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link StaffCorrectSurveyFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StaffCorrectSurveyFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private ArrayList<DataClasses.Branch> mListOfBranches;
    private Spinner mSpinnerBranches;
    private View mRoot;
    private RecyclerView mRecyclerViewSurvey;
    private StaffCorrectSurveyFragmentAdapter mAdapter;
    private ArrayList<DataClasses.SurveyQnA> mListOfSurveyQnA;

    public StaffCorrectSurveyFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment StaffCorrectSurveyFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static StaffCorrectSurveyFragment newInstance(String param1, String param2) {
        StaffCorrectSurveyFragment fragment = new StaffCorrectSurveyFragment();
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
        mRoot = inflater.inflate(R.layout.fragment_staff_correct_survey, container, false);
        return mRoot;
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
//        mSpinnerBranches.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                getListOfQuestions();
//            }
//        });

        mSpinnerBranches.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                getListOfQuestions();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                int k = 0;
            }
        });
        getListOfBranches();
        //getListOfQuestions();

        mRecyclerViewSurvey = getView().findViewById(R.id.rcvSurvey);
    }

    private void getListOfQuestions() {
        String url = HelperClass.baseUrl + "remoteWork/listSurveyQuestions?username={username}&branch_id=" + (mListOfBranches.get(mSpinnerBranches.getSelectedItemPosition()).branch_id == null ? 1 : mListOfBranches.get(mSpinnerBranches.getSelectedItemPosition()).branch_id);
        Request request = new Request.Builder().url(url).build();
        if(!(HelperClass.isConnected(getContext()))){
            Toast.makeText(getContext(),"No Internet Available",Toast.LENGTH_SHORT).show();
            return;
        }
        HelperClass.showIndeterminateHud(getContext());
        HelperClass.myClient().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        HelperClass.dismissHUD();
                    }
                });
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String jsonString = response.body().string();
                Log.d("json",jsonString);
                try {
                    JSONObject jsonObject = new JSONObject(jsonString);
                    Boolean success = jsonObject.getBoolean(HelperClass.success);
                    final String errMsg = jsonObject.getString(HelperClass.errMsg);
                    if (success){
                        JSONArray surveyQuest = jsonObject.getJSONArray("surveyQuest");
                        mListOfSurveyQnA = new ArrayList<>();
                        int count = surveyQuest.length();
                        if (count != 0){
                            for (int i = 0; i < count; i++){
                                JSONObject surveyQuestDict = surveyQuest.getJSONObject(i);
                                int question_id = surveyQuestDict.getInt("question_id");
                                String question = surveyQuestDict.getString("question");
                                JSONArray answers = surveyQuestDict.getJSONArray("answers");
                                ArrayList<String> answersList = new ArrayList<>();
                                int answersCount = answers.length();
                                if(answersCount != 0){
                                    for(int j = 0; j < answersCount; j++){
                                        String singleAnswer = answers.getString(j);
                                        answersList.add(singleAnswer);
                                    }
                                }
                                DataClasses.SurveyQnA singleSurveyQnA = new DataClasses.SurveyQnA(question,question_id,answersList);
                                mListOfSurveyQnA.add(singleSurveyQnA);

                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        mAdapter = new StaffCorrectSurveyFragmentAdapter(mListOfSurveyQnA,StaffCorrectSurveyFragment.this);
                                        mRecyclerViewSurvey.setAdapter(mAdapter);
                                        mRecyclerViewSurvey.setLayoutManager(new LinearLayoutManager(getContext()));
                                    }
                                });
                            }
                        }else {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getContext(),"No Questions Available",Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }else {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getContext(),errMsg,Toast.LENGTH_LONG).show();
                            }
                        });
                    }

                }catch (Exception ex){
                    Log.d("error parsing json",ex.toString());
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

    private void submitSurvey() {
        if(!(HelperClass.isConnected(getContext()))){
            Toast.makeText(getContext(),"No Internet Available",Toast.LENGTH_SHORT).show();
            return;
        }
        if(mListOfSurveyQnA.size() != mAdapter.selectedSurveyAnswers.size()){
            Toast.makeText(getContext(),"Select All Answers",Toast.LENGTH_SHORT).show();
            return;
        }

        String url = HelperClass.baseUrl + "remoteWork/submitSurveyQuestion";

        JSONObject postData = new JSONObject();
        int k = mListOfBranches.size();

        JSONArray arrayObj = new JSONArray();
        for(int i = 0; i < k; i++){
            try {
                JSONObject miniObj = new JSONObject();
                miniObj.put("question_id",mListOfSurveyQnA.get(i).questionID);
                miniObj.put("answer",mAdapter.selectedSurveyAnswers.get(i));
                arrayObj.put(miniObj);
            }catch (Exception ex){
                ex.printStackTrace();
            }
        }

        try {
            postData.put("username", LoginDetails.username);
            postData.put("answers",arrayObj);
        }catch (Exception ex){
            Log.d("error parsing json",ex.toString());
        }

        Log.d("error parsing json",postData.toString());

        RequestBody body = RequestBody.Companion.create(postData.toString(),HelperClass.MEDIA_TYPE);
        Request request = new Request.Builder().url(url).post(body).build();

        HelperClass.showIndeterminateHud(getContext());
        HelperClass.myClient().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                HelperClass.dismissHUD();
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String jsonString = response.body().string();
                Log.d("json",jsonString);
                HelperClass.dismissHUD();
                try {
                    JSONObject jsonDict = new JSONObject(jsonString);
                    Boolean success = jsonDict.getBoolean(HelperClass.success);
                    final String errmsg = jsonDict.getString(HelperClass.errMsg);
                    final String message = jsonDict.getString("message");
                    if (success){
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getContext(),message,Toast.LENGTH_SHORT).show();
                                getActivity().onBackPressed();
                            }
                        });
                    }else {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getContext(),errmsg,Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }catch (Exception ex){
                    Log.d("error parsing json",ex.toString());
                }
            }
        });
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
}
