package com.leadway.remoteportalapp.ui.SupervisorClasses;

import android.app.DatePickerDialog;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.leadway.remoteportalapp.HelperClass;
import com.leadway.remoteportalapp.Helpers.DataClasses;
import com.leadway.remoteportalapp.R;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import segmented_control.widget.custom.android.com.segmentedcontrol.SegmentedControl;
import segmented_control.widget.custom.android.com.segmentedcontrol.item_row_column.SegmentViewHolder;
import segmented_control.widget.custom.android.com.segmentedcontrol.listeners.OnSegmentClickListener;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SupervisorLocatoinFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SupervisorLocatoinFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private LinkedList<String> mWordlist = new LinkedList<>();
    private EditText mTxtDate;
    private EditText mTxtEmailAddress;
    private TextView mLblInformatoin;
    private Calendar mMyCalendar;
    private EditText mTxtBranchDate;
    private Spinner mSpinnerLocation;
    private List<DataClasses.Branch> mListOfBranches;
    private RecyclerView mLocationSupervisorRecyclerView;


    public SupervisorLocatoinFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SupervisorLocatoinFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SupervisorLocatoinFragment newInstance(String param1, String param2) {
        SupervisorLocatoinFragment fragment = new SupervisorLocatoinFragment();
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

        for (int i = 0; i < 20; i++) {
            mWordlist.addLast("Word " + i);
        }

        View root = inflater.inflate(R.layout.fragment_supervisor_locatoin, container, false);
        mLocationSupervisorRecyclerView = root.findViewById(R.id.recyclerViewLocation);


        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final ConstraintLayout locateStaff = getView().findViewById(R.id.locationStaffContraintLayout);
        final ConstraintLayout viewLocations = getView().findViewById(R.id.viewLocationsContraintLayout);

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

        viewLocations.setVisibility(View.GONE);
        mSpinnerLocation = getView().findViewById(R.id.spinnerLocation);
        mTxtBranchDate = getView().findViewById(R.id.txtBranchDate);


        mTxtEmailAddress = getView().findViewById(R.id.txtEmailAddress);
        mTxtDate = getView().findViewById(R.id.txtDate);
        setupDates();
        mLblInformatoin = getView().findViewById(R.id.lblResult);
        mLblInformatoin.setVisibility(View.INVISIBLE);


        SegmentedControl locationSegmentControl = getView().findViewById(R.id.sgmtCanteenHistory);
        locationSegmentControl.addOnSegmentClickListener(new OnSegmentClickListener() {
            @Override
            public void onSegmentClick(SegmentViewHolder segmentViewHolder) {
                Integer position = segmentViewHolder.getSectionPosition();

                switch (position){
                    case 0:
                        locateStaff.setVisibility(View.VISIBLE);
                        viewLocations.setVisibility(View.GONE);
                        break;
                    case 1:
                        getListOfBranches();
                        viewLocations.setVisibility(View.VISIBLE);
                        locateStaff.setVisibility(View.GONE);
                        break;
                    default:
                        throw new IllegalStateException("Unexpected value: " + position);
                }
            }
        });

        Button btnSearch = getView().findViewById(R.id.btnSearch);
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchByUserAndDate();
            }
        });

        ImageButton btnSearchLoaction = getView().findViewById(R.id.btnSearchLocation);
        btnSearchLoaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getListOfWorkUsers();
            }
        });

    }

    private void getListOfWorkUsers() {

        if (!mTxtBranchDate.getText().toString().isEmpty()){
            String url = HelperClass.baseUrl + "remoteWork/searchByBranchAndDate";
            JSONObject postData = new JSONObject();
            try {
                postData.put("branch_id",mListOfBranches.get(mSpinnerLocation.getSelectedItemPosition()).branch_id);
                postData.put("date_mm_dd_yyyy",mTxtBranchDate.getText().toString());
            }catch (Exception ex){
                ex.printStackTrace();
            }

            RequestBody body = RequestBody.Companion.create(postData.toString(),HelperClass.MEDIA_TYPE);
            Request request = new Request.Builder().url(url).post(body).build();
            if(!(HelperClass.isConnected(getContext()))){
                Toast.makeText(getContext(),"No Internet Available",Toast.LENGTH_SHORT).show();
                return;
            }
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
                    String jsonSTring = response.body().string();
                    Log.d("json",jsonSTring);
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            HelperClass.dismissHUD();
                        }
                    });
                    try {
                        JSONObject jsonDict = new JSONObject(jsonSTring);
                        Boolean success = jsonDict.getBoolean(HelperClass.success);
                        final String errMsg = jsonDict.getString(HelperClass.errMsg);

                        if (success){
                            JSONArray workUsers = jsonDict.getJSONArray("workusers");
                            int count = workUsers.length();
                            final ArrayList<DataClasses.WorkUsers> workUsersArrayList = new ArrayList<>();
                            if (count != 0){
                                for (int i = 0; i < count; i++){
                                    JSONObject workuserDict = workUsers.getJSONObject(i);
                                     String branchName = workuserDict.getString("branchName");
                                     String dateFrom = workuserDict.getString("dateFrom");
                                     String dateTo = workuserDict.getString("dateTo");
                                     String fullname = workuserDict.getString("fullname");
                                     String staffNo = workuserDict.getString("staffNo");
                                     String username = workuserDict.getString("username");

                                     DataClasses.WorkUsers workUsers1 = new DataClasses.WorkUsers(branchName,dateFrom,dateTo,fullname,staffNo,username
                                     );
                                     workUsersArrayList.add(workUsers1);
                                }

                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        SupervisorLocationListAdapter adpater = new SupervisorLocationListAdapter(SupervisorLocatoinFragment.this,
                                                workUsersArrayList);
                                        mLocationSupervisorRecyclerView.setAdapter(adpater);
                                        mLocationSupervisorRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                                    }
                                });
                            }else {
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(getContext(),"No Workers Available",Toast.LENGTH_SHORT).show();
                                    }
                                });

                            }
                        }else{
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getContext(),errMsg,Toast.LENGTH_LONG).show();
                                }
                            });

                        }

                    }catch (Exception ex){
                        Log.e("error parsing",ex.toString());
                    }

                }
            });
        }else{
            Toast.makeText(getContext(),"No Date Selected",Toast.LENGTH_SHORT).show();
        }

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
                                mSpinnerLocation.setAdapter(adapterBranches);
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

    private void setupDates(){
        //fromDate
        mMyCalendar = Calendar.getInstance();

        //mTxtFromDate.setTextColor(ContextCompat.getColor(getContext(),R.color.black));
        final DatePickerDialog.OnDateSetListener fromDate = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                //view.setMinDate(System.currentTimeMillis() - 1000);
                // TODO Auto-generated method stub
                mMyCalendar.set(Calendar.YEAR, year);
                mMyCalendar.set(Calendar.MONTH, monthOfYear);
                mMyCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateFromDate();
            }

        };


        mTxtDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                DatePickerDialog fromDateDialog = new DatePickerDialog(getContext(), fromDate, mMyCalendar
                        .get(Calendar.YEAR), mMyCalendar.get(Calendar.MONTH),
                        mMyCalendar.get(Calendar.DAY_OF_MONTH));
