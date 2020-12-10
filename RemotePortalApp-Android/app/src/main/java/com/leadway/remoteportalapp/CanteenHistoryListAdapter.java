package com.leadway.remoteportalapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.leadway.remoteportalapp.Helpers.DataClasses;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class CanteenHistoryListAdapter extends RecyclerView.Adapter<CanteenHistoryListAdapter.CanteenHistoryListViewHolder> {
    public final ArrayList<DataClasses.CanteenDetails> mListOfCanteenDEtails;
    public LayoutInflater mInflater;

    public CanteenHistoryListAdapter(ArrayList<DataClasses.CanteenDetails> listOfCanteenDEtails, CanteenHistoryFragment context){
        mListOfCanteenDEtails = listOfCanteenDEtails;
        this.mInflater = LayoutInflater.from(context.getActivity());

    }

    public class CanteenHistoryListViewHolder extends RecyclerView.ViewHolder{
        public final TextView lblCanteenName;
        public final TextView lblStatus;
        public final TextView lblTerminal;
        public final TextView lblTime;

        public CanteenHistoryListAdapter mAdapter;

        public CanteenHistoryListViewHolder(View itemView, CanteenHistoryListAdapter adapter){
            super(itemView);
            this.lblCanteenName = itemView.findViewById(R.id.lblCanteenName);
            this.lblStatus = itemView.findViewById(R.id.lblStatus);
            this.lblTerminal = itemView.findViewById(R.id.lblTerminal);
            this.lblTime = itemView.findViewById(R.id.lblTime);

            this.mAdapter = adapter;
        }

        public void bind(DataClasses.CanteenDetails details){
            lblCanteenName.setText(details.vendorName);
            lblStatus.setText(details.response);
            lblTerminal.setText(details.terminal);
            lblTime.setText(details.createdDate);
        }
    }

    @NonNull
    @Override
    public CanteenHistoryListAdapter.CanteenHistoryListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.table_view_cell_canteen_history, parent,false);
        return new CanteenHistoryListViewHolder(itemView,this);
    }

    @Override
    public void onBindViewHolder(@NonNull CanteenHistoryListAdapter.CanteenHistoryListViewHolder holder, int position) {
        holder.bind(mListOfCanteenDEtails.get(position));
    }

    @Override
    public int getItemCount() {
        return mListOfCanteenDEtails.size();
    }
}
