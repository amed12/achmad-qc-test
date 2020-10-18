/*
 * *
 *  * Created by Achmad Fathullah on 10/16/20 9:44 PM
 *  * Copyright (c) 2020 . All rights reserved.
 *  * Last modified 10/16/20 9:44 PM
 *
 */

package id.co.santridev.simplechat.core.utils

interface Action<T> {
    fun call(t: T)
}