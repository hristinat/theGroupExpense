package com.example.android.grouptripexpense.database;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "member")
public class MemberEntry {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String name;

    @Ignore
    public MemberEntry(String name) {
        this.name = name;
    }

    public MemberEntry(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
