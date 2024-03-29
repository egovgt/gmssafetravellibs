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

package com.gamatechno.ggfw_ui.circularReveal.animation;

import android.animation.Animator;
import android.view.View;

import com.gamatechno.ggfw_ui.BuildConfig;
import com.gamatechno.ggfw_ui.circularReveal.animation.ViewRevealManager.ChangeViewLayerTypeAdapter;

import static android.os.Build.VERSION.SDK_INT;
import static android.os.Build.VERSION_CODES.LOLLIPOP;
import static  com.gamatechno.ggfw_ui.circularReveal.animation.ViewRevealManager.RevealValues;

public final class ViewAnimationUtils {
  private final static boolean DEBUG = BuildConfig.DEBUG;

  private final static boolean LOLLIPOP_PLUS = SDK_INT >= LOLLIPOP;

  /**
   * Returns an Animator which can animate a clipping circle.
   * <p>
   * Any shadow cast by the View will respect the circular clip from this animator.
   * <p>
   * Only a single non-rectangular clip can be applied on a View at any time.
   * Views clipped by a circular reveal animation take priority over
   * {@link View#setClipToOutline(boolean) View Outline clipping}.
   * <p>
   * Note that the animation returned here is a one-shot animation. It cannot
   * be re-used, and once started it cannot be paused or resumed.
   *
   * @param view The View will be clipped to the clip circle.
   * @param centerX The x coordinate of the center of the clip circle.
   * @param centerY The y coordinate of the center of the clip circle.
   * @param startRadius The starting radius of the clip circle.
   * @param endRadius The ending radius of the clip circle.
   */
  public static Animator createCircularReveal(View view, int centerX, int centerY,
      float startRadius, float endRadius) {

    return createCircularReveal(view, centerX, centerY, startRadius, endRadius,
        View.LAYER_TYPE_SOFTWARE);
  }

  /**
   * Returns an Animator which can animate a clipping circle.
   * <p>
   * Any shadow cast by the View will respect the circular clip from this animator.
   * <p>
   * Only a single non-rectangular clip can be applied on a View at any time.
   * Views clipped by a circular reveal animation take priority over
   * {@link View#setClipToOutline(boolean) View Outline clipping}.
   * <p>
   * Note that the animation returned here is a one-shot animation. It cannot
   * be re-used, and once started it cannot be paused or resumed.
   *
   * @param view The View will be clipped to the clip circle.
   * @param centerX The x coordinate of the center of the clip circle.
   * @param centerY The y coordinate of the center of the clip circle.
   * @param startRadius The starting radius of the clip circle.
   * @param endRadius The ending radius of the clip circle.
   * @param layerType View layer type {@link View#LAYER_TYPE_HARDWARE} or {@link
   * View#LAYER_TYPE_SOFTWARE}
   */
  public static Animator createCircularReveal(View view, int centerX, int centerY,
      float startRadius, float endRadius, int layerType) {

    if (!(view.getParent() instanceof RevealViewGroup)) {
      throw new IllegalArgumentException("Parent must be instance of RevealViewGroup");
    }

    final RevealViewGroup viewGroup = (RevealViewGroup) view.getParent();
    final ViewRevealManager rm = viewGroup.getViewRevealManager();

    if (!rm.overrideNativeAnimator() && LOLLIPOP_PLUS) {
      return android.view.ViewAnimationUtils.createCircularReveal(view, centerX, centerY,
          startRadius, endRadius);
    }

    final RevealValues viewData = new RevealValues(view, centerX, centerY, startRadius, endRadius);
    final Animator animator = rm.dispatchCreateAnimator(viewData);

    if (layerType != view.getLayerType()) {
      animator.addListener(new ChangeViewLayerTypeAdapter(viewData, layerType));
    }

    return animator;
  }
}
