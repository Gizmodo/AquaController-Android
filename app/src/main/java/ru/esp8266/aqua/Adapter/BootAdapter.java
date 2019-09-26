package ru.esp8266.aqua.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import ru.esp8266.aqua.R;
import ru.esp8266.aqua.ViewHolder.BootViewHolder;

public class BootAdapter extends RecyclerView.Adapter<BootViewHolder> {
    List<String> listBootItems;
    Context context;

    public BootAdapter(Context context) {
        this.listBootItems = new ArrayList<>();
        this.context = context;
    }

    public void addAll(List<String> newBootItems) {
        int initSize = listBootItems.size();
        listBootItems.addAll(newBootItems);
        notifyItemRangeChanged(initSize, newBootItems.size());
    }

    public void removeLastItem() {
        listBootItems.remove(listBootItems.size() - 1);
    }

    public String getLastItem() {
        return listBootItems.get(listBootItems.size() - 1);
    }


    @NonNull
    @Override
    public BootViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.boot_item_layout, parent, false);
        return new BootViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull BootViewHolder holder, int position) {
        holder.boot_text.setText(listBootItems.get(position));
    }

    @Override
    public int getItemCount() {
        return listBootItems.size();
    }
}
