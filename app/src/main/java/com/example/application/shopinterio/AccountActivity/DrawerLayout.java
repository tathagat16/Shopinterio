package com.example.application.shopinterio.AccountActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Configuration;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

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

import static com.example.application.shopinterio.R.id.drawer;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class DrawerLayout extends AppCompatActivity {

    private android.support.v4.widget.DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;
    private ProgressBar progressBar;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer_layout);


        auth = FirebaseAuth.getInstance();

        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();


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
       NavigationView nvDrawer= (NavigationView) findViewById(R.id.nv);
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
                fragmentClass=ShareLocation.class;
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


}
