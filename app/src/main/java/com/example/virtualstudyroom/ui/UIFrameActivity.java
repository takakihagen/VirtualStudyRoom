package com.example.virtualstudyroom.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.virtualstudyroom.R;
import com.example.virtualstudyroom.ui.main.StudyHistoryFragment;
import com.example.virtualstudyroom.ui.main.StudyRoomFragment;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

public class UIFrameActivity extends AppCompatActivity {

    protected DrawerLayout mDrawerLayout;
    private Toolbar mToolBar;
    private NavigationView mNavigationView;
    private ImageView mProfileImageView;
    private TextView mDispNameTextView;

    private FirebaseUser mCurrentUser;
    private ActionBarDrawerToggle mDrawerToggle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uiframe);


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

        initNavigationHeader();
        setupDrawerContent(mNavigationView);
        initFragment();
    }

    protected void initFragment(){
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

    protected void LogOut(){
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
        // Create a new fragment and specify the fragment to show based on nav item clicked
        switch(menuItem.getItemId()) {
            case R.id.nav_study_room:
                fragmentClass = StudyRoomFragment.class;
                break;
            case R.id.nav_study_history:
                fragmentClass = StudyHistoryFragment.class;
                break;
            case R.id.nav_sign_out:
                LogOut();
                break;
            default:
                fragmentClass = StudyRoomFragment.class;
        }

        if(fragmentClass != null) {
            try {
                fragment = (Fragment) fragmentClass.newInstance();
            } catch (Exception e) {
                e.printStackTrace();
            }

            // Insert the fragment by replacing any existing fragment
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.nav_host_fragment, fragment).commit();

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
}
