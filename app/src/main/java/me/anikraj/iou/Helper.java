package me.anikraj.iou;

import android.app.Activity;
import android.widget.ListView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.List;

/**
 * Created by anikr on 3/19/2016.
 */
public class Helper {
    DatabaseHandler db;
    Firebase fbusers;

    public void upall(String on,String gn,String gm,Activity x){
        Firebase.setAndroidContext(x);
        // TelephonyManager tMgr = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
        // Toast.makeText(this, tMgr.getLine1Number(), Toast.LENGTH_LONG).show();
        db = new DatabaseHandler(x);
        fbusers = new Firebase("https://iouanik.firebaseio.com/android/data/users");
        fbusers.child(on).setValue(gn);
        fbusers.child(on).child("email").setValue(gm);
        List<OObject> contacts = db.getAllContacts();
        List<OObject> contacts2 = db.getAllContacts2();
        for (OObject contact : contacts){
         //   fbusers.child(on).child("owe").child(fixnumberlength(contact.getNumber()+"")).setValue(contact.getAmount());
            fbusers.child(on).child("owe").child(contact.getNumber()+"").setValue(contact);
        }
        for (OObject contact : contacts2){
            fbusers.child(on).child("owed").child(contact.getNumber()+"").setValue(contact);
        }


    }
    public String fixnumberlength(String n){
        if(n.length()==10)return "91"+n;
        else return  n;

    }
    public void downall(String on,Activity x){
        Firebase.setAndroidContext(x);
        db = new DatabaseHandler(x);
        fbusers = new Firebase("https://iouanik.firebaseio.com/android/data/users");
        fbusers.child(on).child("owe").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    OObject post = postSnapshot.getValue(OObject.class);
                    db.addContact(post);
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                //System.out.println("The read failed: " + firebaseError.getMessage());
            }
        });
        fbusers.child(on).child("owed").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    OObject post = postSnapshot.getValue(OObject.class);
                    db.addContact2(post);
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                //System.out.println("The read failed: " + firebaseError.getMessage());
            }
        });
    }


}
