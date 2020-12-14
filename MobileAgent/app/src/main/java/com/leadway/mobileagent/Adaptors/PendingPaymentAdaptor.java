package com.leadway.mobileagent.Adaptors;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.leadway.mobileagent.R;

public class PendingPaymentAdaptor extends RecyclerView.Adapter<PendingPaymentAdaptor.MyViewHolder> {

    Context context;



    private String productname[];
    private String quotation_number[];
    private String frequency[];
    private String qutoationDate[];
    private String sum[];
    private String premium[];
    private String deathcover[];
    private String critical[];
    private String disability[];


    //constructor
    public PendingPaymentAdaptor(Context ct,
        String _quotation_number[], String _productname[],
        String _frequency[],
        String _qutoationDate[],
        String _sum[],
        String _premium[],
        String _deathcover[],
        String _critical[],
        String _disability[]){
        context=ct;
        productname=_productname;
        quotation_number=_quotation_number;
        frequency=_frequency;
        qutoationDate=_qutoationDate;
        sum=_sum;
        premium=_premium;
        deathcover=_deathcover;
        critical=_critical;
        disability=_disability;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context)
                .inflate(R.layout.pendingpayment_row,parent, false);
//        LayoutInflater layoutInflater= LayoutInflater.from(context);
//        View view=layoutInflater.inflate(R.layout.prospect_row,parent,false);
        return new  MyViewHolder(view) ;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.prod.setText(productname[position]);
        holder.quote_num.setText(quotation_number[position]);
        holder.freq.setText(frequency[position]);
        holder.death.setText(deathcover[position]);
        holder.quote_d.setText(qutoationDate[position]);
        holder.s.setText(sum[position]);
        holder.prem.setText(premium[position]);
        holder.crit.setText(critical[position]);
        holder.dis.setText(disability[position]);

    }

    @Override
    public int getItemCount() {

        return productname.length;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView prod,quote_num,freq,quote_d,s,prem,death,crit,dis;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            prod=itemView.findViewById(R.id.product_name);
            quote_num=itemView.findViewById(R.id.quotation_number);
            freq=itemView.findViewById(R.id.frequency);
            quote_d=itemView.findViewById(R.id.quotation_date);
            s=itemView.findViewById(R.id.sum);
            prem=itemView.findViewById(R.id.premium);
            death=itemView.findViewById(R.id.death);
            crit=itemView.findViewById(R.id.critical);
            dis=itemView.findViewById(R.id.disability);

        }
    }
}
