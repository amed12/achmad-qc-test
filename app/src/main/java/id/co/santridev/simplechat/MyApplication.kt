/*
 * *
 *  * Created by Achmad Fathullah on 10/17/20 10:33 PM
 *  * Copyright (c) 2020 . All rights reserved.
 *  * Last modified 10/17/20 10:01 PM
 *
 */

package id.co.santridev.simplechat

import android.app.Application
import com.qiscus.sdk.chat.core.QiscusCore
import id.co.santridev.simplechat.core.di.Injector
import id.co.santridev.simplechat.core.domain.usecase.IUserUseCase
import id.co.santridev.simplechat.core.utils.extension.Nirmana


class MyApplication : Application() {
    private val useCase: IUserUseCase by lazy { Injector.provideUserUseCase(this) }

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
        Nirmana.init(this)
    }

    fun getUserUseCase(): IUserUseCase = useCase
}