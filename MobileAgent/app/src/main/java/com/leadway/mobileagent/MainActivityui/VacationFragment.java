package com.leadway.mobileagent.MainActivityui;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.leadway.mobileagent.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class VacationFragment extends Fragment {


    public VacationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_vacation, container, false);
    }

}
