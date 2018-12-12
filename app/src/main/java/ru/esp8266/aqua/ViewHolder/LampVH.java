package ru.esp8266.aqua.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import ru.esp8266.aqua.Interface.ItemClickListener;
import ru.esp8266.aqua.R;

public class LampVH extends RecyclerView.ViewHolder implements View.OnClickListener {
    public ItemClickListener mItemClickListener;
    public TextView txt_lamp_title, txt_lamp_from, txt_lamp_to;
    public ImageView img_lamp;

    public LampVH(@NonNull View itemView) {
        super(itemView);
        txt_lamp_title = itemView.findViewById(R.id.txt_lamp_title);
        txt_lamp_from = itemView.findViewById(R.id.txt_lamp_from);
        txt_lamp_to = itemView.findViewById(R.id.txt_lamp_to);

        img_lamp = itemView.findViewById(R.id.img_lamp);


        itemView.setOnClickListener(this);
    }

    public ItemClickListener getItemClickListener() {
        return mItemClickListener;
    }

    public void setItemClickListener(final ItemClickListener itemClickListener) {
        mItemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View v) {
        mItemClickListener.onClick(v, getAdapterPosition(), false);
    }
}
