package com.example.virtualstudyroom.ui.main;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.virtualstudyroom.R;
import com.example.virtualstudyroom.ui.LoginActivity;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;
    private Toolbar mToolBar;
    private NavigationView mNavigationView;
    private ImageView mProfileImageView;
    private TextView mDispNameTextView;

    private FirebaseUser mCurrentUser;
    private ActionBarDrawerToggle mDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mToolBar = (Toolbar) findViewById(R.id.toolbar);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mNavigationView = (NavigationView) findViewById(R.id.nav_view);
        if (mNavigationView.getHeaderCount() > 0) {
            // avoid NPE by first checking if there is at least one Header View available
            View headerLayout = mNavigationView.getHeaderView(0);
            mProfileImageView = headerLayout.findViewById(R.id.tv_profile_img);
            mDispNameTextView = headerLayout.findViewById(R.id.tv_disp_name);
        }


        setSupportActionBar(mToolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolBar, R.string.drawer_open,  R.string.drawer_close);


        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();

        initFragment();
        initNavigationHeader();
        setupDrawerContent(mNavigationView);

        //Firebase firestore
        FirebaseFirestore mFireDb = FirebaseFirestore.getInstance();
    }

    private void initFragment(){
        Fragment fragment = null;
        try {
            fragment = new StudyRoomFragment();
        } catch (Exception e) {
            e.printStackTrace();
        }
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.nav_host_fragment, fragment).commit();
    }

    private void initNavigationHeader(){
        if(mCurrentUser!=null) {
            if(mCurrentUser.getPhotoUrl()!=null) Picasso.get()
                                                    .load(mCurrentUser.getPhotoUrl())
                                                    .into(mProfileImageView);
            if(mCurrentUser.getDisplayName()!=null) mDispNameTextView.setText(mCurrentUser.getDisplayName());
            else mDispNameTextView.setText(getString(R.string.anonymous));
        }

    }

    private void LogOut(){
        AuthUI.getInstance()
                .signOut(this)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    public void onComplete(@NonNull Task<Void> task) {
                        Log.v("+++", "Log out listner!!");
                        goToLoginActivity();
                    }
                });
    }

    private void goToLoginActivity(){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        selectDrawerItem(menuItem);
                        return true;
                    }
                });
    }

    public void selectDrawerItem(MenuItem menuItem) {
        Fragment fragment = null;
        Class fragmentClass=null;
        FragmentManager fragmentManager = getSupportFragmentManager();
        boolean changeFrame = false;
        // Create a new fragment and specify the fragment to show based on nav item clicked
        switch(menuItem.getItemId()) {
            case R.id.nav_study_room:
                fragmentManager.beginTransaction().replace(R.id.nav_host_fragment, new StudyRoomFragment()).commit();
                changeFrame = true;
                break;
            case R.id.nav_study_history:
                fragmentManager.beginTransaction().replace(R.id.nav_host_fragment, new StudyHistoryFragment()).commit();
                changeFrame = true;
                break;
            case R.id.nav_sign_out:
                LogOut();
                break;
            default:
                fragmentManager.beginTransaction().replace(R.id.nav_host_fragment, new StudyRoomFragment()).commit();
                changeFrame = true;
        }

        if(changeFrame) {
            // Highlight the selected item has been done by NavigationView
            menuItem.setChecked(true);
            // Set action bar title
            setTitle(menuItem.getTitle());
            // Close the navigation drawer
            mDrawerLayout.closeDrawers();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        return NavigationUI.navigateUp(Navigation.findNavController(this, R.id.nav_host_fragment), mDrawerLayout);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    public void onStartButtonClick(View view) {
        LinearLayout startLinearLayout = (LinearLayout) findViewById(R.id.start_button_room);
        LinearLayout stopPauseLinerLayout = (LinearLayout) findViewById(R.id.pause_stop_button_room);
        startLinearLayout.setVisibility(View.GONE);
        stopPauseLinerLayout.setVisibility(View.VISIBLE);

        Toast toast = Toast.makeText(this, "Start button clicked", Toast.LENGTH_LONG);
        toast.show();
    }

    public void onStopButtonClick(View view){
        LinearLayout startLinearLayout = (LinearLayout) findViewById(R.id.start_button_room);
        LinearLayout stopPauseLinerLayout = (LinearLayout) findViewById(R.id.pause_stop_button_room);
        startLinearLayout.setVisibility(View.VISIBLE);
        stopPauseLinerLayout.setVisibility(View.GONE);

        Toast toast = Toast.makeText(this, "Stop button clicked", Toast.LENGTH_LONG);
        toast.show();
    }

    public void onPauseButtonClick(View view){

        Toast toast = Toast.makeText(this, "Pause button clicked", Toast.LENGTH_LONG);
        toast.show();
    }
}
