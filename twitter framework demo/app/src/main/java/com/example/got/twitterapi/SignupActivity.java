package com.example.got.twitterapi;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class SignupActivity extends AppCompatActivity {
    EditText newuserText, newpassText , nameText;
    ParseUser user;

    public void signupClick(View view){
        user.setUsername(newuserText.getText().toString());
        user.setPassword(newpassText.getText().toString());

        user.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Toast.makeText(SignupActivity.this, "SIGN UP SUCESSFULL ", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(getApplicationContext() , HomeActivity.class);
                    startActivity(intent);

                } else {
                    Toast.makeText(SignupActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        newuserText = (EditText) findViewById(R.id.userText);
        newpassText = (EditText) findViewById(R.id.passText);
        nameText = (EditText) findViewById(R.id.nameText);

        user = new ParseUser();


    }
}
