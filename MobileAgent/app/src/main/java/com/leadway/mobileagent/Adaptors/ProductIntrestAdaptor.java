package com.leadway.mobileagent.Adaptors;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.leadway.mobileagent.R;

public class ProductIntrestAdaptor extends RecyclerView.Adapter<ProductIntrestAdaptor.MyViewHolder> {

    Context context;
    private String[] title;
    private String[] acronym;




    //constructor
    public ProductIntrestAdaptor(Context ct,String[] t, String[] ac){
        context=ct;
        title=t;
        acronym=ac;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context)
                .inflate(R.layout.product_intrest_row,parent, false);
//        LayoutInflater layoutInflater= LayoutInflater.from(context);
//        View view=layoutInflater.inflate(R.layout.prospect_row,parent,false);
        return new  MyViewHolder(view) ;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.title_.setText(title[position]);
        holder.acronym_.setText(acronym[position]);

    }

    @Override
    public int getItemCount() {
        return title.length;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView title_,acronym_;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            title_=itemView.findViewById(R.id.product_intrest_title);
            acronym_=itemView.findViewById(R.id.product_intrest_acronym);


        }
    }
}
