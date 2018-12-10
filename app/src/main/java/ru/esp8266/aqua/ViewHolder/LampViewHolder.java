package ru.esp8266.aqua.ViewHolder;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import ru.esp8266.aqua.Interface.ItemClickListener;
import ru.esp8266.aqua.R;


public class LampViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public ItemClickListener mItemClickListener;

    public TextView txt_position, txt_on, txt_off, txt_state;
    public Button btn_on, btn_off;
    public EditText edt_pin;
    public Switch sw_enable;

    public LampViewHolder(@NonNull View itemView) {
        super(itemView);
        txt_position = itemView.findViewById(R.id.txt_position);
        txt_on = itemView.findViewById(R.id.txt_on);
        txt_off = itemView.findViewById(R.id.txt_off);
        edt_pin = itemView.findViewById(R.id.edt_pin);
        txt_state = itemView.findViewById(R.id.txt_state);
        sw_enable = itemView.findViewById(R.id.sw_enable);
        btn_on = itemView.findViewById(R.id.btn_on);
        btn_off = itemView.findViewById(R.id.btn_off);
        itemView.setOnClickListener(this);

    }

    public ItemClickListener getItemClickListener() {
        return mItemClickListener;
    }

    public void setItemClickListener(final ItemClickListener itemClickListener) {
        mItemClickListener = itemClickListener;
    }

    @Override
    public void onClick(final View v) {
        mItemClickListener.onClick(v, getAdapterPosition(), false);
    }

}
