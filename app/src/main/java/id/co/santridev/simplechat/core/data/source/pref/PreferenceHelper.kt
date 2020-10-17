/*
 * *
 *  * Created by Achmad Fathullah on 10/17/20 11:58 AM
 *  * Copyright (c) 2020 . All rights reserved.
 *  * Last modified 10/17/20 11:57 AM
 *
 */

package id.co.santridev.simplechat.core.data.source.pref

import android.content.Context
import com.google.gson.FieldNamingPolicy
import com.google.gson.GsonBuilder
import id.co.santridev.simplechat.core.domain.model.User

class PreferenceHelper(context: Context) : PrefObject {
    private val mPrefs by lazy {
        context.getSharedPreferences(
            "user",
            Context.MODE_PRIVATE
        )
    }
    private val gson =
        GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .create()

    override fun setCurrentUser(user: User) {
        mPrefs?.edit()?.putString("current_user", gson?.toJson(user))?.apply()
    }


    override fun getCurrentUser(): User = gson.fromJson(
        mPrefs?.getString("current_user", ""),
        User::class.java
    )


}