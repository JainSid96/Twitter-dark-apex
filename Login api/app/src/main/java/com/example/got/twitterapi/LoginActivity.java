package com.example.got.twitterapi;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class LoginActivity extends AppCompatActivity {
    EditText userText, passText;

    public void loginClick(View view) {

        ParseUser.logInInBackground(userText.getText().toString(), passText.getText().toString(), new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if (user != null) {
                    Toast.makeText(LoginActivity.this, "LOGIN SUCCESSFUL ", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(getApplicationContext() , HomeActivity.class);
                    startActivity(intent);

                } else {
                    Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                }
            }
        });
    }

    public void newuserClick(View view){
        Intent intent = new Intent(getApplicationContext() , SignupActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Parse.initialize(new Parse.Configuration.Builder(getApplicationContext())
                .applicationId("5a967a381894abccdb56c1c6ecc4b385409b1c3f")
                .clientKey("630027cb21a16f374934feedd9195e90a86f6711")
                .server("http://ec2-13-126-125-192.ap-south-1.compute.amazonaws.com:80/parse/")
                .build()
        );

        userText = (EditText) findViewById(R.id.userText);
        passText = (EditText) findViewById(R.id.passText);


        //ParseUser.enableAutomaticUser();
        ParseACL defaultACL = new ParseACL();
        defaultACL.setPublicReadAccess(true);
        defaultACL.setPublicWriteAccess(true);
        ParseACL.setDefaultACL(defaultACL, true);

    }
}
