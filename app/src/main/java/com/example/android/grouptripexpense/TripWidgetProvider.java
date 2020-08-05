package com.example.android.grouptripexpense;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

public class TripWidgetProvider extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId, String tripName, double totalExpenses) {

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.trip_widget);

        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
        views.setOnClickPendingIntent(R.id.widget, pendingIntent);
        views.setTextViewText(R.id.trip_name, tripName);
        views.setTextViewText(R.id.total_expenses, String.valueOf(totalExpenses));

        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        TripWidgetService.startActionUpdateTripWidget(context);
    }

    public static void updateTripWidgets(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds, String tripName, double totalExpenses) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId, tripName, totalExpenses);
        }
    }
}
