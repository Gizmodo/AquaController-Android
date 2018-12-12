package ru.esp8266.aqua.Screen;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
import ru.esp8266.aqua.Model.LampItem;
import ru.esp8266.aqua.R;
import ru.esp8266.aqua.ViewHolder.LampVH;

public class LampActivity extends AppCompatActivity {
    private static final String TAG = "LampActivity";

    private DatabaseReference Ref;
    private ValueEventListener valueEventListener;
    private RecyclerView mRecyclerView;
    private FirebaseRecyclerAdapter<LampItem, LampVH> mLampsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lamp);
        initUI();
        initFirebase();
    }

    private void initFirebase() {
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        Ref = database.getReference("Light");
        valueEventListener = Ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    Query query = Ref.orderByKey();
                    FirebaseRecyclerOptions<LampItem> listOptions =
                            new FirebaseRecyclerOptions.Builder<LampItem>()
                                    .setQuery(query, LampItem.class)
                                    .build();
                    Log.d(TAG, "onDataChange: ");
                    mLampsAdapter = new FirebaseRecyclerAdapter<LampItem, LampVH>(listOptions) {
                        @Override
                        protected void onBindViewHolder(@NonNull LampVH holder, int i, @NonNull LampItem model) {
                            holder.txt_lamp_title.setText(model.getPosition());
                            holder.txt_lamp_from.setText(model.getOn());
                            holder.txt_lamp_to.setText(model.getOff());

                            if (model.getState()) {
                                holder.img_lamp.setImageResource(R.drawable.ic_lamp_on);
                            } else {
                                holder.img_lamp.setImageResource(R.drawable.ic_lamp_off);
                            }

                            holder.setItemClickListener(
                                    this::onClick);
                        }

                        private void onClick(View view, int position1, boolean isLongClick) {
                            Log.d(TAG, "onClick: " + position1);
                            Log.d(TAG, "onClick: " + mLampsAdapter.getRef(position1).getKey());

                            String key = mLampsAdapter.getRef(position1).getKey(); //Light

                            Intent intent = new Intent(LampActivity.this, LampDetailActivity.class);
                            intent.putExtra("key",key);
                            startActivity(intent);
                        }

                        @NonNull
                        @Override
                        public LampVH onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
                            View itemView = LayoutInflater.from(viewGroup.getContext())
                                    .inflate(R.layout.lamp_item, viewGroup, false);
                            return new LampVH(itemView);

                        }
                    };
                    mLampsAdapter.startListening();
                    mRecyclerView.setAdapter(mLampsAdapter);
                } else {
                    if (mLampsAdapter != null) {
                        mLampsAdapter.stopListening();
                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }

    private void initUI() {
        mRecyclerView = findViewById(R.id.rv_lamp);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        RecyclerView.ItemAnimator animator = mRecyclerView.getItemAnimator();
        if (animator instanceof SimpleItemAnimator) {
            ((SimpleItemAnimator) animator).setSupportsChangeAnimations(false);
        }
    }

    private void removeListener() {
        Ref.removeEventListener(valueEventListener);
    }

    @Override
    protected void onDestroy() {
        if (mLampsAdapter != null) {
            mLampsAdapter.stopListening();
        }
        removeListener();
        finish();
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(LampActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mLampsAdapter != null) {
            mLampsAdapter.startListening();
        }
    }

    @Override
    protected void onStop() {
        removeListener();
        super.onStop();
    }
}
