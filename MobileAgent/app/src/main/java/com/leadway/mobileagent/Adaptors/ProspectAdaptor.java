package com.leadway.mobileagent.Adaptors;

import android.content.Context;
import android.service.autofill.TextValueSanitizer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.leadway.mobileagent.R;

public class ProspectAdaptor extends RecyclerView.Adapter<ProspectAdaptor.MyViewHolder> {

    Context context;
    String[] names;
    String[] dates;
    String[] desciptions;
    public ProspectAdaptor(Context ct,
                           String n[], String desciption[], String date[]){

        context=ct;
        names=n;
        desciptions=desciption;
        dates=date;


    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context)
                .inflate(R.layout.prospect_row,parent, false);
//        LayoutInflater layoutInflater= LayoutInflater.from(context);
//        View view=layoutInflater.inflate(R.layout.prospect_row,parent,false);
        return new  MyViewHolder(view) ;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.date_.setText(dates[position]);
        holder.name_.setText(names[position]);
        holder.desciption_.setText(desciptions[position]);

        holder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_prospectFragment_to_prospectDetailFragment);
            }
        });
    }

    @Override
    public int getItemCount() {

        return names.length;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView productname_,date_,name_,desciption_;
        private ConstraintLayout item;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name_=itemView.findViewById(R.id.prospect_name);
            desciption_=itemView.findViewById(R.id.prospect_description);
            date_=itemView.findViewById(R.id.prospect_date);
            item=itemView.findViewById(R.id.prospect_item);

        }
    }
}
