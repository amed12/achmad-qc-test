/*
 * *
 *  * Created by Achmad Fathullah on 10/16/20 10:57 PM
 *  * Copyright (c) 2020 . All rights reserved.
 *  * Last modified 10/16/20 10:40 PM
 *
 */

package id.co.santridev.simplechat.core.domain.repository

import id.co.santridev.simplechat.core.domain.model.User

interface IUserRepository {
    fun setCurrentUser(user: User)
    fun getCurrentUser(): User
}