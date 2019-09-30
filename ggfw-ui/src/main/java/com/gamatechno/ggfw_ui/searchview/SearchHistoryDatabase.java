/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2017. Shendy Aditya Syamsudin
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.gamatechno.ggfw_ui.searchview;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


class SearchHistoryDatabase extends SQLiteOpenHelper {

    static final String SEARCH_HISTORY_TABLE = "search_history";
    static final String SEARCH_HISTORY_COLUMN_ID = "_id";
    static final String SEARCH_HISTORY_COLUMN_TEXT = "_text";
    static final String SEARCH_HISTORY_COLUMN_KEY = "_key";

    private static final String DATABASE_NAME = "search_history_database.db";
    private static final int DATABASE_VERSION = 3;
    private static final String CREATE_TABLE_SEARCH_HISTORY = "CREATE TABLE IF NOT EXISTS "
            + SEARCH_HISTORY_TABLE + " ( "
            + SEARCH_HISTORY_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + SEARCH_HISTORY_COLUMN_TEXT + " TEXT, "
            + SEARCH_HISTORY_COLUMN_KEY + " INTEGER " + ");";

    SearchHistoryDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_SEARCH_HISTORY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        dropAllTables(db);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    private void dropAllTables(SQLiteDatabase db) {
        dropTableIfExists(db);
    }

    private void dropTableIfExists(SQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS " + SearchHistoryDatabase.SEARCH_HISTORY_TABLE);
    }

}
