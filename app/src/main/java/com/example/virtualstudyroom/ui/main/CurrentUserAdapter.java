package com.example.virtualstudyroom.ui.main;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.virtualstudyroom.R;
import com.example.virtualstudyroom.model.CurrentUser;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CurrentUserAdapter extends RecyclerView.Adapter<CurrentUserAdapter.CurrentUserViewHolder> {

    private List<CurrentUser> mCurrentUsers;

    public void setCurrentUsers(List<CurrentUser> cu){
        mCurrentUsers = cu;
    }

    @NonNull
    @Override
    public CurrentUserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        int layoutForListItem = R.layout.user_item;
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutForListItem, parent, shouldAttachToParentImmediately);
        CurrentUserViewHolder viewHolder = new CurrentUserViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CurrentUserViewHolder holder, int position) {
        if(mCurrentUsers!=null){
            CurrentUser currentUser = mCurrentUsers.get(position);
            holder.mUserName.setText(currentUser.getUserName());
            Picasso.get()
                    .load(currentUser.getIconURI())
                    .into(holder.mIcon);
        }
    }

    @Override
    public int getItemCount() {
        return mCurrentUsers==null?0:mCurrentUsers.size();
    }

    public class CurrentUserViewHolder extends RecyclerView.ViewHolder{
        ImageView mIcon;
        TextView mUserName;
        public CurrentUserViewHolder(@NonNull View itemView) {
            super(itemView);
            mIcon = itemView.findViewById(R.id.current_user_icon);
            mUserName = itemView.findViewById(R.id.current_user_name);
        }
    }
}
