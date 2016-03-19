package me.anikraj.iou;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Reduce extends AppCompatActivity {
    DatabaseHandler db;
    Firebase fbusers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reduce);



        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Firebase.setAndroidContext(this);
        // TelephonyManager tMgr = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
        // Toast.makeText(this, tMgr.getLine1Number(), Toast.LENGTH_LONG).show();
        db = new DatabaseHandler(getApplicationContext());
        fbusers = new Firebase("https://iouanik.firebaseio.com/android/data/users");
        storemine();
        check();



        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


    public void storemine(){
        SharedPreferences getPrefs = PreferenceManager
                .getDefaultSharedPreferences(getBaseContext());

        fbusers.child(fixnumberlength(getPrefs.getString("ownnum","0000000000"))).setValue(getPrefs.getString("gname", "error"));
        fbusers.child(fixnumberlength(getPrefs.getString("ownnum","0000000000"))).child("email").setValue(getPrefs.getString("gemail", "error"));
        final DatabaseHandler db = new DatabaseHandler(getApplicationContext());
        ListView listview = (ListView)findViewById(R.id.listViewo);
        // new ReadCardsLoc().execute("k");
        // new ReadCards().execute("k");
        List<OObject> contacts = db.getAllContacts();
        //  listview.setAdapter(new CustomAdapter2(getBaseContext(), R.layout.row, contacts));
        ListView listview2 = (ListView)findViewById(R.id.listViewod);
        // new ReadCardsLoc().execute("k");
        // new ReadCards().execute("k");
        List<OObject> contacts2 = db.getAllContacts2();
        //  listview2.setAdapter(new CustomAdapter2(getBaseContext(), R.layout.row, contacts2));


        for (OObject contact : contacts){
            fbusers.child(fixnumberlength(getPrefs.getString("ownnum","0000000000"))).child("owe").child(fixnumberlength(contact.getNumber()+"")).setValue(contact.getAmount());
        }
        for (OObject contact : contacts2){
            fbusers.child(fixnumberlength(getPrefs.getString("ownnum","0000000000"))).child("owed").child(fixnumberlength(contact.getNumber() + "")).setValue(contact.getAmount());
        }

    }

    public String fixnumberlength(String n){
        if(n.length()==10)return "91"+n;
        else return  n;

    }

    public void check(){
        List<OObject> contacts = db.getAllContacts();
        List<OObject> contacts2 = db.getAllContacts2();
        final List<OObject> contacts3=new ArrayList<>();
        List<OObject> contacts4=new ArrayList<>();

        for (final OObject contact : contacts) { Log.d("contact", contact.getWho());
            fbusers.child(fixnumberlength(contact.getNumber() + "")).child("owe").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    for (DataSnapshot x : snapshot.getChildren()) {
                        if ( Double.parseDouble(x.getValue().toString()) < contact.getAmount()) {
                            Log.d("co find", "same");
                            continue;
                        }

                        if ( Double.parseDouble(x.getValue().toString()) == contact.getAmount()) {
                            Log.d("co find", "equal "+Double.parseDouble(x.getValue().toString())+" "+ contact.getAmount());
                            contacts3.add(new OObject(contact.getWho() + " owes " + x.getKey().toString(), Long.parseLong(x.getKey().toString()), "You owe " + contact.getWho() + " " + contact.getAmount(), Double.parseDouble(x.getValue().toString()), 0L, 3));
                        } else {
                            Log.d("co find", "more");
                            contacts3.add(new OObject(contact.getWho() + " owes " + x.getKey().toString(), Long.parseLong(x.getKey().toString()), "You owe " + contact.getWho() + " " + contact.getAmount(), Double.parseDouble(x.getValue().toString()), 0L, 2));
                        }
                    }
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {
                    // System.out.println("The read failed: " + firebaseError.getMessage());
                }
            });
        }
        for (final OObject contact : contacts2) {
            fbusers.child(fixnumberlength(contact.getNumber() + "")).child("owed").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    for (DataSnapshot x : snapshot.getChildren()) {
                        if (Double.parseDouble(x.getValue().toString()) < contact.getAmount()) {
                            Log.d("cod find", "less");
                            continue;
                        }

                        if (Double.parseDouble(x.getValue().toString()) == contact.getAmount()) {
                            Log.d("cod find", "equal");
                            contacts3.add(new OObject(x.getKey().toString() + " owe " + contact.getWho(), Long.parseLong(x.getKey().toString()), contact.getWho() + " owe you " + contact.getAmount(), Double.parseDouble(x.getValue().toString()), 0L, 3));
                        } else { Log.d("cod find", "more");
                            contacts3.add(new OObject(x.getKey().toString() + " owe " + contact.getWho(), Long.parseLong(x.getKey().toString()), contact.getWho() + " owe you " + contact.getAmount(), Double.parseDouble(x.getValue().toString()), 0L, 2));
                        }
                    }
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {
                    // System.out.println("The read failed: " + firebaseError.getMessage());
                }
            });
        }



        ListView listview = (ListView)findViewById(R.id.listViewo);
        listview.setAdapter(new CustomAdapter2(getBaseContext(), R.layout.row, contacts3));
        ListView listview2 = (ListView)findViewById(R.id.listViewod);
        listview2.setAdapter(new CustomAdapter2(getBaseContext(), R.layout.row, contacts4));


    }

}
