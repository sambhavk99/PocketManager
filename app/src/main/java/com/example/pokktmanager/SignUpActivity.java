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
import android.widget.Toast;

import java.util.List;

public class SignUpActivity extends AppCompatActivity {

    private EditText username, name, email,password1,password2;
    private Button signUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);


        name = (EditText) findViewById(R.id.Name);
        email = (EditText) findViewById(R.id.email);
        password1 = (EditText) findViewById(R.id.password);
        password2 = (EditText) findViewById(R.id.confirmPass);
        signUp = (Button) findViewById(R.id.Sign_up);

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String Name = name.getText().toString();
                final String Email = email.getText().toString();
                final String Password1 = password1.getText().toString();
                final String Password2 = password2.getText().toString();

                User user = new User();

                user.setName(Name);
                user.setEmail(Email);
                user.setPassword(Password1);
                boolean flag=true;
                if(Name.isEmpty())
                {
                    name.setError("Empty field");
                    flag=false;
                }
                if(Email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(Email).matches())                
                {
                    email.setError("Invalid Email");
                    flag=false;
                }
                if(Password1.isEmpty()){
                    password1.setError("Empty field");
                    flag=false;
                }
                if(Password2.isEmpty()){
                    password2.setError("Empty field");
                    flag=false;
                }
                if(Password1.equals(Password2) && flag ) {
                    new SaveUser().execute(user);
                }
                else{
                    Toast.makeText(SignUpActivity.this, "Passwords Do not match", Toast.LENGTH_SHORT).show();
                }
            }

        });

    }

    private class SaveUser extends AsyncTask<User,Void,Boolean>{

        @Override
        protected Boolean doInBackground(final User... param) {
            String email = param[0].getEmail();
            try {
            List<User> users = MainActivity.pmDatabase.userDao().checkUser(email);
            if(users.isEmpty()) {
                MainActivity.pmDatabase.userDao().addUser(param[0]);
                return true;
            }
            else
            {
                return false;
            }
            }
            catch (Exception e){
                MainActivity.pmDatabase.userDao().addUser(param[0]);
                return true;
            }
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {                    // from where this function is called
            super.onPostExecute(aBoolean);
            if(aBoolean){
                Toast.makeText(SignUpActivity.this, "Account Created Successfully", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(SignUpActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
            else
            {
                Toast.makeText(SignUpActivity.this, "E-mail already in use", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
