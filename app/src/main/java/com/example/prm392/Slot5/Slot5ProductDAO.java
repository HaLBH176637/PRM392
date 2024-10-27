package com.example.prm392.Slot5;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class Slot5ProductDAO {
    private Slot5SQLiteOpenHelper dbhelper;
    private SQLiteDatabase db;
    private Context context;

    public Slot5ProductDAO(Context context) {
        this.context = context;
        dbhelper=new Slot5SQLiteOpenHelper(context);
        db=dbhelper.getWritableDatabase();
    }
    //insert
    public int insertProduct(Slot5Product p){
        ContentValues values=new ContentValues();
        values.put("id",p.getId());
        values.put("name",p.getName());
        values.put("price",p.getPrice());
        //
        if(db.insert("Product",null,values)<0){
            return -1;
        }
        return 1;
    }
    //get data
    public List<Slot5Product> getAll(){
        List<Slot5Product> list=new ArrayList<>();
        //cursor read data
        Cursor c=db.query("Product",null,null,null,null,
                null,null);
        c.moveToFirst();
        while (c.isAfterLast()==false){
            Slot5Product product=new Slot5Product();
            product.setId(c.getString(0));
            product.setName(c.getString(1));
            product.setPrice(c.getString(2));
            list.add(product);
            c.moveToNext();
        }
        c.close();
        return list;
    }

}
