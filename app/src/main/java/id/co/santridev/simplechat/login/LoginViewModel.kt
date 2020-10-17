/*
 * *
 *  * Created by Achmad Fathullah on 10/17/20 5:21 PM
 *  * Copyright (c) 2020 . All rights reserved.
 *  * Last modified 10/17/20 5:21 PM
 *
 */

package id.co.santridev.simplechat.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.qiscus.sdk.chat.core.util.QiscusTextUtil.getString
import id.co.santridev.simplechat.R
import id.co.santridev.simplechat.core.domain.model.User
import id.co.santridev.simplechat.core.domain.usecase.IUserUseCase
import id.co.santridev.simplechat.core.utils.Action

class LoginViewModel(
    private val userUseCase: IUserUseCase
) : ViewModel() {
    private var _stateHomeLiveData = MutableLiveData<Boolean>()
    private var _errorStringLiveData = MutableLiveData<String>()
    private var _stateLoadingLiveData = MutableLiveData<Boolean>()
    var stateHomeData: LiveData<Boolean> = _stateHomeLiveData
    var stateLoading: LiveData<Boolean> = _stateLoadingLiveData
    var errorMessage: LiveData<String> = _errorStringLiveData

    companion object {
        const val USER_B = "user_b@example.com"
    }

    fun start(keyUser: String) {
        userUseCase.getCurrentUser(keyUser, onSuccess = object : Action<User> {
            override fun call(t: User) {
                if (t.id == USER_B)
                    _stateHomeLiveData.postValue(true)
            }
        }, onError = object : Action<Throwable> {
            override fun call(t: Throwable) {
                _errorStringLiveData.postValue(t.message.toString())
            }
        })
    }

    fun login(email: String, password: String, name: String, isLoginA: Boolean) {
        _stateLoadingLiveData.postValue(true)
        userUseCase.login(email, password, name, onSuccess = object : Action<User> {
            override fun call(t: User) {
                if (!isLoginA)
                    if (t.id != "user_b@example.com") {
                        _stateLoadingLiveData.postValue(false)
                        _stateHomeLiveData.postValue(false)
                        _errorStringLiveData.postValue(getString(R.string.message_login_as_b))
                    } else {
                        _stateLoadingLiveData.postValue(false)
                        _stateHomeLiveData.postValue(false)
                        _errorStringLiveData.postValue("isLoginA")
                    }
                else {
                    _stateLoadingLiveData.postValue(false)
                    _stateHomeLiveData.postValue(true)
                }

            }

        }, onError = object : Action<Throwable> {
            override fun call(t: Throwable) {
                _stateLoadingLiveData.postValue(false)
                _errorStringLiveData.postValue(t.message.toString())
            }

        })
    }
}