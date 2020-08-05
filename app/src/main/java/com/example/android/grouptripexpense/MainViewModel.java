package com.example.android.grouptripexpense;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.android.grouptripexpense.database.AppDatabase;
import com.example.android.grouptripexpense.database.ExpenseEntry;
import com.example.android.grouptripexpense.database.MemberEntry;
import com.example.android.grouptripexpense.database.TripEntry;

import java.util.List;

public class MainViewModel extends AndroidViewModel {

    LiveData<List<ExpenseEntry>> expenses;
    LiveData<List<MemberEntry>> members;
    LiveData<List<TripEntry>> trips;

    public MainViewModel(@NonNull Application application) {
        super(application);
        AppDatabase database = AppDatabase.getInstance(this.getApplication());
        expenses = database.expenseDao().loadAllExpensesLivedata();
        members = database.memberDao().loadAllMembersLiveData();
        trips = database.tripDao().loadAllTripsLiveData();
    }

    public LiveData<List<ExpenseEntry>> getExpenses() {
        return expenses;
    }

    public LiveData<List<MemberEntry>> getMembers() {
        return members;
    }

    public LiveData<List<TripEntry>> getTrips() {
        return trips;
    }
}
