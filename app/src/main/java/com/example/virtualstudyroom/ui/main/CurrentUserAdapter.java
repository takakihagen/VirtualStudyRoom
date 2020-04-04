package com.example.virtualstudyroom.ui.main;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
    private Context mContext;

    public CurrentUserAdapter(Context context){
        mContext = context;
    }

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
            holder.mSatus.setText(currentUser.getStatus());
            holder.mTime.setText(displayTime(currentUser));
            displayYellButton(currentUser, holder);
        }
    }

    @Override
    public int getItemCount() {
        return mCurrentUsers==null?0:mCurrentUsers.size();
    }

    private String displayTime(CurrentUser cu){
        if(cu.getStatus().equals(mContext.getString(R.string.state_study))){
            return String.valueOf(cu.getTotalStudyTime());
        }else if(cu.getStatus().equals(mContext.getString(R.string.state_pause))){
            return String.valueOf(cu.getTotalPauseTime());
        }
        return null;
    }

    private void displayYellButton(CurrentUser cu, CurrentUserViewHolder holder){
        if(cu.getStatus().equals(mContext.getString(R.string.state_study))){
            holder.mYellButton.setVisibility(View.INVISIBLE);
        }else if(cu.getStatus().equals(mContext.getString(R.string.state_pause))){
            if(cu.getTotalPauseTime()>((long) mContext.getResources().getInteger(R.integer.yell_button_pause_time))) {
                holder.mYellButton.setOnClickListener(new View.OnClickListener() {
                                                          @Override
                                                          public void onClick(View v) {
                                                              Toast t = Toast.makeText(mContext, "clicked button", Toast.LENGTH_LONG);
                                                              t.show();
                                                          }
                                                      }
                );
                holder.mYellButton.setVisibility(View.VISIBLE);
            }else {
                holder.mYellButton.setVisibility(View.INVISIBLE);
            }
        }
    }

    public class CurrentUserViewHolder extends RecyclerView.ViewHolder{
        ImageView mIcon;
        TextView mUserName;
        TextView mSatus;
        TextView mTime;
        Button mYellButton;
        public CurrentUserViewHolder(@NonNull View itemView) {
            super(itemView);
            mIcon = itemView.findViewById(R.id.current_user_icon);
            mUserName = itemView.findViewById(R.id.current_user_name);
            mSatus = itemView.findViewById(R.id.tv_status);
            mTime = itemView.findViewById(R.id.tv_time);
            mYellButton = itemView.findViewById(R.id.yell_button);
        }
    }
}
