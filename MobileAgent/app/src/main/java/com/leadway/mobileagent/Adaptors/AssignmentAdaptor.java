package com.leadway.mobileagent.Adaptors;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.leadway.mobileagent.R;

public class AssignmentAdaptor extends RecyclerView.Adapter<AssignmentAdaptor.MyViewHolder> {

    Context context;
    String[] names;
    String[] desciptions;
    public AssignmentAdaptor(Context ct, String n[], String desciption[]){
        context=ct;
        names=n;
        desciptions=desciption;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context)
                .inflate(R.layout.assignment_row,parent, false);
//        LayoutInflater layoutInflater= LayoutInflater.from(context);
//        View view=layoutInflater.inflate(R.layout.prospect_row,parent,false);
        return new  MyViewHolder(view) ;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.name_.setText(names[position]);
        holder.desciption_.setText(desciptions[position]);
    }

    @Override
    public int getItemCount() {

        return names.length;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView name_,desciption_;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name_=itemView.findViewById(R.id.assignment_name);
            desciption_=itemView.findViewById(R.id.assignment_description);

        }
    }
}
