package com.leadway.remoteportalapp.ui.SupervisorClasses;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
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
import okhttp3.Request;
import okhttp3.Response;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SupervisorRequestListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SupervisorRequestListFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private View mRoot;
    private ArrayList<DataClasses.BriefRequestDetail> mListOfBriefRequestDetails;


    public SupervisorRequestListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SupervisorRequestListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SupervisorRequestListFragment newInstance(String param1, String param2) {
        SupervisorRequestListFragment fragment = new SupervisorRequestListFragment();
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
        mRoot = inflater.inflate(R.layout.fragment_supervisor_request_list, container, false);


        setUpRecyclerView();


        mRoot.setFocusableInTouchMode(true);
        mRoot.requestFocus();
        mRoot.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    return keyCode == KeyEvent.KEYCODE_BACK;
                }
                return false;
            }
        });

        return mRoot;
    }

    private void setUpRecyclerView() {
        if(!(HelperClass.isConnected(getContext()))){
            Toast.makeText(getContext(),"No Internet Available",Toast.LENGTH_SHORT).show();
            return;
        }
        HelperClass.showIndeterminateHud(getContext());
        String getRequestList = HelperClass.baseUrl + "remoteWork/listPendingRemoteWorkApproval?username=" + LoginDetails.username;

//        String getRequestList = HelperClass.baseUrl + "remoteWork/listPendingRemoteWorkApproval?username=" + "o-faseyitan";
        Request request = new Request.Builder().url(getRequestList).build();
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
                Log.d("response",jsonString);
                HelperClass.dismissHUD();

                try {
                    JSONObject myJsonDict = new JSONObject(jsonString);
                    final String errMsg = myJsonDict.getString(HelperClass.errMsg);
                    if(myJsonDict.getBoolean(HelperClass.success)){
                        mListOfBriefRequestDetails = new ArrayList<DataClasses.BriefRequestDetail>();
                        JSONArray pendingList = myJsonDict.getJSONArray("pendingApprovals");
                        int pendingListCount = pendingList.length();

                        if (pendingListCount != 0){
                            for(int i = 0; i < pendingListCount; i++){
                                JSONObject pendingApprovalDict = pendingList.getJSONObject(i);
                                String branchName = pendingApprovalDict.getString("branchName");
                                String department = pendingApprovalDict.getString("department");
                                String fullName = pendingApprovalDict.getString("fullName");
                                int request_id = pendingApprovalDict.getInt("request_id");
                                DataClasses.BriefRequestDetail requestDetail = new DataClasses.BriefRequestDetail(branchName,department,fullName,request_id);
                                mListOfBriefRequestDetails.add(requestDetail);
                            }
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    RecyclerView mRecyclerViewSupervisorRequestList = mRoot.findViewById(R.id.recyclerViewSupervisorRequestList);
                                    SupervisorRequestListAdapter mAdapter = new SupervisorRequestListAdapter(SupervisorRequestListFragment.this, mListOfBriefRequestDetails);
                                    mRecyclerViewSupervisorRequestList.setAdapter(mAdapter);
                                    mRecyclerViewSupervisorRequestList.setLayoutManager(new LinearLayoutManager(getContext()));
                                }
                            });

                        }else {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getContext(),"No Requests Pending Approval",Toast.LENGTH_SHORT).show();
                                }
                            });
                        }



                    }else {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getContext(),errMsg.isEmpty() ? "No pending approval":errMsg,Toast.LENGTH_LONG).show();
                            }
                        });
                    }



                }catch (Exception ex){
                    Log.e("jsonError",ex.toString());
                }
            }
        });


    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

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
    }

    public void onBackPressed(){

    }

}
