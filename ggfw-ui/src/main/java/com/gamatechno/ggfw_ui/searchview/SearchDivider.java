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
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;


@SuppressWarnings({"WeakerAccess", "unused"})
public class SearchDivider extends RecyclerView.ItemDecoration {

    private static final int[] ATTRS = new int[]{android.R.attr.listDivider};
    private Drawable mDivider;
    private int mDividerHeight;
    private int mDividerWidth;

    public SearchDivider(Context context) {
        final TypedArray a = context.obtainStyledAttributes(ATTRS);
        setDivider(a.getDrawable(0));
        a.recycle();
    }

    public void setDivider(Drawable divider) {
        mDivider = divider;
        mDividerHeight = divider == null ? 0 : divider.getIntrinsicHeight();
        mDividerWidth = divider == null ? 0 : divider.getIntrinsicWidth();
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        if (mDivider == null) {
            super.getItemOffsets(outRect, view, parent, state);
            return;
        }

        final int position = ((RecyclerView.LayoutParams) view.getLayoutParams()).getViewLayoutPosition();
        final boolean firstItem = position == 0;
        final boolean dividerBefore = !firstItem;

        if (getOrientation(parent) == LinearLayoutManager.VERTICAL) {
            outRect.top = dividerBefore ? mDividerHeight : 0;
            outRect.bottom = 0;
        } else {
            outRect.left = dividerBefore ? mDividerWidth : 0;
            outRect.right = 0;
        }
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        if (mDivider == null) {
            super.onDraw(c, parent, state);
            return;
        }

        int left = 0;
        int right = 0;
        int top = 0;
        int bottom = 0;

        final int orientation = getOrientation(parent);
        final int childCount = parent.getChildCount();

        final boolean vertical = orientation == LinearLayoutManager.VERTICAL;
        final int size;
        if (vertical) {
            size = mDividerHeight;
            left = parent.getPaddingLeft();
            right = parent.getWidth() - parent.getPaddingRight();
        } else {
            size = mDividerWidth;
            top = parent.getPaddingTop();
            bottom = parent.getHeight() - parent.getPaddingBottom();
        }

        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
            final int position = params.getViewLayoutPosition();
            if (position == 0) {
                continue;
            }
            if (vertical) {
                top = child.getTop() - params.topMargin - size;
                bottom = top + size;
            } else {
                left = child.getLeft() - params.leftMargin - size;
                right = left + size;
            }
            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(c);
        }
    }

    private int getOrientation(RecyclerView parent) {
        final RecyclerView.LayoutManager lm = parent.getLayoutManager();
        if (lm instanceof LinearLayoutManager) {
            return ((LinearLayoutManager) lm).getOrientation();
        } else {
            throw new IllegalStateException("Use only with a LinearLayoutManager!");
        }
    }

}
