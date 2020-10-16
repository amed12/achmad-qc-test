/*
 * *
 *  * Created by Achmad Fathullah on 10/17/20 12:02 AM
 *  * Copyright (c) 2020 . All rights reserved.
 *  * Last modified 10/17/20 12:02 AM
 *
 */

package id.co.santridev.simplechat.core.domain.repository

import id.co.santridev.simplechat.core.domain.model.User
import id.co.santridev.simplechat.core.domain.usecase.IUserUseCase
import id.co.santridev.simplechat.core.utils.Action

class UserInteractor(private val userRepository: IUserRepository) : IUserUseCase {
    override fun setCurrentUser(user: User) {
        userRepository.setCurrentUser(user)
    }

    override fun getCurrentUser(): User = userRepository.getCurrentUser()
    override fun login(
        email: String,
        password: String,
        name: String,
        onSuccess: Action<User>,
        onError: Action<Throwable>
    ) {
        userRepository.login(email, password, name, onSuccess, onError)
    }
}