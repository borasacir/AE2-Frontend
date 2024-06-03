package com.sabanciuniv.appliedenergetics2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import java.util.List;

public class ModpackAdapter extends RecyclerView.Adapter<ModpackAdapter.ModpackViewHolder> {

    private List<Modpack.Item> itemList;
    private OnModpackClickListener listener;

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
        holder.description.setText(item.getDescription());
        Glide.with(holder.itemView.getContext()).load(item.getImageURL()).into(holder.image);
        holder.itemView.setOnClickListener(v -> listener.onModpackClick(item.getId()));
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    static class ModpackViewHolder extends RecyclerView.ViewHolder {
        TextView title, description;
        ImageView image;

        ModpackViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.modpack_title);
            description = itemView.findViewById(R.id.modpack_description);
            image = itemView.findViewById(R.id.modpack_image);
        }
    }

    interface OnModpackClickListener {
        void onModpackClick(String itemId);
    }
}
