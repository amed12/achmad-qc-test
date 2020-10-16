/*
 * *
 *  * Created by Achmad Fathullah on 10/16/20 11:47 PM
 *  * Copyright (c) 2020 . All rights reserved.
 *  * Last modified 10/16/20 11:47 PM
 *
 */

package id.co.santridev.simplechat.core.domain.usecase

import id.co.santridev.simplechat.core.domain.model.User
import id.co.santridev.simplechat.core.utils.Action

interface IUserUseCase {
    fun setCurrentUser(user: User)
    fun getCurrentUser(): User
    fun login(
        email: String,
        password: String,
        name: String,
        onSuccess: Action<User>,
        onError: Action<Throwable>
    )
}