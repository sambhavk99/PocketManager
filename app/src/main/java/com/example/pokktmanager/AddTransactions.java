package com.example.pokktmanager;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class AddTransactions extends AppCompatActivity {


    private RadioGroup select_type;
    private RadioButton income, expense;
    private EditText amount,tag;
    private Button add_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_transactions);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        select_type = findViewById(R.id.select_type);
        income = findViewById(R.id.income_amount);
        expense = findViewById(R.id.expense_amount);
        amount = (EditText) findViewById(R.id.amount);
        tag = (EditText) findViewById(R.id.tag);
        add_btn = (Button) findViewById(R.id.AddAmount_btn);

        Intent intent = getIntent();
        final String email = intent.getStringExtra(LoginActivity.USER_EMAIL);
        System.out.println(email);

        add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MoneyAmount moneyAmount = new MoneyAmount(email);
                final int Amount = Integer.parseInt(amount.getText().toString());
                final String Tag = tag.getText().toString();
                moneyAmount.setAmount(Amount);
                moneyAmount.setEmail(email);
                moneyAmount.setTag(Tag);
                if (income.isChecked()) {
                    moneyAmount.setType("Income");
                } else {
                    moneyAmount.setType("Expense");
                }
                AddAmount ad = new AddAmount();
                ad.execute(moneyAmount);
            }

        });
    }

        private class AddAmount extends AsyncTask<MoneyAmount, Void, Void> {
            @Override
            protected Void doInBackground(MoneyAmount... moneyAmounts) {
                MainActivity.pmDatabase.transactionsDao().addTransactions(moneyAmounts[0]);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                Toast.makeText(AddTransactions.this, "Added Successfully", Toast.LENGTH_SHORT).show();
                amount.setText("");
                tag.setText("");
               // Intent intent =new Intent("update");
                //sendBroadcast(intent);
            }
    }
}
