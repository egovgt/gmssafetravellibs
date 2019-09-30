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
import android.app.ActivityManager;
import android.content.ContentUris;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.DrawableRes;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.gamatechno.ggfw.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.gamatechno.ggfw.utils.FilePath.getDataColumn;

public class Functions {
    public static Calendar cal;
    public static SharedPreferences sp;

    public static String DateInMilis() {
        cal = Calendar.getInstance();
        String calender = String.valueOf(cal.get(Calendar.MILLISECOND));
        return calender;
    }

    public static void showVolleyError(Context context, VolleyError error) {
        if (error instanceof NetworkError) {
            ToastShort(context,context.getResources().getString(R.string.txt_network_error));
        } else if (error instanceof ServerError) {
            ToastShort(context,context.getResources().getString(R.string.txt_server_error));
        } else if (error instanceof NoConnectionError) {
            ToastShort(context,context.getResources().getString(R.string.txt_InfoConnection));
        } else if (error instanceof TimeoutError) {
            ToastShort(context,context.getResources().getString(R.string.txt_server_time_out));
        }
    }

    public static void showThrowwableError(Context context, Throwable error) {
        if (error instanceof NetworkError) {
            ToastShort(context,context.getResources().getString(R.string.txt_network_error));
        } else if (error instanceof ServerError) {
            ToastShort(context,context.getResources().getString(R.string.txt_server_error));
        } else if (error instanceof NoConnectionError) {
            ToastShort(context,context.getResources().getString(R.string.txt_InfoConnection));
        } else if (error instanceof TimeoutError) {
            ToastShort(context,context.getResources().getString(R.string.txt_server_time_out));
        }
    }

    public static void showRxError(Context context, Throwable error) {
        if (error instanceof NetworkError) {
            ToastShort(context,context.getResources().getString(R.string.txt_network_error));
        } else if (error instanceof ServerError) {
            ToastShort(context,context.getResources().getString(R.string.txt_server_error));
        } else if (error instanceof NoConnectionError) {
            ToastShort(context,context.getResources().getString(R.string.txt_InfoConnection));
        } else if (error instanceof TimeoutError) {
            ToastShort(context,context.getResources().getString(R.string.txt_server_time_out));
        }
    }

    public static void ToastShort(Context context, String text) {
        if(context!=null){
            Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
        }
    }

    public static void setDataIntTOSP(Context context, String to, int data) {
        Log.d("Function", "setDataIntTOSP: " + data + " to: "+ to);
        sp = context.getSharedPreferences("id.go.kemlu.safetravel"+ XDefConstant.PREF_NAME, 0);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt(to, data);
        editor.commit();
    }

    public static int getDataIntFromSP(Context context, String from) {
        sp = context.getSharedPreferences("id.go.kemlu.safetravel"+ XDefConstant.PREF_NAME, 0);
        return sp.getInt(from, 0);
    }

    public static boolean isNumeric(String number){
        if(number!=null){
            try {
                double d = Double.parseDouble(number);
            }catch (NumberFormatException e){
                return false;
            }
        }else{
            return false;
        }
        return true;
    }

    public static boolean isDatePassed(String date){
        if(!date.equalsIgnoreCase("")){
            date = date.replaceAll("-", "/");
            Log.d("isDatePassed", "isDatePassed: "+date);
            DateFormat new_date = new SimpleDateFormat("dd/MM/yyyy");
            date = new_date.toString();
            try {
                if(new SimpleDateFormat("dd/MM/yyyy").parse(date).before(new Date())){
                    return false;
                }else{
                    return true;
                }
            } catch (ParseException e) {
                e.printStackTrace();
                return false;
            }
        }else{
            return false;
        }
    }

    public static boolean isEmulator() {
        return Build.FINGERPRINT.startsWith("generic")
                || Build.FINGERPRINT.startsWith("unknown")
                || Build.MODEL.contains("google_sdk")
                || Build.MODEL.contains("Emulator")
                || Build.MODEL.contains("Android SDK built for x86")
                || Build.MANUFACTURER.contains("Genymotion")
                || (Build.BRAND.startsWith("generic") && Build.DEVICE.startsWith("generic"))
                || "google_sdk".equals(Build.PRODUCT);
    }

