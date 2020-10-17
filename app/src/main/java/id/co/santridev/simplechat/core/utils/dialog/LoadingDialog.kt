/*
 * *
 *  * Created by Achmad Fathullah on 10/17/20 9:12 AM
 *  * Copyright (c) 2020 . All rights reserved.
 *  * Last modified 10/17/20 9:08 AM
 *
 */

package id.co.santridev.simplechat.core.utils.dialog

import android.app.Dialog
import android.content.Context
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import id.co.santridev.simplechat.R
import id.co.santridev.simplechat.core.utils.extension.lifecycleOwner

class LoadingDialog(context: Context) : Dialog(context) {
    init {
        window?.setBackgroundDrawableResource(android.R.color.transparent)
        setContentView(R.layout.dialog_loading)
    }

    private inner class ModuleLifecycleObserver : LifecycleObserver {
        fun addObserver(lifecycle: Lifecycle) = lifecycle.addObserver(this)

        @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
        fun onDestroy() = dismiss()

        @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
        fun onPause() = dismiss()
    }

    private fun addLifecycleObserver(lifecycle: Lifecycle) {
        ModuleLifecycleObserver().addObserver(lifecycle)
    }

    fun show(isShown: Boolean, isCancelable: Boolean = false) {
        if (isShown) {
            setCancelable(isCancelable)
            super.show()
            context.lifecycleOwner()?.lifecycle?.let { addLifecycleObserver(it) }
        } else super.dismiss()


    }

    override fun show() {
        show(true)
    }
}