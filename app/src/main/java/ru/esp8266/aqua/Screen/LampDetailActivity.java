package ru.esp8266.aqua.Screen;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import ru.esp8266.aqua.Model.LampItem;
import ru.esp8266.aqua.R;

public class LampDetailActivity extends AppCompatActivity {
    private String key;
    private static final String TAG = "LampDetailActivity";

    private DatabaseReference Ref;

    private TextView txt_lamp_position;
    private Button btn_save;

    private EditText edt_time_on;
    private EditText edt_time_off;
    private EditText edt_pin;

    private ImageView img_state;

    private Switch sw_enabled;
    private Boolean state;
    private String position;
    private DatabaseReference RefUpdate;
    TimePickerDialog tpd_on, tpd_off;
    private int mHourOn, mMinuteOn, mHourOff, mMinuteOff;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lamp_detail);

        initUI();
        initFirebase();
        initListeners();
    }

    private void initFirebase() {
        //Firebase Init
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        Ref = database.getReference("Light/" + key);
        RefUpdate = database.getReference("UpdateSettings");
    }

    private void initListeners() {
        TimePickerDialog.OnTimeSetListener myCallBack_On = (view, hourOfDay, minute) -> edt_time_on.setText(hourOfDay + ":" + minute);
        TimePickerDialog.OnTimeSetListener myCallBack_Off = (view, hourOfDay, minute) -> edt_time_off.setText(hourOfDay + ":" + minute);
        tpd_on = new TimePickerDialog(this, myCallBack_On, mHourOn, mMinuteOn, true);
        tpd_off = new TimePickerDialog(this, myCallBack_Off, mHourOff, mMinuteOff, true);

        //Firebase Listener
        Ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onCancelled(@NonNull final DatabaseError databaseError) {
                Toast.makeText(LampDetailActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {
                try {
                    LampItem lampItem = dataSnapshot.getValue(LampItem.class);

                    txt_lamp_position.setText(lampItem != null ? lampItem.getPosition() : "Нет позиции");
                    position = (lampItem != null ? lampItem.getPosition() : "Нет позиции");
                    edt_time_on.setText(lampItem != null ? lampItem.getOn() : "00:00");
                    edt_time_off.setText(lampItem != null ? lampItem.getOff() : "00:00");
                    edt_pin.setText(lampItem != null ? lampItem.getPin().toString() : "0");
                    sw_enabled.setChecked(lampItem != null ? lampItem.getEnabled() : false);
                    if (lampItem != null ? lampItem.getState() : false) {
                        img_state.setImageResource(R.drawable.ic_lamp_on);
                        state = lampItem.getState();
                    } else {
                        img_state.setImageResource(R.drawable.ic_lamp_off);
                        state = lampItem.getState();
                    }

                    img_state.setOnClickListener(v -> {
                        img_state.setImageResource(state ? R.drawable.ic_lamp_off : R.drawable.ic_lamp_on);
                        state = !state;
                    });

                    btn_save.setOnClickListener(v -> {
                        LampItem item = new LampItem();
                        item.setOn(edt_time_on.getText().toString());
                        item.setOff(edt_time_off.getText().toString());
                        item.setPin(Integer.parseInt(edt_pin.getText().toString()));
                        item.setState(state);
                        item.setPosition(position);
                        item.setEnabled(sw_enabled.isChecked());
                        Ref.setValue(item);
                        RefUpdate.setValue(true);
                    });


                } catch (Exception e) {
                    String msg = "Ошибка при загрузке лампы " + key;
                    Log.e(TAG, msg, e);
                    Toast.makeText(LampDetailActivity.this, msg, Toast.LENGTH_SHORT).show();
                }
            }
        });

        edt_time_on.setOnClickListener(v -> {
            // Get Current Time
            String[] newDesc = edt_time_on.getText().toString().split(":");
            mHourOn = Integer.parseInt(newDesc[0]);
            mMinuteOn = Integer.parseInt(newDesc[1]);
            tpd_on.show();
        });
        edt_time_off.setOnClickListener(v -> {
            // Get Current Time
            String[] newDesc = edt_time_off.getText().toString().split(":");
            mHourOff = Integer.parseInt(newDesc[0]);
            mMinuteOff = Integer.parseInt(newDesc[1]);
            tpd_off.show();
        });
    }

    private void initUI() {
        txt_lamp_position = findViewById(R.id.txt_lamp_position);
        sw_enabled = findViewById(R.id.sw_enabled);
        edt_time_on = findViewById(R.id.edt_time_on);
        edt_time_off = findViewById(R.id.edt_time_off);
        edt_pin = findViewById(R.id.edt_pin);
        img_state = findViewById(R.id.img_state);
        btn_save = findViewById(R.id.btn_save);

        //Получение переданного key
        if (getIntent() != null) {
            key = getIntent().getStringExtra("key");
        }


    }

}
