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

package com.gamatechno.ggfw_ui.slider.Transformers;

import android.view.View;

import com.nineoldandroids.view.ViewHelper;

public class ZoomInTransformer extends BaseTransformer {

	@Override
	protected void onTransform(View view, float position) {
		final float scale = position < 0 ? position + 1f : Math.abs(1f - position);
		ViewHelper.setScaleX(view,scale);
        ViewHelper.setScaleY(view,scale);
        ViewHelper.setPivotX(view,view.getWidth() * 0.5f);
        ViewHelper.setPivotY(view,view.getHeight() * 0.5f);
        ViewHelper.setAlpha(view,position < -1f || position > 1f ? 0f : 1f - (scale - 1f));
	}

}
