package com.example.android.grouptripexpense;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

import com.example.android.grouptripexpense.database.AppDatabase;
import com.example.android.grouptripexpense.database.ExpenseEntry;
import com.example.android.grouptripexpense.database.TripEntry;

import java.util.List;

public class TripWidgetService extends IntentService {
    public static final String ACTION_TRIP_INFO = "get_trip_info";

    public TripWidgetService() {
        super("TripWidgetService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            String action = intent.getAction();
            if (ACTION_TRIP_INFO.equals(action)) {
                handleActionTripInfo(this);
            }
        }
    }

    public static void startActionUpdateTripWidget(Context context) {
        Intent intent = new Intent(context, TripWidgetService.class);
        intent.setAction(ACTION_TRIP_INFO);
        context.startService(intent);
    }

    private void handleActionTripInfo(final Context context) {
        AppDatabase mDb = AppDatabase.getInstance(context);

        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                List<TripEntry> tripEntries = mDb.tripDao().loadAllTrips();
                List<ExpenseEntry> expenseEntries = mDb.expenseDao().loadAllExpenses();
                String tripName = tripEntries.size() == 0 ? "" : tripEntries.get(0).getName();

                double expensesTotal = 0;
                if (expenseEntries.size() != 0) {
                    expensesTotal = expenseEntries.stream().map(expense -> expense.getAmount()).reduce((a, b) -> a + b).get();
                }

                AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
                int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(context, TripWidgetProvider.class));
                TripWidgetProvider.updateTripWidgets(context, appWidgetManager, appWidgetIds, tripName, expensesTotal);
            }
        });
    }
}
