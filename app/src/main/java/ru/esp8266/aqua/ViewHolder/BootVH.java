package ru.esp8266.aqua.ViewHolder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import ru.esp8266.aqua.R;

public class BootVH extends RecyclerView.ViewHolder {
    public TextView boot_text;

    public BootVH(@NonNull View itemView) {
        super(itemView);
        boot_text = itemView.findViewById(R.id.boot_text);
    }
}
