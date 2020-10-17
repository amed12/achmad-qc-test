/*
 * *
 *  * Created by Achmad Fathullah on 10/18/20 4:59 AM
 *  * Copyright (c) 2020 . All rights reserved.
 *  * Last modified 10/18/20 4:58 AM
 *
 */

package id.co.santridev.simplechat

import android.app.Application
import com.qiscus.sdk.chat.core.QiscusCore
import com.vanniktech.emoji.EmojiManager
import com.vanniktech.emoji.one.EmojiOneProvider
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

        private fun initEmoji() {
            EmojiManager.install(EmojiOneProvider())
        }
    }

    override fun onCreate() {
        super.onCreate()
        QiscusCore.setup(this, BuildConfig.QISCUS_SDK_APP_ID)
        Nirmana.init(this)
        initEmoji()
    }

    fun getUserUseCase(): IUserUseCase = useCase
}