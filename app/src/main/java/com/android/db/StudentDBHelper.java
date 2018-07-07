package com.android.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class StudentDBHelper extends SQLiteOpenHelper{

    final static int DATABASE_VERSION = 1;
    final static String DATABASE_NAME= "student.db";
    public final static String TABLE_NAME = "student";

    public static final String name = "name";
    public static final String rollno = "rollno";

    static final String CREATE_TABLE = "CREATE TABLE "+ TABLE_NAME + " ( "+ name + " TEXT NOT NULL ,"
            + rollno + " INTEGER " + " ) " ;

    SQLiteDatabase database;


    public StudentDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+ TABLE_NAME);
        onCreate(sqLiteDatabase);

    }
}