    public static boolean getDataBooleanFromSP(Context context, String from) {
        if(context!=null){
            sp = context.getSharedPreferences("id.go.kemlu.safetravel"+ XDefConstant.PREF_NAME, 0);
            return sp.getBoolean(from, false);
        }else{
            return false;
        }

    }

    public static void setDataStringToSP(Context context, String to, String data) {
        Log.d("Function", "setDataStringToSP: " + data + " to: "+ to);
        sp = context.getSharedPreferences("id.go.kemlu.safetravel"+ XDefConstant.PREF_NAME, 0);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(to, data);
        editor.commit();
    }

    public static String getDataStringFromSP(Context context, String from) {
        sp = context.getSharedPreferences("id.go.kemlu.safetravel"+ XDefConstant.PREF_NAME, 0);
        return sp.getString(from, "");
    }

    public static String getDataStringCountryFromSP(Context context, String from) {
        sp = context.getSharedPreferences(XDefConstant.PREF_NAME, 0);
        return sp.getString(from, "id");
    }

    public static void removeDataStringToSP(Context context, String to) {
        Log.d("Function", "removeDataStringToSP: " + to);
        sp = context.getSharedPreferences("id.go.kemlu.safetravel"+ XDefConstant.PREF_NAME, 0);
        SharedPreferences.Editor editor = sp.edit();
        editor.remove(to);
        editor.commit();
    }

