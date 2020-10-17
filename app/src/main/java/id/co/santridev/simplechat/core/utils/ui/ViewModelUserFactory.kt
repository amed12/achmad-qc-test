/*
 * *
 *  * Created by Achmad Fathullah on 10/17/20 12:30 PM
 *  * Copyright (c) 2020 . All rights reserved.
 *  * Last modified 10/17/20 12:30 PM
 *
 */

package id.co.santridev.simplechat.core.utils.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import id.co.santridev.simplechat.core.di.Injector
import id.co.santridev.simplechat.core.domain.usecase.IUserUseCase
import id.co.santridev.simplechat.login.LoginViewModel

class ViewModelUserFactory private constructor(private val userUseCase: IUserUseCase) :
    ViewModelProvider.NewInstanceFactory() {

    companion object {
        @Volatile
        private var instance: ViewModelUserFactory? = null

        fun getInstance(context: Context): ViewModelUserFactory =
            instance ?: synchronized(this) {
                instance ?: ViewModelUserFactory(
                    Injector.provideUserUseCase(context)
                )
            }
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        when {
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(userUseCase) as T
            }
            else -> throw Throwable("Unknown ViewModel class: " + modelClass.name)
        }
}