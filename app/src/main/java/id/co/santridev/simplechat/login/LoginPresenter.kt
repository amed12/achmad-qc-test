/*
 * *
 *  * Created by Achmad Fathullah on 10/17/20 1:24 AM
 *  * Copyright (c) 2020 . All rights reserved.
 *  * Last modified 10/17/20 1:24 AM
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
    private val presenter: ILoginPresenter,
    private val userUseCase: IUserUseCase
) {
    fun start() {
        userUseCase.getCurrentUser(onSuccess = object : Action<User> {
            override fun call(t: User) {
                if (t.id.isNotEmpty())
                    presenter.showHomePage()
            }
        }, onError = object : Action<Throwable> {
            override fun call(t: Throwable) {
                presenter.showErrorMessage(t.message.toString())
            }
        })
    }
}