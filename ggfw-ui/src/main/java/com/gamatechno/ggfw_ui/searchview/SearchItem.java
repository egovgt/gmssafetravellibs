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

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;

import com.gamatechno.ggfw_ui.R;


@SuppressWarnings({"WeakerAccess", "SameParameterValue", "unused"})
public class SearchItem implements Parcelable {

    public static final Creator<SearchItem> CREATOR = new Creator<SearchItem>() {
        @Override
        public SearchItem createFromParcel(Parcel in) {
            return new SearchItem(in);
        }

        @Override
        public SearchItem[] newArray(int size) {
            return new SearchItem[size];
        }
    };
    private Drawable drawable;
    private int resource;
    private CharSequence text;
    private String tag;

    public SearchItem() {
    }

    public SearchItem(CharSequence text) {
        this(R.drawable.ic_search_black_24dp, text, null);
    }

    public SearchItem(CharSequence text, String tag) {
        this(R.drawable.ic_search_black_24dp, text, tag);
    }

    public SearchItem(int resource, CharSequence text) {
        this(resource, text, null);
    }

    public SearchItem(int resource, CharSequence text, String tag) {
        this.resource = resource;
        this.text = text;
        this.tag = tag;
    }

    public SearchItem(Drawable icon, CharSequence text) {
        this(icon, text, null);
    }

    public SearchItem(Drawable icon, CharSequence text, String tag) {
        this.drawable = icon;
        this.text = text;
        this.tag = tag;
    }

    public SearchItem(Parcel in) {
        this.resource = in.readInt();
        this.text = in.readParcelable(CharSequence.class.getClassLoader());
        this.tag = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
        dest.writeParcelable(bitmap, flags);
        dest.writeString(this.tag);
        dest.writeString(this.text.toString());
        dest.writeInt(this.resource);
        // TextUtils.writeToParcel(this.text, dest, flags);
        // writeValue
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public int getIconResource() {
        return this.resource;
    }

    public void setIconResource(int resource) {
        this.resource = resource;
    }

    public Drawable getIconDrawable() {
        return this.drawable;
    }

    public void setIconDrawable(Drawable drawable) {
        this.drawable = drawable;
    }

    public CharSequence getText() {
        return this.text;
    }

    public void setText(CharSequence text) {
        this.text = text;
    }

    public String getTag() {
        return this.tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

}
