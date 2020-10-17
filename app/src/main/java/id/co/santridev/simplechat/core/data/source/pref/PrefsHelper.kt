/*
 * *
 *  * Created by Achmad Fathullah on 10/17/20 11:58 AM
 *  * Copyright (c) 2020 . All rights reserved.
 *  * Last modified 10/17/20 11:57 AM
 *
 */

package id.co.santridev.simplechat.core.data.source.pref

import android.content.Context
import android.content.SharedPreferences

object PrefsHelper {
    private const val PREFS_NAME = "user"

    fun init(context: Context): SharedPreferences =
        context.applicationContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
}