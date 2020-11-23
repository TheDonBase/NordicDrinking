package com.thedonbase.nordicdrinking;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "NordicDrinking.db";
    public static final String QUESTIONS_TABLE = "questions_table";
    public static final String QUES_1 = "Category";
    public static final String QUES_2 = "Question";
    public static final String PLAYERS_TABLE = "players_table";
    public static final String PLAY_1 = "Name";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 2);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + QUESTIONS_TABLE + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, CATEGORY TEXT, QUESTION TEXT UNIQUE)");
        db.execSQL("create table " + PLAYERS_TABLE + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, NAME TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + QUESTIONS_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + PLAYERS_TABLE);
        onCreate(db);
    }

    public boolean insertData(String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(PLAY_1, name);
        long result = db.insert(PLAYERS_TABLE, null, contentValues);
        if(result == -1)
            return false;
        else
            return true;
    }

    public void deleteAll(String table) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(table, null, null);
        db.close();
    }

    public Cursor getAllPlayers() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from " + PLAYERS_TABLE, null);
        return res;
    }

    public Cursor getAllQuestions(String category)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res;
        if(category.equals("Chaos_Category"))
        {
            res = db.rawQuery("select * from " + QUESTIONS_TABLE, null);
            return res;
        }
        res = db.rawQuery("select * from " + QUESTIONS_TABLE + " where category = ?", new String[] {category});
        return res;
    }

    public boolean insertQuestions(String response) throws JSONException {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(QUESTIONS_TABLE, null, null);
        ContentValues contentValues = new ContentValues();
        JSONArray jsonArray = new JSONArray(response);
        long result = 0;
        for(int i = 0; i < jsonArray.length(); i++)
        {
            String category = jsonArray.getJSONObject(i).getString("category");
            String question = jsonArray.getJSONObject(i).getString("question");
            contentValues.put(QUES_1, category);
            contentValues.put(QUES_2, question);
            result = db.insert(QUESTIONS_TABLE, null, contentValues);
        }
        db.delete(PLAYERS_TABLE, null, null);
        if(result == -1)
            return false;
        else
            return true;
    }

    public void fetchQuestions(String response) throws JSONException {
        boolean isInserted = insertQuestions(response);
        if(isInserted)
        {
            Log.d("Status: ", "Success!");
        } else {
            Log.d("Status: ", "Failure!");
        }
    }
}
