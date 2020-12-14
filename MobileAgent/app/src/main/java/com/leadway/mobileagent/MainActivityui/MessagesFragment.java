package com.leadway.mobileagent.MainActivityui;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.leadway.mobileagent.Adaptors.MessagesAdaptor;
import com.leadway.mobileagent.Adaptors.ProductIntrestAdaptor;
import com.leadway.mobileagent.Helperclass.MessageItem;
import com.leadway.mobileagent.R;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class MessagesFragment extends Fragment {
    boolean isfromprospec=false;
    private String state="email";
    private ArrayList<MessageItem> messageItemsemail;
    private ArrayList<MessageItem> messageItemsactivities;
    private RecyclerView recyclerView;
    private Button activitybtn;
    private Button emailbtn;
    private MessagesAdaptor Adaptor;

    public MessagesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments()!=null){
            MessagesFragmentArgs args=MessagesFragmentArgs.fromBundle(getArguments());
            isfromprospec=args.getIsfromprospect();
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_messages, container, false);
        messageItemsemail=MessageItem.createmessageList(5,"email");
        messageItemsactivities=MessageItem.createmessageList(6,"activities");
        activitybtn=v.findViewById(R.id.messages_actvity_btn);
        emailbtn=v.findViewById(R.id.messages_email_btn);


        recyclerView=v.findViewById(R.id.messages_detail_recycler);

        Adaptor=new MessagesAdaptor(getActivity(),messageItemsemail);
        recyclerView.setAdapter(Adaptor);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));


        activitybtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(state.equals("email"))
                   state="activity";
                    activitybtn.setBackground(getResources().getDrawable(R.drawable.buttonshape));
                    emailbtn.setBackground(getResources().getDrawable(R.drawable.buttonshapeblack));
                    Adaptor.changeDateSet(messageItemsactivities);
                    recyclerView.setAdapter(Adaptor);
            }
        });

        emailbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (state=="activity"){
                    state="email";
                    activitybtn.setBackground(getResources().getDrawable(R.drawable.buttonshapeblack));
                    emailbtn.setBackground(getResources().getDrawable(R.drawable.buttonshape));
                    Adaptor.changeDateSet(messageItemsemail);
                    recyclerView.setAdapter(Adaptor);
                }

            }
        });

        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);



    }

    void renderactivity(){
        Adaptor=new MessagesAdaptor(getActivity(),messageItemsemail);
        Adaptor.changeDateSet(messageItemsactivities);
        recyclerView.setAdapter(Adaptor);
    }

}
