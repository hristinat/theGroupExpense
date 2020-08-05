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

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SummaryTabFragment extends Fragment {

    private AppDatabase mDb;
    private List<ExpenseEntry> mExpenses;

    @BindView(R.id.expenses_total)
    TextView mExpensesTotal;

    @BindView(R.id.divided_amount)
    TextView mDividedAmount;

    @BindView(R.id.member_divided_amount)
    RecyclerView mMemberDividedAmountList;

    List<MemberEntry> members = new ArrayList<>();

    private MemberDividedAmountAdapter mMemberDividedAmountAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.summary_tab_fragment, container, false);
        ButterKnife.bind(this, view);

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
                mExpenses = expenseEntries;
                double expensesTotal = 0;
                if (mExpenses.size() != 0) {
                    expensesTotal = mExpenses.stream().map(expense -> expense.getAmount()).reduce((a, b) -> a + b).get();
                }
                mExpensesTotal.setText(String.valueOf(expensesTotal));
                int membersCount = members.size();

                double dividedAmount = membersCount != 0 ? expensesTotal / membersCount : 0;
                BigDecimal dividedAmountBd = BigDecimal.valueOf(dividedAmount).setScale(2, RoundingMode.HALF_UP);
                mDividedAmount.setText(dividedAmountBd.toString());

                List<MemberDividedAmount> memberDividedAmounts = new ArrayList<>();
                for (ExpenseEntry expense: mExpenses) {
                    MemberEntry member = members.stream().filter(memberEntry -> memberEntry.getId() == expense.getMemberId()).findFirst().get();

                    Optional<MemberDividedAmount> foundMemberDividedAmountOptional = memberDividedAmounts.stream().filter(memberDividedAmount -> memberDividedAmount.getMemberName().equals(member.getName())).findFirst();
                    if (foundMemberDividedAmountOptional.isPresent()) {
                        MemberDividedAmount foundMemberDividedAmount = foundMemberDividedAmountOptional.get();
                        foundMemberDividedAmount.setPaid(foundMemberDividedAmount.getPaid() + expense.getAmount());
                        double toPay = dividedAmountBd.doubleValue() - foundMemberDividedAmount.getPaid();
                        foundMemberDividedAmount.setToPay(toPay < 0 ? 0 : BigDecimal.valueOf(toPay).setScale(2, RoundingMode.HALF_UP).doubleValue());
                        double toReceive = foundMemberDividedAmount.getPaid() - dividedAmountBd.doubleValue();
                        foundMemberDividedAmount.setToReceive(toReceive < 0 ? 0 : BigDecimal.valueOf(toReceive).setScale(2, RoundingMode.HALF_UP).doubleValue());
                    } else {
                        MemberDividedAmount memberDividedAmount = new MemberDividedAmount();
                        memberDividedAmount.setMemberName(member.getName());
                        memberDividedAmount.setPaid(expense.getAmount());
                        double toPay = dividedAmountBd.doubleValue() - memberDividedAmount.getPaid();
                        memberDividedAmount.setToPay(toPay < 0 ? 0 : BigDecimal.valueOf(toPay).setScale(2, RoundingMode.HALF_UP).doubleValue());
                        double toReceive = memberDividedAmount.getPaid() - dividedAmountBd.doubleValue();
                        memberDividedAmount.setToReceive(toReceive < 0 ? 0 : BigDecimal.valueOf(toReceive).setScale(2, RoundingMode.HALF_UP).doubleValue());
                        memberDividedAmounts.add(memberDividedAmount);
                    }
                }

                for (MemberEntry memberEntry: members) {
                    String memberName = memberEntry.getName();
                    Optional<MemberDividedAmount> memberDividedAmountOptional = memberDividedAmounts.stream().filter(memberDividedAmount -> memberDividedAmount.getMemberName().equals(memberName)).findFirst();
                    if (!memberDividedAmountOptional.isPresent()) {
                        MemberDividedAmount memberDividedAmount = new MemberDividedAmount(memberName, 0, dividedAmountBd.doubleValue(), 0);
                        memberDividedAmounts.add(memberDividedAmount);
                    }
                }

                LinearLayoutManager linearLayoutManager
                        = new LinearLayoutManager(getContext());
                mMemberDividedAmountList.setLayoutManager(linearLayoutManager);

                mMemberDividedAmountAdapter = new MemberDividedAmountAdapter();
                mMemberDividedAmountList.setHasFixedSize(true);
                mMemberDividedAmountList.setAdapter(mMemberDividedAmountAdapter);
                mMemberDividedAmountAdapter.setmMemberDivideAmounts(memberDividedAmounts);
            }
        });

        return view;
    }

    @OnClick(R.id.finish_trip)
    void onFinishButtonClick() {
        mDb = AppDatabase.getInstance(getContext().getApplicationContext());
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                mDb.tripDao().deleteAllTrips();
                mDb.expenseDao().deleteAllExpenses();
                mDb.memberDao().deleteAllMembers();
            }
        });
        Intent mainIntent = new Intent(getContext(), MainActivity.class);
        startActivity(mainIntent);
    }
}
