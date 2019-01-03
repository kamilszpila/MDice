package com.example.kamil.mdice;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static android.content.Context.MODE_PRIVATE;

public class DataBaseHelper extends SQLiteOpenHelper {

    SQLiteDatabase db;
    public Context appContext;
    private static final String DATABASE_NAME = "mDice.db";
    private static final String DATABASE_FILE_PATH = Environment.getExternalStorageDirectory().getPath();
    private static final String TABLE_NAME = "MDiceGames";

    private static final String[] COL_O = {"DICE_1", "DICE_2", "DICE_3", "DICE_4", "DICE_5", "DICE_6"};
    private static final String COL_2 = "DATE";
    private static final String COL_3 = "GAME_ID";
    private static final String NAME = "NAME";
    private static final String NUMBER = "NUMBER";


    public DataBaseHelper(Context context) {
        super(context, DATABASE_FILE_PATH + File.separator + DATABASE_NAME, null, 1);
        db = this.getWritableDatabase();
        this.appContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "(NAME TEXT PRIMARY KEY,NUMBER INTEGER)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public boolean insertData(String nameOfGame, int[] values, boolean isVirtual) {
        SQLiteDatabase db = this.getWritableDatabase();
        SharedPreferences sharedPreferences = appContext.getSharedPreferences(appContext.getString(R.string.shPFileName), MODE_PRIVATE);

        ContentValues contentValues = new ContentValues();

        for (int i = 0; i < values.length; i++) {
            contentValues.put(COL_O[i], values[i]);
        }
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();
        contentValues.put(COL_2, formatter.format(date));

        long inserted;
        int gameID;

        String tableName = getTableName(nameOfGame, isVirtual);
        gameID = sharedPreferences.getInt(tableName, 0);
        contentValues.put(COL_3, gameID);
        inserted = db.insert(tableName, null, contentValues);

        db.close();

        if (inserted == -1) {
            return false;
        } else {
            return true;
        }
    }


    public void createTable(String nameOfGame, int numberOfDices, boolean isVirtual) {
        db = this.getWritableDatabase();

        String text;
        String tableName = getTableName(nameOfGame, isVirtual);
        text = "CREATE TABLE IF NOT EXISTS " + tableName.toUpperCase() + "(ID INTEGER PRIMARY KEY AUTOINCREMENT";

        for (int i = 0; i < numberOfDices; i++) {
            text += "," + COL_O[i] + " INTEGER";
        }
        text += "," + COL_2 + " TEXT";
        text += "," + COL_3 + " INTEGER)";
        db.execSQL(text);
    }


    public boolean insertGame(String name, int number) {
        db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(NAME, name.toUpperCase());
        contentValues.put(NUMBER, number);

        long inserted = db.insert(TABLE_NAME, null, contentValues);
        db.close();

        if (inserted == -1) {
            return false;
        } else {
            return true;
        }
    }

