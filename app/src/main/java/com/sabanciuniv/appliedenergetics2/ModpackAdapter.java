package com.sabanciuniv.appliedenergetics2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.sabanciuniv.appliedenergetics2.R;
import com.sabanciuniv.appliedenergetics2.Modpack;
import java.util.List;

public class ModpackAdapter extends RecyclerView.Adapter<ModpackAdapter.ModpackViewHolder> {

    private List<Modpack> modpackList;
    private OnModpackClickListener listener;

    public ModpackAdapter(List<Modpack> modpackList, OnModpackClickListener listener) {
        this.modpackList = modpackList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ModpackViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_box, parent, false);
        return new ModpackViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull ModpackViewHolder holder, int position) {
        Modpack modpack = modpackList.get(position);
        holder.itemDescription.setText(modpack.getDescription());
        // Assuming you have a way to load images, like using Glide or Picasso
        // Glide.with(holder.itemView.getContext()).load(modpack.getImageUrl()).into(holder.itemImage);
    }

    @Override
    public int getItemCount() {
        return modpackList.size();
    }

    public void setModpackList(List<Modpack> modpackList) {
        this.modpackList = modpackList;
        notifyDataSetChanged();
    }

    public interface OnModpackClickListener {
        void onModpackClick(int modpackId);
    }

    static class ModpackViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView itemImage;
        TextView itemDescription;
        OnModpackClickListener listener;

        ModpackViewHolder(@NonNull View itemView, OnModpackClickListener listener) {
            super(itemView);
            itemImage = itemView.findViewById(R.id.item_image);
            itemDescription = itemView.findViewById(R.id.item_description);
            this.listener = listener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            listener.onModpackClick(getBindingAdapterPosition());
        }
    }
}
