package com.darma.wallet.db;

import android.arch.persistence.room.*;

import java.util.List;

/**
 * Created by Darma Project on 2019/9/30.
 */
@Dao
public interface ContactsDao {
    @Query("SELECT * FROM ContactsDB")
    List<ContactsDB> getAllContacts();
    @Query("SELECT * FROM ContactsDB WHERE coin_code=:coin_code")
    List<ContactsDB> getContacts(String coin_code);
    @Insert
    void insert(ContactsDB... contactsDBs);

    @Update
    void update(ContactsDB... contactsDBs);

    @Delete
    void delete(ContactsDB... contactsDBs);
}
