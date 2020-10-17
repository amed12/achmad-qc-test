/*
 * *
 *  * Created by Achmad Fathullah on 10/17/20 9:12 AM
 *  * Copyright (c) 2020 . All rights reserved.
 *  * Last modified 10/17/20 9:11 AM
 *
 */

package id.co.santridev.simplechat

import android.app.Application
import com.qiscus.sdk.chat.core.QiscusCore
import id.co.santridev.simplechat.core.domain.usecase.IUserUseCase
import id.co.santridev.simplechat.core.utils.dialog.LoadingDialog

class MyApplication : Application() {

    companion object {
        @Volatile
        private var instance: MyApplication? = null

        fun getInstance(): MyApplication =
            instance ?: synchronized(this) {
                instance ?: MyApplication()
            }
    }

    override fun onCreate() {
        super.onCreate()
        QiscusCore.setup(this, BuildConfig.QISCUS_SDK_APP_ID)
    }

    fun getUserUseCase(): IUserUseCase = AppComponent(this).getUserUseCase()
    fun getLoadingDialog(): LoadingDialog = AppComponent(this).getLoadingDialog()
}