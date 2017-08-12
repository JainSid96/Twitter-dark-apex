package com.example.got.twitterapi;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    ArrayList<String> users = new ArrayList<>();
    ArrayAdapter arrayAdapter;
    ListView listView;

    public void tweetButton(View view) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Tweet");

        final EditText tweetText = new EditText(this);
        builder.setView(tweetText);

        builder.setPositiveButton("SEND", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                ParseObject tweet = new ParseObject("tweet");
                tweet.put("username" , ParseUser.getCurrentUser().getUsername());
                tweet.put("tweet" , tweetText.getText().toString());
                tweet.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null){
                            Toast.makeText(HomeActivity.this, "Tweet Sent", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(HomeActivity.this, "Tweet Failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        builder.show();
    }

    public void logOut(View view){
        ParseUser.logOut();
        Intent intent = new Intent(getApplicationContext() , LoginActivity.class);
        startActivity(intent);
    }

    public void feedButton(View view){
        Toast.makeText(this, "Loading Tweets", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(getApplicationContext() , FeedActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        if (ParseUser.getCurrentUser().get("isFollowing") == null){
            List<String> emptyList = new ArrayList<>();
            ParseUser.getCurrentUser().put("isFollowing" , emptyList);
        }

        listView = (ListView)findViewById(R.id.listView);
        listView.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);

        arrayAdapter = new ArrayAdapter(this , android.R.layout.simple_list_item_checked , users);
        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                CheckedTextView checkedTextView = (CheckedTextView) view ;
                if (checkedTextView.isChecked()){

                    Toast.makeText(HomeActivity.this, "User Followed", Toast.LENGTH_SHORT).show();
                    ParseUser.getCurrentUser().getList("isFollowing").add(users.get(i));
                    ParseUser.getCurrentUser().saveInBackground();

                }else{

                    Toast.makeText(HomeActivity.this, "User Unfollowed", Toast.LENGTH_SHORT).show();
                    ParseUser.getCurrentUser().getList("isFollowing").remove(users.get(i));
                    ParseUser.getCurrentUser().saveInBackground();

                }
            }
        });

        users.clear();
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereNotEqualTo("username" , ParseUser.getCurrentUser().getUsername());
        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> objects, ParseException e) {
                if (e == null){
                    if (objects.size() > 0){
                        for (ParseUser user : objects){
                            users.add(user.getUsername());
                        }
                        arrayAdapter.notifyDataSetChanged();
                        for (String usernames : users){
                            if (ParseUser.getCurrentUser().getList("isFollowing").contains(usernames)){
                                listView.setItemChecked(users.indexOf(usernames), true);
                            }
                        }
                    }
                }
            }
        });
    }
}
