package com.leadway.mobileagent.MainActivityui.CashierUi;


import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.leadway.mobileagent.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class CashierFragment extends Fragment {


    public CashierFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_cashier, container, false);
        CardView payment=v.findViewById(R.id.cashier_make_payment);
        CardView pending=v.findViewById(R.id.cashier_pending);
        CardView cashout=v.findViewById(R.id.cashier_cashout);
        cashout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_cashierFragment_to_cashoutFragment);
            }
        });
        pending.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_cashierFragment_to_pendingpaymentsFragment);
            }
        });
        payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_cashierFragment_to_makepaymentFragment);
            }
        });
        return v;
    }

}
