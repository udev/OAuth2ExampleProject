package com.delta.myapplication;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.app.Activity;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.facebook.LoggingBehavior;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Settings;
import com.facebook.model.GraphUser;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // set logging behavior
        Settings.addLoggingBehavior(LoggingBehavior.INCLUDE_ACCESS_TOKENS);
        Settings.addLoggingBehavior(LoggingBehavior.REQUESTS);

        // set up the Request and Request.Callback
        Request request = Request.newGraphPathRequest(null, "/4", new Request.Callback() {
            @Override
            public void onCompleted(Response response) {
                if(response.getError() != null) {
                    // there was an error. print the error message
                    Log.i("MainActivity", String.format("Error making request: %s", response.getError()));
                } else {
                    // the request succeeded. print out the information
                    GraphUser user = response.getGraphObjectAs(GraphUser.class);
                    Log.i("MainActivity", String.format("Name: %s", user.getName()));

                    String[] info = new String[]{
                            "Full name: " + user.getFirstName() + " " + user.getLastName(),
                            "Profile: " + user.getLink()
                    };

                    ListAdapter adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.facebook_info, info);
                    ListView list = (ListView)findViewById(R.id.facebook_info_list);
                    list.setAdapter(adapter);
                }
            }
        });
        request.executeAsync(); // execute the request
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
}
