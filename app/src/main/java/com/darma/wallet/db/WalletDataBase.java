package com.darma.wallet.db;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.migration.Migration;
import android.content.Context;
import android.support.annotation.NonNull;

/**
 * Created by Darma Project on 2019/9/30.
 */
@Database(entities = { WalletDB.class ,ContactsDB.class,NodeDB.class}, version = 1,exportSchema = false)
public abstract class WalletDataBase extends RoomDatabase {

    private static final String DB_NAME = "WalletDB.db";
    private static volatile WalletDataBase instance;

    public static synchronized WalletDataBase getInstance(Context context) {
        if (instance == null) {
            instance = create(context);
        }
        return instance;
    }

    private static WalletDataBase create(final Context context) {
        return Room.databaseBuilder(
                context,
                WalletDataBase.class,
                DB_NAME)
//                .addMigrations(MIGRATION_1_2)
                .build();
    }


//   static Migration MIGRATION_1_2 = new Migration(1, 2) {
//        @Override
//        public void migrate(@NonNull SupportSQLiteDatabase database) {
//            database.execSQL("CREATE TABLE `NodeDB` (`id` INTEGER, `name` TEXT, " +
//                    "PRIMARY KEY(`id`))");
//        }
//
//    };




    public abstract WalletDao getWalletDao();

    public abstract ContactsDao getContactsDao();

    public abstract NodesDao getNodesDao();

}
