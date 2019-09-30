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

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;
import android.widget.Toast;

import com.gamatechno.ggfw.R;
import com.thin.downloadmanager.DefaultRetryPolicy;
import com.thin. downloadmanager.DownloadRequest;
import com.thin.downloadmanager.DownloadStatusListener;
import com.thin.downloadmanager.ThinDownloadManager;

import java.io.File;

import static com.facebook.FacebookSdk.getApplicationContext;

public class GTFWDownloadManager {
    private static com.thin.downloadmanager.DownloadManager downloadManager;
    private static Uri url, name;
    private static int image;
    private static Context context;
    private static GTFWDownloadManager gtfwDownloadManager;

    String TAG = "GTFWDownloader";

    public static GTFWDownloadManager getInstance(Context ctx){
        gtfwDownloadManager = new GTFWDownloadManager();
        downloadManager = new ThinDownloadManager();
        context = ctx;
        return gtfwDownloadManager;
    }

    public GTFWDownloadManager setUrl(String link, String nama){
        url = Uri.parse(link);
        name = Uri.parse(nama);
        return gtfwDownloadManager;
    }

    public GTFWDownloadManager setIcon(int img){
        image = img;
        return gtfwDownloadManager;
    }

    Intent intent;

    public void execute(){
        Toast.makeText(context, "Mendownload file...", Toast.LENGTH_SHORT).show();

        if (isSamsung()) {
            intent = context.getPackageManager()
                    .getLaunchIntentForPackage("com.sec.android.app.myfiles");
            intent.setAction("samsung.myfiles.intent.action.LAUNCH_MY_FILES");
            intent.putExtra("samsung.myfiles.intent.extra.START_PATH",
                    getDownloadsFile().getPath());
        }
        else
            intent = new Intent(android.app.DownloadManager.ACTION_VIEW_DOWNLOADS);
//        final Intent intent = new Intent(Intent.ACTION_VIEW);
//        File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
//        File file = new File(path, "pasporSafeTravel.jpg");
//        intent.setData(Uri.fromFile(path));

        final PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        getApplicationContext(),
                        0,
                        intent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );

        final NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

        final NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "1");
        builder.setContentTitle(name.toString())
                .setContentText("Download in progress")
                .setSmallIcon(image)
                .setPriority(NotificationCompat.PRIORITY_LOW);

        final DownloadRequest downloadRequest = new DownloadRequest(url)
                .setRetryPolicy(new DefaultRetryPolicy())
                .setDestinationURI(name).setPriority(DownloadRequest.Priority.HIGH)
                .setDownloadListener(new DownloadStatusListener() {
                    @Override
                    public void onDownloadComplete(int id) {
                        builder.setContentText("Download complete")
                                .setProgress(0,0,false)
                                .setAutoCancel(true)
                                .setContentIntent(resultPendingIntent);
                        notificationManager.notify(1, builder.build());
                        Toast.makeText(context, "Download selesai, Buka folder download", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onDownloadFailed(int id, int errorCode, String errorMessage) {
                        Toast.makeText(context, "Download gagal", Toast.LENGTH_SHORT).show();
                        builder.setContentText("Download Failed")
                                .setAutoCancel(true)
                                .setProgress(0,0,false);
                        notificationManager.notify(1, builder.build());
                        Log.d(TAG, "onDownloadFailed: "+errorMessage);
                    }

                    @Override
                    public void onProgress(int id, long totalBytes, long downlaodedBytes, int progress) {
                        int PROGRESS_MAX = 100;
                        /*builder.setContentText(""+progress+"% "+downlaodedBytes+" bytes downloaded")
                                .setProgress(PROGRESS_MAX, progress, false);
                        notificationManager.notify(1, builder.build());*/
                    }
                });

        downloadManager.add(downloadRequest);
        downloadManager.release();
    }

    public static boolean isSamsung() {
        String manufacturer = Build.MANUFACTURER;
        if (manufacturer != null) return manufacturer.toLowerCase().equals("samsung");
        return false;
    }

    public static File getDownloadsFile() {
        return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
    }

}
