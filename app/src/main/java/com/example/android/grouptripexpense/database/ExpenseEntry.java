package com.example.android.grouptripexpense.database;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "expense", foreignKeys = @ForeignKey(entity = MemberEntry.class,
        parentColumns = "id",
        childColumns = "memberId"), indices = @Index(value = "memberId"))
public class ExpenseEntry {


    @PrimaryKey(autoGenerate = true)
    private int id;

    private String type;

    private double amount;

    private int memberId;

    @Ignore
    public ExpenseEntry(String type, double amount, int memberId) {
        this.type = type;
        this.amount = amount;
        this.memberId = memberId;
    }

    public ExpenseEntry(int id, String type, double amount, int memberId) {
        this.id = id;
        this.type = type;
        this.amount = amount;
        this.memberId = memberId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public int getMemberId() {
        return memberId;
    }
}
