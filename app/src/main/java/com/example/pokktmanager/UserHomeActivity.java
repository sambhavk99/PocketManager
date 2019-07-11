package com.example.pokktmanager;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Toast;

import java.util.List;

public class UserHomeActivity extends AppCompatActivity implements SearchView.OnQueryTextListener,SearchView.OnClickListener,SearchView.OnCloseListener {
    private SharedPreferenceConfig preferenceConfig;
    private Button logOut,addAmount;

    private String Email;

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerAdapter adapter;
    List<MoneyAmount> amountList;

    private FragmentManager  fragmentManager;
    private android.support.v7.widget.Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_user);

        toolbar = (Toolbar) findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        recyclerView = findViewById(R.id.recyclerView);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        final Intent intent = getIntent();
        Email = intent.getStringExtra(LoginActivity.USER_EMAIL);
        System.out.println(Email);

        fragmentManager = getSupportFragmentManager();

        /*logOut = (Button)findViewById(R.id.Logout);
        addAmount =(Button) findViewById(R.id.select_add_amount);*/

        preferenceConfig = new SharedPreferenceConfig(getApplicationContext());

        /*BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if(action.equals("update")) {
                    startActivity(new Intent(UserHomeActivity.this,UserHomeActivity.class));
                    finish();
                }
            }
        };

        registerReceiver(broadcastReceiver,new IntentFilter("update"));*/

        /*addAmount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(UserHomeActivity.this,AddTransactions.class);
                intent1.putExtra(LoginActivity.USER_EMAIL,Email);
                startActivity(intent1);
            }
        });


        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserHomeActivity.this,MainActivity.class);
                startActivity(intent);
                preferenceConfig.writeLoginStatus(false);
                preferenceConfig.writeEmail("");
                finish();
            }
        });*/
        String type;
        RadioButton income = findViewById(R.id.Income);
        RadioButton expense = findViewById(R.id.Expense);
        RadioButton all = findViewById(R.id.All);

        income.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getTransacts("type","income");

            }
        });

        expense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getTransacts("type","expense");
            }
        });

        all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getTransacts("type","All");
            }
        });


        getTransacts("type","All");

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_items,menu);

        MenuItem.OnActionExpandListener onActionExpandListener = new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                getTransacts("type","All");
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                getTransacts("type","All");
                return true;
            }
        };

        MenuItem menuItem = menu.findItem(R.id.search_by_tag);

        menuItem.setOnActionExpandListener(onActionExpandListener);

        android.support.v7.widget.SearchView searchView = (android.support.v7.widget.SearchView) menuItem.getActionView();
        searchView.setOnQueryTextListener(this);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()){

            case R.id.select_add_amount:
                Intent intent1 = new Intent(UserHomeActivity.this,AddTransactions.class);
                intent1.putExtra(LoginActivity.USER_EMAIL,Email);
                startActivity(intent1);
                break;

            case R.id.logout_icon:
                Intent intent = new Intent(UserHomeActivity.this,MainActivity.class);
                startActivity(intent);
                preferenceConfig.writeLoginStatus(false);
                preferenceConfig.writeEmail("");
                finish();
                break;

            case R.id.Summary:
                Intent intent2 = new Intent(UserHomeActivity.this,SummaryActivity.class);
                startActivity(intent2);
                break;

            case R.id.Compare:
                Intent intent3 = new Intent(UserHomeActivity.this, CompareActivity.class);
                startActivity(intent3);
                break;

            default:
                Toast.makeText(this, "Icon clicked", Toast.LENGTH_SHORT).show();

        }

        return super.onOptionsItemSelected(item);
    }

    private void getTransacts(final String query, final String info) {
        System.out.println("Here inside getTransacts");
        class getTransactions extends AsyncTask<String, Void, List<MoneyAmount>> {

            @Override
            protected List<MoneyAmount> doInBackground(String... strings) {
                List<MoneyAmount> list;
                try {
                    if(strings[1].equals("type")) {
                        if (strings[2].equals("All"))
                            list = MainActivity.pmDatabase.transactionsDao().getTransactions(strings[0]);
                        else
                            list = MainActivity.pmDatabase.transactionsDao().getTransactions(strings[0], strings[2]);
                    }
                    else{
                        list = MainActivity.pmDatabase.transactionsDao().getTransactionsbyTag(strings[0],strings[2]);
                    }
                    if(!list.isEmpty()) {
                        System.out.println("Inside do background");
                        return list;
                    }
                }
                catch (Exception e){
                    return null;
                }
                return list;

            }

            @Override
            protected void onPostExecute(List<MoneyAmount> moneyAmounts) {
                super.onPostExecute(moneyAmounts);
                RecyclerAdapter adapter;
                if(moneyAmounts!=null && !moneyAmounts.isEmpty()) {
                    adapter = new RecyclerAdapter(moneyAmounts, UserHomeActivity.this);
                    recyclerView.setAdapter(adapter);
                }
                else
                    Toast.makeText(UserHomeActivity.this, "No Transaction Available of this type!", Toast.LENGTH_SHORT).show();
            }
        }

        getTransactions getTransactions = new getTransactions();
        getTransactions.execute(Email,query,info);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        String userInput = query.toLowerCase();
        getTransacts("Tag",userInput);
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        String userInput = newText.toLowerCase();
        getTransacts("Tag",userInput);
        return true;
    }

    @Override
    public boolean onClose() {
        getTransacts("type","All");
        return true;
    }

    @Override
    public void onClick(View v) {
        getTransacts("type","All");
    }
}
