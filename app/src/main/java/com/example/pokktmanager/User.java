package com.example.pokktmanager;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity
public class User {

    @NonNull
    @PrimaryKey
    @ColumnInfo(name = "Email")
    private String Email;

    private String Name;

    private String Password;

    @NonNull
    public String getEmail() {
        return Email;
    }

    public void setEmail(@NonNull String email) {
        Email = email;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }
}
