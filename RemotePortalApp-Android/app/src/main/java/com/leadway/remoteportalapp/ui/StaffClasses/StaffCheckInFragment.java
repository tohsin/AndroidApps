package com.leadway.remoteportalapp.ui.StaffClasses;

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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.leadway.remoteportalapp.HelperClass;
import com.leadway.remoteportalapp.Helpers.DataClasses;
import com.leadway.remoteportalapp.Helpers.LoginDetails;
import com.leadway.remoteportalapp.R;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;


public class StaffCheckInFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Spinner mApprovedSpinnerBranch;
    private ArrayList<DataClasses.Branch> mListOfBranches;
    private EditText mTxtAddress;
    private ImageView mImgQRCode;

    public StaffCheckInFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static StaffCheckInFragment newInstance(String param1, String param2) {
        StaffCheckInFragment fragment = new StaffCheckInFragment();
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
        return inflater.inflate(R.layout.fragment_staff_check_in, container, false);
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

        mTxtAddress = getView().findViewById(R.id.txtAddress);
        mTxtAddress.setClickable(false);

        Button btnCheckIN = getView().findViewById(R.id.btnCheckIn);
        btnCheckIN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callCheckInApi();
            }
        });

        mApprovedSpinnerBranch = getView().findViewById(R.id.spinnerApproved);
        mImgQRCode = getView().findViewById(R.id.imgQrcode);

        getListOFfApprovedBranches();
        getQrCodeUrl();


        TextView lblFullname = getView().findViewById(R.id.lblFullName);
        lblFullname.setText(LoginDetails.fullName);



    }



    private void getListOFfApprovedBranches() {
        if(!(HelperClass.isConnected(getContext()))){
            Toast.makeText(getContext(),"No Internet Available",Toast.LENGTH_SHORT).show();
            return;
        }
        HelperClass.showIndeterminateHud(getContext());
        String url = HelperClass.baseUrl + "remoteWork/listApprovedWorkLocation?username=" + LoginDetails.username;
        Request request = new Request.Builder().url(url).build();
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
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        HelperClass.dismissHUD();
                    }
                });
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
                                mApprovedSpinnerBranch.setAdapter(adapterBranches);
                                mTxtAddress.setText(mListOfBranches.get(mApprovedSpinnerBranch.getSelectedItemPosition()).address);
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

    private void getQrCodeUrl(){
        //HelperClass.showIndeterminateHud(getContext());
        if(!(HelperClass.isConnected(getContext()))){
            Toast.makeText(getContext(),"No Internet Available",Toast.LENGTH_SHORT).show();
            return;
        }
        String getQRCodeEndpoint = HelperClass.baseUrl + "remoteWork/canteenAccessCodeDisplay?username=" + LoginDetails.username;
        Request request = new Request.Builder().url(getQRCodeEndpoint).build();
        //HelperClass.showIndeterminateHud(getContext());
        HelperClass.myClient().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
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
                Log.d("QRCode response",jsonString);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        HelperClass.dismissHUD();
                    }
                });
                try {
                    JSONObject jsonObj = new JSONObject(jsonString);
                    final String qrCodeUrl = jsonObj.getString("qrCodeUrl");
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //HelperClass.dismissHUD();
                            loadImage(mImgQRCode,qrCodeUrl);
                        }
                    });
                    //jsonObj
                }catch (Exception ex){
                    Log.e("Error JSon",ex.toString());
                }
            }
        });

    }

    public static void loadImage(ImageView view, String imageUrl){
        if(!imageUrl.isEmpty()){
            Picasso.with(view.getContext())
                    .load(imageUrl)
                    .placeholder(R.drawable.ic_qr)
                    .into(view);
        }else
        {
            view.setBackgroundResource(R.drawable.ic_qr);
        }


    }

    private void callCheckInApi(){
        if(!(HelperClass.isConnected(getContext()))){
            Toast.makeText(getContext(),"No Internet Available",Toast.LENGTH_SHORT).show();
            return;
        }
        HelperClass.showIndeterminateHud(getContext());
        String checkInEndpoint = HelperClass.baseUrl + "remoteWork/checkInforWork?accessCode=&username=" + LoginDetails.username;
        Request request =  new Request.Builder().url(checkInEndpoint).build();
        HelperClass.myClient().newCall(request).enqueue(new Callback() {

            private String mErrMsg;

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
                Log.d("checkIn Response",jsonString);
                HelperClass.dismissHUD();
                try {
                    final JSONObject jsonObj = new JSONObject(jsonString);
                    mErrMsg = jsonObj.getString(HelperClass.errMsg);
                    if(jsonObj.getBoolean(HelperClass.success)){
                        NavDirections action = StaffCheckInFragmentDirections.actionStaffCheckInFragmentToRemoteSessions();
                        NavHostFragment.findNavController(getParentFragment()).navigate(action);
                    }else{
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getContext(), mErrMsg,Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }catch (Exception ex){
                    Log.e("Error",ex.toString());
                }
            }
        });
    }

}