//                fromDateDialog.getDatePicker().setMinDate(Calendar.getInstance().getTimeInMillis());
                fromDateDialog.show();
            }
        });

        final DatePickerDialog.OnDateSetListener BranchDate = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                //view.setMinDate(System.currentTimeMillis() - 1000);
                // TODO Auto-generated method stub
                mMyCalendar.set(Calendar.YEAR, year);
                mMyCalendar.set(Calendar.MONTH, monthOfYear);
                mMyCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateBranchDate();
            }

        };


        mTxtBranchDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                DatePickerDialog fromDateDialog = new DatePickerDialog(getContext(), BranchDate, mMyCalendar
                        .get(Calendar.YEAR), mMyCalendar.get(Calendar.MONTH),
                        mMyCalendar.get(Calendar.DAY_OF_MONTH));
//                fromDateDialog.getDatePicker().setMinDate(Calendar.getInstance().getTimeInMillis());
                fromDateDialog.show();
            }
        });

    }

    private void updateFromDate() {
        String myFormat = "MM-dd-yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        mTxtDate.setText(sdf.format(mMyCalendar.getTime()));
    }

    private void updateBranchDate() {
        String myFormat = "MM-dd-yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        mTxtBranchDate.setText(sdf.format(mMyCalendar.getTime()));
    }

    private void searchByUserAndDate() {
        if(!(mTxtDate.getText().toString().isEmpty() || mTxtEmailAddress.getText().toString().isEmpty())){
            String url = HelperClass.baseUrl + "remoteWork/searchByUserAndDate";
            JSONObject postData = new JSONObject();
            try {
                postData.put("username",mTxtEmailAddress.getText().toString());
                postData.put("date_mm_dd_yyyy",mTxtDate.getText().toString());
            }catch (Exception ex){
                Log.e("error parsing JSon",ex.toString());
            }
            RequestBody body = RequestBody.Companion.create(postData.toString(),HelperClass.MEDIA_TYPE);
            final Request request = new Request.Builder()
                    .url(url)
                    .post(body)
                    .build();
            if(!(HelperClass.isConnected(getContext()))){
                Toast.makeText(getContext(),"No Internet Available",Toast.LENGTH_SHORT).show();
                return;
            }
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
                    Log.e("json",jsonString);

                    try {
                        JSONObject jsonDixt = new JSONObject(jsonString);
                        final String errMsg = jsonDixt.getString(HelperClass.message);
                        Boolean success = jsonDixt.getBoolean(HelperClass.success);
                        if(success){
                            final String message = jsonDixt.getString("message");
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    mLblInformatoin.setVisibility(View.VISIBLE);
                                    mLblInformatoin.setText(message);
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
                        Log.e("error parsing JSon",ex.toString());
                    }
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            HelperClass.dismissHUD();
                        }
                    });
                }
            });
        }else {
            Toast.makeText(getContext(),"Fill All Fields",Toast.LENGTH_SHORT).show();
        }

    }


}
