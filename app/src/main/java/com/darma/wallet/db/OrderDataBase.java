package com.darma.wallet.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

/**
 * Created by Darma Project on 2019/9/30.
 */
@Database(entities = { OrderDB.class}, version =1,exportSchema = false)
public abstract class OrderDataBase extends RoomDatabase {

    private static final String DB_NAME = "OrderDB.db";
    private static volatile OrderDataBase instance;

    public static synchronized OrderDataBase getInstance(Context context) {
        if (instance == null) {
            instance = create(context);
        }
        return instance;
    }

    private static OrderDataBase create(final Context context) {
        return Room.databaseBuilder(
                context,
                OrderDataBase.class,
                DB_NAME)
                .build();
    }





    public void insertOrUpdate(OrderDB order){

        OrderDao dao=getOrderDao();


        OrderDB orderDB=dao.getOrder(order.getOrder_id());
        if(orderDB!=null){
            order.setId(orderDB.getId());
            dao.update(order);
        }else{

            dao.insert(order);
        }



    }


    public abstract OrderDao getOrderDao();
}
