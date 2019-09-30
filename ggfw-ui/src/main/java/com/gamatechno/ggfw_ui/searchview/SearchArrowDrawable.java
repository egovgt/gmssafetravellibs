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

import android.animation.ObjectAnimator;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.graphics.drawable.DrawerArrowDrawable;
import android.util.Property;
import android.view.animation.AccelerateDecelerateInterpolator;


class SearchArrowDrawable extends DrawerArrowDrawable {

    static final float STATE_HAMBURGER = 0.0f;
    static final float STATE_ARROW = 1.0f;
    private static final Property<SearchArrowDrawable, Float> PROGRESS = new Property<SearchArrowDrawable, Float>(Float.class, "progress") {
        @Override
        public void set(SearchArrowDrawable object, Float value) {
            object.setProgress(value);
        }

        @Override
        public Float get(SearchArrowDrawable object) {
            return object.getProgress();
        }
    };

    SearchArrowDrawable(Context context) {
        super(context);
        setColor(ContextCompat.getColor(context, android.R.color.black));
    }

    void animate(float state, int duration) {
        ObjectAnimator anim;
        if (state == STATE_ARROW) {
            anim = ObjectAnimator.ofFloat(this, PROGRESS, STATE_HAMBURGER, state);
        } else {
            anim = ObjectAnimator.ofFloat(this, PROGRESS, STATE_ARROW, state);
        }
        anim.setInterpolator(new AccelerateDecelerateInterpolator());
        anim.setDuration(duration);
        anim.start();
    }

}
