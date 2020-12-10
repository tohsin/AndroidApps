package com.leadway.remoteportalapp.ui.StaffClasses;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.leadway.remoteportalapp.Helpers.DataClasses;
import com.leadway.remoteportalapp.R;

import java.util.ArrayList;

public class StaffCorrectSurveyFragmentAdapter extends RecyclerView.Adapter<StaffCorrectSurveyFragmentAdapter.SurveyListViewHolder> {

    public ArrayList<String> selectedSurveyAnswers = new ArrayList<>();
    public ArrayList<DataClasses.SurveyQnA> listOfSurveyQnA;
    public LayoutInflater mLayoutInflater;
    public StaffCorrectSurveyFragment fragment;

    public StaffCorrectSurveyFragmentAdapter(ArrayList<DataClasses.SurveyQnA> listOfSurveyQnA, StaffCorrectSurveyFragment context) {
        this.listOfSurveyQnA = listOfSurveyQnA;
        this.mLayoutInflater = LayoutInflater.from(context.getActivity());
        this.fragment = context;
    }

    @NonNull
    @Override
    public StaffCorrectSurveyFragmentAdapter.SurveyListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = mLayoutInflater.inflate(R.layout.table_view_cell_survey, parent,false);
        return new SurveyListViewHolder(itemView,this);
    }

    @Override
    public void onBindViewHolder(@NonNull StaffCorrectSurveyFragmentAdapter.SurveyListViewHolder holder, int position) {
        holder.Bind(listOfSurveyQnA.get(position));

    }

    @Override
    public int getItemCount() {
        return listOfSurveyQnA.size();
    }

    public class SurveyListViewHolder extends RecyclerView.ViewHolder{

        public TextView lblQuestions;
        public Spinner spinnerAnswers;

        public SurveyListViewHolder(@NonNull View itemView,StaffCorrectSurveyFragmentAdapter adapter) {
            super(itemView);
            this.lblQuestions = itemView.findViewById(R.id.lblQuestion);
            this.spinnerAnswers = itemView.findViewById(R.id.spinnerAnswers);
            spinnerAnswers.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    selectedSurveyAnswers.add(getAdapterPosition(),spinnerAnswers.getSelectedItem().toString());
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    Log.d("spinnner_nothing","hh");
                }
            });
        }

        public void Bind(DataClasses.SurveyQnA surveyQnA){
            lblQuestions.setText(surveyQnA.questionName);
            final ArrayAdapter<String> adapterBranches = new ArrayAdapter<>(fragment.getContext(),android.R.layout.simple_spinner_item, surveyQnA.answers);
            adapterBranches.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            fragment.getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    spinnerAnswers.setAdapter(adapterBranches);
                }
            });
        }
    }
}
