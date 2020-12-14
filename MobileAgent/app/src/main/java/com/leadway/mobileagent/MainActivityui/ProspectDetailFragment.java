package com.leadway.mobileagent.MainActivityui;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.leadway.mobileagent.Adaptors.ProductIntrestAdaptor;
import com.leadway.mobileagent.Adaptors.ProspectAdaptor;
import com.leadway.mobileagent.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProspectDetailFragment extends Fragment {

    boolean isdetailselected=true;
    private String title[]={"Some Good Stuff","Some Really Good Stuff"};
    private String acronym[]={"SGS","SRGF"};
    public ProspectDetailFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_prospect_detail, container, false);



    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final Button detail=view.findViewById(R.id.prospect_details_btn);
        final Button activity=view.findViewById(R.id.prospect_actvity_btn);
        final ConstraintLayout layout1=view.findViewById(R.id.prospect_detail1);
        final LinearLayout layout2=view.findViewById(R.id.prospect_detail2);
        TextView quote_nav=view.findViewById(R.id.nav_to_quotes);
        TextView activity_nav=view.findViewById(R.id.navigate_to_messages);
        RecyclerView recyclerView = view.findViewById(R.id.product_of_intrest_recycler);
        ProductIntrestAdaptor Adaptor=new ProductIntrestAdaptor(getActivity(),title,acronym);
        recyclerView.setAdapter(Adaptor);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity() ));

        quote_nav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_prospectDetailFragment_to_quotationFragment);
            }
        });
        activity_nav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final NavController navController=Navigation.findNavController(view);
                ProspectDetailFragmentDirections.ActionProspectDetailFragmentToMessagesFragment action=ProspectDetailFragmentDirections.actionProspectDetailFragmentToMessagesFragment();
                action.setIsfromprospect(true);
                Navigation.findNavController(v).navigate(R.id.action_prospectDetailFragment_to_messagesFragment);
                navController.navigate(action);
            }
        });


        activity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isdetailselected){
                    isdetailselected=false;
                    activity.setBackground(getResources().getDrawable(R.drawable.buttonshape));
                    detail.setBackground(getResources().getDrawable(R.drawable.buttonshapeblack));
                    if (layout1.getVisibility()==v.VISIBLE) layout1.setVisibility(v.GONE);
                    if (layout2.getVisibility()!=v.VISIBLE) layout2.setVisibility(v.VISIBLE);

                }

            }
        });
        detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isdetailselected){
                    isdetailselected=true;
                    detail.setBackground(getResources().getDrawable(R.drawable.buttonshape));
                    activity.setBackground(getResources().getDrawable(R.drawable.buttonshapeblack));
                    if (layout2.getVisibility()==v.VISIBLE)
                        layout2.setVisibility(v.GONE);
                    if (layout1.getVisibility()!=v.VISIBLE)
                        layout1.setVisibility(v.VISIBLE);
                }

            }
        });
    }
}
