package com.example.android.grouptripexpense;

public class MemberDividedAmount {

    private String memberName;

    private double paid;

    private double toPay;

    private double toReceive;

    public MemberDividedAmount() {
    }

    public MemberDividedAmount(String memberName, double paid, double toPay, double toReceive) {
        this.memberName = memberName;
        this.paid = paid;
        this.toPay = toPay;
        this.toReceive = toReceive;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public double getPaid() {
        return paid;
    }

    public void setPaid(double paid) {
        this.paid = paid;
    }

    public double getToPay() {
        return toPay;
    }

    public void setToPay(double toPay) {
        this.toPay = toPay;
    }

    public double getToReceive() {
        return toReceive;
    }

    public void setToReceive(double toReceive) {
        this.toReceive = toReceive;
    }
}
