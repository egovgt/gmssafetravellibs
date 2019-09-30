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

package com.gamatechno.ggfw.core;


import android.support.multidex.MultiDexApplication;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.crashlytics.android.Crashlytics;
import com.gamatechno.ggfw.utils.Log;

import cat.ereza.customactivityoncrash.config.CaocConfig;
import io.fabric.sdk.android.Fabric;
//import io.realm.Realm;
//import io.realm.RealmConfiguration;

/**
 * Project     : SafeTravel
 * Company     : PT. Gamatechno Indonesia
 * File        : BaseApplication.java
 * User        : Shendy Aditya S. a.k.a xcod
 * Date        : 18 September 2017
 * Time        : 5:49 PM
 */

public class BaseApplication extends MultiDexApplication {

    private static final int TIMEOUT_MS = 25000; // 45second

    private RequestQueue requestQueue;
    private static BaseApplication instance;

    public BaseApplication() {
        super();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());
        /*Realm.init(this);
        RealmConfiguration config = new RealmConfiguration.Builder()
                .name(Realm.DEFAULT_REALM_NAME)
                .schemaVersion(0)
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(config);*/
        CaocConfig.Builder.create()
                .backgroundMode(CaocConfig.BACKGROUND_MODE_SILENT) //default: CaocConfig.BACKGROUND_MODE_SHOW_CUSTOM
//                .enabled(false) //default: true
                .showErrorDetails(false) //default: true
                //.showRestartButton(false) //default: true
                .trackActivities(true) //default: false
                //.minTimeBetweenCrashesMs(2000) //default: 3000
                //.errorDrawable(com.gamatechno.ggfw_ui.R.drawable.customactivityoncrash_error_image) //default: bug image
                //.restartActivity(LandingActivity.class) //default: null (your app's launch activity)
                //.errorActivity(CustomErrorHandlerActivity.class) //default: null (default error activity)
                //.eventListener(new YourCustomEventListener()) //default: null
                .apply();

        instance = this;

/*        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/Montserrat-Regular.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );*/
    }

    public static synchronized BaseApplication getInstance() {
        return instance;
    }

    /**
     * Get RequestQueue
     *
     * @return
     */
    public RequestQueue getRequestQueue() {
        Log.DEBUG("BaseApplication", "getRequestQueue : ");
        if (requestQueue == null) {
            Log.DEBUG("BaseApplication",
                    "getRequestQueue : make new instance ");
            requestQueue = Volley.newRequestQueue(getApplicationContext());
        }
        return requestQueue;
    }

    /**
     * add to request using tag
     *
     * @param request
     * @param tag
     */
    public <T> void addToRequestQueue(Request<T> request, String tag) {
        request.setTag(tag);

        // set retry policy
        request.setRetryPolicy(new DefaultRetryPolicy(TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        request.setShouldCache(false);

        Log.DEBUG("BaseApplication",
                "addToRequestQueue : " + request.getUrl());
        getRequestQueue().add(request);
    }

    public void cancelPendingRequest() {
        if (requestQueue != null)
            requestQueue.cancelAll(new RequestQueue.RequestFilter() {
                @Override
                public boolean apply(Request<?> request) {
                    return true;
                }
            });
    }

    public void cancelPendingRequest(Object tag) {
        if (requestQueue != null)
            requestQueue.cancelAll(tag);
    }


}