    public ArrayList<String> getAllData(String tableName, String column, String groupByColumn) {
        db = this.getReadableDatabase();
        ArrayList<String> results = new ArrayList<>();
        try {
            Cursor cursor = db.rawQuery("SELECT " + column + " FROM " + tableName + " GROUP BY " + groupByColumn, null);
            if (cursor.getCount() > 0) {
                if (column.equals(NAME)) {
                    while (cursor.moveToNext()) {
                        results.add(cursor.getString(cursor.getColumnIndex(column)));
                    }
                } else if (column.equals(NUMBER)) {
                    while (cursor.moveToNext()) {
                        results.add(String.valueOf(cursor.getInt(cursor.getColumnIndex(column))));
                    }
                } else {
                    while (cursor.moveToNext()) {
                        results.add(String.valueOf(cursor.getInt(cursor.getColumnIndex(column))));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close();
        }
        return results;
    }

    public ArrayList<String> getDataFromGame(String nameOfGame, String column, String gameID, boolean isVirtual) {
        db = this.getReadableDatabase();
        ArrayList<String> results = new ArrayList<>();
        String tableName = getTableName(nameOfGame, isVirtual);
        try {
            Cursor cursor;
            if (gameID.equals("All")) {
                cursor = db.rawQuery("SELECT " + column + " FROM " + tableName, null);
            } else {
                cursor = db.rawQuery("SELECT " + column + " FROM " + tableName + " WHERE " + COL_3 + " == " + gameID, null);
            }
            if (cursor.getCount() > 0) {
                if (column.equals(COL_2)) {
                    while (cursor.moveToNext()) {
                        results.add(cursor.getString(cursor.getColumnIndex(column)));
                    }
                } else {
                    while (cursor.moveToNext()) {
                        results.add(String.valueOf(cursor.getInt(cursor.getColumnIndex(column))));

                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close();
        }
        return results;
    }

    public int getNumberOfDices(String gameName) {
        db = this.getReadableDatabase();
        int number = 1;
        try {
            Cursor cursor = db.rawQuery("SELECT " + NUMBER + " FROM " + TABLE_NAME + " WHERE " + NAME + " == " + "\"" + String.valueOf(gameName) + "\"", null);
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    number = cursor.getInt(cursor.getColumnIndex(NUMBER));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close();
        }
        return number;
    }

    public ArrayList<String> getAllGameIDs(String nameOfGame, boolean isVirtual) {
        db = this.getReadableDatabase();
        ArrayList<String> gameIDs = new ArrayList<>();
        String tableName = getTableName(nameOfGame, isVirtual);
        try {
            Cursor cursor = db.rawQuery("SELECT " + COL_3 + " FROM " + tableName + " GROUP BY " + COL_3, null);
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    gameIDs.add(String.valueOf(cursor.getInt(cursor.getColumnIndex(COL_3))));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close();
        }
        return gameIDs;
    }

    public int getLastGameID(String nameOfGame, boolean isVirtual) {
        db = this.getReadableDatabase();
        int gameID = 0;
        String tableName = getTableName(nameOfGame, isVirtual);

        try {
            Cursor cursor = db.rawQuery("SELECT " + COL_3 + " FROM " + tableName + " GROUP BY " + COL_3, null);
            if (cursor.getCount() > 0) {
                cursor.moveToLast();
                gameID = cursor.getInt(cursor.getColumnIndex(COL_3));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close();
        }
        return gameID;
    }

    public int[] countDices(String nameOfGame, String gameID, boolean isVirtual) {
        int numberOfAllDices = getNumberOfDices(nameOfGame);
        db = this.getReadableDatabase();
        int countDices[] = new int[6];
        Cursor cursor;
        String tableName = getTableName(nameOfGame, isVirtual);

        try {
            for (int i = 0; i < numberOfAllDices; i++) {
                for (int j = 0; j < countDices.length; j++) {
                    if (gameID.equals("All")) {
                        cursor = db.rawQuery("SELECT COUNT(" + COL_O[i] + ") FROM " + tableName + " WHERE " + COL_O[i] + " == " + (j + 1), null);
                    } else {
                        cursor = db.rawQuery("SELECT COUNT(" + COL_O[i] + ") FROM " + tableName + " WHERE " + COL_3 + " == " + gameID + " AND " + COL_O[i] + " == " + (j + 1), null);
                    }

                    int index = cursor.getColumnIndex("COUNT(" + COL_O[i] + ")");

                    if (cursor.getCount() > 0) {
                        cursor.moveToFirst();
                        countDices[j] += cursor.getInt(index);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close();
        }
        return countDices;
    }


    public int[] sumDices(String nameOfGame, String gameID, boolean isVirtual) {
        int numberOfAllDices = getNumberOfDices(nameOfGame);
        db = this.getReadableDatabase();
        int sumDices[];
        if (numberOfAllDices == 1) {
            sumDices = new int[numberOfAllDices * 6];
        } else {
            sumDices = new int[numberOfAllDices * 6 - 1];
        }
        Cursor cursor;
        String dices = COL_O[0];
        String tableName = getTableName(nameOfGame, isVirtual);

        try {
            for (int i = 1; i < numberOfAllDices; i++) {
                dices += "+" + COL_O[i];
            }

            for (int j = 0; j < sumDices.length; j++) {
                if (gameID.equals("All")) {
                    cursor = db.rawQuery("SELECT COUNT(" + dices + ") FROM " + tableName + " WHERE " + dices + " == " + (j + numberOfAllDices), null);
                } else {
                    cursor = db.rawQuery("SELECT COUNT(" + dices + ") FROM " + tableName + " WHERE " + COL_3 + " == " + gameID + " AND " + dices + " == " + (j + numberOfAllDices), null);
                }

                int index = cursor.getColumnIndex("COUNT(" + dices + ")");

                if (cursor.getCount() > 0) {
                    cursor.moveToFirst();
                    sumDices[j] = cursor.getInt(index);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close();
        }
        return sumDices;
    }


    public String getTableName(String nameOfGame, boolean isVirtual) {
        if (isVirtual) {
            return nameOfGame + "V";
        } else {
            return nameOfGame;
        }
    }
}
