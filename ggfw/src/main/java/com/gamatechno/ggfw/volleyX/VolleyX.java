/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2018. Shendy Aditya Syamsudin
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

package com.gamatechno.ggfw.volleyX;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.Volley;
import com.gamatechno.ggfw.volleyX.utils.Hack;


import java.util.concurrent.ExecutionException;

import rx.Observable;
import rx.functions.Func0;

/**
 * Created by yangwei on 2016/10/29.
 */
public class VolleyX {
    private static final String DEFAULT_LISTENER_FIELD = "mListener";
    private static final String DEFAULT_ERROR_LISTENER_FIELD = "mErrorListener";

    public static RequestQueue DEFAULT_REQUESTQUEUE;
    static RequestQueue sRequestQueue;
    static Context sContext;
    static boolean sInited = false;

    /**
     * Init the VolleyX enviroment
     *
     * @param context application context
     */
    public static void init(Context context) {
        if (context == null) throw new NullPointerException("context can not be null");
        sContext = context;

        DEFAULT_REQUESTQUEUE = Volley.newRequestQueue(sContext);
        sRequestQueue = DEFAULT_REQUESTQUEUE;

        sInited = true;
    }

    /**
     * Create request builder using volley request
     *
     * @param request volley request
     * @param <T> request type
     * @return builder for chain call
     */
    public static <T> Observable<T> from(final Request<T> request) {
        if (!sInited) throw new IllegalStateException("call init first");
        if (request == null) throw new NullPointerException("request can not be null");
        return Observable.defer(new Func0<Observable<T>>() {
            @Override
            public Observable<T> call() {
                try {
                    return Observable.just(generateData(request));
                } catch (InterruptedException | ExecutionException e) {
                    Log.e("", e.getMessage());
                    return Observable.error(e);
                }
            }
        });
    }

    /**
     * Create request builder using custom volley request
     *
     * @param request custom volley request
     * @param listernerField the field name of result callback listener
     * @param <T>
     * @return builder for chain call
     */
    public static <T> Observable<T> from(final Request<T> request, final String listernerField) {
        if (!sInited) throw new IllegalStateException("call init first");
        if (request == null) throw new NullPointerException("request can not be null");
        return Observable.defer(new Func0<Observable<T>>() {

            @Override
            public Observable<T> call() {
                try {
                    return Observable.just(generateData(request, listernerField));
                } catch (InterruptedException | ExecutionException e) {
                    Log.e("", e.getMessage());
                    return Observable.error(e);
                }
            }
        });
    }

    static <T> T generateData(Request<T> request) throws InterruptedException, ExecutionException {
        return generateData(request, DEFAULT_LISTENER_FIELD);
    }

    static <T> T generateData(Request<T> request, String listernerField) throws InterruptedException, ExecutionException {
        if (request == null) throw new NullPointerException("request can not be null");
        RequestFuture<T> future = getRequestFuture(request, listernerField);

        return future.get();
    }

    static <T> RequestFuture<T> getRequestFuture(Request<T> request, String listernerField) {
        if (request == null) throw new NullPointerException("request can not be null");
        RequestFuture<T> future = RequestFuture.newFuture();

        String listenerFieldName = TextUtils.isEmpty(listernerField)? DEFAULT_LISTENER_FIELD: listernerField;
        String errorListenerFieldName = DEFAULT_ERROR_LISTENER_FIELD;
        try {
            Hack.HackedClass hackedClass = Hack.into(request.getClass());
            hackedClass.field(listenerFieldName).set(request, future);
            hackedClass.field(errorListenerFieldName).set(request, future);
        } catch (Hack.HackDeclaration.HackAssertionException e) {
            throw new IllegalStateException("the field name of your class is not correct: " + e.getHackedFieldName());
        }

        sRequestQueue.add(request);
        return future;
    }

    /**
     * Set the volley request queue. If not set, the default request queue of volley is used.
     * restore the default volley request queue by calling setRequestQueue(VolleyX.DEFAULT_REQUESTQUEUE)}
     *
     * @param queue
     */
    public static void setRequestQueue(RequestQueue queue) {
        if (!sInited) throw new IllegalStateException("call init first");

        if (queue != null)
            sRequestQueue = queue;
    }
}
