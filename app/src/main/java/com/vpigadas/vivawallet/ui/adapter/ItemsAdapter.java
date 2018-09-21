package com.vpigadas.vivawallet.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.vpigadas.vivawallet.R;
import com.vpigadas.vivawallet.models.Items;
import com.vpigadas.vivawallet.ui.viewholder.ItemViewHolder;

import java.util.List;

public class ItemsAdapter extends RecyclerView.Adapter<ItemViewHolder> {

    private final List<Items> array;

    public static ItemsAdapter getInstance(List<Items> array) {
        return new ItemsAdapter(array);
    }

    private ItemsAdapter(List<Items> array) {
        this.array = array;
    }

    private Items getItem(int position) {
        return array.get(position);
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View itemView = LayoutInflater.from(context).inflate(R.layout.holder_list_of_item, parent, false);

        return ItemViewHolder.getInstance(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        holder.bind(getItem(position));
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).hashCode();
    }

    @Override
    public int getItemCount() {
        return array.size();
    }
}
