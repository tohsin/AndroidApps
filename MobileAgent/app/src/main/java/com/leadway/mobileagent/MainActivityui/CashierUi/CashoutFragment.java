package com.leadway.mobileagent.MainActivityui.CashierUi;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.leadway.mobileagent.Helperclass.Dialogboxes;
import com.leadway.mobileagent.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class CashoutFragment extends Fragment {


    public CashoutFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_cashout, container, false);
        Button cancel=v.findViewById(R.id.cashier_cashout_cancel);
        Button save=v.findViewById(R.id.cashier_cashout_save);


        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).popBackStack();
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialogboxes alert = new Dialogboxes();
                alert.saveMakePaymentDialog(getActivity(),"Transaction succesful");
            }
        });



        return v;
    }

}
