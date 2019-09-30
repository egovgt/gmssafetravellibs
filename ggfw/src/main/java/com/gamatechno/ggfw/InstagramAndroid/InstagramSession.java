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

package com.gamatechno.ggfw.InstagramAndroid;


import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.Context;

import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;

/**
 * Manage access token and user data. Token and user data data are stored in shared preferences.
 * 
 * @author Lorensius W. L T <lorenz@londatiga.net>
 *
 */
public class InstagramSession {
	private Context mContext;
	private SharedPreferences mSharedPref;
	
	private static final String SHARED = "Instagram_Preferences";
	private static final String USERID	= "userid";
	private static final String USERNAME = "username";
	private static final String FULLNAME = "fullname";
	private static final String PROFILPIC = "profilpic";
	private static final String ACCESS_TOKEN = "access_token";
	
	public InstagramSession(Context context) {
		mContext	= context;
		mSharedPref = context.getSharedPreferences(SHARED, Context.MODE_PRIVATE);
	}
	
	/**
	 * Save user data
	 * 
	 * @param user User data
	 */
	public void store(InstagramUser user) {
		Editor editor = mSharedPref.edit();
		
		editor.putString(ACCESS_TOKEN,  user.accessToken);
		editor.putString(USERID, 		user.id);
		editor.putString(USERNAME, 		user.username);
		editor.putString(FULLNAME, 		user.fullName);
		editor.putString(PROFILPIC, 	user.profilPicture);
		
		editor.commit();
	}
	
	/**
	 * Reset user data
	 */
	public void reset() {
		Editor editor = mSharedPref.edit();
		
		editor.putString(ACCESS_TOKEN, 	"");
		editor.putString(USERID, 		"");
		editor.putString(USERNAME, 		"");
		editor.putString(FULLNAME, 		"");
		editor.putString(PROFILPIC, 	"");
		
		editor.commit();
		
		CookieSyncManager.createInstance(mContext);
		
		CookieManager cookieManager = CookieManager.getInstance();
		cookieManager.removeAllCookie();
	}
	
	/**
	 * Get user data
	 * 
	 * @return User data
	 */
	public InstagramUser getUser() {
		if (mSharedPref.getString(ACCESS_TOKEN, "").equals("")) {
			return null;
		}
		
		InstagramUser user 	= new InstagramUser();
		
		user.id				= mSharedPref.getString(USERID, "");
		user.username		= mSharedPref.getString(USERNAME, "");
		user.fullName		= mSharedPref.getString(FULLNAME, "");
		user.profilPicture	= mSharedPref.getString(PROFILPIC, "");
		user.accessToken	= mSharedPref.getString(ACCESS_TOKEN, "");
		
		return user;
	}
	
	/**
	 * Get access token
	 * 
	 * @return Access token
	 */
	public String getAccessToken() {
		return mSharedPref.getString(ACCESS_TOKEN, "");
	}
	
	/**
	 * Check if ther is an active session.
	 * 
	 * @return true if active and vice versa
	 */
	public boolean isActive() {
		return (mSharedPref.getString(ACCESS_TOKEN, "").equals("")) ? false : true;
	}
}