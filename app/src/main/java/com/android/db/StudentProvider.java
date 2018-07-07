package com.android.db;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import java.net.URI;
import java.util.HashMap;

public class StudentProvider extends ContentProvider{
    final static String PROVIDER_NAME = "com.android.db.studentprovider";
    final static String URL = "content://"+PROVIDER_NAME+"/students";
    public final static Uri CONTENT_URI = Uri.parse(URL);

    static final int STUDENTS = 1;
    static final int STUDENT_ID = 2;

    static HashMap<String,String> studentMap;
    static UriMatcher urimatcher;
    static {
        urimatcher = new UriMatcher(UriMatcher.NO_MATCH);
        urimatcher.addURI(PROVIDER_NAME, "/students", STUDENTS);
        urimatcher.addURI(PROVIDER_NAME, "/students/#", STUDENT_ID);
    }

    StudentDBHelper studentDBHelper;
    Context context;
    SQLiteDatabase database;

    @Override
    public boolean onCreate() {
        this.context = getContext();
        studentDBHelper = new StudentDBHelper(context);
        database = studentDBHelper.getWritableDatabase();

        return database !=null ? true:  false;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        long rowId = database.insert(StudentDBHelper.TABLE_NAME," ", contentValues);

        if(rowId > 0){
            Uri _uri = ContentUris.withAppendedId(CONTENT_URI,rowId);
            getContext().getContentResolver().notifyChange(_uri,null);
            return _uri;
        }

        throw new SQLException("Failed to add new records "+ uri);
    }


    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection,
                        @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setTables(StudentDBHelper.TABLE_NAME);
        switch (urimatcher.match(uri)) {
            case STUDENTS:
                queryBuilder.setProjectionMap(studentMap);
                break;
            case STUDENT_ID:
                queryBuilder.appendWhere( StudentDBHelper.rollno + "=" + uri.getPathSegments().get(1));
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
        if (sortOrder == null || sortOrder == ""){
            sortOrder = StudentDBHelper.rollno;
        }
        Cursor cursor = queryBuilder.query(database, projection, selection, selectionArgs, null, null, sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues,
                      @Nullable String s, @Nullable String[] strings) {
        return 0;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        int count = 0;
        switch (urimatcher.match(uri)){
            case STUDENTS:
                count = database.delete(StudentDBHelper.TABLE_NAME, selection, selectionArgs);
                break;

            case STUDENT_ID:
                String id = uri.getPathSegments().get(1);
                count = database.delete( StudentDBHelper.TABLE_NAME, StudentDBHelper.rollno +  " = " + id +
                                (!TextUtils.isEmpty(selection) ?
                        "AND (" + selection + ')' : ""), selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }
}
