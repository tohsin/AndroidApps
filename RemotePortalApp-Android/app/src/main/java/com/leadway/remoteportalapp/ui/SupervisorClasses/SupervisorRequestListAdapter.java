package com.leadway.remoteportalapp.ui.SupervisorClasses;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.navigation.NavDirections;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.leadway.remoteportalapp.Helpers.DataClasses;
import com.leadway.remoteportalapp.R;

import java.util.ArrayList;

public class SupervisorRequestListAdapter extends RecyclerView.Adapter<SupervisorRequestListAdapter.RequestListViewHolder> {

    public final ArrayList<DataClasses.BriefRequestDetail> mListOfBriefRequestDetail;
    private LayoutInflater mInflater;
    private SupervisorRequestListFragment fragment;

    public SupervisorRequestListAdapter(SupervisorRequestListFragment context, ArrayList<DataClasses.BriefRequestDetail> mListOfBriefRequestDetail){
        this.mListOfBriefRequestDetail = mListOfBriefRequestDetail;
        this.mInflater = LayoutInflater.from(context.getActivity());
        this.fragment = context;
    }

    public class RequestListViewHolder extends RecyclerView.ViewHolder{
        public final TextView lblRequestListName;
        public final TextView lblRequestREquestID;
        public final TextView lblRequestDepartment;
        public final TextView lblRequestBranchName;


        final SupervisorRequestListAdapter mAdapter;
        public final CardView myCardview;


        public RequestListViewHolder(View itemView, SupervisorRequestListAdapter mAdapter) {
            super(itemView);
            this.lblRequestListName = itemView.findViewById(R.id.lblWorkerName);
            this.lblRequestREquestID = itemView.findViewById(R.id.lblRequest_ID);
            this.lblRequestDepartment = itemView.findViewById(R.id.lblDepartment);
            this.lblRequestBranchName = itemView.findViewById(R.id.lblBranch);
            this.mAdapter = mAdapter;
            this.myCardview = itemView.findViewById(R.id.cardViewRequestList);
            myCardview.setClickable(true);
            myCardview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    NavDirections action = SupervisorRequestListFragmentDirections.actionSupervisorRequestListFragmentToSupervisorRequestDetailsFragment(lblRequestREquestID.getText().toString());
                    NavHostFragment.findNavController(fragment.getParentFragment()).navigate(action);
                }
            });
        }

        public void bind(DataClasses.BriefRequestDetail requestDetail){
            lblRequestBranchName.setText(requestDetail.branchName);
            lblRequestDepartment.setText(requestDetail.department);
            lblRequestREquestID.setText( String.format("%03d", requestDetail.request_id));
            lblRequestListName.setText(requestDetail.fullName);

        }

    }

    @NonNull
    @Override
    public SupervisorRequestListAdapter.RequestListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mItemView = mInflater.inflate(R.layout.table_view_cell_supervisor_request_list, parent,false);

        return new RequestListViewHolder(mItemView,this);
    }

    @Override
    public void onBindViewHolder(@NonNull SupervisorRequestListAdapter.RequestListViewHolder holder, int position) {
        holder.bind(mListOfBriefRequestDetail.get(position));
    }

    @Override
    public int getItemCount() {
        return mListOfBriefRequestDetail.size();
    }


}
