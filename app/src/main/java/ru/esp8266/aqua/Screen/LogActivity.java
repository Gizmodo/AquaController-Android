package ru.esp8266.aqua.Screen;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import ru.esp8266.aqua.Adapter.BootAdapter;
import ru.esp8266.aqua.R;

public class LogActivity extends AppCompatActivity {
    private DatabaseReference RefLastOnline;
    private DatabaseReference RefUptime;
    private DatabaseReference RefBootHistory;
    private TextView txtLastOnline, txtUptime;
    private RecyclerView rvBootHistory;
    private ValueEventListener valueEventListener;
    private static final String TAG = "LogActivity";
    final int ITEM_LOAD_COUNT = 21;
    int total_item = 0, last_visible_item;
    BootAdapter adapter;
    boolean isLoading = false, isMaxData = false;
    String last_node = "", last_key = "";
    private LinearLayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log);
        layoutManager = new LinearLayoutManager(this);
        initUI();
        initFirebase();
        getItems();

        rvBootHistory.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                total_item = layoutManager.getItemCount();
                last_visible_item = layoutManager.findLastVisibleItemPosition();

                if (!isLoading && total_item <= (last_visible_item + ITEM_LOAD_COUNT)) {
                    getItems();
                    isLoading = true;
                }
            }
        });
    }

    private void getItems() {
        if (!isMaxData) {
            Query query;
            if (TextUtils.isEmpty(last_node)) {
                query = FirebaseDatabase.getInstance().getReference("BootHistory")
                      //  .child("BootHistory")
                        .orderByKey()
                        .limitToFirst(ITEM_LOAD_COUNT);
            } else {
                query = FirebaseDatabase.getInstance().getReference("BootHistory")
                       // .child()
                        .orderByKey()
                        .startAt(last_node)
                        .limitToFirst(ITEM_LOAD_COUNT);
            }
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.hasChildren()) {
                        List<String> newBootItems = new ArrayList<>();
                        for (DataSnapshot bootItemsSnapshot : dataSnapshot.getChildren()) {
                            newBootItems.add(bootItemsSnapshot.getValue(String.class));
                        }

                        last_node = newBootItems.get(newBootItems.size() - 1);

                        if (!last_node.equals(last_key)) {
                            newBootItems.remove(newBootItems.size() - 1);
                        } else {
                            last_node = "end";
                        }

                        adapter.addAll(newBootItems);
                        isLoading = false;
                    } else {
                        isLoading = false;
                        isMaxData = true;
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    isLoading = false;
                }
            });
        }
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

        Query getLastKey = FirebaseDatabase.getInstance().getReference("BootHistory")
               // .child("BootHistory")
                .orderByKey()
                .limitToLast(1);
        getLastKey.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot lastKey : dataSnapshot.getChildren()) {
                    last_key = lastKey.getKey();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(LogActivity.this, "Cannot get last key", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initUI() {
        txtLastOnline = findViewById(R.id.txtLastOnline);
        txtUptime = findViewById(R.id.txtUptime);
        rvBootHistory = findViewById(R.id.rvBootHistory);

        rvBootHistory.setLayoutManager(layoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(rvBootHistory.getContext(), layoutManager.getOrientation());
        rvBootHistory.addItemDecoration(dividerItemDecoration);

        adapter = new BootAdapter(this);
        rvBootHistory.setAdapter(adapter);
    }
}