    public static String encodeImageToBASE64(Bitmap bmp) {
        ByteArrayOutputStream bao = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, bao);
        byte[] byteArray = bao.toByteArray();
        return CustomBase64.encodeBytes(byteArray);
    }

    public static void setDataBooleanToSP(Context context, String to,
                                          boolean data) {
        Log.d("Function", "setDataBooleanToSP: " + data + " to: "+ to);
        sp = context.getSharedPreferences("id.go.kemlu.safetravel"+ XDefConstant.PREF_NAME, 0);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(to, data);
        editor.commit();
    }

    public static String hitungJarak(LatLng posA, LatLng posB) {
        float[] result = new float[1];
        Location.distanceBetween(posA.latitude, posA.longitude, posB.latitude,
                posB.longitude, result);

        double jarak = result[0];
        jarak = jarak/1000;

        //System.out.println(String.format("%,.2f", jarak) + " km");
        return String.format("%,.2f", jarak) + " km";
    }

    public static double angkaJarak(LatLng posA, LatLng posB) {
        float[] result = new float[1];
        Location.distanceBetween(posA.latitude, posA.longitude, posB.latitude,
                posB.longitude, result);

        return result[0];
    }

    public static void ToastLong(Context context, String text) {
        Toast.makeText(context, text, Toast.LENGTH_LONG).show();
    }

    public static void ToastNoConnection(Context context) {
        Toast.makeText(context, context.getResources().getString(R.string.txt_InfoConnection), Toast.LENGTH_SHORT).show();
    }

    public static void ToastLoggedIn(Context context) {
        Toast.makeText(context, "Logged in", Toast.LENGTH_SHORT).show();
    }

    public static void ToastComingSoon(Context context) {
        Toast.makeText(context, "Akan Segera Tersedia", Toast.LENGTH_SHORT).show();
    }

    /**
     * @author Shendy Aditya No internet Connection Handling
     * @param context
     * @return status
     */

    public static boolean isConnectedToInternet(Context context) {
        ConnectivityManager conn = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (conn != null) {
            NetworkInfo[] info = conn.getAllNetworkInfo();
            if (info != null)
                for (int i = 0; i < info.length; i++)
                    if (info[i].getState() == NetworkInfo.State.CONNECTED)
                        return true;
        }
        return false;
    }

    public static boolean isPreLolipop(){
        return Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP;
    }

    /**
     * Membaca JSON dari local file
     * @param context
     * @param name
     * @return
     */
    public static String loadJSONFromAsset(Context context, String name) {
        String json = null;
        try {
            InputStream is = context.getAssets().open(name+".json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    public static boolean isPermissionGranted(Context context, String permission, String permissionAccess){
        if (Build.VERSION.SDK_INT >= 23) {
            if (context.checkSelfPermission(permission)
                    == PackageManager.PERMISSION_GRANTED) {
//                android.util.Log.v(TAG, "Permission is granted");
                return true;
            } else {

//                android.util.Log.v(TAG, "Permission is revoked");
                ActivityCompat.requestPermissions((Activity) context, new String[]{permissionAccess}, 1);
                return false;
            }
        } else { //permission is automatically granted on sdk<23 upon installation
//            android.util.Log.v(TAG, "Permission is granted");
            return true;
        }
    }

    public static boolean hasPermissions(Context context, String... permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    public final static boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    public static Bitmap getBitmapFromURL(String src) {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            // Log exception
            return null;
        }
    }

    public static final String md5(final String s) {
        final String MD5 = "MD5";
        try {
            // Create MD5 Hash
            MessageDigest digest = MessageDigest
                    .getInstance(MD5);
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuilder hexString = new StringBuilder();
            for (byte aMessageDigest : messageDigest) {
                String h = Integer.toHexString(0xFF & aMessageDigest);
                while (h.length() < 2)
                    h = "0" + h;
                hexString.append(h);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static Uri getImageUri(Context ctx, ImageView imageView) {
        Drawable drawable = imageView.getDrawable();
        Bitmap bmp = null;
        if (drawable instanceof BitmapDrawable){
            bmp = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        } else {
            return null;
        }
        // Store image to default external storage directory
        Uri bmpUri = null;
        try {
            File file =  new File(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DOWNLOADS),"safetravel_"+ System.currentTimeMillis() + ".jpg");
            file.getParentFile().mkdirs();
            FileOutputStream out = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.close();
            //bmpUri = Uri.fromFile(file);
            bmpUri = FileProvider.getUriForFile(ctx, "id.go.kemlu.safetravel" + ".provider",file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(bmpUri);
        return bmpUri;
    }

    public static String getTypeFileFromUri(Uri uri){
        String type = "";
        Log.d("shit", "getTypeFile: "+uri.getPath());
        for(String x : getNameFromUri(uri).split("\\.")){
            type = x;
        }
        Log.d(".asdtype", "getFile: "+uri.getPath());
        Log.d(".asdtype", "getTypeFile: "+uri.getPath().split("\\.").length);
        Log.d(".asdtype", "getTypeFile: "+type);
        switch (type){
            case "pdf":
                type = "application/pdf";
                break;
            case "docx":
                type = "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
                break;
            case "jpg":
                type = "image/jpg";
                break;
            case "png":
                type = "image/png";
                break;
            case "mp4":
                type = "video/mp4";
                break;
            case "mkv":
                type = "video/x-matroska";
                break;
            default:
                type = "video/x-matroska";
                break;
        }
        return type;
    }

    public static String getNameFromUri(Uri uri){
        String name = "";
        String uriTemp = "";
            /*if(FilePath.isFile(this.uri)){
                uriTemp = this.uri.getPath();
            }*/
        uriTemp = uri.getPath();
            /*else{
                uriTemp = FilePath.getPath(context, this.uri);
            }*/
        for(String x : uriTemp.split("/")){
            name = x;
        }
        return name;
    }

    public static byte[] getBytesFileFromUri(Context context, Uri uri){
        Uri new_uri = Uri.fromFile(new File(uri.getPath()));
        Log.d(".asd", "getBytesFileFromUri: "+new_uri);

        InputStream iStream = null;
        byte[] inputData = null;
        try {
            iStream = context.getContentResolver().openInputStream(new_uri);
            ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
            int bufferSize = 1024;
            byte[] buffer = new byte[bufferSize];

            int len = 0;
            while ((len = iStream.read(buffer)) != -1) {
                byteBuffer.write(buffer, 0, len);
            }
            inputData = byteBuffer.toByteArray();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return inputData;
    }

    public static String getPath(final Context context, final Uri uri) {

        // check here to KITKAT or new version
        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {

                // ExternalStorageProvider
                if (isExternalStorageDocument(uri)) {
                    final String docId = DocumentsContract.getDocumentId(uri);
                    final String[] split = docId.split(":");
                    final String type = split[0];

                    if ("primary".equalsIgnoreCase(type)) {
                        return Environment.getExternalStorageDirectory() + "/"
                                + split[1];
                    }
                }
                // DownloadsProvider
                else if (isDownloadsDocument(uri)) {

                    final String id = DocumentsContract.getDocumentId(uri);
                    final Uri contentUri = ContentUris.withAppendedId(
                            Uri.parse("content://downloads/public_downloads"),
                            Long.valueOf(id));

                    return getDataColumn(context, contentUri, null, null);
                }
                // MediaProvider
                else if (isMediaDocument(uri)) {
                    final String docId = DocumentsContract.getDocumentId(uri);
                    final String[] split = docId.split(":");
                    final String type = split[0];

                    Uri contentUri = null;
                    if ("image".equals(type)) {
                        contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                    } else if ("video".equals(type)) {
                        contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                    } else if ("audio".equals(type)) {
                        contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                    }

                    final String selection = "_id=?";
                    final String[] selectionArgs = new String[] { split[1] };

                    return getDataColumn(context, contentUri, selection,
                            selectionArgs);
                }
                else if ("file".equalsIgnoreCase(uri.getScheme())) {
                    return uri.getPath();
                }
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {

            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();

            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }

    /**
     * @param uri
     *            The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri
                .getAuthority());
    }

    /**
     * @param uri
     *            The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri
                .getAuthority());
    }

    /**
     * @param uri
     *            The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri
                .getAuthority());
    }

    /**
     * @param uri
     *            The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri
                .getAuthority());
    }

    public static boolean isFile(Uri uri){
        String isUri = uri.toString().split(":")[0];
        if(isUri.equals("file"))
            return true;
        return false;
    }

    public static Calendar calendar_now(){
        Date date = new Date(); // your date
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal;
    }

    public static boolean isFormValid(Context context, View view, List<Integer> forms){
        boolean isValid = true;
        for(int id: forms)
        {
            EditText et = view.findViewById(id);
            if(TextUtils.isEmpty(et.getText().toString()))
            {
                et.setError("Wajib Diisi");
                isValid = false;
            }
        }
        return isValid;
    }

    private static String pattern = "^[a-z0-9_]*$";
    public static boolean isUsernameValid(String x){
        if(x.matches(pattern)){
            if(x.length()>=3){
                return true;
            }else{
                return false;
            }
        }else{
            return false;
        }
    }

    private static String number_pattern = "^[0-9]*$";
    public static boolean isNumberValid(String x){
        if(x.matches(number_pattern)){
            return true;
        }else{
            return false;
        }
    }

    private static String word_pattern = "^[A-Z]*$";
    public static boolean isWordValid(String x){
        if(x.matches(word_pattern)){
            return true;
        }else{
            return false;
        }
    }

    public static boolean isPassportTrue(String passport){
        boolean isTrue = false;
        for(int a=0;a < passport.length(); a++){
            if(a==0){
                if(isWordValid(String.valueOf(passport.charAt(a)))){
                    isTrue = true;
                }else{
                    return false;
                }
            }else{
                if(isNumberValid(String.valueOf(passport.charAt(a)))){
                    isTrue = true;
                }else{
                    return false;
                }
            }
        }
        return isTrue;
    }

    public static int toDP(Context context, int height){
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();

        float densityFloat = displayMetrics.density;
        int pixels = (int) (height * densityFloat + 0.5f); // 264 is height of AppBar Layout
        float heightDp = displayMetrics.heightPixels / densityFloat;
        return pixels;
    }

    public static boolean checkPlayServices(Context context) {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(context);
        if (resultCode != ConnectionResult.SUCCESS) {
            return false;
        }
        return true;
    }

    public static Bitmap getBitmap(int drawableRes, Activity activity) {
        Drawable drawable = activity.getResources().getDrawable(drawableRes);
        Canvas canvas = new Canvas();
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        canvas.setBitmap(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        drawable.draw(canvas);

        return bitmap;
    }

    public static boolean isMyServiceRunning(Context getContext, Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getContext.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                Log.i ("isMyServiceRunning", "isMyServiceRunning "+true+"");
                return true;
            }
        }
        Log.i ("isMyServiceRunning", "isMyServiceRunning "+false+"");
        return false;
    }

    public static BitmapDescriptor bitmapDescriptorFromVector(Context context, @DrawableRes int vectorDrawableResourceId) {
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorDrawableResourceId);
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }
}
