package com.example.android.grouptripexpense;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android.grouptripexpense.database.AppDatabase;
import com.example.android.grouptripexpense.database.ExpenseEntry;
import com.example.android.grouptripexpense.database.MemberEntry;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ExpenseTabFragment extends Fragment {

    @BindView(R.id.add_fab)
    FloatingActionButton mAddFab;

    @BindView(R.id.expenses)
    RecyclerView mExpensesList;

    @BindView(R.id.no_expenses_yet)
    TextView mNoExpensesTextView;

    private AppDatabase mDb;
    private ExpenseAdapter mExpenseAdapter;
    private List<MemberEntry> members = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.expenses_tab_fragment, container, false);
        ButterKnife.bind(this, view);
        mDb = AppDatabase.getInstance(getContext().getApplicationContext());
        LinearLayoutManager linearLayoutManager
                = new LinearLayoutManager(getContext());
        mExpensesList.setLayoutManager(linearLayoutManager);

        mExpenseAdapter = new ExpenseAdapter();
        mExpensesList.setHasFixedSize(true);
        mExpensesList.setAdapter(mExpenseAdapter);

        MainViewModel viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        viewModel.getMembers().observe(getViewLifecycleOwner(), new Observer<List<MemberEntry>>() {
            @Override
            public void onChanged(List<MemberEntry> memberEntries) {
                members = memberEntries;
            }
        });

        viewModel.getExpenses().observe(getViewLifecycleOwner(), new Observer<List<ExpenseEntry>>() {
            @Override
            public void onChanged(List<ExpenseEntry> expenseEntries) {
                if (expenseEntries.size() == 0) {
                    mNoExpensesTextView.setVisibility(View.VISIBLE);
                } else {
                    mNoExpensesTextView.setVisibility(View.INVISIBLE);
                }

                List<Expense> expenses = new ArrayList<>();

                for (ExpenseEntry expenseEntry : expenseEntries) {
                    MemberEntry memberEntry;
                    if (members.size() == 0) {
                        return;
                    } else {
                        memberEntry = members.stream().filter(mE -> mE.getId() == expenseEntry.getMemberId()).findFirst().get();
                    }
                    expenses.add(new Expense(expenseEntry.getType(), expenseEntry.getAmount(), memberEntry.getName()));
                }

                mExpenseAdapter.setExpenses(expenses);
            }
        });

        return view;
    }

    @OnClick(R.id.add_fab)
    public void onFabClick() {
        Intent expensesIntent = new Intent(getContext(), AddExpenseActivity.class);
        startActivity(expensesIntent);
    }
}
