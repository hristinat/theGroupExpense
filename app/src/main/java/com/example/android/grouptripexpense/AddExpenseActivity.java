package com.example.android.grouptripexpense;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.android.grouptripexpense.database.AppDatabase;
import com.example.android.grouptripexpense.database.ExpenseEntry;
import com.example.android.grouptripexpense.database.MemberEntry;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddExpenseActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private List<String> mMembersNames = new ArrayList<>();
    private List<MemberEntry> mMembers;
    private AppDatabase mDb;

    @BindView(R.id.who_paid_spinner)
    Spinner mWhoPaidSpinner;

    @BindView(R.id.expense_type_input)
    EditText mExpenseTypeEditText;

    @BindView(R.id.amount_input)
    EditText mAmountEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expense);
        ButterKnife.bind(this);

        mDb = AppDatabase.getInstance(getApplicationContext());

        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                mMembers = mDb.memberDao().loadAllMembers();
                for (MemberEntry member: mMembers) {
                    mMembersNames.add(member.getName());
                }
                ArrayAdapter<String> adapter =
                        new ArrayAdapter<>(AddExpenseActivity.this, android.R.layout.simple_spinner_item, mMembersNames);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                mWhoPaidSpinner.setAdapter(adapter);
                mWhoPaidSpinner.setOnItemSelectedListener(AddExpenseActivity.this);
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @OnClick(R.id.add_expense_button)
    public void onAddExpenseButtonClick() {
        String selectedMember = mWhoPaidSpinner.getSelectedItem().toString();
        int selectedMemberId = mMembers.stream().filter(member -> member.getName().equals(selectedMember)).findFirst().get().getId();
        String expenseType = mExpenseTypeEditText.getText().toString().trim();
        String amount = mAmountEditText.getText().toString().trim();

        if (expenseType.isEmpty()) {
            Toast.makeText(this, "Please add expense type.", Toast.LENGTH_LONG).show();
            return;
        }

        if (amount.isEmpty()) {
            Toast.makeText(this, "Please add amount.", Toast.LENGTH_LONG).show();
            return;
        }

        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                mDb.expenseDao().insertExpense(new ExpenseEntry(expenseType, Double.parseDouble(amount), selectedMemberId));
                finish();
            }
        });
        TripWidgetService.startActionUpdateTripWidget(this);
    }
}
