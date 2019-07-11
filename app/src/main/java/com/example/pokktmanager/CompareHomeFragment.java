package com.example.pokktmanager;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import java.util.Calendar;


/**
 * A simple {@link Fragment} subclass.
 */
public class CompareHomeFragment extends Fragment {

    Button date1_start, date1_end, date2_start, date2_end;
    Button compare;

    OnDateRangeSendListener onDateRangeSendListener;

    public interface OnDateRangeSendListener
    {
        public void OnDateSend(String date1, String date2, String date3, String date4);
    }

    public CompareHomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        View view = inflater.inflate(R.layout.fragment_compare_home, container, false);

        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolBar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);

        ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);



        Calendar cal = Calendar.getInstance();
        int mYear = cal.get(Calendar.YEAR);
        int mMonth = cal.get(Calendar.MONTH);
        int mDay = cal.get(Calendar.DAY_OF_MONTH);

        date1_start = (Button) view.findViewById(R.id.compare_date1_start);
        date1_end = (Button) view.findViewById(R.id.compare_date1_end);
        date2_start = (Button) view.findViewById(R.id.compare_date2_start);
        date2_end = (Button) view.findViewById(R.id.compare_date2_end);

        compare = (Button) view.findViewById(R.id.get_comparison_btn);

        compare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String date1 = date1_start.getText().toString();
                String date2 = date1_end.getText().toString();
                String date3 = date2_start.getText().toString();
                String date4 = date2_end.getText().toString();

                if(date1.compareTo(date2)>0 || date3.compareTo(date4)>0) {
                    Toast.makeText(getActivity().getApplicationContext(), "Start date can't come after end date", Toast.LENGTH_SHORT).show();
                    return;
                }
                onDateRangeSendListener.OnDateSend(date1,date2,date3,date4);
            }
        });

        date1_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(v);
            }
        });

        date1_end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(v);
            }
        });

        date2_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(v);
            }
        });

        date2_end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(v);
            }
        });

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Activity activity = (Activity) context;
        try{
            onDateRangeSendListener = (OnDateRangeSendListener) activity;
        }
        catch (ClassCastException e)
        {
            throw new ClassCastException(activity.toString() + "must implement ondateRanegSend...");
        }


    }

    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        Bundle bundle = new Bundle();
        int id = v.getId();
        bundle.putInt("Button Id",id);
        newFragment.setArguments(bundle);
        newFragment.show(getActivity().getSupportFragmentManager(), "datePicker");
    }
}
