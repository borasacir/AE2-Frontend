// ModpackAdapter.java
package com.sabanciuniv.appliedenergetics2.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.sabanciuniv.appliedenergetics2.R;
import com.sabanciuniv.appliedenergetics2.models.Modpack;
import java.util.List;

public class ModpackAdapter extends RecyclerView.Adapter<ModpackAdapter.ModpackViewHolder> {

    private List<Modpack> modpackList;

    public ModpackAdapter(List<Modpack> modpackList) {
        this.modpackList = modpackList;
    }

    @NonNull
    @Override
    public ModpackViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_modpack, parent, false);
        return new ModpackViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ModpackViewHolder holder, int position) {
        Modpack modpack = modpackList.get(position);
        holder.tvName.setText(modpack.getName());
        holder.tvVersion.setText(modpack.getVersion());
        holder.tvDescription.setText(modpack.getDescription());
    }

    @Override
    public int getItemCount() {
        return modpackList.size();
    }

    public static class ModpackViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvVersion, tvDescription;

        public ModpackViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_name);
            tvVersion = itemView.findViewById(R.id.tv_version);
            tvDescription = itemView.findViewById(R.id.tv_description);
        }
    }
}
