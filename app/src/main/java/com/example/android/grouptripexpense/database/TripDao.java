package com.example.android.grouptripexpense.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface TripDao {

    @Query("SELECT * FROM trip")
    LiveData<List<TripEntry>>loadAllTripsLiveData();

    @Query("SELECT * FROM trip")
    List<TripEntry> loadAllTrips();

    @Insert
    void insertTrip(TripEntry tripEntry);

    @Query("Delete FROM trip")
    void deleteAllTrips();
}
