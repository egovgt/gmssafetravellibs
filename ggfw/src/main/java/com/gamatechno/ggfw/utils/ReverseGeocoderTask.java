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

import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.util.List;

/**
 * Project     : SafeTravel
 * Company     : PT. Gamatechno Indonesia
 * File        : ReverseGeocoderTask.java
 * User        : Shendy Aditya S. a.k.a xcod
 * Date        : 27 November 2017
 * Time        : 14.37
 */
public class ReverseGeocoderTask extends AsyncTask<Void, Void, String> {
    private static final String TAG = "ReverseGeocoder";
    public interface Callback {
        void onComplete(String location);
    }
    private Geocoder mGeocoder;
    private float mLat;
    private float mLng;
    private Callback mCallback;
    public ReverseGeocoderTask(Geocoder geocoder, float[] latlng,
                               Callback callback) {
        mGeocoder = geocoder;
        mLat = latlng[0];
        mLng = latlng[1];
        mCallback = callback;
    }
    @Override
    protected String doInBackground(Void... params) {
        String value = "";
        try {
            List<Address> address =
                    mGeocoder.getFromLocation(mLat, mLng, 1);
            StringBuilder sb = new StringBuilder();
            for (Address addr : address) {
                int index = addr.getMaxAddressLineIndex();
                sb.append(addr.getAddressLine(index));
            }
            value = sb.toString();
        } catch (IOException ex) {
            value = "";
            Log.e(TAG, "Geocoder exception: ", ex);
        } catch (RuntimeException ex) {
            value = "";
            Log.e(TAG, "Geocoder exception: ", ex);
        }
        return value;
    }
    @Override
    protected void onPostExecute(String location) {
        mCallback.onComplete(location);
    }
}