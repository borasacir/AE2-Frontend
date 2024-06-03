package com.sabanciuniv.appliedenergetics2;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class ModpackAdapter extends RecyclerView.Adapter<ModpackAdapter.ModpackViewHolder> {

    private List<Modpack.Item> itemList;
    private OnModpackClickListener listener;
    private static final String TAG = "annen";

    public ModpackAdapter(List<Modpack.Item> itemList, OnModpackClickListener listener) {
        this.itemList = itemList;
        this.listener = listener;
    }

    public void setItemList(List<Modpack.Item> itemList) {
        this.itemList = itemList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ModpackViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_modpack, parent, false);
        return new ModpackViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ModpackViewHolder holder, int position) {
        Modpack.Item item = itemList.get(position);
        holder.title.setText(item.getTitle());
        String itemTitle = item.getTitle();
        Log.d(TAG, "Item Title: " + itemTitle);
        holder.itemView.setOnClickListener(v -> {
            Log.d(TAG, "Clicked item title: " + itemTitle);
            listener.onModpackClick(itemTitle);
        });
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    static class ModpackViewHolder extends RecyclerView.ViewHolder {
        TextView title;

        ModpackViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.modpack_title);
        }
    }

    interface OnModpackClickListener {
        void onModpackClick(String itemId);
    }
}
