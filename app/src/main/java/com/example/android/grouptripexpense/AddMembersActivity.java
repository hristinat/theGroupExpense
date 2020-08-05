package com.example.android.grouptripexpense;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.android.grouptripexpense.database.AppDatabase;
import com.example.android.grouptripexpense.database.MemberEntry;
import com.example.android.grouptripexpense.database.TripEntry;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddMembersActivity extends AppCompatActivity {

    private static final String MEMBERS = "members";

    private AppDatabase mDb;

    @BindView(R.id.trip_name)
    TextView mTripName;

    @BindView(R.id.member_input)
    EditText mEditText;

    EditText mLastAddedEditText;

    @BindView(R.id.add_member_button)
    Button mAddMemberButton;

    @BindView(R.id.members_layout)
    RelativeLayout mMembersLayout;

    List<MemberEntry> mMembers = new ArrayList<>();

    List<String> mMembersString = new ArrayList<>();

    public int numberOfLines = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_members);
        ButterKnife.bind(this);

        mDb = AppDatabase.getInstance(getApplicationContext());

        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(MEMBERS)) {
                mMembersString = savedInstanceState
                        .getStringArrayList(MEMBERS);
                for (String name : mMembersString) {
                    mMembers.add(new MemberEntry(name));
                    addEditText();
                }
            }
        }

        MainViewModel viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        viewModel.getTrips().observe(this, new Observer<List<TripEntry>>() {
            @Override
            public void onChanged(List<TripEntry> tripEntries) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mTripName.setText(tripEntries.get(0).getName());
                    }
                });
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                saveMembers();
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putStringArrayList(MEMBERS, (ArrayList<String>) mMembersString);
    }

    @OnClick(R.id.add_member_button)
    public void onAddButtonClicked() {
        String memberName;
        if (numberOfLines == 1) {
            memberName = mEditText.getText().toString().trim();
        } else {
            memberName = mLastAddedEditText.getText().toString().trim();
        }
        if (memberName.isEmpty()) {
            Toast.makeText(this, "Please enter member name.", Toast.LENGTH_LONG).show();
            return;
        }
        mMembers.add(new MemberEntry(memberName));
        mMembersString.add(memberName);
        addEditText();
    }

    public void addEditText() {
        if (numberOfLines == 1) {
            mEditText.setEnabled(false);
        } else {
            mLastAddedEditText.setEnabled(false);
        }
        mLastAddedEditText = new EditText(this);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        if (numberOfLines == 1) {
            layoutParams.addRule(RelativeLayout.BELOW, R.id.member_input);
        } else {
            layoutParams.addRule(RelativeLayout.BELOW, numberOfLines);
        }
        mLastAddedEditText.setLayoutParams(layoutParams);
        mLastAddedEditText.setId(numberOfLines + 1);
        mMembersLayout.addView(mLastAddedEditText, mMembersLayout.getChildCount() - 1);
        RelativeLayout.LayoutParams btnLаyoutParams = (RelativeLayout.LayoutParams) mAddMemberButton.getLayoutParams();
        btnLаyoutParams.addRule(RelativeLayout.BELOW, numberOfLines + 1);
        mAddMemberButton.setLayoutParams(btnLаyoutParams);
        numberOfLines++;
    }

    public void saveMembers() {
        if (mMembers.size() == 0) {
            Toast.makeText(this, "Please add member.", Toast.LENGTH_LONG).show();
            return;
        }

        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                mDb.memberDao().insertAllMembers(mMembers);
                finish();
            }
        });

        Intent expensesIntent = new Intent(this, ExpensesActivity.class);
        startActivity(expensesIntent);
    }
}
