package com.example.pokktmanager;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface UserDao {

    @Insert
    public void addUser(User user);

    @Query("SELECT * FROM user WHERE Email LIKE :email " +
            "AND Password LIKE :password")
    public List<User> checkUser(String email, String password);

    @Query("SELECT * FROM user WHERE Email LIKE :email")
    public List<User> checkUser(String email);

    @Delete
    public void deleteUser(User user);

}
