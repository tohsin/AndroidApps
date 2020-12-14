package com.leadway.mobileagent.MainActivityui;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.leadway.mobileagent.Adaptors.ProspectAdaptor;
import com.leadway.mobileagent.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProspectFragment extends Fragment {

    private String name[]={"Elon musk"};
    private String descrption[]={"I am the most recent prospect clisk me to know whats up",};
    private String date[]={"9th nov 2019"};
    public ProspectFragment() {
        // Required empty public constructor
    }

    private RecyclerView recyclerView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_prospect, container, false);

        recyclerView = v.findViewById(R.id.recyclerview_prospect);
        ProspectAdaptor prospectAdaptor=new ProspectAdaptor(getActivity(),name,descrption,date);
        recyclerView.setAdapter(prospectAdaptor);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity() ));
        return v;
    }

}
