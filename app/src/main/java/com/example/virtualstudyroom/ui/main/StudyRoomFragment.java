package com.example.virtualstudyroom.ui.main;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
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
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class StudyRoomFragment extends Fragment {

    private List<CurrentUser> mCurrentUsers = new ArrayList<CurrentUser>();
    private CurrentUserAdapter mAdapter;
    private RecyclerView mCurrentUserList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_study_room, container, false);
        mCurrentUserList = (RecyclerView) view.findViewById(R.id.list_of_current_users);
        mAdapter = new CurrentUserAdapter();
        mCurrentUserList.setLayoutManager(new LinearLayoutManager(getContext()));
        mCurrentUserList.setAdapter(mAdapter);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        ((MainActivity) getActivity()).setButtons();
        getData();
    }

    private void getData(){
        ((MainActivity) getActivity()).mFireDb.collection(getString(R.string.study_user_collection))
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for (QueryDocumentSnapshot doc: task.getResult()){
                            CurrentUser cu = new CurrentUser((String) doc.getData().get(getString(R.string.fs_user_display_name)),
                                    Uri.parse((String) doc.getData().get(getString(R.string.fs_user_icon_url))),
                                    (Timestamp) doc.getData().get(getString(R.string.fs_start_time)));
                            mCurrentUsers.add(cu);
                        }
                        mAdapter.setCurrentUsers(mCurrentUsers);
                        mAdapter.notifyDataSetChanged();
                    }
                });
    }
}
