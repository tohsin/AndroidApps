package com.leadway.remoteportalapp.ui.StaffClasses;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.fragment.NavHostFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.leadway.remoteportalapp.HelperClass;
import com.leadway.remoteportalapp.Helpers.DataClasses;
import com.leadway.remoteportalapp.Helpers.LoginDetails;
import com.leadway.remoteportalapp.R;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link StaffRequestRemoteWork#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StaffRequestRemoteWork extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Spinner mSpinnerBranches;
    private Calendar mMyCalendar;
    private EditText mTxtFromDate;
    private EditText mTxtToDAte;
    private EditText mTxtFromTime;
    private EditText mTxtToTime;
    private EditText mTxtReason;
    private List<DataClasses.Branch> mListOfBranches;
    private DataClasses.IndepthSessionDetails mIndepthSessionDetails;

    public StaffRequestRemoteWork() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment StaffRequestRemoteWork.
     */
    // TODO: Rename and change types and number of parameters
    public static StaffRequestRemoteWork newInstance(String param1, String param2) {
        StaffRequestRemoteWork fragment = new StaffRequestRemoteWork();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

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

    private void setupDates(){
        //fromDate
        mMyCalendar = Calendar.getInstance();

        mTxtFromDate = getView().findViewById(R.id.fromDate);
        mTxtToDAte = getView().findViewById(R.id.toDate);

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

        final DatePickerDialog.OnDateSetListener toDate = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                //view.setMinDate(System.currentTimeMillis() - 1000);
                mMyCalendar.set(Calendar.YEAR, year);
                mMyCalendar.set(Calendar.MONTH, monthOfYear);
                mMyCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateToDAte();
            }

        };
        mTxtFromDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                DatePickerDialog fromDateDialog = new DatePickerDialog(getContext(), fromDate, mMyCalendar
                        .get(Calendar.YEAR), mMyCalendar.get(Calendar.MONTH),
                        mMyCalendar.get(Calendar.DAY_OF_MONTH));
                fromDateDialog.getDatePicker().setMinDate(Calendar.getInstance().getTimeInMillis());
                fromDateDialog.show();
            }
        });

        mTxtToDAte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog toDateDialog = new DatePickerDialog(getContext(), toDate, mMyCalendar
                        .get(Calendar.YEAR), mMyCalendar.get(Calendar.MONTH),
                        mMyCalendar.get(Calendar.DAY_OF_MONTH));
                toDateDialog.getDatePicker().setMinDate(mMyCalendar.getTimeInMillis());
                toDateDialog.show();
            }
        });
    }

    private void updateFromDate() {
        String myFormat = "MM-dd-yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        mTxtFromDate.setText(sdf.format(mMyCalendar.getTime()));
    }

    private void updateToDAte() {
        String myFormat = "MM-dd-yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        mTxtToDAte.setText(sdf.format(mMyCalendar.getTime()));
    }

    private void setupTime(){
        mTxtFromTime = getView().findViewById(R.id.fromTime);
        mTxtToTime = getView().findViewById(R.id.toTime);

        mTxtFromTime.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);

                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        mTxtFromTime.setText( String.format("%02d", selectedHour) + ":" + String.format("%02d", selectedMinute));
                    }
                }, hour, minute, true);
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();

            }
        });

        mTxtToTime.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);

                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        mTxtToTime.setText( String.format("%02d", selectedHour) + ":" + String.format("%02d", selectedMinute));
                    }
                }, hour, minute, true);
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();

            }
        });



    }

    private void submitRequest(){

        if(mTxtToDAte.getText().toString().isEmpty() ||
                mTxtFromDate.getText().toString().isEmpty() || mTxtFromTime.getText().toString().isEmpty() ||
                mTxtToTime.getText().toString().isEmpty() || mTxtReason.getText().toString().isEmpty()){
            Toast.makeText(getContext(),"Please Fill All Fields",Toast.LENGTH_SHORT).show();
        }else{

            if(!(HelperClass.isConnected(getContext()))){
                Toast.makeText(getContext(),"No Internet Available",Toast.LENGTH_SHORT).show();
                return;
            }
            HelperClass.showIndeterminateHud(getContext());


            String url;
            JSONObject postdata;
            RequestBody body;
            Request request;

            if (mIndepthSessionDetails != null) {
                url = HelperClass.baseUrl + "remoteWork/editSession";
                postdata = new JSONObject();
                try {
                    postdata.put("branch_id",mListOfBranches.get(mSpinnerBranches.getSelectedItemPosition()).branch_id);
                    postdata.put("fromDate_mm_dd_yyyy",mTxtFromDate.getText().toString());
                    postdata.put("toDate_mm_dd_yyyy",mTxtToDAte.getText().toString());
                    postdata.put("fromTime",mTxtFromTime.getText().toString());
                    postdata.put("toTime",mTxtToTime.getText().toString());
                    postdata.put("session_id",mIndepthSessionDetails.session_id);
                    postdata.put("reason",mTxtReason.getText().toString());
                } catch(JSONException e){
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                Log.d("Json",postdata.toString());
                body = RequestBody.Companion.create(postdata.toString(), HelperClass.MEDIA_TYPE);

                request = new Request.Builder()
                        .url(url)
                        .post(body)
                        .header("Accept", "application/json")
                        .header("Content-Type", "application/json")
                        .build();
            }else {
                url = HelperClass.baseUrl + "remoteWork/requestRemoteWork";
                postdata = new JSONObject();
                try {
                    postdata.put("branch_id",mListOfBranches.get(mSpinnerBranches.getSelectedItemPosition()).branch_id);
                    postdata.put("fromDate_mm_dd_yyyy",mTxtFromDate.getText().toString());
                    postdata.put("toDate_mm_dd_yyyy",mTxtToDAte.getText().toString());
                    postdata.put("fromTime",mTxtFromTime.getText().toString());
                    postdata.put("toTime",mTxtToTime.getText().toString());
                    postdata.put("username",LoginDetails.username);
                    postdata.put("reason",mTxtReason.getText().toString());
                } catch(JSONException e){
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                Log.d("Json",postdata.toString());
                body = RequestBody.Companion.create(postdata.toString(), HelperClass.MEDIA_TYPE);

                request = new Request.Builder()
                        .url(url)
                        .post(body)
                        .header("Accept", "application/json")
                        .header("Content-Type", "application/json")
                        .build();
            }



            HelperClass.myClient().newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    String mMessage = e.getMessage();
                    Log.w("failure Response", mMessage);
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            HelperClass.dismissHUD();
                        }
                    });
                }


                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    try {
                        String mMessage = response.body().string();

                        Log.e("success response", mMessage);

                        final JSONObject jsonLoginDetails = new JSONObject(mMessage);
                        final String errMSg = jsonLoginDetails.getString(HelperClass.errMsg);
                        final String message = jsonLoginDetails.getString("message");

                        if (jsonLoginDetails.getBoolean(HelperClass.success)){

                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    HelperClass.dismissHUD();

                                }
                            });
                            NavDirections actions = StaffRequestRemoteWorkDirections.actionRequestRemoteWorkToRequestSuccessFragment();
                            NavHostFragment.findNavController(getParentFragment()).navigate(actions);
                            Toast.makeText(getContext(),message,Toast.LENGTH_SHORT).show();

                        }else{
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getContext(),errMSg,Toast.LENGTH_LONG).show();

                                }
                            });
                        }

                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                HelperClass.dismissHUD();

                            }
                        });
                    }catch (final Exception ex){
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getContext(),ex.toString(),Toast.LENGTH_SHORT).show();

                            }
                        });
                    }
                    //String mMessage = response.body().string();
                    //Log.e("success response", mMessage);
                }
            });

        }


    }

    //Remove AppBar
