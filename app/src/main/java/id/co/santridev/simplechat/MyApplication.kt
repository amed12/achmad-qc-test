/*
 * *
 *  * Created by Achmad Fathullah on 10/16/20 3:43 PM
 *  * Copyright (c) 2020 . All rights reserved.
 *  * Last modified 10/16/20 3:43 PM
 *
 */

package id.co.santridev.simplechat

import android.app.Application
import com.qiscus.sdk.chat.core.QiscusCore

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        QiscusCore.setup(this, BuildConfig.QISCUS_SDK_APP_ID)
    }
}