package com.example.pokktmanager;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

@Database(entities = {User.class,MoneyAmount.class},version = 11)
public abstract class PMDatabase extends RoomDatabase {

    public abstract UserDao userDao();

    public abstract TransactionsDao transactionsDao();

}
