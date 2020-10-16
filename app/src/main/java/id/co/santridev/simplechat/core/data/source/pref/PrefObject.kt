/*
 * *
 *  * Created by Achmad Fathullah on 10/16/20 9:36 PM
 *  * Copyright (c) 2020 . All rights reserved.
 *  * Last modified 10/16/20 9:36 PM
 *
 */

package id.co.santridev.simplechat.core.data.source.pref

import id.co.santridev.simplechat.core.domain.model.User

interface PrefObject {
    fun setCurrentUser(user: User)
    fun getCurrentUser(): User
}