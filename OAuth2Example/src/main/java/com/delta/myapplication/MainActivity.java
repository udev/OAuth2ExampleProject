package com.delta.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.facebook.*;
import com.facebook.model.*;

/* imports used for app key creation */
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //hack:generate an app key hash because keytool fails at life
//        try {
//            PackageInfo info = getPackageManager().getPackageInfo(
//                    "com.delta.myapplication", PackageManager.GET_SIGNATURES);
//            for (Signature signature : info.signatures) {
//                MessageDigest md = MessageDigest.getInstance("SHA");
//                md.update(signature.toByteArray());
//                Log.e("MY KEY HASH:",
//                        Base64.encodeToString(md.digest(), Base64.DEFAULT));
//            }
//        } catch (PackageManager.NameNotFoundException e) {
//            e.printStackTrace();
//        } catch (NoSuchAlgorithmException e) {
//            e.printStackTrace();
//        }

        // start Facebook Login
        Session.openActiveSession(this, true, new Session.StatusCallback() {
            @Override
            public void call(Session session, SessionState state, Exception exception) {
                Log.i("session.getState()", session.getState().toString());

                if (session.isOpened()) {
                    Request.executeMeRequestAsync(session, new Request.GraphUserCallback() {
                        @Override
                        public void onCompleted(GraphUser user, Response response) {
                            String[] info = new String[]{
                                    "Full name: " + user.getFirstName() + " " + user.getMiddleName() + " " + user.getLastName(),
                                    "Profile: " + user.getLink()
                            };

                            ListAdapter adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.facebook_info, info);
                            ListView list = (ListView)findViewById(R.id.facebook_info_list);
                            list.setAdapter(adapter);
                        }
                    });
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Session.getActiveSession().onActivityResult(this, requestCode, resultCode, data);
    }
}
