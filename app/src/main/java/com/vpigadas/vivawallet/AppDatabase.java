package com.vpigadas.vivawallet;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.vpigadas.vivawallet.models.ItemDao;
import com.vpigadas.vivawallet.models.Items;

@Database(entities = {Items.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract ItemDao products();
}