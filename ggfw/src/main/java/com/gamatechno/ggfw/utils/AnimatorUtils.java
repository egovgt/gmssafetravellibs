/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2019. Shendy Aditya Syamsudin
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

package com.gamatechno.ggfw.utils;

import android.animation.PropertyValuesHolder;

public class AnimatorUtils {

  public static final String ROTATION = "rotation";
  public static final String SCALE_X = "scaleX";
  public static final String SCALE_Y = "scaleY";
  public static final String TRANSLATION_X = "translationX";
  public static final String TRANSLATION_Y = "translationY";

  private AnimatorUtils() {
    //No instances.
  }

  public static PropertyValuesHolder rotation(float... values) {
    return PropertyValuesHolder.ofFloat(ROTATION, values);
  }

  public static PropertyValuesHolder translationX(float... values) {
    return PropertyValuesHolder.ofFloat(TRANSLATION_X, values);
  }

  public static PropertyValuesHolder translationY(float... values) {
    return PropertyValuesHolder.ofFloat(TRANSLATION_Y, values);
  }

  public static PropertyValuesHolder scaleX(float... values) {
    return PropertyValuesHolder.ofFloat(SCALE_X, values);
  }

  public static PropertyValuesHolder scaleY(float... values) {
    return PropertyValuesHolder.ofFloat(SCALE_Y, values);
  }
}
