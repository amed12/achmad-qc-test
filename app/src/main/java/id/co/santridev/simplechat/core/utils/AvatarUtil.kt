/*
 * *
 *  * Created by Achmad Fathullah on 10/16/20 11:17 PM
 *  * Copyright (c) 2020 . All rights reserved.
 *  * Last modified 10/16/20 11:17 PM
 *
 */

package id.co.santridev.simplechat.core.utils

object AvatarUtil {
    fun generateAvatar(s: String): String {
        s.replace("\\s".toRegex(), "")
        return "https://robohash.org/$s/bgset_bg2/3.14160?set=set4"
    }
}