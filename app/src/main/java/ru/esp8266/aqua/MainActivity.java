package ru.esp8266.aqua;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import ru.esp8266.aqua.Screen.HeaterActivity;
import ru.esp8266.aqua.Screen.LampActivity;
import ru.esp8266.aqua.Screen.LogActivity;
import ru.esp8266.aqua.Screen.SettingsActivity;
import ru.esp8266.aqua.Screen.TemperatureActivity;
import ru.esp8266.aqua.Screen.UDOActivity;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = MainActivity.class
            .getSimpleName();
    ImageView btn_temperature, btn_lamp, btn_settings, btn_ntp, btn_heater, btn_refresh, btn_udo, btn_log;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initUI();
        initButtonListeners();
    }

    private void initButtonListeners() {
        btn_temperature.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, TemperatureActivity.class);
            startActivity(intent);
            finish();
        });
        btn_lamp.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, LampActivity.class);
            startActivity(intent);

        });
        btn_udo.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, UDOActivity.class);
            startActivity(intent);

        });
        btn_log.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, LogActivity.class);
            startActivity(intent);

        });
        btn_heater.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, HeaterActivity.class);
            startActivity(intent);

        });
        btn_settings.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(intent);

        });
        btn_ntp.setOnClickListener(v -> {
            final FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference RefNTP = database.getReference("TimeSync");
            RefNTP.setValue(true);
            Toast.makeText(this, "Выполнен запрос синхронизацию времени", Toast.LENGTH_SHORT).show();
        });
        btn_refresh.setOnClickListener(v -> {
            final FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference RefUpdate = database.getReference("UpdateSettings");
            RefUpdate.setValue(true);
            Toast.makeText(this, "Выполнен запрос на обновление настроек", Toast.LENGTH_SHORT).show();
        });

    }

    private void initUI() {
        btn_temperature = findViewById(R.id.btn_temperature);
        btn_lamp = findViewById(R.id.btn_lamp);
        btn_udo = findViewById(R.id.btn_udo);
        btn_refresh = findViewById(R.id.btn_refresh);
        btn_log = findViewById(R.id.btn_log);
        btn_heater = findViewById(R.id.btn_heater);
        btn_settings = findViewById(R.id.btn_settings);
        btn_ntp = findViewById(R.id.btn_ntp);
    }


}
