package ru.esp8266.aqua.Screen;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;
import java.util.Objects;

import ru.esp8266.aqua.MainActivity;
import ru.esp8266.aqua.R;

public class TemperatureActivity extends AppCompatActivity {
    private DatabaseReference Ref;
    private TextView txt_temperature_first, txt_temperature_second, txt_temperature_datetime;
    private ValueEventListener valueEventListener;
    private static final String TAG = "TemperatureActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temperature);
        initUI();
        initFirebase();
    }


    private void initFirebase() {
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        Ref = database.getReference("Temperature/Online");
        valueEventListener = Ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();
                txt_temperature_first.setText(Objects.requireNonNull(Objects.requireNonNull(map).get("temp1")).toString()+" °C");
                txt_temperature_second.setText(Objects.requireNonNull(map.get("temp2")).toString()+" °C");
                txt_temperature_datetime.setText(Objects.requireNonNull(map.get("DateTime")).toString());
                Log.d(TAG, "onDataChange: " + map);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w(TAG, "Failed to read value.", databaseError.toException());
            }
        });
    }

    private void initUI() {
        txt_temperature_first = findViewById(R.id.txt_temperature_first);
        txt_temperature_second = findViewById(R.id.txt_temperature_second);
        txt_temperature_datetime = findViewById(R.id.txt_temperature_datetime);
    }

    private void removeListener() {
        Ref.removeEventListener(valueEventListener);
    }

    @Override
    protected void onDestroy() {
        removeListener();
        finish();
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(TemperatureActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onStop() {
        removeListener();
        super.onStop();
    }
}
