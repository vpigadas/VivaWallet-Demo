package com.vpigadas.vivawallet.models;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import io.reactivex.Observable;

@Dao
public interface ItemDao {

    @Query("SELECT * FROM items")
    LiveData<List<Items>> getAll();

    @Query("SELECT * FROM items WHERE id IS :itemId")
    LiveData<Items> loadAllByIds(int itemId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(Items... items);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Items> items);

    @Update
    void update(Items... items);

    @Update
    void update(List<Items> items);

    @Delete
    void delete(Items item);
}
