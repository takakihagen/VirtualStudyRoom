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
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.virtualstudyroom.R;
import com.example.virtualstudyroom.database.StudyHistory;
import com.example.virtualstudyroom.database.StudyHistoryDatabase;
import com.example.virtualstudyroom.model.CurrentUser;
import com.example.virtualstudyroom.ui.LoginActivity;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.sql.Time;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private DrawerLayout mDrawerLayout;
    private Toolbar mToolBar;
    private NavigationView mNavigationView;
    private ImageView mProfileImageView;
    private TextView mDispNameTextView;

    private FirebaseUser mCurrentUser;
    private ActionBarDrawerToggle mDrawerToggle;

    FirebaseFirestore mFireDb;
    String mState;
    private String mDocumentId;

    private StudyHistoryDatabase mStuyHisDb;

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

        initNavigationHeader();
        setupDrawerContent(mNavigationView);

        //Firebase firestore
        mFireDb = FirebaseFirestore.getInstance();

        //Local room database
        mStuyHisDb = StudyHistoryDatabase.getInstance(getApplicationContext());

        if(savedInstanceState==null) {
            initFragment();
        }
    }

    public void setButtons(){
        displayButtonRoom(getResources().getInteger(R.integer.load_state_val));
        mFireDb.collection(getString(R.string.study_user_collection))
                .whereEqualTo(getString(R.string.user_id), mCurrentUser.getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        Log.d("++______",getString(R.string.fs_start_time));
                        QueryDocumentSnapshot validDoc = null;
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("KKKKKKK", document.getId() + " => " + document.getData());
                                if(validDoc==null) validDoc = document;

                                Timestamp validDocTime = (Timestamp) validDoc.getData().get(getString(R.string.fs_start_time));
                                Timestamp docTime = (Timestamp) document.getData().get(getString(R.string.fs_start_time));
                                if(docTime.compareTo(validDocTime)>0){
                                    validDoc = document;
                                }
                            }

                            if(validDoc!=null) {
                                mState = (String) validDoc.getData().get(getResources().getString(R.string.fs_status));
                                setmDocumentId((String) validDoc.getId());
                                if (mState.equals(getString(R.string.state_study))) {
                                    displayButtonRoom(getResources().getInteger(R.integer.study_state_val));
                                } else if (mState.equals(getString(R.string.state_pause))) {
                                    displayButtonRoom(getResources().getInteger(R.integer.pause_state_val));
                                } else if (mState.equals(getString(R.string.state_stop))) {
                                    displayButtonRoom(getResources().getInteger(R.integer.stop_state_val));
                                }
                            }else{
                                displayButtonRoom(getResources().getInteger(R.integer.stop_state_val));
                            }
                        } else {
                            Log.d("JIJIJIJ","lll");
                            displayButtonRoom(getResources().getInteger(R.integer.stop_state_val));
                        }
                    }
                });
    }

    private void initFragment(){
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.nav_host_fragment, new StudyRoomFragment()).commit();
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
        displayButtonRoom(getResources().getInteger(R.integer.load_state_val));

        Timestamp time = Timestamp.now();
        Map<String, Object> user = new HashMap<>();
        user.put(getString(R.string.fs_user_id), mCurrentUser.getUid());
        user.put(getString(R.string.fs_user_display_name), mCurrentUser.getDisplayName());
        user.put(getString(R.string.fs_user_icon_url), mCurrentUser.getPhotoUrl().toString());
        user.put(getString(R.string.fs_status), getString(R.string.state_study));
        user.put(getString(R.string.fs_pause_total_time), 0);
        user.put(getString(R.string.fs_start_time), time);

        mFireDb.collection(getString(R.string.study_user_collection))
                .add(user)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d("OOOO", "DocumentSnapshot added with ID: " + documentReference.getId());
                        setmDocumentId(documentReference.getId());
                        displayButtonRoom(getResources().getInteger(R.integer.study_state_val));

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("OOOO", "Error adding document", e);
                    }
                });
    }

    public void onStopButtonClick(View view){
        displayButtonRoom(getResources().getInteger(R.integer.load_state_val));

        final DocumentReference docRef = mFireDb.collection(getString(R.string.study_user_collection)).document(mDocumentId);

        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if(documentSnapshot.exists()) {
                        Timestamp studyStart = (Timestamp) documentSnapshot.getData().get(getString(R.string.fs_start_time));
                        long totalPauseTime = (long) documentSnapshot.getData().get(getString(R.string.fs_pause_total_time));
                        long totalStudyTime = Math.abs(studyStart.getSeconds()-Timestamp.now().getSeconds())-totalPauseTime;
                        String status = (String) documentSnapshot.getData().get(getString(R.string.fs_status));
                        if(getString(R.string.state_pause).equals(status)){
                            Timestamp pauseStart = (Timestamp) documentSnapshot.getData().get(getString(R.string.fs_pause_start_time));
                            totalStudyTime -= Math.abs(pauseStart.getSeconds()-Timestamp.now().getSeconds());
                        }

                        docRef.delete()
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d("LLLL", "DocumentSnapshot successfully updated!");
                                        displayButtonRoom(getResources().getInteger(R.integer.stop_state_val));
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.w("LLLL", "Error updating document", e);
                                    }
                                });

                        final StudyHistory history = new StudyHistory((Long) totalPauseTime, studyStart.toDate());
                        new AsyncTask<Void, Void, Void>(){
                            @Override
                            protected Void doInBackground(Void... voids) {
                                mStuyHisDb.studyHistoryDao().insertHistory(history);
                                return null;
                            }
                        }.execute();
                    }
                    else
                        Log.d("JJJJJJ", "get failed with ", task.getException());
                } else {
                    Log.d("JJJJJJ", "get failed with ", task.getException());
                }
            }
        });
    }

    public void onPauseButtonClick(View view){
        displayButtonRoom(getResources().getInteger(R.integer.load_state_val));

        Timestamp time = Timestamp.now();
        Map<String, Object> user = new HashMap<>();
        user.put(getString(R.string.fs_status), getString(R.string.state_pause));
        user.put(getString(R.string.fs_pause_start_time), time);
        DocumentReference docRef = mFireDb.collection(getString(R.string.study_user_collection)).document(mDocumentId);
        docRef.update(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("LLLL", "DocumentSnapshot successfully updated!");
                        displayButtonRoom(getResources().getInteger(R.integer.pause_state_val));
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("LLLL", "Error updating document", e);
                    }
                });
    }

    public void onPauseStartButtonClick(View view) {
        displayButtonRoom(getResources().getInteger(R.integer.load_state_val));
        final DocumentReference docRef = mFireDb.collection(getString(R.string.study_user_collection)).document(mDocumentId);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if(documentSnapshot.exists()) {
                        Timestamp pauseStart = (Timestamp) documentSnapshot.getData().get(getString(R.string.fs_pause_start_time));
                        long PauseTime = Math.abs(pauseStart.getSeconds() - Timestamp.now().getSeconds());

                        long pauseTotalTime = (long) documentSnapshot.getData().get(getString(R.string.fs_pause_total_time));
                        pauseTotalTime += PauseTime;

                        Map<String, Object> user = new HashMap<>();
                        user.put(getString(R.string.fs_status), getString(R.string.state_study));
                        user.put(getString(R.string.fs_pause_total_time), pauseTotalTime);

                        docRef.update(user)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d("LLLL", "DocumentSnapshot successfully updated!");
                                        displayButtonRoom(getResources().getInteger(R.integer.study_state_val));
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.w("LLLL", "Error updating document", e);
                                    }
                                });
                    }
                    else
                        Log.d("JJJJJJ", "get failed with ", task.getException());
                } else {
                    Log.d("JJJJJJ", "get failed with ", task.getException());
                }
            }
        });
    }

    private void displayButtonRoom(int buttonRoomState){
        //setButtonRoom: true=> show start button, false=>show stop buttons
        LinearLayout studyStateLinearLayout = (LinearLayout) findViewById(R.id.study_state_room);
        LinearLayout pauseStateLinerLayout = (LinearLayout) findViewById(R.id.pause_state_room);
        LinearLayout stopStateLinerLayout = (LinearLayout) findViewById(R.id.stop_state_room);
        LinearLayout loadStateLinearLayout = (LinearLayout) findViewById(R.id.progress_bar_room);

        if(getResources().getInteger(R.integer.study_state_val)==buttonRoomState) {
            studyStateLinearLayout.setVisibility(View.VISIBLE);
            pauseStateLinerLayout.setVisibility(View.GONE);
            stopStateLinerLayout.setVisibility(View.GONE);
            loadStateLinearLayout.setVisibility(View.GONE);
        }else if(getResources().getInteger(R.integer.pause_state_val)==buttonRoomState){
            studyStateLinearLayout.setVisibility(View.GONE);
            pauseStateLinerLayout.setVisibility(View.VISIBLE);
            stopStateLinerLayout.setVisibility(View.GONE);
            loadStateLinearLayout.setVisibility(View.GONE);
        }else if(getResources().getInteger(R.integer.stop_state_val)==buttonRoomState){
            studyStateLinearLayout.setVisibility(View.GONE);
            pauseStateLinerLayout.setVisibility(View.GONE);
            stopStateLinerLayout.setVisibility(View.VISIBLE);
            loadStateLinearLayout.setVisibility(View.GONE);
        }else if(getResources().getInteger(R.integer.load_state_val)==buttonRoomState){
            studyStateLinearLayout.setVisibility(View.GONE);
            pauseStateLinerLayout.setVisibility(View.GONE);
            stopStateLinerLayout.setVisibility(View.GONE);
            loadStateLinearLayout.setVisibility(View.VISIBLE);
        }
    }

    private void setmDocumentId(String dId){
        mDocumentId = dId;
    }
}
