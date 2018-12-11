package ru.esp8266.aqua;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import ru.esp8266.aqua.Screen.LampActivity;
import ru.esp8266.aqua.Screen.TemperatureActivity;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = MainActivity.class
            .getSimpleName();
    ImageView btn_temperature, btn_lamp, btn_settings, btn_exit, btn_heater, btn_refresh, btn_udo, btn_log;

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

    }

    private void initUI() {
        btn_temperature = findViewById(R.id.btn_temperature);
        btn_lamp = findViewById(R.id.btn_lamp);
        btn_udo = findViewById(R.id.btn_udo);
        btn_refresh = findViewById(R.id.btn_refresh);
        btn_log = findViewById(R.id.btn_log);
        btn_heater = findViewById(R.id.btn_heater);
        btn_settings = findViewById(R.id.btn_settings);
        btn_exit = findViewById(R.id.btn_exit);
    }


}
