/*
 * *
 *  * Created by Achmad Fathullah on 10/17/20 12:28 PM
 *  * Copyright (c) 2020 . All rights reserved.
 *  * Last modified 10/17/20 12:28 PM
 *
 */

package id.co.santridev.simplechat.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import id.co.santridev.simplechat.core.domain.model.User
import id.co.santridev.simplechat.core.domain.usecase.IUserUseCase
import id.co.santridev.simplechat.core.utils.Action

interface ILoginPresenter {
    fun showLoading()
    fun dismissLoading()
    fun showHomePage()
    fun showErrorMessage(errorMessage: String)
}

class LoginViewModel(
    private val userUseCase: IUserUseCase
) : ViewModel() {
    private var _stateHomeLiveData = MutableLiveData<Boolean>()
    private var _errorStringLiveData = MutableLiveData<String>()
    private var _stateLoadingLiveData = MutableLiveData<Boolean>()
    var stateHomeData: LiveData<Boolean> = _stateHomeLiveData
    var stateLoading: LiveData<Boolean> = _stateLoadingLiveData
    var errorMessage: LiveData<String> = _errorStringLiveData

    fun start() {
        userUseCase.getCurrentUser(onSuccess = object : Action<User> {
            override fun call(t: User) {
                if (t.id.isNotEmpty())
                    _stateHomeLiveData.postValue(true)
                else
                    _stateHomeLiveData.postValue(false)
            }
        }, onError = object : Action<Throwable> {
            override fun call(t: Throwable) {
                _errorStringLiveData.postValue(t.message.toString())
            }
        })
    }

    fun login(email: String, password: String, name: String) {
        _stateLoadingLiveData.postValue(true)
        userUseCase.login(email, password, name, onSuccess = object : Action<User> {
            override fun call(t: User) {
                _stateLoadingLiveData.postValue(false)
                _stateHomeLiveData.postValue(true)
            }

        }, onError = object : Action<Throwable> {
            override fun call(t: Throwable) {
                _stateLoadingLiveData.postValue(false)
                _errorStringLiveData.postValue(t.message.toString())
            }

        })
    }
}