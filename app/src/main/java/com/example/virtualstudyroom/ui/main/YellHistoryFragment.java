package com.example.virtualstudyroom.ui.main;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.virtualstudyroom.R;
import com.example.virtualstudyroom.model.Yell;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class YellHistoryFragment extends Fragment {
    private List<Yell> mYells = new ArrayList<Yell>();
    private RecyclerView mYellsList;
    private FragmentActivity mActivity;
    private YellAdapter mAdapter;


    public YellHistoryFragment(){}

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_yell_history, container, false);

        mYellsList = (RecyclerView) view.findViewById(R.id.list_yells);
        mAdapter = new YellAdapter();
        mYellsList.setLayoutManager(new LinearLayoutManager(getContext()));
        mYellsList.setAdapter(mAdapter);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        mActivity = getActivity();
        updateUI(mActivity);
        setOnDBchangeListener();
    }

    private void updateUI(final FragmentActivity activity){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(activity.getResources().getString(R.string.yell_collections)).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        mYells = new ArrayList<Yell>();
                        for (QueryDocumentSnapshot doc: task.getResult()){
                            String userId = (String) doc.getData().get(activity.getResources().getString(R.string.fs_send_to_uid));
                            if(userId.equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                                String userName = (String) doc.getData().get(activity.getResources().getString(R.string.fs_send_from_name));
                                Uri iconURI = Uri.parse((String) doc.getData().get(activity.getResources().getString(R.string.fs_send_from_icon_url)));
                                Timestamp startAt = (Timestamp) doc.getData().get(activity.getResources().getString(R.string.fs_send_at));

                                Yell yell = new Yell(userName, userId, iconURI, startAt);
                                mYells.add(yell);
                            }
                        }

                        Collections.sort(mYells);
                        Collections.reverse(mYells);
                        mAdapter.setYells(mYells);
                        mAdapter.notifyDataSetChanged();
                    }
                });
    }

    private void setOnDBchangeListener(){
        ((MainActivity) getActivity()).mFireDb.collection(getString(R.string.yell_collections))
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        updateUI(mActivity);
                    }
                });
    }
}
