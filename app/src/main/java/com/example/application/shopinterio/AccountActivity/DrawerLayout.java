package com.example.application.shopinterio.AccountActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.application.shopinterio.MainActivity;
import com.example.application.shopinterio.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import Fragments.Attendance;
import Fragments.Catalogue;
import Fragments.LocationReport;
import Fragments.MeetingDetails;
import Fragments.MeetingReminders;
import Fragments.SendQuotation;
import Fragments.ShareLocation;
import Fragments.SignOut;
import Fragments.VisitingCards;
import Fragments.WorkReport;

import static com.example.application.shopinterio.R.id.auto;
import static com.example.application.shopinterio.R.id.drawer;
import static com.example.application.shopinterio.R.id.email;
import static com.example.application.shopinterio.R.id.nav_attendance;
import static com.example.application.shopinterio.R.id.nv;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class DrawerLayout extends AppCompatActivity {

    private android.support.v4.widget.DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;
    private ProgressBar progressBar;
    private FirebaseAuth auth;
    private DatabaseReference databaseReference;
    private TextView t;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer_layout);
        if(checkRunTimePermission());

        auth = FirebaseAuth.getInstance();

        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();


        t=(TextView) findViewById(R.id.mail);
        String s = auth.getCurrentUser().getEmail();

        t.setText(s);


        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null) {
                    // user auth state is changed - user is null
                    // launch login activity
                    startActivity(new Intent(DrawerLayout.this, LoginActivity.class));
                    finish();
                }
            }
        };


        mDrawerLayout = (android.support.v4.widget.DrawerLayout) findViewById(drawer);
        mToggle= new ActionBarDrawerToggle(this,mDrawerLayout,R.string.open,R.string.close);
        mDrawerLayout.addDrawerListener(mToggle);
       NavigationView nvDrawer= (NavigationView) findViewById(nv);
        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
       setupDrawerContent(nvDrawer);



        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        if (progressBar != null) {
            progressBar.setVisibility(View.GONE);
        }




    }


    public void selectItemDrawer(MenuItem menuItem){
        Fragment myFragment =null;
        Class fragmentClass;
        switch (menuItem.getItemId()){

            case R.id.nav_attendance:
                fragmentClass= Attendance.class;
                break;

            case R.id.nav_catalogue:
                fragmentClass= Catalogue.class;
                break;

            case R.id.nav_locationReport:
                fragmentClass= LocationReport.class;
                break;

            case R.id.nav_meetingDetails:
                fragmentClass= MeetingDetails.class;
                break;

            case R.id.nav_meetingReminders:
                fragmentClass= MeetingReminders.class;
                break;

            case R.id.nav_quotation:
                fragmentClass= SendQuotation.class;
                break;

            case R.id.nav_shareLocation:
                fragmentClass= ShareLocation.class;
                break;

            case R.id.nav_visitingCards:
                fragmentClass= VisitingCards.class;
                break;

            case R.id.nav_workReport:
                fragmentClass= WorkReport.class;
                break;

            case R.id.nav_signout:
                fragmentClass= SignOut.class;
                break;
            default:
                fragmentClass=Attendance.class;
        }

        try{
            myFragment= (Fragment) fragmentClass.newInstance();
        }

        catch (Exception e)
        {
            e.printStackTrace();
        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.flcontent,myFragment).commit();
        menuItem.setChecked(true);
        setTitle(menuItem.getTitle());
        mDrawerLayout.closeDrawers();
     }

     private void setupDrawerContent(NavigationView navigationView){
         navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
             @Override
             public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                 selectItemDrawer(item);
                 return true;
             }
         });
     }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(mToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    FirebaseAuth.AuthStateListener authListener = new FirebaseAuth.AuthStateListener() {
        @SuppressLint("SetTextI18n")
        @Override
        public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
            FirebaseUser user = firebaseAuth.getCurrentUser();
            if (user == null) {
                // user auth state is changed - user is null
                // launch login activity
                startActivity(new Intent(DrawerLayout.this, LoginActivity.class));
                finish();
            } else {
                //setDataToView(user);
                //startActivity(new Intent(MainActivity.this, NavigationDrawer.class));

            }
        }

    };


    private boolean checkRunTimePermission() {

       /* String[] permissionArrays = new String[]{ Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.WRITE_EXTERNAL_STORAGE};

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(permissionArrays, 11111);
        } else {
            // if already permition granted
            // PUT YOUR ACTION (Like Open cemara etc..)
        }*/

        int permissionFLocation = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        int permissionCLocation = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);
        int permissionExternal = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);


        List<String> listPermissionsNeeded = new ArrayList<>();
        if (permissionFLocation != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (permissionCLocation != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        }
        if (permissionExternal != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]),11111);
            return false;
        }
        return true;
    }

}