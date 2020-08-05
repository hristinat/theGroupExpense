package com.example.android.grouptripexpense;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.android.grouptripexpense.database.AppDatabase;
import com.example.android.grouptripexpense.database.TripEntry;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.create_trip_button)
    Button mCreateTripButton;

    @BindView(R.id.trip_input)
    EditText mTripEditText;

    @BindView(R.id.ad_view)
    AdView mAdView;

    private AppDatabase mDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mDb = AppDatabase.getInstance(getApplicationContext());

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                if (mDb.tripDao().loadAllTrips().size() == 1 && mDb.memberDao().loadAllMembers().size() == 0) {
                    Intent expensesIntent = new Intent(MainActivity.this, AddMembersActivity.class);
                    startActivity(expensesIntent);
                    finish();
                } else {
                    if (mDb.tripDao().loadAllTrips().size() == 1) {
                        Intent expensesIntent = new Intent(MainActivity.this, ExpensesActivity.class);
                        startActivity(expensesIntent);
                        finish();
                    }
                }
            }
        });
    }

    @OnClick(R.id.create_trip_button)
    public void onCreateButtonClicked() {
        String tripName = mTripEditText.getText().toString().trim();
        if (tripName.isEmpty()) {
            Toast.makeText(this, "Please enter trip name.", Toast.LENGTH_LONG).show();
            return;
        }

        final TripEntry tripEntry = new TripEntry(tripName);
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                mDb.tripDao().insertTrip(tripEntry);
                finish();
            }
        });

        Intent addTripIntent = new Intent(this, AddMembersActivity.class);
        startActivity(addTripIntent);
    }
}
