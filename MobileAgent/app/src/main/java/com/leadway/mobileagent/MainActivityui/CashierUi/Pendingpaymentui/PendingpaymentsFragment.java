package com.leadway.mobileagent.MainActivityui.CashierUi.Pendingpaymentui;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.leadway.mobileagent.Adaptors.PendingPaymentAdaptor;
import com.leadway.mobileagent.Adaptors.ProspectAdaptor;
import com.leadway.mobileagent.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class PendingpaymentsFragment extends Fragment {
    private String productname[]={"Leadway Saving Plan","Leadway Saving Plan"};
    private String quotation_number[]={"43537","43537"};
    private String frequency[]={"monthly","monthly"};
    private String qutoationDate[]={"05-apr-2019","05-apr-2019"};
    private String sum[]={"500","500"};
    private String premium[]={"5000.00","5000.00"};
    private String deathcover[]={"Nil","Nil"};
    private String critical[]={"Nil","Nil"};
    private String disability[]={"Nil","Nil"};

    private RecyclerView recyclerView;
    public PendingpaymentsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_pendingpayments, container, false);
        Button cancel=v.findViewById(R.id.pending_payment_button);
        recyclerView=v.findViewById(R.id.pending_payment_recycler);


        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).popBackStack();
            }
        });

        PendingPaymentAdaptor pendingAdaptor=new PendingPaymentAdaptor(getActivity(),productname,
                quotation_number,frequency,qutoationDate,sum,premium,deathcover,critical,disability);
        recyclerView.setAdapter(pendingAdaptor);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity() ));
        return v;
    }

}
