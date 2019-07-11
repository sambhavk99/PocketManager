package com.example.pokktmanager;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.widget.Button;
import android.widget.DatePicker;

import java.util.Calendar;


/**
 * A simple {@link Fragment} subclass.
 */



public class DatePickerFragment extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {

    Button date;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        Bundle bundle = getArguments();
        int id = bundle.getInt("Button Id");

        try {
            date = (Button) getActivity().findViewById(id);
        }
        catch (Exception e){
            throw new NullPointerException("must implement date");
        }

        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }


    public void onDateSet(DatePicker view, int year, int month, int day) {
        // Do something with the date chosen by the user
        String m = String.valueOf(month+1) ,d = String.valueOf(day);
        if(month+1<10)
            m = "0"+m;
        if(day<10)
            d = "0"+d;
        //System.out.println(m);
        //System.out.println(d);

        String date1 = new StringBuilder().append(year).append('-').append(m).append('-').append(d).toString();
        date.setText(date1);


    }
}
