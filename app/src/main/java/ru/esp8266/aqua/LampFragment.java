package ru.esp8266.aqua;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import ru.esp8266.aqua.Model.LampItem;
import ru.esp8266.aqua.ViewHolder.LampViewHolder;


public class LampFragment extends Fragment {


    private DatabaseReference mLampsRef;
    private FirebaseRecyclerAdapter<LampItem, LampViewHolder> mLampsAdapter;
    private RecyclerView mRecyclerView;

    public LampFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_lamp, container, false);

        //final FragmentActivity c = getActivity();
        mRecyclerView = view.findViewById(R.id.rv);
        mRecyclerView.setHasFixedSize(true);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));





        return view;
    }

    ValueEventListener valueEventListener;
    private static final String TAG = "1";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initFirebase();

        valueEventListener = mLampsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    Query query = mLampsRef.orderByKey();
                    FirebaseRecyclerOptions<LampItem> listOptions =
                            new FirebaseRecyclerOptions.Builder<LampItem>()
                                    .setQuery(query, LampItem.class)
                                    .build();

                    mLampsAdapter = new FirebaseRecyclerAdapter<LampItem, LampViewHolder>(listOptions) {
                        @Override
                        protected void onBindViewHolder(@NonNull LampViewHolder holder, int i, @NonNull LampItem model) {
                            holder.txt_position.setText(model.getPosition());
                            holder.txt_on.setText(model.getOn());
                            holder.txt_off.setText(model.getOff());
                            holder.txt_state.setText(model.getState().toString());

                            holder.edt_pin.setText(model.getPin().toString());
                            holder.sw_enable.setChecked(model.getEnabled());
                            holder.btn_on.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Log.d(TAG, "onClick: On");
                                }
                            });
                            holder.btn_off.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Log.d(TAG, "onClick: Off");
                                }
                            });

                            holder.setItemClickListener(
                                    this::onClick);
                        }

                        private void onClick(View view, int position1, boolean isLongClick) {
                            Log.d(TAG, "onClick: " + position1);
                            /*String key = mLampsAdapter.getItem(position1).getId();
                            Intent installmentCardDetailIntent = new Intent(Cards.this,
                                    CardInstallmentDetail.class);
                            installmentCardDetailIntent.putExtra("installmentCardId", key);
                            startActivity(installmentCardDetailIntent);*/
                        }

                        @NonNull
                        @Override
                        public LampViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
                            View itemView = LayoutInflater.from(viewGroup.getContext())
                                    .inflate(R.layout.lamp, viewGroup, false);
                            return new LampViewHolder(itemView);

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

    @Override
    public void onResume() {
        super.onResume();
        if (mLampsAdapter != null) {
            mLampsAdapter.startListening();
        }
    }

    private void initFirebase() {
        //Firebase Init
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        mLampsRef = database.getReference("Light");
    }

    @Override
    public void onDestroy() {
        if (mLampsAdapter != null) {
            mLampsAdapter.stopListening();
        }
        mLampsRef.removeEventListener(valueEventListener);
        super.onDestroy();

    }

}
