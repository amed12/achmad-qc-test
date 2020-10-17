/*
 * *
 *  * Created by Achmad Fathullah on 10/17/20 12:17 PM
 *  * Copyright (c) 2020 . All rights reserved.
 *  * Last modified 10/17/20 12:17 PM
 *
 */

package id.co.santridev.simplechat.core.utils.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import id.co.santridev.simplechat.core.di.Injector
import id.co.santridev.simplechat.core.domain.usecase.IUserUseCase

class ViewModelFactory private constructor(private val userUseCase: IUserUseCase) :
    ViewModelProvider.NewInstanceFactory() {

    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null

        fun getInstance(context: Context): ViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: ViewModelFactory(
                    Injector.provideUserUseCase(context)
                )
            }
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        when {
//            modelClass.isAssignableFrom(?ViewModel::class.java) -> {
//                ?ViewModel(userUseCase) as T
//            }
            else -> throw Throwable("Unknown ViewModel class: " + modelClass.name)
        }
}