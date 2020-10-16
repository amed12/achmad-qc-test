/*
 * *
 *  * Created by Achmad Fathullah on 10/17/20 1:12 AM
 *  * Copyright (c) 2020 . All rights reserved.
 *  * Last modified 10/17/20 12:53 AM
 *
 */

package id.co.santridev.simplechat.core.domain.usecase

import id.co.santridev.simplechat.core.domain.model.User
import id.co.santridev.simplechat.core.domain.repository.IUserRepository
import id.co.santridev.simplechat.core.utils.Action

class UserInteractor(private val userRepository: IUserRepository) : IUserUseCase {
    override fun getCurrentUser(onSuccess: Action<User>, onError: Action<Throwable>) {
        userRepository.getCurrentUser(onSuccess, onError)
    }

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