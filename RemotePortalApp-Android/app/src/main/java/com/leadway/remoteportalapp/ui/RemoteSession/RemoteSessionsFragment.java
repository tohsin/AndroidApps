package com.leadway.remoteportalapp.ui.RemoteSession;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.leadway.remoteportalapp.HelperClass;
import com.leadway.remoteportalapp.Helpers.DataClasses;
import com.leadway.remoteportalapp.Helpers.LoginDetails;
import com.leadway.remoteportalapp.R;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;

public class RemoteSessionsFragment extends Fragment {

    private RecyclerView requestSessionRecyclerView;
    private RemoteSessionListAdapter mAdapter;

    private final LinkedList<String> mWordList = new LinkedList<>();

    private GalleryViewModel galleryViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        galleryViewModel =
                ViewModelProviders.of(this).get(GalleryViewModel.class);
        View root = inflater.inflate(R.layout.fragment_staff_remote_sessions, container, false);

        for (int i = 0; i < 20; i++) {
            mWordList.addLast("Word " + i);
        }

        requestSessionRecyclerView = root.findViewById(R.id.requestSessionRecyclerView);


        return root;
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

        getListOfRemoteSessions();

    }

    private void getListOfRemoteSessions() {

        String getRemoteEndpoint = HelperClass.baseUrl + "remoteWork/listRemoteSessions?username=" + LoginDetails.username;

        //String getRemoteEndpoint = HelperClass.baseUrl + "remoteWork/listRemoteSessions?username=" + "o-faseyitan";
        Request request = new Request.Builder().url(getRemoteEndpoint).build();
        if(!(HelperClass.isConnected(getContext()))){
            Toast.makeText(getContext(),"No Internet Available",Toast.LENGTH_SHORT).show();
            return;
        }
        HelperClass.showIndeterminateHud(getContext());
        HelperClass.myClient().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.e("Failure",e.toString());
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
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        HelperClass.dismissHUD();
                    }
                });
                try {
                    JSONObject myjsonDict = new JSONObject(jsonString);
                    final String errmsg = myjsonDict.getString(HelperClass.errMsg);
                    Boolean success = myjsonDict.getBoolean(HelperClass.success);
                    if(success){
                        JSONArray sessionList = myjsonDict.getJSONArray("sessionList");
                        int count = sessionList.length();
                        if (count != 0 ){
                            final ArrayList<DataClasses.BriefSessionDetails> sessionArray = new ArrayList<>();
                            for(int i = 0; i < count; i++){
                                JSONObject sessionItemDict = sessionList.getJSONObject(i);
                                String branchName = sessionItemDict.getString("branchName");
                                String dayNumber = sessionItemDict.getString("dayNumber");
                                int session_id = sessionItemDict.getInt("session_id");
                                String timeRange = sessionItemDict.getString("timeRange");
                                String workDate = sessionItemDict.getString("workDate");

                                DataClasses.BriefSessionDetails singleSession =  new DataClasses.BriefSessionDetails(branchName,dayNumber,session_id,timeRange,workDate);
                                sessionArray.add(singleSession);
                            }

                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    mAdapter = new RemoteSessionListAdapter(RemoteSessionsFragment.this,sessionArray);
                                    requestSessionRecyclerView.setAdapter(mAdapter);
                                    requestSessionRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                                }
                            });

                        }else {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getContext(),"No Remote Sessions Available",Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }else {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getContext(),errmsg,Toast.LENGTH_LONG).show();
                            }
                        });
                    }

                }catch (Exception ex){
                    Log.e("error parsing json",ex.toString());
                }
            }
        });
    }

//    public static void goToCancelPopMenuAction(){
//        NavDirections action = RemoteSessionsFragmentDirections.actionRemoteSessionsToNavHome();
//        NavHostFragment.findNavController(this.getParentFragment()).navigate(action);
//    }
}
