package com.example.android.grouptripexpense;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ExpenseAdapter extends RecyclerView.Adapter<ExpenseAdapter.ExpenseViewHolder> {

    private List<Expense> expenses;

    public ExpenseAdapter() {
    }

    @NonNull
    @Override
    public ExpenseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.expense;
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(layoutIdForListItem, parent, false);
        return new ExpenseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExpenseViewHolder holder, int position) {
        Expense expense = expenses.get(position);
        holder.mExpenseType.setText(expense.getType());
        holder.mAmount.setText(String.valueOf(expense.getAmount()));
        holder.mPaidBy.setText(expense.getMemberName());
    }

    @Override
    public int getItemCount() {
        return null != expenses ? expenses.size() : 0;
    }

    public void setExpenses(List<Expense> expenses) {
        this.expenses = expenses;
        notifyDataSetChanged();
    }

    public class ExpenseViewHolder extends RecyclerView.ViewHolder {
        TextView mExpenseType;
        TextView mAmount;
        TextView mPaidBy;

        public ExpenseViewHolder(@NonNull View itemView) {
            super(itemView);
            mExpenseType = itemView.findViewById(R.id.expense_type);
            mAmount = itemView.findViewById(R.id.amount);
            mPaidBy = itemView.findViewById(R.id.paid_by);
        }
    }
}
