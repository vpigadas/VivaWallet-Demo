package com.vpigadas.vivawallet;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.vpigadas.vivawallet.models.Items;
import com.vpigadas.vivawallet.service.ServerClient;
import com.vpigadas.vivawallet.ui.adapter.ItemsAdapter;
import com.vpigadas.vivawallet.ui.viewmodels.MainViewModel;

import java.util.List;

public class MainScreen extends AppCompatActivity {

    private RecyclerView recyclerView;
    private MainViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);

        viewModel = ViewModelProviders.of(this).get(MainViewModel.class);

    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        recyclerView = findViewById(R.id.recycler);

        viewModel.getSuccessResult().observe(this, new Observer<List<Items>>() {
            @Override
            public void onChanged(@Nullable List<Items> items) {
                if (items == null) {
                    return;
                }

                recyclerView.setAdapter(ItemsAdapter.getInstance(items));
            }
        });

        viewModel.getErrorResult().observe(this, new Observer<Throwable>() {
            @Override
            public void onChanged(@Nullable Throwable throwable) {
                if (throwable == null) {
                    return;
                }

                View view = getCurrentFocus();

                if (view != null) {
                    Snackbar.make(view, throwable.getMessage(), Snackbar.LENGTH_LONG).show();
                }
            }
        });
        ServerClient.getIntance(getApplicationContext()).getCashedProducts(viewModel.getSuccessResult(), viewModel.getErrorResult());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (R.id.meny_refresh == item.getItemId()) {
            ServerClient.getIntance(getApplicationContext()).getProducts(viewModel.getSuccessResult(), viewModel.getErrorResult());

        }
        return super.onOptionsItemSelected(item);
    }
}
