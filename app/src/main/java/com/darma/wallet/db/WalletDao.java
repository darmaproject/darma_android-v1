package com.darma.wallet.db;

import android.arch.persistence.room.*;

import java.util.List;

/**
 * Created by Darma Project on 2019/9/30.
 */
@Dao
public interface  WalletDao {
    @Query("SELECT * FROM WalletDB")
    List<WalletDB> getAllWallets();
    @Query("SELECT * FROM WalletDB WHERE name=:name")
    WalletDB getWallet(String name);
    @Insert
    void insert(WalletDB... walletDBS);

    @Update
    void update(WalletDB... walletDBS);

    @Delete
    void delete(WalletDB... walletDBS);
}
