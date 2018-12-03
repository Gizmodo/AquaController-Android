package ru.esp8266.aqua;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;
import java.util.Objects;


public class TempFragment extends Fragment {

    private TextView temp1, temp2, dt;
    private FirebaseDatabase database;
    private DatabaseReference myRef;

    public TempFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_temp, container, false);
        temp1 = view.findViewById(R.id.temp1);
        temp2 = view.findViewById(R.id.temp2);
        dt = view.findViewById(R.id.dt);
        return view;
    }

    ValueEventListener valueEventListener;
    private static final String TAG = "TempFragment";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Temperature/Online");
        valueEventListener = myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();
                temp1.setText(Objects.requireNonNull(Objects.requireNonNull(map).get("Device1")).toString());
                temp2.setText(Objects.requireNonNull(map.get("Device2")).toString());
                dt.setText(Objects.requireNonNull(map.get("DateTime")).toString());
                Log.d(TAG, "onDataChange: " + map);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }

    @Override
    public void onDestroy() {
        myRef.removeEventListener(valueEventListener);
        super.onDestroy();

    }
}
