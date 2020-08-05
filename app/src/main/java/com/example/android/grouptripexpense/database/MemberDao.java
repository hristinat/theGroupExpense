package com.example.android.grouptripexpense.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface MemberDao {

    @Query("SELECT * FROM member")
    LiveData<List<MemberEntry>> loadAllMembersLiveData();

    @Query("SELECT * FROM member")
   List<MemberEntry> loadAllMembers();

    @Insert
    void insertAllMembers(List<MemberEntry> members);

    @Query("Delete FROM member")
    void deleteAllMembers();
}
