package com.example.pokktmanager;


import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.text.SimpleDateFormat;
import java.util.Date;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(tableName = "transactions",indices = {@Index("user_email")},foreignKeys = @ForeignKey(entity = User.class,parentColumns = "Email",
                                                                childColumns = "user_email",onDelete = CASCADE))
public class MoneyAmount {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String Email;

    @ColumnInfo(name = "user_email")
    private String email;

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    private int amount;

    private String type;

    private String tag;

    @ColumnInfo(name = "transaction_date")
    private String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());

    public MoneyAmount(String email) {
        Email = email;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(@NonNull String email) {
        this.email = email;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }



}
