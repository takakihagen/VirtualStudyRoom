package com.example.virtualstudyroom.ui.main;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.virtualstudyroom.R;
import com.example.virtualstudyroom.model.CurrentUser;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class StudyRoomFragment extends Fragment {

    private List<CurrentUser> mCurrentUsers = new ArrayList<CurrentUser>();
    private CurrentUserAdapter mAdapter;
    private RecyclerView mCurrentUserList;

    private FragmentActivity mActivity;

    public StudyRoomFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_study_room, container, false);
        mCurrentUserList = (RecyclerView) view.findViewById(R.id.list_of_current_users);
        mAdapter = new CurrentUserAdapter(getContext());
        mCurrentUserList.setLayoutManager(new LinearLayoutManager(getContext()));
        mCurrentUserList.setAdapter(mAdapter);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        ((MainActivity) getActivity()).setButtons();
        mActivity = getActivity();
        updateUI(mActivity);
        setOnDBchangeListener();
    }

    private void updateUI(final FragmentActivity activity){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("StudyUsers").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        mCurrentUsers = new ArrayList<CurrentUser>();
                        for (QueryDocumentSnapshot doc: task.getResult()){
                            String docId = doc.getId();
                            String userName = (String) doc.getData().get(activity.getResources().getString(R.string.fs_user_display_name));
                            Uri iconURI = Uri.parse((String) doc.getData().get(activity.getResources().getString(R.string.fs_user_icon_url)));
                            Timestamp startAt = (Timestamp) doc.getData().get(activity.getResources().getString(R.string.fs_start_time));
                            String status = (String) doc.getData().get(activity.getResources().getString(R.string.fs_status));
                            long pauseTieme = (long) doc.getData().get(activity.getResources().getString(R.string.fs_pause_total_time));

                            CurrentUser cu = null;
                            if(status.equals(activity.getResources().getString(R.string.state_study))){
                                cu = new CurrentUser(docId, userName, iconURI, startAt, status, pauseTieme);
                            }else if(status.equals(activity.getResources().getString(R.string.state_pause))){
                                Timestamp pauseStartTieme = (Timestamp) doc.getData().get(activity.getResources().getString(R.string.fs_pause_start_time));
                                boolean sendable = (boolean) doc.getData().get(activity.getResources().getString(R.string.fs_sendable));
                                String token = (String) doc.getData().get(activity.getResources().getString(R.string.fs_registered_token));
                                cu = new CurrentUser(docId, userName, iconURI, startAt, status, pauseTieme, pauseStartTieme, token, sendable);
                            }
                            mCurrentUsers.add(cu);
                        }
                        mAdapter.setCurrentUsers(mCurrentUsers);
                        mAdapter.notifyDataSetChanged();
                    }
                });
    }

    private void setOnDBchangeListener(){
        ((MainActivity) getActivity()).mFireDb.collection(getString(R.string.study_user_collection))
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        updateUI(mActivity);
                    }
                });
    }
}
