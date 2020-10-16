/*
 * *
 *  * Created by Achmad Fathullah on 10/17/20 12:02 AM
 *  * Copyright (c) 2020 . All rights reserved.
 *  * Last modified 10/17/20 12:02 AM
 *
 */

package id.co.santridev.simplechat.core.di

import android.content.Context
import id.co.santridev.simplechat.core.data.source.UserRepository
import id.co.santridev.simplechat.core.domain.repository.IUserRepository
import id.co.santridev.simplechat.core.domain.repository.UserInteractor
import id.co.santridev.simplechat.core.domain.usecase.IUserUseCase

object Injector {
    private fun provideRepository(context: Context): IUserRepository =
        UserRepository.getInstance(context)

    fun provideUserUseCase(context: Context): IUserUseCase =
        UserInteractor(provideRepository(context))
}