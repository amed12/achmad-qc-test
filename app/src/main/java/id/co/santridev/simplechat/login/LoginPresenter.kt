/*
 * *
 *  * Created by Achmad Fathullah on 10/17/20 1:12 AM
 *  * Copyright (c) 2020 . All rights reserved.
 *  * Last modified 10/17/20 1:12 AM
 *
 */

package id.co.santridev.simplechat.login

import id.co.santridev.simplechat.core.domain.usecase.IUserUseCase

interface ILoginPresenter {
    fun showLoading()
    fun dismissLoading()
    fun showHomePage()
    fun showErrorMessage(errorMessage: String)
}

class LoginPresenter(private val presenter: ILoginPresenter, private val userUseCase: IUserUseCase)