package com.darma.wallet.db;

import android.arch.persistence.room.*;

import java.util.List;

/**
 * Created by Darma Project on 2019/9/30.
 */
@Dao
public interface NodesDao {

    @Query("SELECT * FROM NodeDB")
    List<NodeDB> getAllNodes();

    @Query("SELECT * FROM NodeDB WHERE url=:url")
    NodeDB getNode(String url);

    @Insert
    void insert(NodeDB... nodeDBs);

    @Update
    void update(NodeDB... nodeDBs);

    @Delete
    void delete(NodeDB... nodeDBs);
}
