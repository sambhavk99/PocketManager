package com.example.pokktmanager;

import android.arch.persistence.room.Room;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    //public static Activity mAct;

    private android.support.v7.widget.Toolbar toolbar;
    public static PMDatabase pmDatabase;
    public static FragmentManager fragmentManager;
    public static BroadcastReceiver broadcastReceiver;
    ///public static LoggedInUser loggedInUser;
    private SharedPreferenceConfig preferenceConfig;
    

    private Button signUp,login,about;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);

        preferenceConfig = new SharedPreferenceConfig(getApplicationContext());
        pmDatabase = Room.databaseBuilder(getApplicationContext(),PMDatabase.class,"pmdb").fallbackToDestructiveMigration().build();
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if(action.equals("finish"))
                    finish();
            }
        };

        registerReceiver(broadcastReceiver,new IntentFilter("finish"));

        if(preferenceConfig.readLoginStatus())
        {
            Intent intent = new Intent(MainActivity.this, UserHomeActivity.class);
            intent.putExtra(LoginActivity.USER_EMAIL,preferenceConfig.getEmail());
            startActivity(intent);
            finish();
        }

        signUp = findViewById(R.id.select_signup);
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,SignUpActivity.class);
                startActivity(intent);
            }
        });
        login = findViewById(R.id.select_login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        about = findViewById(R.id.learn_more);
        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AboutActivity.class);
                startActivity(intent);
            }
        });
    }
}
