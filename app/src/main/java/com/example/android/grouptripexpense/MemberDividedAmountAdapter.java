package com.example.android.grouptripexpense;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MemberDividedAmountAdapter extends RecyclerView.Adapter<MemberDividedAmountAdapter.MemberDivideAmountViewHolder> {

    private List<MemberDividedAmount> mMemberDivideAmounts;

    public MemberDividedAmountAdapter() {
    }

    @NonNull
    @Override
    public MemberDivideAmountViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.member_divided_amount;
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(layoutIdForListItem, parent, false);
        return new MemberDivideAmountViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MemberDivideAmountViewHolder holder, int position) {
        MemberDividedAmount memberDividedAmount = mMemberDivideAmounts.get(position);
        holder.mMemberName.setText(memberDividedAmount.getMemberName());
        holder.mPaid.setText(String.valueOf(memberDividedAmount.getPaid()));
        holder.mToPay.setText(String.valueOf(memberDividedAmount.getToPay()));
        holder.mToReceive.setText(String.valueOf(memberDividedAmount.getToReceive()));
    }

    @Override
    public int getItemCount() {
        return null != mMemberDivideAmounts ? mMemberDivideAmounts.size() : 0;
    }

    public void setmMemberDivideAmounts(List<MemberDividedAmount> memberDivideAmounts) {
        this.mMemberDivideAmounts = memberDivideAmounts;
        notifyDataSetChanged();
    }

    public class MemberDivideAmountViewHolder extends RecyclerView.ViewHolder {
        TextView mMemberName;
        TextView mPaid;
        TextView mToPay;
        TextView mToReceive;

        public MemberDivideAmountViewHolder(@NonNull View itemView) {
            super(itemView);
            mMemberName = itemView.findViewById(R.id.member_name);
            mPaid = itemView.findViewById(R.id.paid);
            mToPay = itemView.findViewById(R.id.to_pay);
            mToReceive = itemView.findViewById(R.id.to_receive);
        }
    }
}

