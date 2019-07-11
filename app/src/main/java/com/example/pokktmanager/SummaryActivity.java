package com.example.pokktmanager;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.anychart.APIlib;
import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Cartesian;
import com.anychart.core.cartesian.series.Column;
import com.anychart.data.Set;
import com.anychart.enums.Anchor;
import com.anychart.enums.HoverMode;
import com.anychart.enums.Position;
import com.anychart.enums.TooltipPositionMode;

import java.util.ArrayList;
import java.util.List;

import static java.lang.System.out;


public class SummaryActivity extends AppCompatActivity implements View.OnClickListener {
    private String email;
    private SharedPreferenceConfig sharedPreferenceConfig;
    private RadioButton weekly,monthly,yearly;
    private TextView tot_income,tot_expense,user_name;
    Button delete_user;

    AnyChartView anyChartView;
    Set set;
    AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary);

        sharedPreferenceConfig = new SharedPreferenceConfig(getApplicationContext());
        email = sharedPreferenceConfig.getEmail();

        alertDialog = new AlertDialog.Builder(SummaryActivity.this).create();
        alertDialog.setTitle("Alert");
        alertDialog.setMessage("Are you sure you want to delete your account?");
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        new deleteUser().execute(email);
                        dialog.dismiss();
                        sharedPreferenceConfig.writeEmail("");
                        sharedPreferenceConfig.writeLoginStatus(false);
                        startActivity(new Intent(SummaryActivity.this,MainActivity.class));
                        Toast.makeText(SummaryActivity.this, "Account Deleted Successfully", Toast.LENGTH_SHORT).show();
                    }
                }
        );
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        anyChartView = findViewById(R.id.graph);
        anyChartView.setProgressBar(findViewById(R.id.progressBar));

        weekly = findViewById(R.id.weekly);
        monthly = findViewById(R.id.monthly);
        yearly = findViewById(R.id.daily);
        user_name = (TextView) findViewById(R.id.user_name);
        tot_expense = (TextView) findViewById(R.id.total_expense);
        tot_income = (TextView) findViewById(R.id.total_income);
        delete_user =(Button) findViewById(R.id.del_account_btn);
        weekly.setOnClickListener(this);
        monthly.setOnClickListener(this);
        yearly.setOnClickListener(this);
        delete_user.setOnClickListener(this);

        new getSummary().execute("weekly");
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.weekly:
                out.println("in weekly");
                new getSummary().execute("weekly");
                APIlib.getInstance().setActiveAnyChartView(anyChartView);
                break;
            case R.id.monthly:
                out.println("in monthly");
                new getSummary().execute("monthly");
                APIlib.getInstance().setActiveAnyChartView(anyChartView);
                break;
            case R.id.daily:
                out.println("in daily");
                new getSummary().execute("daily");
                APIlib.getInstance().setActiveAnyChartView(anyChartView);
                break;
            case R.id.del_account_btn:
                alertDialog.show();
                break;
            default:
                set=Set.instantiate();
                new getSummary().execute("weekly");
                break;

        }
    }

    private class getSummary extends AsyncTask<String,Void,Void>{

        long income=0,expense=0;
        User user;
        @Override
        protected Void doInBackground(String... strings) {
            try {
                user = MainActivity.pmDatabase.userDao().checkUser(email).get(0);
                System.out.println("inside try weekly");
                if (strings[0].equals("weekly")) {
                    System.out.println("inside try weekly");
                    expense = MainActivity.pmDatabase.transactionsDao().getTransactionSummaryWeekly(email, "expense");
                    income = MainActivity.pmDatabase.transactionsDao().getTransactionSummaryWeekly(email, "income");
                }
                else if(strings[0].equals("monthly")) {
                    System.out.println("inside try monthly");
                    expense = MainActivity.pmDatabase.transactionsDao().getTransactionSummaryMonthly(email, "expense");
                    income = MainActivity.pmDatabase.transactionsDao().getTransactionSummaryMonthly(email, "income");
                }
                else{
                    System.out.println("inside try monthly");
                    expense = MainActivity.pmDatabase.transactionsDao().getTransactionSummaryDaily(email, "expense");
                    income = MainActivity.pmDatabase.transactionsDao().getTransactionSummaryDaily(email, "income");
                }
            }
            catch (Exception e){
                out.println("error"+e);
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            user_name.setText(user.getName());
            out.println(expense);
            out.println(income);
            String e = "Rs. "+String.valueOf(expense);
            String i = "Rs. "+String.valueOf(income);
            out.println(e);
            out.println(i);
            tot_expense.setText(e);
            tot_income.setText(i);

            Cartesian cartesian = AnyChart.column();

            APIlib.getInstance().setActiveAnyChartView(anyChartView);


            List<DataEntry> data = new ArrayList<>();
            data.add(new ValueDataEntry("Expense",expense));
            data.add(new ValueDataEntry("Income",income));
            Column column = cartesian.column(data);

            //Column column = cartesian.column(series1Data);

            column.tooltip()
                    .titleFormat("{%X}")
                    .position(Position.CENTER_BOTTOM)
                    .anchor(Anchor.CENTER_BOTTOM)
                    .offsetX(0d)
                    .offsetY(5d)
                    .format("INR {%Value}{groupsSeparator:}");

            cartesian.animation(true);
            cartesian.title("Income and expense Summary");

            cartesian.yScale().minimum(0d);

            cartesian.yAxis(0).labels().format("INR {%Value}{groupsSeparator:}");

            cartesian.tooltip().positionMode(TooltipPositionMode.POINT);
            cartesian.interactivity().hoverMode(HoverMode.BY_X);

            anyChartView.setChart(cartesian);

            Toast.makeText(SummaryActivity.this, "Summary Updated!", Toast.LENGTH_SHORT).show();

        }
    }

    private static class deleteUser extends AsyncTask<String,Void,Void>{

        @Override
        protected Void doInBackground(String... strings) {
            User user;
            try{
                user = MainActivity.pmDatabase.userDao().checkUser(strings[0]).get(0);
                MainActivity.pmDatabase.userDao().deleteUser(user);
            }
            catch (Exception e){
                return null;
            }
            return null;
        }
    }
}
