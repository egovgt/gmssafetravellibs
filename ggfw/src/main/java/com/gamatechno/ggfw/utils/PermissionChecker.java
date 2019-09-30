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
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;

import java.util.ArrayList;


public class PermissionChecker {

    private final int REQUEST_MULTIPLE_PERMISSION = 100;
    private VerifyPermissionsCallback callbackMultiple;

    public void verifyPermissions(Activity activity, @NonNull String[] permissions, VerifyPermissionsCallback callback) {
        String[] denyPermissions = getDenyPermissions(activity, permissions);
        if (denyPermissions.length > 0) {
            ActivityCompat.requestPermissions(activity, denyPermissions, REQUEST_MULTIPLE_PERMISSION);
            this.callbackMultiple = callback;
        } else {
            if (callback != null) {
                callback.onPermissionAllGranted();
            }
        }
    }

    private String[] getDenyPermissions(@NonNull Context context, @NonNull String[] permissions) {
        ArrayList<String> denyPermissions = new ArrayList<>();
        for (String permission : permissions) {
            if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                denyPermissions.add(permission);
            }
        }
        return denyPermissions.toArray(new String[0]);
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_MULTIPLE_PERMISSION:
                if (grantResults.length > 0 && callbackMultiple != null) {
                    ArrayList<String> denyPermissions = new ArrayList<>();
                    int i = 0;
                    for (String permission : permissions) {
                        if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                            denyPermissions.add(permission);
                        }
                        i++;
                    }
                    if (denyPermissions.size() == 0) {
                        callbackMultiple.onPermissionAllGranted();
                    } else {
                        callbackMultiple.onPermissionDeny(denyPermissions.toArray(new String[0]));
                    }
                }
                break;
        }
    }

    public static boolean hasPermissions(@NonNull Context context, @NonNull String[] permissions) {
        for (String permission : permissions) {
            if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    public interface VerifyPermissionsCallback {
        void onPermissionAllGranted();

        void onPermissionDeny(String[] permissions);
    }
}
