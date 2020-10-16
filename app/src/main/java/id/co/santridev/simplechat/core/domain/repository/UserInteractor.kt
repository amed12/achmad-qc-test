/*
 * *
 *  * Created by Achmad Fathullah on 10/16/20 10:57 PM
 *  * Copyright (c) 2020 . All rights reserved.
 *  * Last modified 10/16/20 10:57 PM
 *
 */

package id.co.santridev.simplechat.core.domain.repository

import id.co.santridev.simplechat.core.data.source.UserRepository
import id.co.santridev.simplechat.core.domain.model.User
import id.co.santridev.simplechat.core.domain.usecase.IUserUseCase

class UserInteractor(private val userRepository: UserRepository) : IUserUseCase {
    override fun setCurrentUser(user: User) {
        userRepository.setCurrentUser(user)
    }

    override fun getCurrentUser(): User = userRepository.getCurrentUser()
}