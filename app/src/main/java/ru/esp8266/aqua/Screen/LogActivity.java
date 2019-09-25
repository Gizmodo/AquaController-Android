package ru.esp8266.aqua.Screen;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import ru.esp8266.aqua.R;

public class LogActivity extends AppCompatActivity {
    private DatabaseReference RefLastOnline;
    private DatabaseReference RefUptime;
    private DatabaseReference RefBootHistory;
    private TextView txtLastOnline, txtUptime;
    private RecyclerView rvBootHistory;
    private ValueEventListener valueEventListener;
    private static final String TAG = "LogActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log);
        initUI();
        initFirebase();
    }

    private void initFirebase() {
        final FirebaseDatabase database = FirebaseDatabase.getInstance();

        RefLastOnline = database.getReference("LastOnline");
        valueEventListener = RefLastOnline.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                txtLastOnline.setText(dataSnapshot.getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w(TAG, "Failed to read value.", databaseError.toException());
            }
        });

        RefUptime = database.getReference("Uptime");
        valueEventListener = RefUptime.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                txtUptime.setText(dataSnapshot.getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w(TAG, "Failed to read value.", databaseError.toException());
            }
        });
    }

    private void initUI() {
        txtLastOnline = findViewById(R.id.txtLastOnline);
        txtUptime = findViewById(R.id.txtUptime);
        rvBootHistory = findViewById(R.id.rvBootHistory);
    }
}
