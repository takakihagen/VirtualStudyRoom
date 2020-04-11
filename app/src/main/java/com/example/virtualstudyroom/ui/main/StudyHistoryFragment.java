package com.example.virtualstudyroom.ui.main;

import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.virtualstudyroom.R;
import com.example.virtualstudyroom.database.StudyHistory;
import com.example.virtualstudyroom.ui.viewmodel.MainViewModel;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.google.firebase.Timestamp;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class StudyHistoryFragment extends Fragment {
    private BarChart mBarChartThisWeek;
    private BarChart mBarChartLastWeek;
    final private int thisWeekId = 0;
    final private int lastWeekId = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_study_history, container, false);

        mBarChartThisWeek = (BarChart) view.findViewById(R.id.bar_chart_this_week);
        mBarChartLastWeek = (BarChart) view.findViewById(R.id.bar_chart_last_week);
        retrieveTask(thisWeekId);
        retrieveTask(lastWeekId);
        return view;
    }

    private void retrieveTask(final int weekId){
        MainViewModel vModel = new ViewModelProvider(getActivity()).get(MainViewModel.class);
        vModel.getHistories().observe(getActivity(), new Observer<List<StudyHistory>>() {
            @Override
            public void onChanged(List<StudyHistory> studyHistories) {
                if(weekId == thisWeekId) {
                    ArrayList<BarEntry> entries = getData(studyHistories, thisWeekId);
                    setWeekChart(mBarChartThisWeek, entries);
                }else{
                    ArrayList<BarEntry> entries = getData(studyHistories, lastWeekId);
                    setWeekChart(mBarChartLastWeek, entries);
                }
            }
        });
    }

    private ArrayList<BarEntry> getData(List<StudyHistory> studyHistories, int weekId){
        ArrayList<BarEntry> entries = new ArrayList<>();
        long[] totalStudyTimes = new long[]{0,0,0,0,0,0,0,0,0,0,0,0,0,0};

        Calendar cal = Calendar.getInstance(TimeZone.getDefault());
        Date today = Timestamp.now().toDate();
        for(StudyHistory s: studyHistories){
            long diff = Math.abs(today.getTime()/(1000*60*60*24)-s.getStartTime().getTime()/(1000*60*60*24));
            if(diff < 14){
                totalStudyTimes[(int) diff]+=s.getTotalTime();
            }
        }

        Calendar c = Calendar.getInstance();
        c.setTime(today);
        int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
        if(weekId == thisWeekId) {
            for(int i = dayOfWeek-7; i < dayOfWeek; i++){
                if(i<0) {
                    entries.add(new BarEntry(dayOfWeek-1-i, 0));
                }else{
                    entries.add(new BarEntry(dayOfWeek-1-i, ((float) totalStudyTimes[i])/(60*60)));
                }
            }
        }if(weekId == lastWeekId) {
            for(int i = dayOfWeek; i < (dayOfWeek+7); i++){
                entries.add(new BarEntry(dayOfWeek+6-i, ((float) totalStudyTimes[i])/(60*60)));
            }
        }

        return entries;
    }

    private void setWeekChart(BarChart barChart, ArrayList<BarEntry> entries){

        final String[] week = new String[]{"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};

        IndexAxisValueFormatter formatter = new IndexAxisValueFormatter(week);
        barChart.getXAxis().setGranularity(1f);
        barChart.getXAxis().setValueFormatter(formatter);

        barChart.getAxisLeft().setDrawGridLines(false);
        barChart.getAxisRight().setDrawGridLines(false);
        barChart.getXAxis().setDrawGridLines(false);
        barChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        barChart.setDescription(null);
        barChart.setTouchEnabled(false);

        BarDataSet bardataset = new BarDataSet(entries, "");
        
        bardataset.setDrawIcons(false);
        //bardataset.setDrawValues(false);//value on each chart
        BarData barData = new BarData(bardataset);
        barChart.setData(barData);
        barChart.setFitBars(true);
        barChart.getLegend().setEnabled(false);
        barChart.invalidate();//refresh
    }
}
