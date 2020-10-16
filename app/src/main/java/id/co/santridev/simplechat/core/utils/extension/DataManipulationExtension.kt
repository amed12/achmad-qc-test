/*
 * *
 *  * Created by Achmad Fathullah on 10/17/20 1:47 AM
 *  * Copyright (c) 2020 . All rights reserved.
 *  * Last modified 10/17/20 1:47 AM
 *
 */

package id.co.santridev.simplechat.core.utils.extension

fun <T> anyNotNull(vararg elements: T) = elements.any { it != null && it != "" }