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

import android.os.Parcel;
import android.os.Parcelable;


@SuppressWarnings({"WeakerAccess", "unused"})
public class SearchFilter implements Parcelable {

    public static final Creator<SearchFilter> CREATOR = new Creator<SearchFilter>() {
        @Override
        public SearchFilter createFromParcel(Parcel in) {
            return new SearchFilter(in);
        }

        @Override
        public SearchFilter[] newArray(int size) {
            return new SearchFilter[size];
        }
    };
    private String mTitle;
    private boolean mIsChecked;
    private String mTagId;

    public SearchFilter(String title, boolean checked) {
        this(title, checked, null);
    }

    public SearchFilter(String title, boolean checked, String tagId) {
        mTitle = title;
        mIsChecked = checked;
        mTagId = tagId;
    }

    protected SearchFilter(Parcel in) {
        mTitle = in.readString();
        mIsChecked = in.readByte() != 0;
        mTagId = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mTitle);
        dest.writeByte((byte) (mIsChecked ? 1 : 0));
        dest.writeString(mTagId);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public boolean isChecked() {
        return mIsChecked;
    }

    public void setChecked(boolean checked) {
        mIsChecked = checked;
    }

    public String getTagId() {
        return mTagId;
    }

    public void setTagId(String tagId) {
        mTagId = tagId;
    }

}
