/*
 * *
 *  * Created by Achmad Fathullah on 10/17/20 1:12 AM
 *  * Copyright (c) 2020 . All rights reserved.
 *  * Last modified 10/17/20 1:12 AM
 *
 */

package id.co.santridev.simplechat.core.domain.repository

import id.co.santridev.simplechat.core.domain.model.User
import id.co.santridev.simplechat.core.utils.Action

interface IUserRepository {
    fun getCurrentUser(onSuccess: Action<User>, onError: Action<Throwable>)
    fun login(
        email: String,
        password: String,
        name: String,
        onSuccess: Action<User>,
        onError: Action<Throwable>
    )
}