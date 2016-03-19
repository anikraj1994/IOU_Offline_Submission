package me.anikraj.iou;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.actions.NoteIntents;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Add extends AppCompatActivity {
    private static final int CONTACT_PICKER_RESULT = 1001;
    boolean owed=true;
    Add thisifier;
    DialogInterface.OnClickListener dialogClickListener;
    String name=null,number=null;
    EditText et;
    Button o,ob,pb;
    DatabaseHandler db;
    TextView prev;

    private GoogleApiClient mClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        mClient = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
        getSupportActionBar().hide();
        o=(Button)findViewById(R.id.button2);
        ob=(Button)findViewById(R.id.button3);
        pb=(Button)findViewById(R.id.button);
        et=(EditText)findViewById(R.id.editText);
        thisifier=this;
       db = new DatabaseHandler(getApplicationContext());
        prev=(TextView)findViewById(R.id.prev);

        // Get the intent
        Intent intent = getIntent();
        if (NoteIntents.ACTION_CREATE_NOTE.equals(intent.getAction())) {

            Toast.makeText(this, "From google now", Toast.LENGTH_SHORT).show();


            Bundle bundle = intent.getExtras();
           for (String key : bundle.keySet()) {
                Object value = bundle.get(key);
                Log.d("Anik2", String.format("%s %s %s (%s)", key, value.getClass().getCanonicalName(),
                        value.toString(), value.getClass().getName()));
            }


            Log.d("anik", intent.getStringExtra(NoteIntents.EXTRA_NAME) + "    " + intent.getStringExtra(NoteIntents.EXTRA_TEXT));

               String text= bundle.get("android.intent.extra.TEXT").toString();
                String st[]= text.split("\\s+");

                if(st[0].compareTo("i")==0||st[0].compareTo("I")==0){
                    o.setBackground(getResources().getDrawable(R.drawable.addbuttonafter,this.getTheme()));
                    ob.setBackground(getResources().getDrawable(R.drawable.addbuttonbefore, this.getTheme()));
                    owed=false;

                    et.setText(st[3]);

                  //  Uri lookupUri = Uri.withAppendedPath(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                 //           Uri.encode("hari"));

                  //  i.setType(ContactsContract.CommonDataKinds.Phone.SEARCH_DISPLAY_NAME_KEY);
                  //  lookupUri.para


                    String[] fetch_contact=getPhoneNumber(st[2],this);
                    Toast.makeText(this,fetch_contact[0]+ " "+ fetch_contact[1],Toast.LENGTH_LONG).show();
                    if(fetch_contact[0].compareTo("Unsaved")==0){
                        Toast.makeText(this,st[2]+" not found. Select contact!",Toast.LENGTH_LONG).show();
                        Intent i = new Intent(Intent.ACTION_PICK, ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
                        startActivityForResult(i, CONTACT_PICKER_RESULT);
                    }
                    else {
                        name   = fetch_contact[1];
                        number = fetch_contact[0];
                        pb.setBackground(getResources().getDrawable(R.drawable.addbuttonafter,this.getTheme()));
                        pb.setText(name);
                        number = number.replaceAll("[^0-9]", "");
                        new GetPrev().execute(Long.parseLong(number));
                    }

                    Thing alarm = new Thing.Builder()
                            .setName("Saved !")
                            .setDescription("You owe " + st[2] + " " + st[3] + " Rupees")
                            .setUrl(Uri.parse("android-app://me.anikraj.iou/add"))
                                    .build();

                    Action setAlarmAction = new Action.Builder(Action.TYPE_ADD)
                            .setObject(alarm)
                            .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                            .build();

                    AppIndex.AppIndexApi.end(mClient, setAlarmAction);
                }
                else {
                    et.setText(st[3]);


                    Uri uri = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_FILTER_URI,Uri.encode(st[2].toString().trim()));
                    Cursor mapContact = this.getContentResolver().query(uri, new String[]{ContactsContract.PhoneLookup._ID}, null, null, null);
                    if(mapContact.moveToNext())
                    {
                        String _id = mapContact.getString(mapContact.getColumnIndex(ContactsContract.Contacts._ID));

                    }
                    //  Uri lookupUri = Uri.withAppendedPath(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    //           Uri.encode("hari"));
                    String[] fetch_contact=getPhoneNumber(st[0],this);
                    Toast.makeText(this,fetch_contact[0]+ " "+ fetch_contact[1],Toast.LENGTH_LONG).show();
                    if(fetch_contact[0].compareTo("Unsaved")==0){
                        Toast.makeText(this,st[0]+" not found. Select contact!",Toast.LENGTH_LONG).show();
                        Intent i = new Intent(Intent.ACTION_PICK, ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
                        startActivityForResult(i, CONTACT_PICKER_RESULT);
                    }
                    else {
                        name   = fetch_contact[1];
                        number = fetch_contact[0];
                        pb.setBackground(getResources().getDrawable(R.drawable.addbuttonafter,this.getTheme()));
                        pb.setText(name);
                        number = number.replaceAll("[^0-9]", "");
                        new GetPrev().execute(Long.parseLong(number));
                    }

                    Thing alarm = new Thing.Builder()
                            .setName("Saved !")
                            .setDescription("You owe " + st[2] + " " + st[3] + " Rupees")
                            .setUrl(Uri.parse("android-app://me.anikraj.iou/add"))
                            .build();

                    Action setAlarmAction = new Action.Builder(Action.TYPE_ADD)
                            .setObject(alarm)
                            .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                            .build();

                    AppIndex.AppIndexApi.end(mClient, setAlarmAction);
                }




            }


        dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                SharedPreferences settings = getSharedPreferences("butt", 0);
                SharedPreferences.Editor editor = settings.edit();
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        //Yes button clicked

                        editor.putInt("addpreferance",0);
                        editor.commit();
                        add();
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        //No button clicked
                        editor.putInt("addpreferance",1);
                        editor.commit();
                        seperate();
                        break;
                }
            }
        };

    }

    public String[] getPhoneNumber(String name, Context context) {
        String[] ret = new String[2];
        String selection = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME+" like'%" + name +"%'";
        String[] projection = new String[] { ContactsContract.CommonDataKinds.Phone.NUMBER,ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME};
        Cursor c = context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                projection, selection, null, null);
        if (c.moveToFirst()) {
            ret[0]=(c.getString(0));ret[1]=(c.getString(1));
        }
        c.close();
        if(ret[0]==null){
            ret[0] = "Unsaved";ret[1] = "Unsaved";}
        return ret;
    }
    @Override
    public void onStart() {
        super.onStart();
        mClient.connect();
        AppIndex.AppIndexApi.start(mClient, getAction());
    }
    public Action getAction() {
        Thing object = new Thing.Builder()
                .setName("IOU")
                .setDescription("Micro management")
                .setUrl(Uri.parse("android-app://me.anikraj.iou/add"))
                .build();

        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }
    @Override
    public void onStop() {
        AppIndex.AppIndexApi.end(mClient, getAction());
        mClient.disconnect();
        super.onStop();
    }
    public void o(View v){
        o.setBackground(v.getResources().getDrawable(R.drawable.addbuttonafter, v.getContext().getTheme()));
        ob.setBackground(v.getResources().getDrawable(R.drawable.addbuttonbefore, v.getContext().getTheme()));
        owed=false;
    }

    public void ob(View v){
        ob.setBackground(v.getResources().getDrawable(R.drawable.addbuttonafter,v.getContext().getTheme()));
        o.setBackground(v.getResources().getDrawable(R.drawable.addbuttonbefore,v.getContext().getTheme()));
        owed=true;
    }

    public void fab(View v){

        if(name==null) Snackbar.make(getCurrentFocus(), "Choose person.", Snackbar.LENGTH_LONG).setAction("Action", null).show();
        else if(et.getText().toString().length()==0)Snackbar.make(getCurrentFocus(), "Enter Amount.", Snackbar.LENGTH_LONG).setAction("Action", null).show();
        else {
            SharedPreferences settings = getSharedPreferences("butt", 0);
            if (settings.getInt("addpreferance", -1) == -1) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);

                LayoutInflater inflater = this.getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.alertadd, null);

                AlertDialog.Builder builder2 =
                        new AlertDialog.Builder(this);
                builder2.setMessage("Choose insert type")
                        .setView(dialogView)
                        .setPositiveButton("Add", dialogClickListener)
                        .setNegativeButton("Seperate", dialogClickListener)
                        .show();
                ;

            } else if (settings.getInt("addpreferance", -1) == 0) add();
            else if (settings.getInt("addpreferance", -1) == 1) seperate();
        }
    }

  public void add(){
      OObject oobject =new OObject();
      oobject.setAmount(Double.parseDouble(et.getText().toString()));
      oobject.setDate(new Date().getTime());
      oobject.setEmail("null");
      oobject.setNumber(Long.parseLong(number));
      oobject.setWho(name);

      if(owed)oobject.setPayed(2);
      else oobject.setPayed(1);

      SharedPreferences settings = getSharedPreferences("butt", 0);
      SharedPreferences.Editor editor = settings.edit();
      float owes=settings.getFloat("owe",0);
      float oweds=settings.getFloat("owed",0);
      float owet=0,owedt=0;




      List<OObject> owe = db.getAllContactsByNum(Long.parseLong(number));
      List<OObject> owed = db.getAllContactsByNum2(Long.parseLong(number));
      if(owe.size()==0&&owed.size()==0)
          seperate();
      else{
          double totaletemp=0;
          for(int i=0;i<owe.size();i++){
              if(owe.get(i).getPayed()==3)continue;
              totaletemp-=owe.get(i).getAmount();
              db.deleteContact(owe.get(i));
              owet+=owe.get(i).getAmount();
          }
          for(int i=0;i<owed.size();i++){
              if(owed.get(i).getPayed()==3)continue;
              totaletemp+=owed.get(i).getAmount();
              db.deleteContact2(owed.get(i));
              owedt+=owed.get(i).getAmount();
          }

          owes-=owet;oweds-=owedt;

          if(totaletemp>0){
              if(oobject.getPayed()==2){
                oobject.setAmount(oobject.getAmount()+totaletemp);
                db.addContact2(oobject);Toast.makeText(this,name+" owes you \u20B9"+oobject.getAmount(),Toast.LENGTH_SHORT).show();
                oweds+=oobject.getAmount();
              }
              else{
                  totaletemp-=oobject.getAmount();
                  if(totaletemp>0){
                      oobject.setPayed(2);
                      oobject.setAmount(totaletemp);
                      db.addContact2(oobject);Toast.makeText(this,name+" owes you \u20B9"+oobject.getAmount(),Toast.LENGTH_SHORT).show();
                      oweds+=oobject.getAmount();
                  }
                  else if(totaletemp<0){
                      oobject.setPayed(1);
                      oobject.setAmount(-totaletemp);
                      db.addContact(oobject);Toast.makeText(this,"You owe "+name+" \u20B9"+oobject.getAmount(),Toast.LENGTH_SHORT).show();
                      owes+=oobject.getAmount();
                  }
                  else {
                      Toast.makeText(this,"Amount balanced with "+oobject.getWho(),Toast.LENGTH_SHORT).show();
                  }
              }
          }
          else{
              if(oobject.getPayed()==1){
                  oobject.setAmount(oobject.getAmount()-totaletemp);
                  db.addContact(oobject);Toast.makeText(this,"You owe "+name+" \u20B9"+oobject.getAmount(),Toast.LENGTH_SHORT).show();
                  owes+=oobject.getAmount();
              }
              else{
                  totaletemp+=oobject.getAmount();
                  if(totaletemp>0){
                      oobject.setAmount(totaletemp);
                      db.addContact2(oobject);Toast.makeText(this,name+" owes you \u20B9"+oobject.getAmount(),Toast.LENGTH_SHORT).show();
                      oweds+=oobject.getAmount();

                  }
                  else if(totaletemp<0){
                      oobject.setAmount(-totaletemp);
                      db.addContact(oobject);Toast.makeText(this,"You owe "+name+" \u20B9"+oobject.getAmount(),Toast.LENGTH_SHORT).show();
                      owes+=oobject.getAmount();
                  }
                  else {
                      Toast.makeText(this,"Amount balanced with "+oobject.getWho(),Toast.LENGTH_SHORT).show();
                  }
              }

          }
          editor.putFloat("owe",owes);
          editor.putFloat("owed",oweds);
          // Commit the edits!
          editor.commit();
            finish();
      }
  }

    public void seperate(){

            OObject oobject =new OObject();
            oobject.setAmount(Double.parseDouble(et.getText().toString()));
            oobject.setDate(new Date().getTime());
            oobject.setEmail("null");
            oobject.setNumber(Long.parseLong(number));
            oobject.setWho(name);

            if(owed)oobject.setPayed(2);
            else oobject.setPayed(1);




            if(owed) {db.addContact2(oobject);Toast.makeText(this,name+" owes you \u20B9"+et.getText().toString(),Toast.LENGTH_SHORT).show();}
            else{ db.addContact(oobject);Toast.makeText(this,"You owe "+name+" \u20B9"+et.getText().toString(),Toast.LENGTH_SHORT).show();}

            SharedPreferences settings = getSharedPreferences("butt", 0);
            SharedPreferences.Editor editor = settings.edit();
            float owee=settings.getFloat("owe", 0);
            float owedd=settings.getFloat("owed", 0);

            if(owed)owedd+=Double.parseDouble(et.getText().toString());
            else owee+=Double.parseDouble(et.getText().toString());

            editor.putFloat("owe",owee);
            editor.putFloat("owed", owedd);
            // Commit the edits!
            editor.commit();


        SharedPreferences getPrefs = PreferenceManager
                .getDefaultSharedPreferences(getBaseContext());
            new Helper().upall(fixnumberlength(getPrefs.getString("ownnum", "0000000000")),getPrefs.getString("gname", "error"),getPrefs.getString("gemail", "error") , this);

            finish();




    }

    public String fixnumberlength(String n){
        if(n.length()==10)return "91"+n;
        else return  n;

    }

    public void doLaunchContactPicker(View view) {
        Intent i = new Intent(Intent.ACTION_PICK, ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
        startActivityForResult(i, CONTACT_PICKER_RESULT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CONTACT_PICKER_RESULT && resultCode == RESULT_OK) {
            Uri contactUri = data.getData();
            String[] projection    = new String[] {ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                    ContactsContract.CommonDataKinds.Phone.NUMBER};

            Cursor cursor = getContentResolver().query(contactUri, projection, null, null, null);
            int indexName = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
            int indexNumber = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);

            cursor.moveToFirst();
            name   = cursor.getString(indexName);
            number = cursor.getString(indexNumber);
            pb.setBackground(getResources().getDrawable(R.drawable.addbuttonafter,this.getTheme()));
            pb.setText(name);
            number = number.replaceAll("[^0-9]", "");
            new GetPrev().execute(Long.parseLong(number));
        }

    }


    private class GetPrev extends AsyncTask<Long, Void, String> {

        @Override
        protected String doInBackground(Long... params) {
            List<OObject> owe = db.getAllContactsByNum(params[0]);
            List<OObject> owed = db.getAllContactsByNum2(params[0]);
            if(owe.size()==0&&owed.size()==0)
            return null;
            else{
                String x="";
                for(int i=0;i<owe.size();i++){
                    if(owe.get(i).getPayed()==3)continue;
                    x+="I owe "+owe.get(i).getWho()+" \u20B9 "+owe.get(i).getAmount()+"\n";
                }
                for(int i=0;i<owed.size();i++){
                    if(owed.get(i).getPayed()==3)continue;
                    x+=owed.get(i).getWho()+" owes me \u20B9 "+owed.get(i).getAmount()+"\n";
                }
                return x;
            }
        }

        @Override
        protected void onPostExecute(String a) {
            if(a!=null){
                prev.setVisibility(View.VISIBLE);
                prev.setText(a);
            }
            else{
                prev.setVisibility(View.GONE);
            }
        }
    }
}
