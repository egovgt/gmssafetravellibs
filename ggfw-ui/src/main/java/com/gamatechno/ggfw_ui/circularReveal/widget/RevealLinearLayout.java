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

package com.gamatechno.ggfw_ui.circularReveal.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import com.gamatechno.ggfw_ui.circularReveal.animation.RevealViewGroup;
import com.gamatechno.ggfw_ui.circularReveal.animation.ViewRevealManager;

public class RevealLinearLayout extends LinearLayout implements RevealViewGroup {
  private ViewRevealManager manager;

  public RevealLinearLayout(Context context) {
    this(context, null);
  }

  public RevealLinearLayout(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public RevealLinearLayout(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs);
    manager = new ViewRevealManager();
  }

  @Override protected boolean drawChild(Canvas canvas, View child, long drawingTime) {
    try {
      canvas.save();

      manager.transform(canvas, child);
      return super.drawChild(canvas, child, drawingTime);
    } finally {
      canvas.restore();
    }
  }

  public void setViewRevealManager(ViewRevealManager manager) {
    if (manager == null) {
      throw new NullPointerException("ViewRevealManager is null");
    }

    this.manager = manager;
  }

  @Override public ViewRevealManager getViewRevealManager() {
    return manager;
  }
}
