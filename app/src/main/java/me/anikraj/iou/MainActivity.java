package me.anikraj.iou;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
   
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;

public class MainActivity extends AppCompatActivity {
    private GoogleApiClient mGoogleApiClient;
    AccountHeader headerResult;
    private SectionsPagerAdapter mSectionsPagerAdapter;




    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
  //  private static final String TWITTER_KEY = "0oWWRawtKuLWzVQ2IVrdBujvQ";
  //  private static final String TWITTER_SECRET = "H7dfbyEDOjHk1txIoG0K6T56l5yNVtf7r3f90coyQ5mnkC7t6r";



    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    TextView total;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);




        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        //  Declare a new thread to do a preference check
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                //  Initialize SharedPreferences
                SharedPreferences getPrefs = PreferenceManager
                        .getDefaultSharedPreferences(getBaseContext());

                //  Create a new boolean and preference and set it to true
                boolean isFirstStart = getPrefs.getBoolean("firstStart", true);

                //  If the activity has never started before...
                if (isFirstStart) {

                    //  Launch app intro
                    Intent i = new Intent(MainActivity.this, Splash.class);
                    startActivity(i);

                    //  Make a new preferences editor
                    SharedPreferences.Editor e = getPrefs.edit();

                    //  Edit preference to make it false because we don't want this to run again
                    e.putBoolean("firstStart", false);

                    //  Apply changes
                    e.apply();
                }
            }
        });

        // Start the thread
        t.start();

        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
       // setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
       // Backendless.initApp(this, "3D5C89C7-FEF2-6DC6-FF50-1DB39A696400", "DEA24989-FD53-7D98-FFE5-4BD8CA0D7C00", "v1" ); // where to get the argument values for this call

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        total=(TextView)findViewById(R.id.total);



      FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
      fab.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
               final View dup = view;
         /*     OObject oobject =new OObject();
              oobject.setAmount(100);
              oobject.setDate(new Date().getTime());
              oobject.setEmail("ASdsad");
              oobject.setNumber(1231232);
              oobject.setWho("anik");
              oobject.setPayed(1);
              DatabaseHandler db = new DatabaseHandler(getApplicationContext());
              db.addContact(oobject);
              oobject.setPayed(2);
              db.addContact2(oobject); */
              startActivity(new Intent(MainActivity.this,Add.class));



           /*   Backendless.Persistence.save( oobject, new AsyncCallback<OObject>() {
                  public void handleResponse( OObject response )
                  {
                      Snackbar.make(dup, "added new object", Snackbar.LENGTH_LONG)
                              .setAction("Action", null).show();
                  }

                  public void handleFault( BackendlessFault fault )
                  {
                      Snackbar.make(dup, "Error. "+fault.getMessage(), Snackbar.LENGTH_LONG)
                              .setAction("Action", null).show();
                  }
              });  */








            //  Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)                      .setAction("Action", null).show();
              //startActivity(new Intent(MainActivity.this,Login.class));
          }
      });


        headerResult = new AccountHeaderBuilder()
                .withActivity(this)
                .withHeaderBackground(R.drawable.header)

                .withOnAccountHeaderListener(new AccountHeader.OnAccountHeaderListener() {
                    @Override
                    public boolean onProfileChanged(View view, IProfile profile, boolean currentProfile) {
                        return false;
                    }
                })
                .build();
        SharedPreferences getPrefs = PreferenceManager
                .getDefaultSharedPreferences(getBaseContext());
        if(getPrefs.getInt("login",-1)!=-1){
            headerResult.addProfiles(
                    new ProfileDrawerItem().withName(getPrefs.getString("gname", "error")).withEmail(getPrefs.getString("gemail", "error")).withIcon(getResources().getDrawable(R.drawable.profile3))
            );
        }


        PrimaryDrawerItem item1 = new PrimaryDrawerItem().withName("Home").withSelectable(false);
        PrimaryDrawerItem item2 = new PrimaryDrawerItem().withName("Reduce").withSelectable(false);
        SecondaryDrawerItem item3 = new SecondaryDrawerItem().withName("Login").withSelectable(false);
        SecondaryDrawerItem item4 = new SecondaryDrawerItem().withName("Logout").withSelectable(false);

