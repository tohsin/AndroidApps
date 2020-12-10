package com.leadway.remoteportalapp.ui.RemoteSession;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.navigation.NavDirections;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.leadway.remoteportalapp.HelperClass;
import com.leadway.remoteportalapp.Helpers.DataClasses;
import com.leadway.remoteportalapp.R;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class RemoteSessionListAdapter extends RecyclerView.Adapter<RemoteSessionListAdapter.RemoteSessionListViewHolder> {

    private RemoteSessionsFragment fragment;
    private final ArrayList<DataClasses.BriefSessionDetails> mListOfSessions;
    private LayoutInflater mInflater;



    public RemoteSessionListAdapter(RemoteSessionsFragment context, ArrayList<DataClasses.BriefSessionDetails> ListOfSessions){
        mInflater = LayoutInflater.from(context.getActivity());
        this.mListOfSessions = ListOfSessions;
        this.fragment = context;
    }

    public class RemoteSessionListViewHolder extends  RecyclerView.ViewHolder implements PopupMenu.OnMenuItemClickListener {
        public final TextView lblLocation;
        public final TextView lblDayNumber;
        public final TextView lblTimeRange;
        public final TextView lblWorkDate;
        public final ImageButton btnRemoteSession;
        final RemoteSessionListAdapter mAdapter;
        public int sessionID;

        public RemoteSessionListViewHolder(final View itemView, final RemoteSessionListAdapter adapter){
            super(itemView);
            //final RemoteSessionsFragment context = new RemoteSessionsFragment();
            lblLocation = itemView.findViewById(R.id.lblLocation);
            lblDayNumber = itemView.findViewById(R.id.lblDayNumber);
            lblTimeRange = itemView.findViewById(R.id.lblTimeRange);
            lblWorkDate = itemView.findViewById(R.id.lblWorkDate);
            this.mAdapter = adapter;
            btnRemoteSession = itemView.findViewById(R.id.btnRemoteSession);
            //Menu theMenu = R.menu.remote_sessions_menu;
            btnRemoteSession.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PopupMenu popup = new PopupMenu(itemView.getContext(),v);
                    popup.setOnMenuItemClickListener(RemoteSessionListViewHolder.this);
                    popup.inflate(R.menu.remote_sessions_menu);
                    if(getAdapterPosition() == 0){
                        popup.getMenu().findItem(R.id.cancelAction).setVisible(false);
                        popup.getMenu().findItem(R.id.editDate).setVisible(false);

                    }else {
                        popup.getMenu().findItem(R.id.endSession).setVisible(false);
                        popup.getMenu().findItem(R.id.suspendSession).setVisible(false);
                    }
                    popup.show();
                }
            });
        }

        public void bind(DataClasses.BriefSessionDetails details){
            lblWorkDate.setText(details.workDate);
            lblTimeRange.setText(details.timeRange);
            lblDayNumber.setText(details.dayNumber);
            lblLocation.setText(details.branchName);
            sessionID = details.session_id;
        }


        private void handleCancelSession() {
            String url = HelperClass.baseUrl + "remoteWork/suspendSession";

            JSONObject postData = new JSONObject();
            try {
                postData.put("session_id",sessionID);
                postData.put("remark","Cancel Session");
            }catch (Exception ex){
                ex.printStackTrace();
            }

            if(!(HelperClass.isConnected(fragment.getContext()))){
                Toast.makeText(fragment.getContext(),"No Internet Available",Toast.LENGTH_SHORT).show();
                return;
            }
            HelperClass.showIndeterminateHud(fragment.getContext());
            RequestBody body = RequestBody.Companion.create(postData.toString(),HelperClass.MEDIA_TYPE);
            final Request request = new Request.Builder().url(url).post(body).build();
            HelperClass.myClient().newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    e.printStackTrace();
                    fragment.getActivity().runOnUiThread(new Runnable() {
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
                        JSONObject myJsonDict = new JSONObject(jsonString);
                        Boolean success = myJsonDict.getBoolean(HelperClass.success);
                        final String errMsg = myJsonDict.getString(HelperClass.errMsg);
                        if (success){
                            final String message = myJsonDict.getString("message");
                            fragment.getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(fragment.getContext(),message,Toast.LENGTH_SHORT).show();
                                    NavDirections action = RemoteSessionsFragmentDirections.actionRemoteSessionsToNavHome();
                                    NavHostFragment.findNavController(fragment.getParentFragment()).navigate(action);
                                }
                            });
                        }else {
                            fragment.getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(fragment.getContext(),errMsg,Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                    }catch (Exception ex){
                        Log.e("error parsing json",ex.toString());
                    }
                    fragment.getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            HelperClass.dismissHUD();
                        }
                    });

                }
            });
        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            switch (item.getItemId()){
                case R.id.endSession:
                    StaffEndOrSuspendBottomSheetFragment bottomSheetFragment = new StaffEndOrSuspendBottomSheetFragment();
                    StaffEndOrSuspendBottomSheetFragment.fragmentTitle = "End Remote Session";
                    StaffEndOrSuspendBottomSheetFragment.sessionID = String.valueOf(sessionID);
                    bottomSheetFragment.show(fragment.getChildFragmentManager(), bottomSheetFragment.getTag());
                    break;
                case R.id.suspendSession:
                    StaffEndOrSuspendBottomSheetFragment bottomSheetFragmentSuspend = new StaffEndOrSuspendBottomSheetFragment();
                    StaffEndOrSuspendBottomSheetFragment.fragmentTitle = "Suspend Remote Session";
                    StaffEndOrSuspendBottomSheetFragment.sessionID = String.valueOf(sessionID);
                    bottomSheetFragmentSuspend.show(fragment.getChildFragmentManager(), bottomSheetFragmentSuspend.getTag());

                    break;
                case R.id.cancelAction:
                    handleCancelSession();
                    break;

                case R.id.editDate:
                    if(!(HelperClass.isConnected(fragment.getContext()))){
                        Toast.makeText(fragment.getContext(),"No Internet Available",Toast.LENGTH_SHORT).show();
                        break;
                    }
                    String url = HelperClass.baseUrl + "remoteWork/getSessionDetails?session_id=" + String.valueOf(sessionID);
                    Request request = new Request.Builder().url(url).build();
                    HelperClass.showIndeterminateHud(fragment.getContext());
                    HelperClass.myClient().newCall(request).enqueue(new Callback() {
                        @Override
                        public void onFailure(@NotNull Call call, @NotNull IOException e) {
                            fragment.getActivity().runOnUiThread(new Runnable() {
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
                            Log.d("json response",jsonString);
                            fragment.getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    HelperClass.dismissHUD();
                                }
                            });
                            try{
                                JSONObject jsonDict = new JSONObject(jsonString);
                                Boolean success = jsonDict.getBoolean(HelperClass.success);
                                final String errMsg = jsonDict.getString(HelperClass.errMsg);
                                if (success){
                                    Boolean checkedIn = jsonDict.getBoolean("checkedIn");
                                    String checkedInDate = jsonDict.getString("checkedInDate");
                                    String checkedInTime = jsonDict.getString("checkedInTime");
                                    String createdDate =  jsonDict.getString("createdDate");
                                    String durationFromNow = jsonDict.getString("durationFromNow");
                                    String endTime = jsonDict.getString("endTime");
                                    int session_id = jsonDict.getInt("session_id");
                                    String startTime = jsonDict.getString("startTime");
                                    String workDate = jsonDict.getString("workDate");

                                    DataClasses.IndepthSessionDetails indepthSessionDetails = new DataClasses.IndepthSessionDetails(checkedIn,checkedInDate,
                                            checkedInTime,createdDate,durationFromNow,endTime,session_id,startTime,workDate);
                                    NavDirections editDateAction = RemoteSessionsFragmentDirections.actionRemoteSessionsToRequestRemoteWork().setEditRequestDetalis(indepthSessionDetails);
                                    NavHostFragment.findNavController(fragment.getParentFragment()).navigate(editDateAction);

                                }else {
                                    fragment.getActivity().runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(fragment.getContext(), errMsg,Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            }catch (Exception ex){
                                Log.e("error parsing json",ex.toString());
                            }
                        }
                    });

                    break;
                default:
                    System.out.print("hmm");

            }
            return false;
        }
    }


    @NonNull
    @Override
    public RemoteSessionListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View mItemView = mInflater.inflate(R.layout.table_view_cell_staff_remote_sessions, parent,false);
        return new RemoteSessionListViewHolder(mItemView,this);
    }

    @Override
    public void onBindViewHolder(@NonNull RemoteSessionListViewHolder holder, int position) {
        holder.bind(mListOfSessions.get(position));
    }

    @Override
    public int getItemCount() {
        return mListOfSessions.size();
    }
}
