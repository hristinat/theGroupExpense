package com.example.android.grouptripexpense;

public class Expense {

    private String type;

    private double amount;

    private String memberName;

    public Expense(String type, double amount, String memberName) {
        this.type = type;
        this.amount = amount;
        this.memberName = memberName;
    }

    public String getType() {
        return type;
    }

    public double getAmount() {
        return amount;
    }

    public String getMemberName() {
        return memberName;
    }
}