//create the drawer and remember the `Drawer` result object
        Drawer result = new DrawerBuilder()
                .withActivity(this) .withAccountHeader(headerResult)
                .addDrawerItems(
                        item1,
                        item2,
                        new DividerDrawerItem(),
                        item3
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        // do something with the clicked item :D
                        if (position == 0) {
                            Toast.makeText(getApplicationContext(), "Blah Feature.", Toast.LENGTH_SHORT).show();
                        } else if (position == 1) {
                            Toast.makeText(getApplicationContext(), "Future Feature.", Toast.LENGTH_SHORT).show();
                        } else if (position == 2) {
                            SharedPreferences getPrefs = PreferenceManager
                                    .getDefaultSharedPreferences(getBaseContext());
                            if (getPrefs.getInt("login", -1) != -1) {

                                    Intent in = new Intent(MainActivity.this, Reduce.class);
                                    startActivity(in);

                            } else {
                                Toast.makeText(getApplicationContext(),"Login required!",Toast.LENGTH_LONG).show();
                            }
                        } else if (position == 4) {
                            signIn();
                        } else if (position == 5) {
                            signOut();
                        }
                        else {

                        }
                        return true;
                    }
                })
                .build();
      
    }
    public void signIn() {
       // Toast.makeText(this,"Asd",Toast.LENGTH_LONG).show();
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, 101);



    }

    private void signOut() {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        // ...
                    }
                });
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == 101) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {
        Log.d("anik signin", "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            final GoogleSignInAccount acct = result.getSignInAccount();
            Toast.makeText(this,"Welcome "+acct.getDisplayName(),Toast.LENGTH_LONG).show();
            Log.d("anik signin", "namew:" + acct.getDisplayName());

                    SharedPreferences getPrefs = PreferenceManager
                            .getDefaultSharedPreferences(getBaseContext());
                        SharedPreferences.Editor e = getPrefs.edit();

                        //  Edit preference to make it false because we don't want this to run again
                        e.putString("gname", acct.getDisplayName());
                        e.putString("gemail", acct.getEmail());
                        e.putInt("login",1);
            if(getPrefs.getInt("login",-1)==-1){
                    headerResult.addProfiles(
                            new ProfileDrawerItem().withName(acct.getDisplayName()).withEmail(acct.getEmail()).withIcon(getResources().getDrawable(R.drawable.profile3))
                    );}
                        //  Apply changes
                        e.apply();



            if (getPrefs.getInt("hasnum", -1) == -1) {
                Intent in = new Intent(MainActivity.this, GetNumber.class);
                startActivity(in);
            }
        } else {
            Toast.makeText(this,"Something went wrong :(",Toast.LENGTH_LONG).show();
            // Signed out, show unauthenticated UI.
            // updateUI(false);
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences settings = getSharedPreferences("butt", 0);
        float owe=settings.getFloat("owe",0);
        float owed=settings.getFloat("owed",0);
        float totall=owed-owe;
        total.setText("\u20B9 "+totall);

        TabLayout vTabs = (TabLayout)findViewById(R.id.tabs);
        vTabs.getTabAt(0).setText("I Owe\n\u20B9 "+owe);
        vTabs.getTabAt(1).setText("IM Owed\n\u20B9 "+owed);
    }


    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        SharedPreferences settings = getSharedPreferences("butt", 0);
        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new Owe();
                case 1:
                    return new Owed();
            }
            return null;

        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            float owe=settings.getFloat("owe",0);
            float owed=settings.getFloat("owed",0);
            switch (position) {
                case 0:
                    return "I Owe\n\u20B9 "+owe;
                case 1:
                    return "Im Owed\n\u20B9 "+owed;
            }
            return null;
        }
    }


}
