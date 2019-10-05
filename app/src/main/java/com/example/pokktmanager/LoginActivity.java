package com.example.pokktmanager;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;

public class LoginActivity extends AppCompatActivity{
    private EditText email, password;
    private Button login;
    private SharedPreferenceConfig preferenceConfig;
    public static final String USER_EMAIL = "com.example.pokktmanager.EMAIL";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        preferenceConfig =new SharedPreferenceConfig(getApplicationContext());

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);


        email = (EditText)findViewById(R.id.login_email);
        password = (EditText)findViewById(R.id.login_password);
        login = (Button) findViewById(R.id.login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Email = email.getText().toString();
                String Pass = password.getText().toString();
                boolean flag=true;
                if(Email.isEmpty())
                {
                    email.setError("Enter email");
                    flag=false;
                }
                else if( !android.util.Patterns.EMAIL_ADDRESS.matcher(Email).matches()){                
                    email.setError("Enter correct mail id");
                    flag=false;
                }
                if(Pass.isEmpty())
                {
                    flag=false;
                    password.setError("Empty field");
                }
                CheckUser checkUser = new CheckUser();
                if(flag)
                checkUser.execute(Email,Pass);
                }

            });
        }


    private class CheckUser extends AsyncTask<String,Void,Boolean>{

        @Override
        protected Boolean doInBackground(String... detail) {
            boolean success =false;
            try{
                List<User> users= MainActivity.pmDatabase.userDao().checkUser(detail[0],detail[1]);
                if(!users.isEmpty()) {
                    success=true;
                }
            }
            catch (Exception e){
                    success=false;
            }

            return success;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            if(aBoolean){
                Toast.makeText(LoginActivity.this, "Login Successful!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(LoginActivity.this, UserHomeActivity.class);
                intent.putExtra(USER_EMAIL,email.getText().toString());
                startActivity(intent);
                preferenceConfig.writeLoginStatus(true);
                preferenceConfig.writeEmail(email.getText().toString());
                Intent intent1 = new Intent("finish");
                sendBroadcast(intent1);
                finish();
            }
            else{
                Toast.makeText(LoginActivity.this, "User Not Found!! Enter Valid Credentials!!", Toast.LENGTH_SHORT).show();
            }

        }
    }
}
