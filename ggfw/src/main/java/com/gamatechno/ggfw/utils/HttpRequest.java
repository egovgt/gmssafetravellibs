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

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.gamatechno.ggfw.volleyX.VolleyX;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Map;

import cz.msebera.android.httpclient.Header;

import com.gamatechno.ggfw.core.BaseApplication;
import rx.Observer;
import rx.schedulers.Schedulers;

/**
 * Project     : SafeTravel
 * Company     : PT. Gamatechno Indonesia
 * File        : HttpRequest.java
 * User        : Shendy Aditya S. a.k.a xcod
 * Date        : 03 October 2017
 * Time        : 10:42 AM
 */
public class HttpRequest {

    public static void GET(final String URL, Context context, final OnGetRequest onGetRequest){
        final JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET,
                URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("asdResponse ", URL+" onResponse: "+response);
                onGetRequest.onSuccess(response);
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                onGetRequest.onFailure(error.toString());
            }
        }){

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Log.d("onRequest "+URL, "");
                return onGetRequest.requestHeaders();
            }

        };
        request.setShouldCache(false);
        BaseApplication.getInstance().addToRequestQueue(request, Functions.DateInMilis()+URL);
        onGetRequest.onPreExecuted();
    }


    public static void GET_STRING(String URL, final String key, Context context, final OnGetRequest onGetRequest){
        StringRequest request = new StringRequest(Request.Method.GET,
                URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //Functions.DEBUG("Format ORI", response);
                System.out.println(response);
//                Functions.DEBUG("Format PARAM", key);
//                Functions.DEBUG("Format PARAM", Functions.packageName());
                try {

                    //String resPlain = Base64.Respon(response,key);

                    //JSONArray jsonArray = new JSONArray(resPlain);
                    //JSONObject jsonObject = new JSONObject(resPlain);
                    JSONObject jsonObject = new JSONObject(response);
                    //jsonObject.put("response", "yeah");
                    //jsonObject.put("data",jsonArray);

                    onGetRequest.onSuccess(jsonObject);

                } catch (JSONException e) {
                    onGetRequest.onFailure(e.toString());
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                onGetRequest.onFailure(error.toString());
            }
        }){

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return onGetRequest.requestHeaders();
            }

        };
        request.setShouldCache(false);
        BaseApplication.getInstance().addToRequestQueue(request, Functions.DateInMilis()+URL);
        onGetRequest.onPreExecuted();
    }

    public interface OnGetRequest{

        void onPreExecuted();

        void onSuccess(JSONObject response);

        void onFailure(String error);

        Map<String, String> requestHeaders();
    }

    public static void POSTwithNoToast(final String URL, final Context context, final OnPostRequest onPostRequest){
        StringRequest request = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.d("asdResponse "+URL, "onResponse: "+response);
                            JSONObject jsonObject = new JSONObject(response);
                            onPostRequest.onSuccess(jsonObject);
                        } catch (JSONException e) {
                            Log.d("asdResponseFailure", "onResponse: "+URL);
                            onPostRequest.onFailure(e.toString());
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("asdResponseFailure ", "onResponse: "+URL+" "+error.getCause());
                String errorCode = "1012";
                if (error instanceof NetworkError) {
                    errorCode = "1012";
                } else if (error instanceof ServerError) {
                    errorCode = "503";
                } else if (error instanceof NoConnectionError) {
                    errorCode = "1019";
                } else if (error instanceof TimeoutError) {
                    errorCode = "522";
                }
                onPostRequest.onFailure(errorCode);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Log.d("onRequest "+URL, "onRequest: "+onPostRequest.requestParam());
                return onPostRequest.requestParam();
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return onPostRequest.requestHeaders();
            }
        };
        BaseApplication.getInstance().addToRequestQueue(request, Functions.DateInMilis()+URL);
        onPostRequest.onPreExecuted();


    }

    public static void POST(final String URL, final Context context, final OnPostRequest onPostRequest){
        StringRequest request = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.d("asdResponse "+URL, "onResponse: "+response);
                            JSONObject jsonObject = new JSONObject(response);
                            onPostRequest.onSuccess(jsonObject);
                        } catch (JSONException e) {
                            Log.d("asdResponseFailure", "onResponse: "+URL);
                            onPostRequest.onFailure(e.toString());
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("asdResponseFailure ", "onResponse: "+URL+" "+error.getCause());
                Functions.showVolleyError(context, error);
                String errorCode = "1012";
                if (error instanceof NetworkError) {
                    errorCode = "1012";
                } else if (error instanceof ServerError) {
                    errorCode = "503";
                } else if (error instanceof NoConnectionError) {
                    errorCode = "1019";
                } else if (error instanceof TimeoutError) {
                    errorCode = "522";
                }
                onPostRequest.onFailure(errorCode);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Log.d("onRequest "+URL, "onRequest: "+onPostRequest.requestParam());
                return onPostRequest.requestParam();
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return onPostRequest.requestHeaders();
            }
        };
        BaseApplication.getInstance().addToRequestQueue(request, Functions.DateInMilis()+URL);
        onPostRequest.onPreExecuted();


    }

    public static void POSTLoopJ(final String URL, final Context context, final OnPostLoopJRequest onPostRequest){
        AsyncHttpClient client = new AsyncHttpClient();


        client.post(URL, onPostRequest.requestParam(), new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    String str = new String(responseBody, "UTF-8");
                    Log.d("asdResponse "+URL, "onResponse: "+str);
                    JSONObject jsonObject = new JSONObject(str);
                    onPostRequest.onSuccess(jsonObject);
                } catch (JSONException e) {
                    Log.d("asdResponseFailure", "onResponse: "+URL);
                    onPostRequest.onFailure(e.toString());
                } catch (UnsupportedEncodingException e) {
                    Log.e("asdResponse "+URL, "onResponse: "+responseBody.length);
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Functions.showThrowwableError(context, error);
                Log.d("asdFailure", "onResponse: "+error);
                String errorCode = "1012";
                if (error instanceof NetworkError) {
                    errorCode = "1012";
                } else if (error instanceof ServerError) {
                    errorCode = "503";
                } else if (error instanceof NoConnectionError) {
                    errorCode = "1019";
                } else if (error instanceof TimeoutError) {
                    errorCode = "522";
                }
                onPostRequest.onFailure(errorCode);
            }
        });
        onPostRequest.onPreExecuted();
    }

    public static void RxPOST(final String URL, final Context context, final OnPostRequestRx onPostRequestRx){
        StringRequest request = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Log.d("onRequest "+URL, "onRequest: "+onPostRequestRx.requestParam());
                return onPostRequestRx.requestParam();
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return onPostRequestRx.requestHeaders();
            }
        };
        VolleyX.from(request).subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onCompleted() {
//                        Log.d(TAG, "onCompleted");
                        Log.d("asdResponse "+URL, "reactive: complete");
                        ((Activity)context).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                onPostRequestRx.onSuccess();
                            }
                        });
                    }

                    @Override
                    public void onError(Throwable error) {
//                        Log.d(TAG, "onError " + Log.getStackTraceString(e));
                        Log.d("asdResponseFailure ", "onResponse: "+URL);
                        Functions.showRxError(context, error);
                        String errorCode = "1012";
                        if (error instanceof NetworkError) {
                            errorCode = "1012";
                        } else if (error instanceof ServerError) {
                            errorCode = "503";
                        } else if (error instanceof NoConnectionError) {
                            errorCode = "1019";
                        } else if (error instanceof TimeoutError) {
                            errorCode = "522";
                        }
                        final String finalErrorCode = errorCode;
                        ((Activity)context).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                onPostRequestRx.onFailure(finalErrorCode);
                            }
                        });
                    }

                    @Override
                    public void onNext(String response) {
//                        Log.d(TAG, "onNext");
                        Log.d("asdResponse "+URL, "reactive: next"+response);
                        try {
                            Log.d("asdResponse "+URL, "onResponse: "+response);
                            JSONObject jsonObject = new JSONObject(response);
                            onPostRequestRx.onNext(jsonObject);
                        } catch (JSONException e) {
                            Log.d("asdResponseFailure", "onResponse: "+URL);
                            onPostRequestRx.onFailure(e.toString());
                        }
                    }
                });
        onPostRequestRx.onPreExecuted();
        BaseApplication.getInstance().addToRequestQueue(request, Functions.DateInMilis()+URL);
    }

    public static void POSTMultipart(final String URL, final Context context, final OnMultipartRequest onPostRequest){
        VolleyMultipartRequest multipartRequest = new VolleyMultipartRequest(Request.Method.POST, URL, new Response.Listener<NetworkResponse>() {
            @Override
            public void onResponse(NetworkResponse response) {
                String resultResponse = new String(response.data);
                try {
                    JSONObject result = new JSONObject(resultResponse);
                    onPostRequest.onSuccess(result);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                NetworkResponse networkResponse = error.networkResponse;
                Log.d("asdResponseFailure ", "onResponse: "+URL);
                Functions.showVolleyError(context, error);
                String errorCode = "1012";
                if (error instanceof NetworkError) {
                    errorCode = "1012";
                } else if (error instanceof ServerError) {
                    errorCode = "503";
                } else if (error instanceof NoConnectionError) {
                    errorCode = "1019";
                } else if (error instanceof TimeoutError) {
                    errorCode = "522";
                }
                onPostRequest.onFailure(errorCode);
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                return onPostRequest.requestParam();
            }

            @Override
            protected Map<String, DataPart> getByteData() {
                return onPostRequest.requestData();
            }
        };
//        multipartRequest.setRetryPolicy(new DefaultRetryPolicy( 30000, 1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        multipartRequest.setShouldCache(false);
        BaseApplication.getInstance().addToRequestQueue(multipartRequest, Functions.DateInMilis()+URL);
        onPostRequest.onPreExecuted();
    }

    public interface OnMultipartRequest{
        Map<String, VolleyMultipartRequest.DataPart> requestData();

        void onPreExecuted();

        void onSuccess(JSONObject response);

        void onFailure(String error);

        Map<String, String> requestParam();

    }

    public interface OnPostRequest{

        void onPreExecuted();

        void onSuccess(JSONObject response);

        void onFailure(String error);

        Map<String, String> requestParam();

        Map<String, String> requestHeaders();
    }

    public interface OnPostRequestRx{

        void onPreExecuted();

        void onNext(JSONObject response);

        void onSuccess();

        void onFailure(String error);

        Map<String, String> requestParam();

        Map<String, String> requestHeaders();
    }

    public interface OnPostLoopJRequest{

        void onPreExecuted();

        void onSuccess(JSONObject response);

        void onFailure(String error);

        RequestParams requestParam();
    }

}
