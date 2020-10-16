/*
 * *
 *  * Created by Achmad Fathullah on 10/16/20 11:05 PM
 *  * Copyright (c) 2020 . All rights reserved.
 *  * Last modified 10/16/20 11:05 PM
 *
 */

package id.co.santridev.simplechat.core.data.source

import android.content.Context
import id.co.santridev.simplechat.core.data.source.pref.PreferenceHelper
import id.co.santridev.simplechat.core.domain.model.User
import id.co.santridev.simplechat.core.domain.repository.IUserRepository
import id.co.santridev.simplechat.core.utils.Action

class UserRepository private constructor(private val context: Context) : IUserRepository {
    private val preferenceHelpers by lazy {
        PreferenceHelper(context)
    }

    override fun setCurrentUser(user: User) {
        preferenceHelpers.setCurrentUser(user)
    }

    override fun getCurrentUser(): User = preferenceHelpers.getCurrentUser()
    override fun login(
        email: String,
        password: String,
        name: String,
        onSuccess: Action<User>,
        onError: Action<Throwable>
    ) {
        TODO("Not yet implemented")
    }
}