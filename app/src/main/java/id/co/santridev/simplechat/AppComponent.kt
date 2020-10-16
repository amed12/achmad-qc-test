/*
 * *
 *  * Created by Achmad Fathullah on 10/17/20 12:20 AM
 *  * Copyright (c) 2020 . All rights reserved.
 *  * Last modified 10/17/20 12:19 AM
 *
 */

package id.co.santridev.simplechat

import android.content.Context
import id.co.santridev.simplechat.core.di.Injector
import id.co.santridev.simplechat.core.domain.usecase.IUserUseCase

class AppComponent(private val context: Context) {
    fun getUserUseCase(): IUserUseCase = Injector.provideUserUseCase(context)
}