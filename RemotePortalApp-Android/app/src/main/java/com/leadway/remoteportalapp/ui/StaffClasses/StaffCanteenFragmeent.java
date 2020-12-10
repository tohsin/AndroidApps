package com.leadway.remoteportalapp.ui.StaffClasses;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.fragment.NavHostFragment;
import androidx.print.PrintHelper;

import android.util.Log;
import android.view.KeyEvent;
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
import okhttp3.Response;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link StaffCanteenFragmeent#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StaffCanteenFragmeent extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private ImageView mImgQrcode;
    private Boolean mImgLoaded = false;

    public StaffCanteenFragmeent() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment StaffCanteenFragmeent.
     */
    // TODO: Rename and change types and number of parameters
    public static StaffCanteenFragmeent newInstance(String param1, String param2) {
        StaffCanteenFragmeent fragment = new StaffCanteenFragmeent();
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

        View root = inflater.inflate(R.layout.fragment_staff_canteen_fragmeent, container, false);
        root.setFocusableInTouchMode(true);
        root.requestFocus();
        root.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    return keyCode == KeyEvent.KEYCODE_BACK;
                }
                return false;
            }
        });
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        ImageView btnBAckRequest = getView().findViewById(R.id.btnBackRequestSuccess);
//        btnBAckRequest.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                getActivity().onBackPressed();
//            }
//        });

        mImgQrcode = getView().findViewById(R.id.imgQrcode);

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

        Button btnGoToCanteenHistory = getView().findViewById(R.id.btnViewCanteenHistory);
        btnGoToCanteenHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavDirections action = StaffCanteenFragmeentDirections.actionStaffCanteenFragmeentToCanteenHistoryFragment();
                NavHostFragment.findNavController(getParentFragment()).navigate(action);

            }
        });

        Button btnGoToRequestlist = getView().findViewById(R.id.btnStaffCanteenPrint);
        btnGoToRequestlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doPhotoPrint();
            }
        });

        TextView lblFullname = getView().findViewById(R.id.lblFullname);
        lblFullname.setText(LoginDetails.fullName);

        loadQrCodeImage();
    }

    private void doPhotoPrint() {
        if (mImgLoaded){
            PrintHelper photoPrinter = new PrintHelper(getContext());
            photoPrinter.setScaleMode(PrintHelper.SCALE_MODE_FIT);
            BitmapDrawable drawable = (BitmapDrawable) mImgQrcode.getDrawable();
            Bitmap bitmap = drawable.getBitmap();
            photoPrinter.printBitmap("droids.jpg - test print", bitmap);
        }
    }

    private void loadQrCodeImage() {
        if(!(HelperClass.isConnected(getContext()))){
            Toast.makeText(getContext(),"No Internet Available",Toast.LENGTH_SHORT).show();
            return;
        }
        HelperClass.showIndeterminateHud(getContext());
        String getQRCodeEndpoint = HelperClass.baseUrl + "remoteWork/canteenAccessCodeDisplay?username=" + LoginDetails.username;
        Request request = new Request.Builder().url(getQRCodeEndpoint).build();
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
                            StaffCheckInFragment.loadImage(mImgQrcode,qrCodeUrl);
                            mImgLoaded = true;
                        }
                    });
                    //jsonObj
                }catch (Exception ex){
                    Log.e("Error JSon",ex.toString());
                }
            }
        });

    }
}
