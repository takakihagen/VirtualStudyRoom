package com.example.virtualstudyroom.ui.main;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.virtualstudyroom.R;
import com.example.virtualstudyroom.model.Yell;

import java.util.List;

public class YellAdapter extends RecyclerView.Adapter<YellAdapter.YellViewHolder> {

    private List<Yell> mYells;

    public void setYells(List<Yell> yells){
        mYells = yells;
    }

    @NonNull
    @Override
    public YellViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        int layoutForListItem = R.layout.yell_item;
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutForListItem, parent, shouldAttachToParentImmediately);
        YellAdapter.YellViewHolder viewHolder = new YellAdapter.YellViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull YellViewHolder holder, int position) {
        if(mYells!=null){
            Yell yell = mYells.get(position);
            holder.mName.setText(yell.getUserName());
        }
    }

    @Override
    public int getItemCount() {
        return mYells==null?0:mYells.size();
    }

    public class YellViewHolder extends RecyclerView.ViewHolder{
        TextView mName;
        public YellViewHolder(@NonNull View itemView) {
            super(itemView);
            mName = (TextView) itemView.findViewById(R.id.sent_from_user_name);
        }
    }
}
