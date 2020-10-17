/*
 * *
 *  * Created by Achmad Fathullah on 10/17/20 9:12 AM
 *  * Copyright (c) 2020 . All rights reserved.
 *  * Last modified 10/17/20 9:08 AM
 *
 */

package id.co.santridev.simplechat.core.utils.extension

import android.content.Context
import android.content.ContextWrapper
import androidx.lifecycle.LifecycleOwner

fun Context.lifecycleOwner(): LifecycleOwner? {
    var curContext = this
    var maxDepth = 20
    while (maxDepth-- > 0 && curContext !is LifecycleOwner) {
        curContext = (curContext as ContextWrapper).baseContext
    }
    return if (curContext is LifecycleOwner) {
        curContext
    } else {
        null
    }
}