package com.example.pokktmanager;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.anychart.APIlib;
import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Cartesian;
import com.anychart.core.cartesian.series.Column;
import com.anychart.enums.Anchor;
import com.anychart.enums.HoverMode;
import com.anychart.enums.Position;
import com.anychart.enums.TooltipPositionMode;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class CompareResultFragment extends Fragment {

    private TextView date1_start, date1_end, date2_start, date2_end, dr1_income, dr2_income, dr1_expense, dr2_expense;

    private String email;

    AnyChartView graph1,graph2;

    private String[] dates;

    private SharedPreferenceConfig sharedPreferenceConfig;

    public CompareResultFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_compare_result, container, false);

        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolBar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);

        ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        graph1 = view.findViewById(R.id.graph1);
        graph2 = view.findViewById(R.id.any_chart_view2);
        graph1.setProgressBar(view.findViewById(R.id.progressBar1));
        graph2.setProgressBar(view.findViewById(R.id.progressBar2));

        /*date1_end = (TextView) view.findViewById(R.id.end_date1);
        date2_end = (TextView) view.findViewById(R.id.end_date2);
        date1_start = (TextView) view.findViewById(R.id.start_date1);
        date2_start = (TextView) view.findViewById(R.id.start_date2);*/

        /*dr1_income = (TextView) view.findViewById(R.id.total_income_dr1);
        dr1_expense = (TextView) view.findViewById(R.id.total_expense_dr1);
        dr2_income = (TextView) view.findViewById(R.id.total_income_dr2);
        dr2_expense = (TextView) view.findViewById(R.id.total_expense_dr2);*/

        sharedPreferenceConfig =new SharedPreferenceConfig(getActivity().getApplicationContext());
        email = sharedPreferenceConfig.getEmail();

        Bundle bundle = getArguments();
        try {
            dates = bundle.getStringArray("DateRanges");
        }
        catch (NullPointerException e){
            throw new NullPointerException("exception occured");
        }

        System.out.println(email);
        System.out.println(dates[0]+dates[1]+dates[2]+dates[3]);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getTotalInRange total_in_range = new getTotalInRange();
        total_in_range.execute(email,dates[0],dates[1],dates[2],dates[3]);
    }


    private class getTotalInRange extends AsyncTask<String,Void,Void>{
        int income_dr1=0,expense_dr1=0,expense_dr2=0,income_dr2=0;
        @Override
        protected Void doInBackground(String... strings) {
            income_dr1 = MainActivity.pmDatabase.transactionsDao().getTransactionsInDateRange(strings[0],"income",strings[1],strings[2]);
            expense_dr1 =  MainActivity.pmDatabase.transactionsDao().getTransactionsInDateRange(strings[0],"expense",strings[1],strings[2]);
            income_dr2 =  MainActivity.pmDatabase.transactionsDao().getTransactionsInDateRange(strings[0],"income",strings[3],strings[4]);
            expense_dr2 =  MainActivity.pmDatabase.transactionsDao().getTransactionsInDateRange(strings[0],"expense",strings[3],strings[4]);

            return null;

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            /*date1_start.setText(dates[0]);
            date1_end.setText(dates[1]);
            date2_start.setText(dates[2]);
            date2_end.setText(dates[3]);
            */

            /*dr1_income.setText(String.valueOf(income_dr1));
            dr1_expense.setText(String.valueOf(expense_dr1));
            dr2_income.setText(String.valueOf(income_dr2));
            dr2_expense.setText(String.valueOf(expense_dr2));*/
            APIlib.getInstance().setActiveAnyChartView(graph1);
            List<DataEntry> data = new ArrayList<>();
            data.add(new ValueDataEntry("Expense from " + dates[0] + " to " + dates[1], expense_dr1));
            data.add(new ValueDataEntry("Expense from " + dates[2] + " to " + dates[3], expense_dr2));

            Cartesian cartesian2 = AnyChart.column();
            Column column2 = cartesian2.column(data);

            column2.tooltip()
                    .titleFormat("{%X}")
                    .position(Position.CENTER_BOTTOM)
                    .anchor(Anchor.CENTER_BOTTOM)
                    .offsetX(0d)
                    .offsetY(5d)
                    .format("INR {%Value}{groupsSeparator:}");

            cartesian2.animation(true);
            cartesian2.title("Expense Comparison");

            cartesian2.yScale().minimum(0d);

            cartesian2.yAxis(0).labels().format("INR {%Value}{groupsSeparator:}");

            cartesian2.tooltip().positionMode(TooltipPositionMode.POINT);
            cartesian2.interactivity().hoverMode(HoverMode.BY_X);

            graph1.setChart(cartesian2);


            APIlib.getInstance().setActiveAnyChartView(graph2);

            Cartesian cartesian1 = AnyChart.column();

            List<DataEntry> data1 = new ArrayList<>();
            data1.add(new ValueDataEntry("Income from " + dates[0] + " to " + dates[1], income_dr1));
            data1.add(new ValueDataEntry("Income from " + dates[2] + " to " + dates[3], income_dr2));
            Column column1 =cartesian1.column(data1);

            column1.tooltip()
                    .titleFormat("{%X}")
                    .position(Position.CENTER_BOTTOM)
                    .anchor(Anchor.CENTER_BOTTOM)
                    .offsetX(0d)
                    .offsetY(5d)
                    .format("INR {%Value}{groupsSeparator:}");

            cartesian1.animation(true);
            cartesian1.title("Income Comparison");

            cartesian1.yScale().minimum(0d);

            cartesian1.yAxis(0).labels().format("INR {%Value}{groupsSeparator:}");

            cartesian1.tooltip().positionMode(TooltipPositionMode.POINT);
            cartesian1.interactivity().hoverMode(HoverMode.BY_X);

            graph2.setChart(cartesian1);


            Toast.makeText(getActivity().getApplicationContext(), "Comparison results obtained", Toast.LENGTH_SHORT).show();
        }
    }

}
