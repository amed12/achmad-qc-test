/*
 * *
 *  * Created by Achmad Fathullah on 10/17/20 11:43 PM
 *  * Copyright (c) 2020 . All rights reserved.
 *  * Last modified 10/17/20 11:43 PM
 *
 */

package id.co.santridev.simplechat.core.di

import android.content.Context
import id.co.santridev.simplechat.core.data.source.ChatRoomRepository
import id.co.santridev.simplechat.core.data.source.UserRepository
import id.co.santridev.simplechat.core.domain.repository.IChatRoomRepository
import id.co.santridev.simplechat.core.domain.repository.IUserRepository
import id.co.santridev.simplechat.core.domain.usecase.ChatRoomInteractor
import id.co.santridev.simplechat.core.domain.usecase.IChatRoomUseCase
import id.co.santridev.simplechat.core.domain.usecase.IUserUseCase
import id.co.santridev.simplechat.core.domain.usecase.UserInteractor

object Injector {
    private fun provideRepository(context: Context): IUserRepository =
        UserRepository.getInstance(context)

    fun provideUserUseCase(context: Context): IUserUseCase =
        UserInteractor(provideRepository(context))

    private fun provideChatRoomRepository(): IChatRoomRepository = ChatRoomRepository.getInstance()

    fun provideChatRoomUseCase(): IChatRoomUseCase = ChatRoomInteractor(provideChatRoomRepository())
}