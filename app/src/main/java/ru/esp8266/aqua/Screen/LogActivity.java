package ru.esp8266.aqua.Screen;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import ru.esp8266.aqua.MainActivity;
import ru.esp8266.aqua.R;
import ru.esp8266.aqua.ViewHolder.BootVH;

public class LogActivity extends AppCompatActivity {
    private DatabaseReference RefLastOnline;
    private DatabaseReference RefUptime;
    private DatabaseReference RefBootHistory;
    private TextView txtLastOnline, txtUptime;
    private RecyclerView rvBootHistory;
    private ValueEventListener lastOnlineValueEventListener, uptimeValueEventListener, bootHistoryValueEventListener;
    private static final String TAG = "LogActivity";
    private FirebaseRecyclerAdapter<String, BootVH> mBootAdapter;

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
        lastOnlineValueEventListener = RefLastOnline.addValueEventListener(new ValueEventListener() {
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
        uptimeValueEventListener = RefUptime.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                txtUptime.setText(dataSnapshot.getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w(TAG, "Failed to read value.", databaseError.toException());
            }
        });

        RefBootHistory = database.getReference("BootHistory");
        bootHistoryValueEventListener = RefBootHistory.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Query query = RefBootHistory.orderByKey();
                FirebaseRecyclerOptions<String> listOptions =
                        new FirebaseRecyclerOptions.Builder<String>()
                                .setQuery(query, String.class)
                                .build();
                mBootAdapter = new FirebaseRecyclerAdapter<String, BootVH>(listOptions) {
                    @Override
                    protected void onBindViewHolder(@NonNull BootVH holder, int i, @NonNull String s) {
                        holder.boot_text.setText(s);
                    }

                    @NonNull
                    @Override
                    public BootVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View itemView = LayoutInflater.from(parent.getContext())
                                .inflate(R.layout.boot_item_layout, parent, false);
                        return new BootVH(itemView);
                    }
                };
                mBootAdapter.startListening();
                rvBootHistory.setAdapter(mBootAdapter);
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

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);

        rvBootHistory = findViewById(R.id.rvBootHistory);
        rvBootHistory.setHasFixedSize(true);
        rvBootHistory.setLayoutManager(layoutManager);
        RecyclerView.ItemAnimator animator = rvBootHistory.getItemAnimator();
        if (animator instanceof SimpleItemAnimator) {
            ((SimpleItemAnimator) animator).setSupportsChangeAnimations(false);
        }
    }

    private void removeListeners() {
        RefBootHistory.removeEventListener(bootHistoryValueEventListener);
        RefLastOnline.removeEventListener(lastOnlineValueEventListener);
        RefUptime.removeEventListener(uptimeValueEventListener);
    }

    @Override
    protected void onDestroy() {
        if (mBootAdapter != null) {
            mBootAdapter.stopListening();
        }
        removeListeners();
        finish();
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(LogActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mBootAdapter != null) {
            mBootAdapter.startListening();
        }
    }

    @Override
    protected void onStop() {
        removeListeners();
        super.onStop();
    }

}
