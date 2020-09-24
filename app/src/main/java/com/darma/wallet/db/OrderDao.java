package com.darma.wallet.db;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

/**
 * Created by Darma Project on 2019/9/30.
 */
@Dao
public interface OrderDao {
    @Query("SELECT * FROM OrderDB")
    List<OrderDB> getAllOrders();
    @Query("SELECT * FROM OrderDB WHERE order_id=:order_id")
    OrderDB getOrder(String order_id);
    @Insert
    void insert(OrderDB... orderDBs);

    @Update
    void update(OrderDB... orderDBs);

    @Delete
    void delete(OrderDB... orderDBs);

}
