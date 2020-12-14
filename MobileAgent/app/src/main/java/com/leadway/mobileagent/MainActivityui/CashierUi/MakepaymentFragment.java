package com.leadway.mobileagent.MainActivityui.CashierUi;


import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.leadway.mobileagent.Helperclass.Dialogboxes;
import com.leadway.mobileagent.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class MakepaymentFragment extends Fragment {


    public MakepaymentFragment() {
        // Required empty public constructor
    }
    private void handlespinner(Spinner spinner){
        // Initializing a String Array
        String[] options = new String[]{
                "Select Payment method",
                "Cash",
                "Cheque"
        };

        final List<String> optionList = new ArrayList<>(Arrays.asList(options));

        // Initializing an ArrayAdapter
        final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                getContext(),android.R.layout.simple_spinner_item,optionList){
            @Override
            public boolean isEnabled(int position){
                if(position == 0)
                {
                    // Disable the first item from Spinner
                    // First item will be use for hint

                    return false;
                }
                else
                {
                    return true;
                }
            }
            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if(position == 0){
                    // Set the hint text color gray
                    tv.setTextColor(getResources().getColor(R.color.orange1));
                }
                else {
                    tv.setTextColor(getResources().getColor(R.color.lightgray));
                }
                return view;
            }
        };

        spinner.setAdapter(spinnerArrayAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItemText = (String) parent.getItemAtPosition(position);
                // If user change the default selection
                // First item is disable and it is used for hint
                if(position > 0){
                    // Notify the selected item text
                    //do nothing for now
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_makepayment, container, false);

        Spinner spinner=v.findViewById(R.id.cashier_makepayment_spinner);
        handlespinner(spinner);

        Button cancel=v.findViewById(R.id.cashier_makepayment_cancel);
        Button save=v.findViewById(R.id.cashier_makepayment_save);


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
