/*
 * *
 *  * Created by Achmad Fathullah on 10/18/20 4:59 AM
 *  * Copyright (c) 2020 . All rights reserved.
 *  * Last modified 10/18/20 4:58 AM
 *
 */

package id.co.santridev.simplechat.core.utils.extension

import android.content.Context
import android.content.ContextWrapper
import android.widget.Toast
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

fun Context.showToast(string: String) {
    Toast.makeText(this, string, Toast.LENGTH_SHORT).show()
}