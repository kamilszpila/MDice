package com.example.kamil.mdice;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class DataBaseHelper extends SQLiteOpenHelper {

    SQLiteDatabase db;
    private static final String DATABASE_NAME = "mDice.db";
    private static final String DATABASE_FILE_PATH = Environment.getExternalStorageDirectory().getPath();
    public static final String TABLE_NAME = "MDiceGames";

    public static final String COL_1 = "ID";
    public static final String[] COL_O = {"DICE_1","DICE_2","DICE_3","DICE_4","DICE_5", "DICE_6"};
    public static final String COL_2 = "DATE";
    public static final String NAME = "NAME";
    public static final String NUMBER = "NUMBER";


    public DataBaseHelper(Context context) {
        super(context, DATABASE_FILE_PATH + File.separator + DATABASE_NAME, null, 1);
        db = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "(NAME TEXT PRIMARY KEY,NUMBER INTEGER)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public boolean insertData(String tableName,int[] values, boolean isVirtual){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        for(int i=0; i<values.length;i++){
            contentValues.put(COL_O[i],values[i]);
        }
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();
        contentValues.put(COL_2,formatter.format(date));

        long inserted;

        if(isVirtual){
            inserted = db.insert(tableName + "V",null,contentValues);
        }else {
            inserted = db.insert(tableName,null,contentValues);
        }

        db.close();

        if(inserted==-1){ return false;
        }else{ return true;
        }
    }


     public void createTable(String tableName, int numberOfDices, boolean isVirtual){
         db = this.getWritableDatabase();

         String text;
         if(isVirtual){
             text = "CREATE TABLE IF NOT EXISTS " + tableName.toUpperCase() + "V(ID INTEGER PRIMARY KEY AUTOINCREMENT";
         }else {
             text = "CREATE TABLE IF NOT EXISTS " + tableName.toUpperCase() + "(ID INTEGER PRIMARY KEY AUTOINCREMENT";
         }

         for(int i=0; i<numberOfDices;i++){
             text += "," + COL_O[i] + " INTEGER";
         }
        text += ",DATE TEXT)";
         db.execSQL(text);
     }

     public void createNameTable(){
         db = this.getWritableDatabase();
         ContentValues contentValues = new ContentValues();
         contentValues.put(NAME,"CATAN");
         contentValues.put(NUMBER,2);
         db.insert(TABLE_NAME,null,contentValues);
         db.close();

     }

    public boolean insertGame(String name, int number) {
        db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(NAME, name.toUpperCase());
        contentValues.put(NUMBER, number);

        long inserted = db.insert(TABLE_NAME,null,contentValues);
        db.close();

        if(inserted==-1){ return false;
        }else{ return true;
        }
    }

        public ArrayList<String> getAllData(String tableName, String column){
        ArrayList<String> names = new ArrayList<String>();
        db = this.getReadableDatabase();
        try{
            Cursor cursor = db.rawQuery("SELECT * FROM " + tableName,null);
            if(cursor.getCount()>0){
                while(cursor.moveToNext()){
                    names.add(cursor.getString(cursor.getColumnIndex(column)));
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            db.close();
        }
        return names;
    }


}
