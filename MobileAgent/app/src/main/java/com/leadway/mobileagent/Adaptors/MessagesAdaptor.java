package com.leadway.mobileagent.Adaptors;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListPopupWindow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.leadway.mobileagent.Helperclass.MessageItem;
import com.leadway.mobileagent.R;

import java.util.ArrayList;
import java.util.List;

public class MessagesAdaptor extends RecyclerView.Adapter<MessagesAdaptor.MyViewHolder> {

    Context context;
    private List<MessageItem> mMessageItems;
    String Page;
    public MessagesAdaptor(Context ct, List<MessageItem> item,String tab){
        context=ct;
        mMessageItems=item;
        Page=tab;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context)
                .inflate(R.layout.messages_row,parent, false);
        return new  MyViewHolder(view) ;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        MessageItem messageItem=mMessageItems.get(position);
        holder.nameTextView.setText(messageItem.getName());
        holder.desciptionTextView.setText(messageItem.getDescription());
        holder.dateTextView.setText(messageItem.getDate());
        holder.cardItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Page.equals("Email")){
                    Navigation.findNavController(v).navigate(R.id.action_messagesFragment_to_emailPageFragment);
                }else if(Page.equals("Activities")){
                    Navigation.findNavController(v).navigate(R.id.action_messagesFragment_to_chatFragment);
                }

            }
        });
    }

    @Override
    public int getItemCount() {

        return mMessageItems.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView nameTextView,desciptionTextView,dateTextView;
        private CardView cardItem;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView=itemView.findViewById(R.id.messages_name);
            dateTextView=itemView.findViewById(R.id.messages_date);
            desciptionTextView=itemView.findViewById(R.id.messages_description);
            cardItem=itemView.findViewById(R.id.messages_card);

        }
    }
    public void changeDateSet(ArrayList<MessageItem> aDataList){
        mMessageItems = aDataList;
        notifyDataSetChanged();
    }
}
