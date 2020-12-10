package com.leadway.remoteportalapp;

import android.app.DatePickerDialog;
import android.icu.text.NumberFormat;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.icu.util.Currency;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.JsonReader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.leadway.remoteportalapp.Helpers.DataClasses;
import com.leadway.remoteportalapp.Helpers.LoginDetails;
import com.leadway.remoteportalapp.ui.SupervisorClasses.SupervisorRequestListAdapter;
import com.leadway.remoteportalapp.ui.SupervisorClasses.SupervisorRequestListFragment;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Locale;
import java.util.stream.Collectors;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;
import segmented_control.widget.custom.android.com.segmentedcontrol.SegmentedControl;
import segmented_control.widget.custom.android.com.segmentedcontrol.item_row_column.SegmentViewHolder;
import segmented_control.widget.custom.android.com.segmentedcontrol.listeners.OnSegmentClickListener;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CanteenHistoryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CanteenHistoryFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private LinkedList<String> mWordList = new LinkedList<>();
    private EditText mTxtToHistoryDate;
    private EditText mTxtFromHistoryDate;
    private View mRoot;
    private TextView mLblAmount;
    private ArrayList<DataClasses.CanteenDetails> mDetailsArray;
    private ArrayList<DataClasses.CanteenDetails> originalArray = new ArrayList<>();

    private RecyclerView mCanteenHistoryRecyclerView;
    private CanteenHistoryListAdapter mMAdapter;
    private Calendar mMyCalendar;


    public CanteenHistoryFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CanteenHistoryFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CanteenHistoryFragment newInstance(String param1, String param2) {
        CanteenHistoryFragment fragment = new CanteenHistoryFragment();
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

        for (int i = 0; i < 20; i++) {
            mWordList.addLast("Word " + i);
        }

        mRoot = inflater.inflate(R.layout.fragment_staff_canteen_history, container, false);


        return mRoot;
    }

    private void setupDates(){
        //fromDate
        mMyCalendar = Calendar.getInstance();

//        mTxtFromDate = (EditText) getView().findViewById(R.id.fromDate);
//        mTxtToDAte = (EditText) getView().findViewById(R.id.toDate);

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
        mTxtFromHistoryDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                DatePickerDialog fromDateDialog = new DatePickerDialog(getContext(), fromDate, mMyCalendar
                        .get(Calendar.YEAR), mMyCalendar.get(Calendar.MONTH),
                        mMyCalendar.get(Calendar.DAY_OF_MONTH));
                //fromDateDialog.getDatePicker().setMinDate(Calendar.getInstance().getTimeInMillis());
                fromDateDialog.show();
            }
        });

        mTxtToHistoryDate.setOnClickListener(new View.OnClickListener() {
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

        mTxtFromHistoryDate.setText(sdf.format(mMyCalendar.getTime()));
    }

    private void updateToDAte() {
        String myFormat = "MM-dd-yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        mTxtToHistoryDate.setText(sdf.format(mMyCalendar.getTime()));
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ImageView btnMenuRequestSuccess = getView().findViewById(R.id.btnMenu);
        btnMenuRequestSuccess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HelperClass.handleMenuSlide(getActivity());
            }
        });

        ImageView btnBackRequestSuccess = getView().findViewById(R.id.btnBackRequestSuccess);
        btnBackRequestSuccess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });


        final TextView textDate = getView().findViewById(R.id.textTodayDate);
        textDate.setText(HelperClass.getCurrentTime());

        TextView textTime = getView().findViewById(R.id.textTodayTime);
        textTime.setText(HelperClass.getCurrentDate());

        mLblAmount = getView().findViewById(R.id.lblAmount);
        mLblAmount.setVisibility(View.INVISIBLE);

        mTxtFromHistoryDate = getView().findViewById(R.id.fromHistoryDAte);
        mTxtToHistoryDate = getView().findViewById(R.id.toiHistoryDate);

        setupDates();

        Button btnSearch = getView().findViewById(R.id.btnSearch);
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getCanteenHistoryData();
            }
        });

        mCanteenHistoryRecyclerView = getView().findViewById(R.id.recyclerViewCanteenHistory);


        final SegmentedControl sgmtCanteenHistory = getView().findViewById(R.id.sgmtCanteenHistory);
        sgmtCanteenHistory.addOnSegmentClickListener(new OnSegmentClickListener() {
            @Override
            public void onSegmentClick(SegmentViewHolder segmentViewHolder) {
                int position = segmentViewHolder.getSectionPosition();

                switch (position){
                    case 0:
                        if(originalArray.size()!=0){
                            mDetailsArray.clear();
                            mDetailsArray.addAll(originalArray);
                            mMAdapter.notifyDataSetChanged();
                        }
                        break;

                    case 1:
                        if (originalArray.size() != 0){
                            mDetailsArray.clear();
                            for(DataClasses.CanteenDetails details: originalArray){
                                if (details.response != "VALID"){
                                    mDetailsArray.add(details);
                                }
                            }
                            mMAdapter.notifyDataSetChanged();
                        }
                        break;

                    case 2:
                        if (originalArray.size() != 0){
                            mDetailsArray.clear();
                            for(DataClasses.CanteenDetails details: originalArray){
                                if (details.response == "VALID"){
                                    mDetailsArray.add(details);
                                }
                            }
                            mMAdapter.notifyDataSetChanged();
                        }
                        break;

                    default:
                        throw new IllegalStateException("Unexpected value: " + position);

                }
            }
        });

    }

    private void getCanteenHistoryData() {

        if (!(mTxtFromHistoryDate.getText().toString().isEmpty() || mTxtToHistoryDate.getText().toString().isEmpty())){
            String url = HelperClass.baseUrl + "remoteWork/staffCanteenUsage?username=" + LoginDetails.username +
                    "&startEnd_mm_dd_yyyy=" + mTxtFromHistoryDate.getText().toString() + "&endDate_mm_dd_yyyy="+
                    mTxtToHistoryDate.getText().toString();
            Request request =  new Request.Builder().url(url).build();

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
                    Log.e("failed",e.toString());
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    String jsonString = response.body().string();
                    Log.d("response",jsonString);
                    try {
                        JSONObject myJsonDict = new JSONObject(jsonString);

                        JSONArray scannedList = myJsonDict.getJSONArray("scanned");

                        Boolean success = myJsonDict.getBoolean(HelperClass.success);
                        final String errorMsg = myJsonDict.getString(HelperClass.errMsg);

                        if(!success){
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getContext(),errorMsg,Toast.LENGTH_LONG).show();
                                }
                            });
                            return;
                        }
                        final int TotalAmount = myJsonDict.getInt("totalAmount");

                        int count = scannedList.length();
                        if (count != 0){

                            mDetailsArray = new ArrayList<DataClasses.CanteenDetails>();
                            for(int i = 0; i < count; i++){
                                JSONObject scannedItem = scannedList.getJSONObject(i);
                                int cost = scannedItem.getInt("cost");
                                String createdDate = scannedItem.getString("createdDate");
                                String emailAddress = scannedItem.getString("emailAddress");
                                String fullName = scannedItem.getString("fullName");
                                String canteenresponse = scannedItem.getString("response");
                                int scan_id = scannedItem.getInt("scan_id");
                                String staffNo = scannedItem.getString("staffNo");
                                String terminal = scannedItem.getString("terminal");
                                String vendorName = scannedItem.getString("vendorName");
                                int vendor_id = scannedItem.getInt("vendor_id");

                                DataClasses.CanteenDetails singleDetails = new DataClasses.CanteenDetails(cost,createdDate, emailAddress,fullName,
                                        canteenresponse,scan_id,staffNo,terminal,vendorName,vendor_id);
                                mDetailsArray.add(singleDetails);
                            }
                            originalArray.addAll(mDetailsArray);
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    mMAdapter = new CanteenHistoryListAdapter(mDetailsArray,CanteenHistoryFragment.this);
                                mCanteenHistoryRecyclerView.setAdapter(mMAdapter);
                                mCanteenHistoryRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                                mLblAmount.setVisibility(View.VISIBLE);
                                NumberFormat format = NumberFormat.getCurrencyInstance();
                                format.setMaximumFractionDigits(0);
                                format.setCurrency(Currency.getInstance("NGN"));

                                ;
                                //Currency cur1 = Currency.getInstance("");
                                mLblAmount.setText(String.valueOf(format.format(TotalAmount)));
                                }
                            });

                        }else {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getContext(),"No Canteen History Available",Toast.LENGTH_SHORT).show();
                                }
                            });
                        }


                    }catch (Exception ex){
                        Log.e("error parsing json", ex.toString());
                    }
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            HelperClass.dismissHUD();
                        }
                    });

                }
            });

        }else{
            Toast.makeText(getContext(),"Fill All Fields",Toast.LENGTH_SHORT).show();
        }
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                HelperClass.dismissHUD();
            }
        });

    }
}
