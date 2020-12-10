package com.leadway.remoteportalapp.ui.SupervisorClasses;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.leadway.remoteportalapp.Helpers.DataClasses;
import com.leadway.remoteportalapp.R;

import java.util.ArrayList;

public class SupervisorLocationListAdapter extends RecyclerView.Adapter<SupervisorLocationListAdapter.SupervisorLocationListViewHolder> {

    public final ArrayList<DataClasses.WorkUsers> mListOfWorkUsers;
    public LayoutInflater mInfalter;

    public SupervisorLocationListAdapter(SupervisorLocatoinFragment context, ArrayList<DataClasses.WorkUsers> mlistOfWorkUsers){
        this.mListOfWorkUsers = mlistOfWorkUsers;
        this.mInfalter = LayoutInflater.from(context.getActivity());

    }

    public class SupervisorLocationListViewHolder extends RecyclerView.ViewHolder{
        public final TextView lblStaffName;
        public final TextView lblLocation;
        public final TextView lblFromDate;
        public final TextView lblToDate;

        public SupervisorLocationListAdapter mAdapter;

        public SupervisorLocationListViewHolder(View itemView, SupervisorLocationListAdapter adapter){
            super(itemView);
            this.lblStaffName = itemView.findViewById(R.id.lblWorkerName);
            this.lblLocation = itemView.findViewById(R.id.lblLocation);
            this.lblFromDate = itemView.findViewById(R.id.lblFromDate);
            this.lblToDate = itemView.findViewById(R.id.lblToDate);
            this.mAdapter = adapter;
        }

        public void bind(DataClasses.WorkUsers workUsers){
            lblToDate.setText("To: "+ workUsers.dateTo);
            lblFromDate.setText("From: "+ workUsers.dateFrom);
            lblLocation.setText(workUsers.branchName);
            lblStaffName.setText(workUsers.fullname);
        }
    }


    @NonNull
    @Override
    public SupervisorLocationListAdapter.SupervisorLocationListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mInfalter.inflate(R.layout.table_view_cell_supervisor_location_work_users,parent,false);
        return new SupervisorLocationListViewHolder(itemView, this);
    }

    @Override
    public void onBindViewHolder(@NonNull SupervisorLocationListAdapter.SupervisorLocationListViewHolder holder, int position) {
        holder.bind(mListOfWorkUsers.get(position));
    }

    @Override
    public int getItemCount() {
        return mListOfWorkUsers.size();
    }
}
