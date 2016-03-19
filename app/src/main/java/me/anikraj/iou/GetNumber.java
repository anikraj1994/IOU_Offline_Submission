package me.anikraj.iou;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.view.View;
import android.widget.EditText;

import com.firebase.client.Firebase;

public class GetNumber extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_number);
       // Firebase.setAndroidContext(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().hide();
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences getPrefs = PreferenceManager
                        .getDefaultSharedPreferences(getBaseContext());
                SharedPreferences.Editor e = getPrefs.edit();

                //  Edit preference to make it false because we don't want this to run again
                e.putString("ownnum", ((EditText) findViewById(R.id.number)).getText().toString());
                e.putInt("hasnum", 1);
                e.apply();

                // TelephonyManager tMgr = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
                // Toast.makeText(this, tMgr.getLine1Number(), Toast.LENGTH_LONG).show();
               // db = new DatabaseHandler(getApplicationContext());
             //  Firebase fbusers = new Firebase("https://iouanik.firebaseio.com/android/data/users");


                SharedPreferences getPrefs2 = PreferenceManager
                        .getDefaultSharedPreferences(getBaseContext());
                new Helper().downall(fixnumberlength(getPrefs.getString("ownnum", "0000000000")),GetNumber.this);




                finish();
            }
        });
    }
    public String fixnumberlength(String n){
        if(n.length()==10)return "91"+n;
        else return  n;

    }
}