//    @Override
//    public void onResume() {
//        super.onResume();
//        ((AppCompatActivity)getActivity()).getSupportActionBar().hide();
//    }
//    @Override
//    public void onStop() {
//        super.onStop();
//        ((AppCompatActivity)getActivity()).getSupportActionBar().show();
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_request_remote_work, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        getActivity().getActionBar().hide();
        Button btnRequestWork = getView().findViewById(R.id.btnRequestWork);
        btnRequestWork.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                NavDirections action = HomeFragmentDirections.actionNavHomeToStaffCanteenFragmeent();
//                NavHostFragment.findNavController(getParentFragment()).navigate(action);
                submitRequest();


            }
        });



        mSpinnerBranches = getView().findViewById(R.id.spinnerBranches);
        mTxtReason = getView().findViewById(R.id.txtReason);
        setupDates();
        setupTime();

        mIndepthSessionDetails = StaffRequestRemoteWorkArgs.fromBundle(getArguments()).getEditRequestDetalis();
        if (mIndepthSessionDetails != null){
            mTxtToTime.setText(mIndepthSessionDetails.endTime);
            mTxtFromTime.setText(mIndepthSessionDetails.startTime);
            btnRequestWork.setText("Edit Request");
            String myFormat = "MM-dd-yyyy"; //In which you need put here
            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.UK);
            Date d;
            try {
                SimpleDateFormat MMMdate = new SimpleDateFormat("dd-MMM-yyyy");
                d = MMMdate.parse(mIndepthSessionDetails.workDate);
                mTxtFromDate.setText(sdf.format(d));
            }catch (Exception ex){
                ex.printStackTrace();
            }
        }
        TextView textDate = getView().findViewById(R.id.textTodayDate);
        textDate.setText(HelperClass.getCurrentTime());

        TextView textTime = getView().findViewById(R.id.textTodayTime);
        textTime.setText(HelperClass.getCurrentDate());


        ImageView btnBAckRequest = getView().findViewById(R.id.btnBackRequestSuccess);
        btnBAckRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
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

    }
}
