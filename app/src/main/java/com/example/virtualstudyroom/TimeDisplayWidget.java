package com.example.virtualstudyroom;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.RemoteViews;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import com.example.virtualstudyroom.database.StudyHistory;
import com.example.virtualstudyroom.database.StudyHistoryDatabase;
import com.google.firebase.Timestamp;

import java.util.List;

/**
 * Implementation of App Widget functionality.
 */
public class TimeDisplayWidget extends AppWidgetProvider {

    static void updateAppWidget(Context context, final AppWidgetManager appWidgetManager,
                                final int appWidgetId) {

        CharSequence widgetText = context.getString(R.string.appwidget_text);
        // Construct the RemoteViews object
        final RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.time_display_widget);


        final StudyHistoryDatabase database = StudyHistoryDatabase.getInstance(context);
        new AsyncTask<Void, Void, Void>(){
            @Override
            protected Void doInBackground(Void... voids) {
                List<StudyHistory> studyHistories = database.studyHistoryDao().fetchAllHistoryNoLive();

                long totalSec = 0;
                for(StudyHistory s: studyHistories){
                    if(Timestamp.now().toDate().getTime()/(1000*60*60*24)==s.getStartTime().getTime()/(1000*60*60*24))
                        totalSec += s.getTotalTime();
                }
                long hours = totalSec/(60*60);
                long minDef = totalSec%(60*60);
                String diplayTime = String.valueOf(hours) + " h ";
                if(minDef>=1800){
                    diplayTime+="30 min";
                }else {
                    diplayTime+="00 min";
                }
                views.setTextViewText(R.id.appwidget_text, diplayTime);
                appWidgetManager.updateAppWidget(appWidgetId, views);
                return null;
            }
        }.execute();
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

