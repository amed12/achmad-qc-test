/*
 * *
 *  * Created by Achmad Fathullah on 10/18/20 12:48 PM
 *  * Copyright (c) 2020 . All rights reserved.
 *  * Last modified 10/18/20 12:48 PM
 *
 */

package id.co.santridev.simplechat.core.domain.usecase

import id.co.santridev.simplechat.core.domain.model.User
import id.co.santridev.simplechat.core.utils.Action

interface IUserUseCase {
    fun getCurrentUser(key: String, onSuccess: Action<User>, onError: Action<Throwable>)
    fun login(
        email: String,
        password: String,
        name: String,
        onSuccess: Action<User>,
        onError: Action<Throwable>
    )

    fun logOut()
}