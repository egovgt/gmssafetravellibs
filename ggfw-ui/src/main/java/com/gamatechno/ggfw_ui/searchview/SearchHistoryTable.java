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

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.gamatechno.ggfw_ui.R;

import java.util.ArrayList;
import java.util.List;


@SuppressWarnings({"WeakerAccess", "SameParameterValue", "unused"})
public class SearchHistoryTable {

    private static int mHistorySize = 2;
    private static Integer mCurrentDatabaseKey = null;
    private final SearchHistoryDatabase dbHelper;
    private SQLiteDatabase db;

    public SearchHistoryTable(Context mContext) {
        dbHelper = new SearchHistoryDatabase(mContext);
    }

    // FOR onResume AND onPause
    public void open() throws SQLException {
        db = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public void addItem(SearchItem item) {
        addItem(item, mCurrentDatabaseKey);
    }

    public void addItem(SearchItem item, Integer databaseKey) {
        ContentValues values = new ContentValues();
        if (!checkText(item.getText().toString())) {
            values.put(SearchHistoryDatabase.SEARCH_HISTORY_COLUMN_TEXT, item.getText().toString());
            if (databaseKey != null) {
                values.put(SearchHistoryDatabase.SEARCH_HISTORY_COLUMN_KEY, databaseKey);
            }
            open();
            db.insert(SearchHistoryDatabase.SEARCH_HISTORY_TABLE, null, values);
            close();
        } else {
            values.put(SearchHistoryDatabase.SEARCH_HISTORY_COLUMN_ID, getLastItemId(databaseKey) + 1);
            open();
            db.update(SearchHistoryDatabase.SEARCH_HISTORY_TABLE, values, SearchHistoryDatabase.SEARCH_HISTORY_COLUMN_ID + " = ? ", new String[]{Integer.toString(getItemId(item))});
            close();
        }
    }

    private int getItemId(SearchItem item) {
        open();
        String query = "SELECT " + SearchHistoryDatabase.SEARCH_HISTORY_COLUMN_ID +
                " FROM " + SearchHistoryDatabase.SEARCH_HISTORY_TABLE +
                " WHERE " + SearchHistoryDatabase.SEARCH_HISTORY_COLUMN_TEXT + " = ?";
        Cursor res = db.rawQuery(query, new String[]{item.getText().toString()});
        res.moveToFirst();
        int id = res.getInt(0);
        close();
        res.close();
        return id;
    }

    private int getLastItemId(Integer databaseKey) {
        open();
        String sql = "SELECT " + SearchHistoryDatabase.SEARCH_HISTORY_COLUMN_ID + " FROM " + SearchHistoryDatabase.SEARCH_HISTORY_TABLE;
        if (databaseKey != null)
            sql += " WHERE " + SearchHistoryDatabase.SEARCH_HISTORY_COLUMN_KEY + " = " + databaseKey;
        Cursor res = db.rawQuery(sql, null);
        res.moveToLast();
        int count = res.getInt(0);
        close();
        res.close();
        return count;
    }

    private boolean checkText(String text) {
        open();

        String query = "SELECT * FROM " + SearchHistoryDatabase.SEARCH_HISTORY_TABLE + " WHERE " + SearchHistoryDatabase.SEARCH_HISTORY_COLUMN_TEXT + " =?";
        Cursor cursor = db.rawQuery(query, new String[]{text});

        boolean hasObject = false;

        if (cursor.moveToFirst()) {
            hasObject = true;
        }

        cursor.close();
        close();
        return hasObject;
    }

    public List<SearchItem> getAllItems(Integer databaseKey) {
        mCurrentDatabaseKey = databaseKey;
        List<SearchItem> list = new ArrayList<>();

        String selectQuery = "SELECT * FROM " + SearchHistoryDatabase.SEARCH_HISTORY_TABLE;
        if (databaseKey != null) {
            selectQuery += " WHERE " + SearchHistoryDatabase.SEARCH_HISTORY_COLUMN_KEY + " = " + databaseKey;
        }
        selectQuery += " ORDER BY " + SearchHistoryDatabase.SEARCH_HISTORY_COLUMN_ID + " DESC LIMIT " + mHistorySize;

        open();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                SearchItem item = new SearchItem();
                item.setIconResource(R.drawable.ic_history_black_24dp);
                item.setText(cursor.getString(1));
                list.add(item);
            } while (cursor.moveToNext());
        }
        cursor.close();
        close();
        return list;
    }

    public void setHistorySize(int historySize) {
        mHistorySize = historySize;
    }

    public void clearDatabase() {
        clearDatabase(null);
    }

    public void clearDatabase(Integer key) {
        open();
        if (key == null) {
            db.delete(SearchHistoryDatabase.SEARCH_HISTORY_TABLE, null, null);
        } else {
            db.delete(SearchHistoryDatabase.SEARCH_HISTORY_TABLE, SearchHistoryDatabase.SEARCH_HISTORY_COLUMN_KEY + " = ?", new String[]{String.valueOf(key)});
        }
        close();
    }

    public int getItemsCount() {
        open();
        String countQuery = "SELECT * FROM " + SearchHistoryDatabase.SEARCH_HISTORY_TABLE;
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        close();
        return count;
    }

}
