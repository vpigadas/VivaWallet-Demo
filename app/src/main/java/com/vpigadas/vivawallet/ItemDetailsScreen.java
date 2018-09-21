package com.vpigadas.vivawallet;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.arch.persistence.room.Room;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.vpigadas.vivawallet.models.Items;
import com.vpigadas.vivawallet.service.ServerClient;
import com.vpigadas.vivawallet.ui.viewmodels.MainViewModel;

public class ItemDetailsScreen extends AppCompatActivity {

    public static final String ARG_ITEM_ID = ItemDetailsScreen.class.getSimpleName() + ".ARG_ITEM_ID";

    private MainViewModel viewModel;
    private AppDatabase appDatabase;

    private Items product;

    private ImageView itemImage;
    private TextView itemName;
    private TextView itemDesc;

    private Button btnBuy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_details);

        viewModel = ViewModelProviders.of(this).get(MainViewModel.class);

        itemImage = findViewById(R.id.item_details_image);
        itemName = findViewById(R.id.item_details_title);
        itemDesc = findViewById(R.id.item_details_desc);

        btnBuy = findViewById(R.id.item_details_buy);
        btnBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ServerClient.getIntance(getApplicationContext()).postOrder(product.getPrice(), viewModel.getSuccessResult(), viewModel.getErrorResult());
            }
        });
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        int id = getIntent().getIntExtra(ARG_ITEM_ID, Integer.MIN_VALUE);

        if (id == Integer.MIN_VALUE) {
            finish();
        }

        appDatabase = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "viva-wallet").build();

        appDatabase.products().loadAllByIds(id).observe(this, new Observer<Items>() {
            @Override
            public void onChanged(@Nullable Items items) {
                if (items == null) {
                    finish();
                    return;
                }

                product = items;

                Picasso.get().load(items.getImage()).into(itemImage);

                itemName.setText(items.getName());
                itemDesc.setText(items.getDescription());

                btnBuy.setText(getString(R.string.buy_for, items.getPrice()));

            }
        });
    }
}
