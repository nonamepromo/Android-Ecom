package com.example.justplay;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.justplay.Model.Wished;

import java.util.ArrayList;

public class DBHandler extends SQLiteOpenHelper {

    private static final String DB_NAME = "wishlist";

    private static final int DB_VERSION = 1;

    private static final String TABLE_NAME = "favorites";

    private static final String ID_COL = "id";

    private static final String NAME_COL = "name";

    private static final String CONSOLE_COL = "console";

    private static final String PRICE_COL = "price";

    public DBHandler(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_NAME + " ("
                + ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + NAME_COL + " TEXT,"
                + CONSOLE_COL + " TEXT,"
                + PRICE_COL + " TEXT)";

        db.execSQL(query);
    }

    public boolean addNewFavorite(String gameName, String gameConsole) {

        SQLiteDatabase db = this.getWritableDatabase();

        String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + NAME_COL + " = '" + gameName + "'";
        Cursor c = db.rawQuery(query, null);
        if (c.moveToFirst()) {
            c.close();
            return false;
        }else {
            ContentValues values = new ContentValues();

            values.put(NAME_COL, gameName);
            values.put(CONSOLE_COL, gameConsole);

            db.insert(TABLE_NAME, null, values);

            db.close();
        c.close();
        return true;
        }
    }

    public boolean checkWished(String gameName){
        SQLiteDatabase db = this.getWritableDatabase();

        String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + NAME_COL + " = '" + gameName + "'";
        Cursor c = db.rawQuery(query, null);
        if (c.moveToFirst()) {
            c.close();
            return false;
        }else {
            c.close();
            return true;
        }
    }

    public void deleteWishedGame(String wishedTitle) {

        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(TABLE_NAME, "name=?", new String[]{wishedTitle});
        db.close();
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public ArrayList<Wished> readWishedGame() {
        // on below line we are creating a database for reading our database.
        SQLiteDatabase db = this.getReadableDatabase();

        // on below line we are creating a cursor with query to read data from database.
        Cursor cursorWished = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);

        // on below line we are creating a new array list.
        ArrayList<Wished> wishedModalArrayList = new ArrayList<>();

        // moving our cursor to first position.
        if (cursorWished.moveToFirst()) {
            do {
                // on below line we are adding the data from cursor to our array list.
                wishedModalArrayList.add(new Wished(cursorWished.getString(1),
                        cursorWished.getString(2),
                        cursorWished.getString(3)));
            } while (cursorWished.moveToNext());
            // moving our cursor to next.
        }
        // at last closing our cursor
        // and returning our array list.
        cursorWished.close();
        return wishedModalArrayList;
    }

}

