/*
 * *
 *  * Created by Achmad Fathullah on 10/17/20 1:30 AM
 *  * Copyright (c) 2020 . All rights reserved.
 *  * Last modified 10/17/20 1:30 AM
 *
 */

package id.co.santridev.simplechat.login

import id.co.santridev.simplechat.core.domain.model.User
import id.co.santridev.simplechat.core.domain.usecase.IUserUseCase
import id.co.santridev.simplechat.core.utils.Action

interface ILoginPresenter {
    fun showLoading()
    fun dismissLoading()
    fun showHomePage()
    fun showErrorMessage(errorMessage: String)
}

class LoginPresenter(
    private val presenterListener: ILoginPresenter,
    private val userUseCase: IUserUseCase
) {
    fun start() {
        userUseCase.getCurrentUser(onSuccess = object : Action<User> {
            override fun call(t: User) {
                if (t.id.isNotEmpty())
                    presenterListener.showHomePage()
            }
        }, onError = object : Action<Throwable> {
            override fun call(t: Throwable) {
                presenterListener.showErrorMessage(t.message.toString())
            }
        })
    }

    fun login(email: String, password: String, name: String) {
        presenterListener.showLoading()
        userUseCase.login(email, password, name, onSuccess = object : Action<User> {
            override fun call(t: User) {
                presenterListener.dismissLoading()
                presenterListener.showHomePage()
            }

        }, onError = object : Action<Throwable> {
            override fun call(t: Throwable) {
                presenterListener.dismissLoading()
                presenterListener.showErrorMessage(t.message.toString())
            }

        })
    }
}