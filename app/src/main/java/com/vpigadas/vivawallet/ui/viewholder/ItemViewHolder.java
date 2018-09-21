package com.vpigadas.vivawallet.ui.viewholder;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.vpigadas.vivawallet.ItemDetailsScreen;
import com.vpigadas.vivawallet.R;
import com.vpigadas.vivawallet.models.Items;

public class ItemViewHolder extends RecyclerView.ViewHolder {

    private Items localData;

    private ImageView itemImage;
    private TextView itemName;
    private TextView itemPrice;

    public static ItemViewHolder getInstance(@NonNull View itemView) {
        return new ItemViewHolder(itemView);
    }

    private ItemViewHolder(final View itemView) {
        super(itemView);

        itemImage = itemView.findViewById(R.id.list_of_item_image);
        itemName = itemView.findViewById(R.id.list_of_item_name);
        itemPrice = itemView.findViewById(R.id.list_of_item_price);

        View view = itemView.findViewById(R.id.parent_container);
        if (view != null) {
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (localData == null) {
                        return;
                    }

                    Context context = itemView.getContext();

                    Intent intent = new Intent(context, ItemDetailsScreen.class);
                    intent.putExtra(ItemDetailsScreen.ARG_ITEM_ID, localData.getId());

                    context.startActivity(intent);
                }
            });
        }
    }


    public void bind(@NonNull Items item) {
        localData = item;

        itemName.setText(item.getName());
        itemPrice.setText(item.getPrice());

        Picasso.get().load(item.getThumbnail()).into(itemImage);
    }
}
